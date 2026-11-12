package com.wheelbound.persistence;

import com.wheelbound.domain.FateCardOwnership;
import com.wheelbound.domain.FateCardPurchaseJournal;
import com.wheelbound.domain.FateCardPurchaseProposal;
import com.wheelbound.domain.FateCardPurchaseReceipt;
import com.wheelbound.domain.FatePointLedger;
import com.wheelbound.domain.MasterSpinState;
import com.wheelbound.domain.RunIdentity;
import com.wheelbound.domain.RunLifecycle;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/** Pure atomic snapshot proposal for the supported card/lifecycle/plain-debit segment.
 * The caller authenticates the seed and reviewed quote. No storage acknowledgement,
 * broader history admission or gameplay activation is provided by this class.
 */
public final class FateCardPurchaseAdmission
{
    private FateCardPurchaseAdmission() { }

    public static final class Result
    {
        private final RunDocument proposed;
        private final FateCardPurchaseReceipt receipt;
        private final boolean replay;

        private Result(RunDocument proposed, FateCardPurchaseReceipt receipt, boolean replay)
        { this.proposed = proposed; this.receipt = receipt; this.replay = replay; }

        public RunDocument proposed() { return proposed; }
        public FateCardPurchaseReceipt receipt() { return receipt; }
        public boolean replay() { return replay; }
    }

    /** Revalidates the complete supported segment before fresh admission or replay.
     * A replay returns the unchanged current head and the original payment response,
     * even when the current balance, lifecycle state or ownership has advanced.
     * A store must call under its write lock and persist the entire proposed snapshot
     * before acknowledging a fresh purchase.
     */
    public static Result prepare(RunDocument head, RunLifecycle seedLifecycle,
        FatePointLedger seedLedger, FateCardOwnership seedOwnership, RunIdentity identity,
        UUID commandId, long expectedRevision, FateCardPurchaseProposal.Quote quote)
    {
        Objects.requireNonNull(head, "head");
        Objects.requireNonNull(commandId, "commandId");
        Objects.requireNonNull(quote, "quote");
        FateCardLifecycleHistory.validate(head, seedLifecycle, seedLedger, seedOwnership);
        var journal = head.cardPurchases().orElseThrow();
        if (journal.receipts().containsKey(commandId))
        {
            return new Result(head, journal.replay(identity, head.lifecycle().revision(),
                commandId, expectedRevision, quote), true);
        }
        var purchase = FateCardPurchasePreparation.prepareFresh(head, identity, commandId,
            expectedRevision, quote);
        var receipt = purchase.receipt();
        var retained = new ArrayList<>(journal.receipts().values());
        retained.add(receipt);
        var next = purchase.lifecycle();
        var proposed = new RunDocument(next, head.catalogVersion(), head.receipts(),
            Optional.of(purchase.ledger()), head.inventory(), head.grandFate(),
            head.grandProgress(), head.grandJournal(), head.grandActivations(),
            head.retainedGrandAttempt(next), head.pendingMasterSelection(), head.masterCreations(),
            head.masterResolutions(), head.masterSpins().map(spins -> new MasterSpinState(
                spins.version(), spins.identity(), next.revision(), spins.allotted(), spins.reservation())),
            head.currentMasterSpecial().map(value -> value.obligation()), Optional.of(purchase.ownership()),
            Optional.of(new FateCardPurchaseJournal(identity, next.revision(), retained)),
            head.currentQuestAssignment().map(value -> value.assignment()), head.questCompletionHistory());
        FateCardLifecycleHistory.validate(proposed, seedLifecycle, seedLedger, seedOwnership);
        return new Result(proposed, receipt, false);
    }
}
