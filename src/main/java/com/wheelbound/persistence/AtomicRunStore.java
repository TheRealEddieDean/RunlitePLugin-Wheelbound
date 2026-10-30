package com.wheelbound.persistence;

import com.wheelbound.domain.RunIdentity;
import com.wheelbound.domain.SlicePurchaseReceipt;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Local single-writer foundation snapshots. The future adapter supplies Plugin.getPluginDirectory().
 * No non-atomic fallback, automatic corrupt-save reset or cross-device synchronization.
 */
public final class AtomicRunStore
{
    enum Boundary { TEMP_FORCED, REPLACED }
    interface CommitHook { void at(Boundary boundary) throws IOException; }
    interface DirectoryForcer { void force(Path directory) throws IOException; }
    private final Path root;
    private final RunCodec codec;
    private final RunIdentity identity;
    private final String name;
    private final CommitHook hook;
    private final DirectoryForcer directoryForcer;
    private volatile boolean uncertain;

    public AtomicRunStore(Path pluginDirectory, RunCodec codec, RunIdentity identity)
    { this(pluginDirectory, codec, identity, ignored -> {}); }

    AtomicRunStore(Path pluginDirectory, RunCodec codec, RunIdentity identity, CommitHook hook)
    {
        this(pluginDirectory, codec, identity, hook,
            directoryForcer(System.getProperty("os.name", ""), AtomicRunStore::forceDirectory));
    }

    AtomicRunStore(Path pluginDirectory, RunCodec codec, RunIdentity identity, CommitHook hook,
        DirectoryForcer directoryForcer)
    {
        this.root = Objects.requireNonNull(pluginDirectory, "pluginDirectory").resolve("runs-v1");
        this.codec = Objects.requireNonNull(codec, "codec");
        this.identity = Objects.requireNonNull(identity, "identity");
        this.hook = Objects.requireNonNull(hook, "hook");
        this.directoryForcer = Objects.requireNonNull(directoryForcer, "directoryForcer");
        name = RunCodec.sha256((identity.accountKey() + "\0" + identity.runId()).getBytes(StandardCharsets.UTF_8));
    }

    Path snapshotPath() { return root.resolve(name + ".json"); }
    Path pendingPath() { return root.resolve(name + ".pending"); }
    Path previousPath() { return root.resolve(name + ".previous"); }
    Path lockPath() { return root.resolve(name + ".lock"); }

    public Optional<RunDocument> load() throws IOException
    { return locked(this::read); }

    /**
     * Recover an exact run-start seal after restart or lost acknowledgement, without
     * sampling or writing. Absence is not permission to create a run or retrofit a
     * legacy save. Eligibility and the initial atomic creation remain caller concerns.
     * Pending/corrupt saves fail closed through the normal locked read.
     */
    public Optional<com.wheelbound.domain.SealedGrandFate> committedGrandFate(
        com.wheelbound.domain.SealedGrandFate request) throws IOException
    {
        Objects.requireNonNull(request, "request");
        if (!identity.equals(request.identity()))
        { throw new IllegalArgumentException("Foreign run context"); }
        return locked(() -> {
            Optional<RunDocument> current = read();
            if (!current.isPresent()) { return Optional.empty(); }
            Optional<com.wheelbound.domain.SealedGrandFate> sealed = current.get().grandFate();
            if (sealed.isPresent())
            {
                if (!sealed.get().equals(request))
                { throw new IOException("Run already has a different Grand Fate sealing request"); }
                return sealed;
            }
            for (RunReceipt receipt : current.get().receipts())
            {
                if (request.commandId().equals(receipt.commandId()))
                { throw new IOException("Command ID belongs to a different operation"); }
            }
            return Optional.empty();
        });
    }

    /**
     * Read the original persisted purchase response after a restart or lost acknowledgement.
     * Pending/corrupt snapshots still require reconciliation. A command belonging to another
     * operation is a collision, not an absent purchase. This query never executes a purchase;
     * callers must match the full original request before treating it as a replay response.
     * Absence alone does not authorize retrying a command or establish quote authority.
     */
    public Optional<SlicePurchaseReceipt> committedPurchase(UUID commandId) throws IOException
    {
        Objects.requireNonNull(commandId, "commandId");
        return locked(() -> {
            Optional<RunDocument> current = read();
            if (!current.isPresent()) { return Optional.empty(); }
            for (RunReceipt receipt : current.get().receipts())
            {
                if (!commandId.equals(receipt.commandId())) { continue; }
                if (receipt.purchase() == null)
                { throw new IOException("Command ID belongs to a different operation"); }
                return Optional.of(receipt.purchase());
            }
            return Optional.empty();
        });
    }

    private Optional<RunDocument> read() throws IOException
    {
        if (Files.exists(pendingPath())) { throw new IOException("Interrupted write retained; reconciliation required"); }
        if (!Files.exists(snapshotPath())) { return Optional.empty(); }
        if (Files.size(snapshotPath()) > RunCodec.MAX_BYTES) { throw new IOException("Snapshot too large; original retained"); }
        return Optional.of(codec.decode(Files.readAllBytes(snapshotPath()), identity));
    }

    /**
     * expectedRevision=-1 creates revision zero only. Files are forced before atomic replacement.
     * Directory entries are also forced except on Windows, where Java cannot open directories
     * as FileChannels; directory-entry durability across power loss is not guaranteed there.
     */
    public void write(RunDocument next, long expectedRevision) throws IOException
    { write(next, expectedRevision, false); }

    /**
     * Atomically stage a prepared run-start request without activating gameplay.
     * The caller freezes randomness before calling and retains the whole request on failure.
     * Only an identical initial snapshot is replayable here; after later mutations use
     * committedGrandFate for recovery. This does not establish fresh-account eligibility,
     * authorize activation, supply starting grants or retrofit existing saves.
     */
    public com.wheelbound.domain.SealedGrandFate stageGrandFate(RunDocument initial) throws IOException
    {
        Objects.requireNonNull(initial, "initial");
        if (initial.lifecycle().revision() != 0
            || initial.lifecycle().state() != com.wheelbound.domain.RunLifecycle.State.NOT_STARTED
            || initial.lifecycle().obligation() != com.wheelbound.domain.RunLifecycle.Obligation.NONE
            || !initial.receipts().isEmpty() || !initial.grandFate().isPresent())
        { throw new IllegalArgumentException("Expected an unstarted revision-zero sealed request"); }
        write(initial, -1, true);
        return initial.grandFate().get();
    }

    private void write(RunDocument next, long expectedRevision, boolean replayInitial) throws IOException
    {
        if (expectedRevision < -1 || !identity.equals(next.lifecycle().identity())
            || next.lifecycle().revision() != Math.addExact(expectedRevision, 1))
        { throw new IllegalArgumentException("Invalid next identity/revision"); }
        byte[] bytes = codec.encode(next);
        locked(() -> {
            if (uncertain) { throw new IOException("Reopen and reconcile after failed persistence"); }
            Optional<RunDocument> current = read();
            if (replayInitial && current.isPresent())
            {
                if (!java.util.Arrays.equals(bytes, codec.encode(current.get())))
                { throw new IOException("Run already exists with a different initial request or later state"); }
                return null;
            }
            long actual = current.map(d -> d.lifecycle().revision()).orElse(-1L);
            if (actual != expectedRevision) { throw new IOException("Stale stored revision"); }
            if (current.isPresent())
            {
                if (!current.get().grandFate().equals(next.grandFate()))
                { throw new IOException("Cannot add, discard or replace sealed Grand Fate on an existing run"); }
                java.util.List<RunReceipt> history = current.get().receipts();
                if (next.receipts().size() < history.size()
                    || !next.receipts().subList(0, history.size()).equals(history))
                { throw new IOException("Cannot discard or rewrite committed receipts"); }
            }
            try { PurchaseTransition.validate(current.orElse(null), next); }
            catch (IllegalArgumentException ex) { throw new IOException("Invalid purchase transition", ex); }
            try
            {
                forceFile(pendingPath(), bytes);
                hook.at(Boundary.TEMP_FORCED);
                if (current.isPresent())
                {
                    Path backupPending = root.resolve(name + ".previous-pending");
                    forceFile(backupPending, Files.readAllBytes(snapshotPath()));
                    Files.move(backupPending, previousPath(), StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
                }
                directoryForcer.force(root);
                Files.move(pendingPath(), snapshotPath(), StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
                hook.at(Boundary.REPLACED);
                directoryForcer.force(root);
                return null;
            }
            catch (IOException | RuntimeException ex)
            {
                uncertain = true;
                throw ex;
            }
        });
    }

    private void forceFile(Path path, byte[] bytes) throws IOException
    {
        try (FileChannel channel = FileChannel.open(path, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE))
        {
            ByteBuffer buffer = ByteBuffer.wrap(bytes);
            while (buffer.hasRemaining()) { channel.write(buffer); }
            channel.force(true);
        }
    }

    static DirectoryForcer directoryForcer(String osName, DirectoryForcer supported)
    {
        // Skip the unsupported operation up front, never infer support from AccessDeniedException:
        // real permission failures from file writes, moves, locks, and supported directory force
        // must still propagate to the caller.
        return osName.startsWith("Windows") ? ignored -> {} : supported;
    }

    private static void forceDirectory(Path directoryPath) throws IOException
    {
        try (FileChannel directory = FileChannel.open(directoryPath, StandardOpenOption.READ)) { directory.force(true); }
    }

    private interface IOAction<T> { T run() throws IOException; }
    private <T> T locked(IOAction<T> action) throws IOException
    {
        Files.createDirectories(root);
        try (FileChannel channel = FileChannel.open(lockPath(), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            FileLock lock = channel.tryLock())
        {
            if (lock == null) { throw new IOException("Run is already being written"); }
            return action.run();
        }
        catch (OverlappingFileLockException ex) { throw new IOException("Run is already being written", ex); }
    }
}
