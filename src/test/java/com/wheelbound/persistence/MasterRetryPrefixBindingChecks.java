package com.wheelbound.persistence;

import com.wheelbound.domain.*;
import java.util.*;

public final class MasterRetryPrefixBindingChecks
{
    public static void main(String[] args)
    {
        for (int count : new int[] {0, 1, 513})
        {
            var head = MasterRetryContinuationChecks.fixture(count);
            var pending = head.pendingMasterSelection().get();
            var steps = new ArrayList<MasterSelectionProvenance.Step>();
            pending.landings().forEach(l -> steps.add(l.step()));
            MasterRetryPrefixBinding.validate(head, copy(pending, steps, pending.streamDescriptor()));
            steps.add(new MasterSelectionProvenance.Step(new DrawCommitment(pending.pool(), 0), "next"));
            var extension = copy(pending, steps, pending.streamDescriptor());
            MasterRetryPrefixBinding.validate(head, extension);
            // Same outcome and repeated final state cannot hide an altered raw ticket.
            if (count > 0)
            {
                var changedTicket = new ArrayList<>(steps);
                changedTicket.set(0, new MasterSelectionProvenance.Step(
                    new DrawCommitment(pending.pool(), 1), "repeated"));
                rejects(() -> MasterRetryPrefixBinding.validate(head,
                    copy(pending, changedTicket, pending.streamDescriptor())));
                var changedState = new ArrayList<>(steps);
                changedState.set(0, new MasterSelectionProvenance.Step(
                    new DrawCommitment(pending.pool(), 0), "rewritten"));
                rejects(() -> MasterRetryPrefixBinding.validate(head,
                    copy(pending, changedState, pending.streamDescriptor())));
                rejects(() -> MasterRetryPrefixBinding.validate(head,
                    copy(pending, steps.subList(0, count - 1), pending.streamDescriptor())));
            }
            rejects(() -> MasterRetryPrefixBinding.validate(head, copy(pending, steps, "new-stream")));
            var terminal = pending.append(pending.transactionId(), count, pending.currentStreamState(),
                new MasterSelectionProvenance.Step(new DrawCommitment(pending.pool(), 19), "valid"));
            rejects(() -> MasterRetryPrefixBinding.validate(head, terminal));
            var paused = LifecycleAdmission.prepare(head, head.lifecycle().identity(),
                new UUID(14, count), head.lifecycle().revision(), RunReceipt.Operation.PAUSE);
            rejects(() -> MasterRetryPrefixBinding.validate(paused, extension));
            var foreign = new MasterSelectionProvenance(new UUID(88, 99), pending.snapshotVersion(),
                pending.catalogVersion(), pending.streamDescriptor(), pending.initialStreamState(),
                pending.readiness(), steps);
            rejects(() -> MasterRetryPrefixBinding.validate(head, foreign));
            if (head.pendingMasterSelection().get() != pending || pending.drawCount() != count
                || head.lifecycle().revision() != 1 || head.receipts().size() != 1)
            { throw new AssertionError("Source aggregate changed"); }
        }
        System.out.println("MasterRetryPrefixBindingChecks PASSED");
    }

    private static MasterSelectionProvenance copy(MasterSelectionProvenance prefix,
        List<MasterSelectionProvenance.Step> steps, String descriptor)
    {
        return new MasterSelectionProvenance(prefix.transactionId(), prefix.snapshotVersion(),
            prefix.catalogVersion(), descriptor, prefix.initialStreamState(), prefix.readiness(), steps);
    }

    private static void rejects(Runnable action)
    {
        try { action.run(); } catch (IllegalArgumentException expected) { return; }
        throw new AssertionError("Expected changed-prefix refusal");
    }
}
