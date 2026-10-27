package com.wheelbound.persistence;

import com.wheelbound.domain.RunIdentity;
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

/**
 * Local single-writer foundation snapshots. The future adapter supplies Plugin.getPluginDirectory().
 * No non-atomic fallback, automatic corrupt-save reset or cross-device synchronization.
 */
public final class AtomicRunStore
{
    enum Boundary { TEMP_FORCED, REPLACED }
    interface CommitHook { void at(Boundary boundary) throws IOException; }
    private final Path root;
    private final RunCodec codec;
    private final RunIdentity identity;
    private final String name;
    private final CommitHook hook;
    private boolean uncertain;

    public AtomicRunStore(Path pluginDirectory, RunCodec codec, RunIdentity identity)
    { this(pluginDirectory, codec, identity, ignored -> {}); }

    AtomicRunStore(Path pluginDirectory, RunCodec codec, RunIdentity identity, CommitHook hook)
    {
        this.root = Objects.requireNonNull(pluginDirectory, "pluginDirectory").resolve("runs-v1");
        this.codec = Objects.requireNonNull(codec, "codec");
        this.identity = Objects.requireNonNull(identity, "identity");
        this.hook = Objects.requireNonNull(hook, "hook");
        name = RunCodec.sha256((identity.accountKey() + "\0" + identity.runId()).getBytes(StandardCharsets.UTF_8));
    }

    Path snapshotPath() { return root.resolve(name + ".json"); }
    Path pendingPath() { return root.resolve(name + ".pending"); }
    Path previousPath() { return root.resolve(name + ".previous"); }
    Path lockPath() { return root.resolve(name + ".lock"); }

    public Optional<RunDocument> load() throws IOException
    { return locked(this::read); }

    private Optional<RunDocument> read() throws IOException
    {
        if (Files.exists(pendingPath())) { throw new IOException("Interrupted write retained; reconciliation required"); }
        if (!Files.exists(snapshotPath())) { return Optional.empty(); }
        if (Files.size(snapshotPath()) > RunCodec.MAX_BYTES) { throw new IOException("Snapshot too large; original retained"); }
        return Optional.of(codec.decode(Files.readAllBytes(snapshotPath()), identity));
    }

    /** expectedRevision=-1 creates revision zero only. Success is returned after file/directory force. */
    public void write(RunDocument next, long expectedRevision) throws IOException
    {
        if (expectedRevision < -1 || !identity.equals(next.lifecycle().identity())
            || next.lifecycle().revision() != Math.addExact(expectedRevision, 1))
        { throw new IllegalArgumentException("Invalid next identity/revision"); }
        byte[] bytes = codec.encode(next);
        locked(() -> {
            if (uncertain) { throw new IOException("Reopen and reconcile after failed persistence"); }
            Optional<RunDocument> current = read();
            long actual = current.map(d -> d.lifecycle().revision()).orElse(-1L);
            if (actual != expectedRevision) { throw new IOException("Stale stored revision"); }
            if (current.isPresent())
            {
                java.util.List<RunReceipt> history = current.get().receipts();
                if (next.receipts().size() < history.size()
                    || !next.receipts().subList(0, history.size()).equals(history))
                { throw new IOException("Cannot discard or rewrite committed receipts"); }
            }
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
                forceDirectory();
                Files.move(pendingPath(), snapshotPath(), StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
                hook.at(Boundary.REPLACED);
                forceDirectory();
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

    private void forceDirectory() throws IOException
    {
        try (FileChannel directory = FileChannel.open(root, StandardOpenOption.READ)) { directory.force(true); }
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
