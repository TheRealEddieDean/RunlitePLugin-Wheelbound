package com.wheelbound.domain;

import java.util.*;

/** Dependency-free purchase checks, also invoked by JUnit. All prices are fixtures. */
public final class FateCardPurchaseProposalChecks
{
    public static void main(String[] args)
    {
        var id = new RunIdentity("cards", new UUID(181, 1));
        var foreign = new RunIdentity("other", id.runId());
        var command = new UUID(181, 2);
        var lifecycle = new RunLifecycle(id, 7, RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE);
        var ownership = FateCardOwnership.starting(id);
        var ledger = new FatePointLedger(11, List.of());
        for (var pool : FateCardOwnership.Pool.values())
        for (var type : List.of(FateCardOwnership.Type.LESSER, FateCardOwnership.Type.GREATER))
        {
            var quote = new FateCardPurchaseProposal.Quote(pool, type, "fixture-card", "fixture-v1", 11);
            var result = FateCardPurchaseProposal.propose(lifecycle, ledger, ownership, id, command, 7, quote);
            check(result.lifecycle().revision() == 8 && result.ledger().balance() == 0);
            check(result.lifecycle().identity().equals(id) && result.quote() == quote);
            check(result.debit().commandId().equals(command) && result.debit().beforeRevision() == 7
                && result.debit().afterRevision() == 8 && result.debit().amount() == 11
                && result.debit().balance() == 0 && result.debit().quoteKey().equals(quote.key())
                && result.debit().quoteVersion().equals(quote.version()));
            for (var p : FateCardOwnership.Pool.values())
            for (var t : FateCardOwnership.Type.values())
            { check(result.ownership().owns(p, t) == (ownership.owns(p, t) || p == pool && t == type)); }
            check(ledger.balance() == 11 && ledger.entries().isEmpty() && lifecycle.revision() == 7);
            check(ownership.equals(FateCardOwnership.starting(id)));
            rejects(() -> FateCardPurchaseProposal.propose(result.lifecycle(), result.ledger(),
                result.ownership(), id, command, 8, quote));
            rejects(() -> FateCardPurchaseProposal.propose(lifecycle, ledger, ownership, foreign, command, 7, quote));
            rejects(() -> FateCardPurchaseProposal.propose(lifecycle, ledger,
                FateCardOwnership.starting(foreign), id, command, 7, quote));
            rejects(() -> FateCardPurchaseProposal.propose(lifecycle, ledger, ownership, id, command, 6, quote));
            for (long balance : new long[] {-1, 0, 10})
            { rejects(() -> FateCardPurchaseProposal.propose(lifecycle, new FatePointLedger(balance, List.of()),
                ownership, id, command, 7, quote)); }
            for (var state : RunLifecycle.State.values())
            for (var obligation : RunLifecycle.Obligation.values())
            {
                if (state == RunLifecycle.State.ACTIVE && obligation == RunLifecycle.Obligation.NONE) { continue; }
                var blocked = new RunLifecycle(id, 7, state, obligation);
                rejects(() -> FateCardPurchaseProposal.propose(blocked, ledger, ownership, id, command, 7, quote));
            }
            rejects(() -> FateCardPurchaseProposal.propose(new RunLifecycle(id, Long.MAX_VALUE,
                RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE), ledger, ownership,
                id, command, Long.MAX_VALUE, quote));
            rejects(() -> FateCardPurchaseProposal.propose(lifecycle, ledger, ownership, id, null, 7, quote));
            // A proposal does not publish state; re-preparing the same trusted input is deterministic.
            check(FateCardPurchaseProposal.propose(lifecycle, ledger, ownership, id, command, 7, quote)
                .ownership().equals(result.ownership()));
        }
        var lesser = new FateCardPurchaseProposal.Quote(FateCardOwnership.Pool.BOSSING,
            FateCardOwnership.Type.LESSER, "fixture-lesser", "v1", 1);
        var greater = new FateCardPurchaseProposal.Quote(FateCardOwnership.Pool.BOSSING,
            FateCardOwnership.Type.GREATER, "fixture-greater", "v2", 1);
        var first = FateCardPurchaseProposal.propose(lifecycle, ledger, ownership, id, command, 7, lesser);
        var second = FateCardPurchaseProposal.propose(first.lifecycle(), first.ledger(), first.ownership(),
            id, new UUID(181, 3), 8, greater);
        check(second.ownership().hasBossingChallengeCardPrerequisites());
        check(second.ledger().balance() == 9 && second.ledger().entries().size() == 2);
        check(!first.ownership().hasBossingChallengeCardPrerequisites());
        var maximum = new FateCardPurchaseProposal.Quote(FateCardOwnership.Pool.SKILLING,
            FateCardOwnership.Type.GREATER, "fixture-max", "v1", Long.MAX_VALUE);
        check(FateCardPurchaseProposal.propose(lifecycle, new FatePointLedger(Long.MAX_VALUE, List.of()),
            ownership, id, command, 7, maximum).ledger().balance() == 0);
        for (long amount : new long[] {-1, 0})
        { rejects(() -> new FateCardPurchaseProposal.Quote(FateCardOwnership.Pool.BOSSING,
            FateCardOwnership.Type.LESSER, "fixture", "v1", amount)); }
        for (String text : new String[] {null, "", " ", " v1", "v1 "})
        {
            rejects(() -> new FateCardPurchaseProposal.Quote(FateCardOwnership.Pool.BOSSING,
                FateCardOwnership.Type.LESSER, text, "v1", 1));
            rejects(() -> new FateCardPurchaseProposal.Quote(FateCardOwnership.Pool.BOSSING,
                FateCardOwnership.Type.LESSER, "fixture", text, 1));
        }
        rejects(() -> new FateCardPurchaseProposal.Quote(FateCardOwnership.Pool.BOSSING,
            FateCardOwnership.Type.STANDARD, "fixture", "v1", 1));
        System.out.println("FateCardPurchaseProposalChecks PASSED");
    }

    private static void check(boolean condition)
    { if (!condition) { throw new AssertionError(); } }
    private static void rejects(Runnable action)
    {
        try { action.run(); }
        catch (IllegalArgumentException | NullPointerException | ArithmeticException expected) { return; }
        throw new AssertionError("Expected refusal");
    }
}
