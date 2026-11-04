package com.wheelbound.persistence;

import com.wheelbound.domain.FateCardOwnership;
import com.wheelbound.domain.FatePointLedger;
import com.wheelbound.domain.RunLifecycle;
import java.util.HashSet;
import java.util.Objects;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;

/** Pure reconstruction of a complete card/Pause/Resume segment from a trusted seed.
 * This restricted validator does not authenticate the seed or quotes, admit other
 * gameplay histories, publish a purchase, or authorize storage or feature activation.
 */
public final class FateCardLifecycleHistory
{
    private FateCardLifecycleHistory() { }

    /**
     * Every retained event must belong to the segment beginning at seedLifecycle;
     * no missing revision or unexplained FP entry is allowed. Other activity state
     * is refused conservatively, including domain-only history without envelopes.
     * Callers must establish the original seed's authority independently.
     * RunDocument currently permits only a contiguous generic receipt suffix, so
     * interleaved card and lifecycle retention needs a separate aggregate change.
     */
    public static void validate(RunDocument head, RunLifecycle seedLifecycle,
        FatePointLedger seedLedger, FateCardOwnership seedOwnership)
    {
        Objects.requireNonNull(head, "head");
        Objects.requireNonNull(seedLifecycle, "seedLifecycle");
        Objects.requireNonNull(seedLedger, "seedLedger");
        Objects.requireNonNull(seedOwnership, "seedOwnership");
        var identity = head.lifecycle().identity();
        if (!identity.equals(seedLifecycle.identity()) || !identity.equals(seedOwnership.identity())
            || seedLifecycle.revision() > head.lifecycle().revision())
        { throw new IllegalArgumentException("Foreign or future card history seed"); }
        if (head.pendingMasterSelection().isPresent() || head.currentMasterSpecial().isPresent()
            || head.masterCreations().isPresent() || head.masterResolutions().isPresent()
            || head.grandActivations().isPresent() || head.grandCurrentAttempt().isPresent()
            || head.grandJournal().isPresent() || head.grandProgress().isPresent()
            || head.masterSpins().filter(s -> s.reservation().isPresent()).isPresent())
        { throw new IllegalArgumentException("Unsupported activity context in lifecycle-only card history"); }
        var journal = head.cardPurchases().orElseThrow(() ->
            new IllegalArgumentException("Explicit card journal required"));
        var headOwnership = head.fateCards().orElseThrow(() ->
            new IllegalArgumentException("Explicit card ownership required"));
        var headLedger = head.fatePoints().orElseThrow(() ->
            new IllegalArgumentException("Explicit FP accounting required"));
        var lifecycleEvents = new TreeMap<Long, RunReceipt>();
        var cardEvents = new TreeMap<Long, com.wheelbound.domain.FateCardPurchaseReceipt>();
        var revisions = new TreeSet<Long>();
        var commands = new HashSet<UUID>();
        for (var receipt : head.receipts())
        {
            if (receipt.operation() != RunReceipt.Operation.PAUSE
                && receipt.operation() != RunReceipt.Operation.RESUME)
            { throw new IllegalArgumentException("Unsupported operation in lifecycle-only card history"); }
            retain(revisions, commands, receipt.beforeRevision(), receipt.commandId());
            lifecycleEvents.put(receipt.beforeRevision(), receipt);
        }
        for (var receipt : journal.receipts().values())
        {
            retain(revisions, commands, receipt.debit().beforeRevision(), receipt.debit().commandId());
            cardEvents.put(receipt.debit().beforeRevision(), receipt);
        }
        var lifecycle = seedLifecycle;
        var ledger = seedLedger;
        var ownership = seedOwnership;
        for (long revision : revisions)
        {
            if (revision != lifecycle.revision())
            { throw new IllegalArgumentException("Missing or pre-seed card history operation"); }
            var event = lifecycleEvents.get(revision);
            if (event != null)
            {
                var next = event.operation() == RunReceipt.Operation.PAUSE
                    ? lifecycle.pause(identity, revision) : lifecycle.resume(identity, revision);
                if (next.revision() != event.afterRevision() || next.state() != event.state()
                    || next.obligation() != event.obligation())
                { throw new IllegalArgumentException("Lifecycle receipt disagrees with original state"); }
                lifecycle = next;
            }
            else
            {
                var result = cardEvents.get(revision).reconstruct(lifecycle, ledger, ownership);
                lifecycle = result.lifecycle();
                ledger = result.ledger();
                ownership = result.ownership();
            }
        }
        if (lifecycle.revision() != head.lifecycle().revision()
            || lifecycle.state() != head.lifecycle().state()
            || lifecycle.obligation() != head.lifecycle().obligation()
            || !ownership.equals(headOwnership))
        { throw new IllegalArgumentException("Card history disagrees with aggregate head"); }
        if (ledger.startingBalance() != headLedger.startingBalance()
            || ledger.entries().size() != headLedger.entries().size())
        { throw new IllegalArgumentException("Unexplained or missing card accounting"); }
        for (int i = 0; i < ledger.entries().size(); i++)
        {
            var expected = ledger.entries().get(i);
            var actual = headLedger.entries().get(i);
            if (expected.kind() != actual.kind() || expected.amount() != actual.amount())
            { throw new IllegalArgumentException("Card accounting history was rewritten"); }
        }
    }

    private static void retain(TreeSet<Long> revisions, HashSet<UUID> commands,
        long revision, UUID command)
    {
        if (!revisions.add(revision) || !commands.add(command))
        { throw new IllegalArgumentException("Overlapping card history operation"); }
    }
}
