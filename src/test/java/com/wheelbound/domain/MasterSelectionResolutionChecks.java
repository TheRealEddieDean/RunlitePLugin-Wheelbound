package com.wheelbound.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import static com.wheelbound.domain.MasterSelectionReadiness.*;

/** Offline structural fixtures; no live evidence or spin accounting claim. */
public final class MasterSelectionResolutionChecks
{
    private static void check(boolean value)
    { if (!value) { throw new AssertionError("Resolution invariant violated"); } }

    private static void rejects(Runnable action)
    {
        try { action.run(); }
        catch (IllegalArgumentException | ArithmeticException expected) { return; }
        throw new AssertionError("Expected invalid resolution refusal");
    }

    private static MasterSelectionProvenance start(ActiveWheel.Kind special)
    {
        List<ActiveWheel.Slice> slices = new ArrayList<>();
        Map<UUID, Classification> evidence = new HashMap<>();
        for (int i = 0; i < 5; i++)
        {
            UUID id = new UUID(0, i + 1);
            slices.add(ActiveWheel.Slice.ordinary(id, "activity" + i, ActiveWheel.Enhancement.BASE));
            evidence.put(id, new Classification(i == 4 ? Availability.ASSIGNABLE
                : Availability.VERIFIED_EMPTY, "fixture" + i));
        }
        if (special != null)
        {
            UUID id = new UUID(0, 6);
            slices.add(ActiveWheel.Slice.special(id, special, 7));
            evidence.put(id, new Classification(Availability.ASSIGNABLE, "supported fixture"));
        }
        return new MasterSelectionProvenance(UUID.randomUUID(), "snapshot", "catalog", "stream",
            "initial", new MasterSelectionReadiness(new ActiveWheel(slices), evidence), List.of());
    }

    private static MasterSelectionProvenance append(MasterSelectionProvenance prefix, int ticket, String state)
    {
        return prefix.append(prefix.transactionId(), prefix.drawCount(), prefix.currentStreamState(),
            new MasterSelectionProvenance.Step(new DrawCommitment(prefix.pool(), ticket), state));
    }

    public static void main(String[] args)
    {
        RunIdentity identity = new RunIdentity("offline", UUID.randomUUID());
        for (ActiveWheel.Kind special : new ActiveWheel.Kind[] { null, ActiveWheel.Kind.TAINT, ActiveWheel.Kind.SACRIFICE })
        {
            MasterSelectionProvenance zero = start(special);
            MasterSelectionCreationReceipt creation = new MasterSelectionCreationReceipt(identity, 2, zero);
            UUID command = UUID.randomUUID();
            rejects(() -> new MasterSelectionResolutionReceipt(command, 3, creation, zero));
            MasterSelectionProvenance prefix = zero;
            for (int i = 0; i < 513; i++) { prefix = append(prefix, 0, "empty" + i); }
            final MasterSelectionProvenance empty = prefix;
            rejects(() -> new MasterSelectionResolutionReceipt(command, 3, creation, empty));
            MasterSelectionProvenance terminal = append(empty, zero.pool().totalWeight() - 1, "terminal");
            MasterSelectionResolutionReceipt receipt = new MasterSelectionResolutionReceipt(command, 9, creation, terminal);
            MasterSelectionProvenance.Step terminalStep = terminal.landings().get(513).step();
            check(receipt.equals(MasterSelectionResolutionReceipt.prepare(command, 9, creation, empty, terminalStep)));
            rejects(() -> MasterSelectionResolutionReceipt.prepare(command, 9, creation, empty,
                new MasterSelectionProvenance.Step(new DrawCommitment(zero.pool(), 0), "still-empty")));
            try
            {
                MasterSelectionResolutionReceipt.prepare(command, 9, creation, terminal, terminalStep);
                throw new AssertionError("Redrew a valid landing");
            }
            catch (IllegalStateException expected) { }
            check(receipt.identity().equals(identity) && receipt.transactionId().equals(zero.transactionId()));
            check(receipt.beforeRevision() == 9 && receipt.afterRevision() == 10);
            check(receipt.resolved() == terminal && receipt.creation() == creation);
            check(empty.drawCount() == 513 && terminal.drawCount() == 514 && zero.drawCount() == 0);
            check(terminal.landings().get(513).kind() == (special == null
                ? Landing.ORDINARY_ASSIGNMENT : Landing.SPECIAL_OBLIGATION));
            MasterSelectionProvenance restored = new MasterSelectionProvenance(zero.transactionId(),
                zero.snapshotVersion(), zero.catalogVersion(), zero.streamDescriptor(), zero.initialStreamState(),
                zero.readiness(), terminal.landings().stream().map(MasterSelectionProvenance.Landing::step)
                    .collect(java.util.stream.Collectors.toList()));
            MasterSelectionResolutionReceipt same = new MasterSelectionResolutionReceipt(command, 9, creation, restored);
            check(receipt.equals(same) && same.equals(receipt) && receipt.hashCode() == same.hashCode());
            ArrayList<MasterSelectionResolutionReceipt> source = new ArrayList<>(List.of(receipt));
            MasterSelectionResolutions history = new MasterSelectionResolutions(identity, 20, source);
            source.clear();
            check(history.replay(same, identity, 20) == receipt && history.receipts().size() == 1);
            try { history.receipts().clear(); throw new AssertionError("Mutable history"); }
            catch (UnsupportedOperationException expected) { }
            rejects(() -> history.replay(same, identity, 9));
            rejects(() -> history.replay(same, new RunIdentity("foreign", identity.runId()), 20));
            rejects(() -> history.replay(same, new RunIdentity(identity.accountKey(), UUID.randomUUID()), 20));
            rejects(() -> history.replay(new MasterSelectionResolutionReceipt(UUID.randomUUID(), 9, creation, terminal), identity, 20));
            rejects(() -> history.replay(new MasterSelectionResolutionReceipt(command, 10, creation, terminal), identity, 20));
            MasterSelectionProvenance changedState = append(empty, zero.pool().totalWeight() - 1, "changed");
            rejects(() -> history.replay(new MasterSelectionResolutionReceipt(command, 9, creation, changedState), identity, 20));
            MasterSelectionProvenance shorter = append(zero, zero.pool().totalWeight() - 1, "terminal");
            rejects(() -> history.replay(new MasterSelectionResolutionReceipt(command, 9, creation, shorter), identity, 20));
            List<MasterSelectionProvenance.Step> changedSteps = new ArrayList<>();
            terminal.landings().forEach(landing -> changedSteps.add(landing.step()));
            changedSteps.set(0, new MasterSelectionProvenance.Step(new DrawCommitment(zero.pool(), 0), "changed-empty"));
            MasterSelectionProvenance changedPrefix = new MasterSelectionProvenance(zero.transactionId(),
                zero.snapshotVersion(), zero.catalogVersion(), zero.streamDescriptor(), zero.initialStreamState(),
                zero.readiness(), changedSteps);
            rejects(() -> history.replay(new MasterSelectionResolutionReceipt(command, 9, creation, changedPrefix), identity, 20));
            rejects(() -> new MasterSelectionResolutionReceipt(command, 2, creation, terminal));
            rejects(() -> new MasterSelectionResolutionReceipt(zero.transactionId(), 9, creation, terminal));
            rejects(() -> new MasterSelectionResolutionReceipt(command, Long.MAX_VALUE, creation, terminal));
            MasterSelectionProvenance foreign = start(special);
            rejects(() -> new MasterSelectionResolutionReceipt(command, 9, creation,
                append(foreign, foreign.pool().totalWeight() - 1, "terminal")));
            rejects(() -> new MasterSelectionResolutions(identity, 20, List.of(receipt, receipt)));
            rejects(() -> new MasterSelectionResolutions(identity, 20, List.of(receipt,
                new MasterSelectionResolutionReceipt(UUID.randomUUID(), 12, creation, terminal))));
            rejects(() -> new MasterSelectionResolutions(identity, 9, List.of(receipt)));
            rejects(() -> new MasterSelectionResolutions(identity, -1, List.of()));

            MasterSelectionProvenance nextZero = start(special);
            MasterSelectionCreationReceipt nextCreation = new MasterSelectionCreationReceipt(identity, 11, nextZero);
            MasterSelectionProvenance nextTerminal = append(nextZero, nextZero.pool().totalWeight() - 1, "next");
            MasterSelectionResolutionReceipt next = new MasterSelectionResolutionReceipt(UUID.randomUUID(), 12, nextCreation, nextTerminal);
            check(new MasterSelectionResolutions(identity, 13, List.of(receipt, next)).receipts().size() == 2);
            rejects(() -> new MasterSelectionResolutions(identity, 13, List.of(next, receipt)));
            rejects(() -> new MasterSelectionResolutions(identity, 20, List.of(receipt,
                new MasterSelectionResolutionReceipt(command, 12, nextCreation, nextTerminal))));
            MasterSelectionResolutionReceipt crossCollision = new MasterSelectionResolutionReceipt(
                nextZero.transactionId(), 9, creation, terminal);
            rejects(() -> new MasterSelectionResolutions(identity, 13, List.of(crossCollision, next)));
        }
        System.out.println("Master selection resolution checks passed");
    }
}
