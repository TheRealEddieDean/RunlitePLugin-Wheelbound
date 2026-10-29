package com.wheelbound.domain;

import java.util.*;
import static com.wheelbound.domain.ActiveWheel.*;
import static com.wheelbound.domain.SliceInventory.*;

/** Executable without build dependencies; also exercised by the focused JUnit suite. */
public final class SlicePurchaseProposalChecks
{
    public static void main(String[] args)
    {
        successfulProposals();
        rejectedProposals();
        System.out.println("SlicePurchaseProposalChecks passed");
    }

    static void successfulProposals()
    {
        SliceInventory seed = inventory();
        RunLifecycle run = run(7, RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE);
        FatePointLedger ledger = new FatePointLedger(20, List.of());
        UUID command = UUID.randomUUID();
        UUID instance = UUID.randomUUID();
        SlicePurchaseProposal first = propose(run, ledger, seed, command, instance, quote("new", 0, 5));
        check(first.lifecycle().revision() == 8 && first.ledger().balance() == 15);
        check(first.inventory().activeWheel().slices().size() == 6);
        Slice acquired = first.inventory().activeWheel().slices().get(5);
        check(acquired.id().equals(instance) && acquired.enhancement() == Enhancement.BASE);
        check(first.debit().commandId().equals(command) && first.debit().beforeRevision() == 7);
        check(first.debit().afterRevision() == 8 && first.debit().balance() == 15);
        check(first.quote().activity().equals("new") && first.instanceId().equals(instance));
        check(first.inventory().acquisitionHistory().get("new").paidDuplicates() == 0);
        SlicePurchaseProposal second = propose(first.lifecycle(), first.ledger(), first.inventory(),
            UUID.randomUUID(), UUID.randomUUID(), quote("new", 1, 15));
        check(second.ledger().balance() == 0 && second.lifecycle().revision() == 9);
        check(second.inventory().acquisitionHistory().get("new").paidDuplicates() == 1);
        SlicePurchaseProposal reacquired = propose(run, ledger, seed, UUID.randomUUID(), UUID.randomUUID(),
            quote("destroyed", 5, 3));
        check(reacquired.inventory().acquisitionHistory().get("destroyed").paidDuplicates() == 5);
        check(seed.activeWheel().slices().size() == 5 && !seed.acquisitionHistory().containsKey("new"));
        check(ledger.balance() == 20 && ledger.entries().isEmpty() && run.revision() == 7);
        // Repeating a proposal leaves the seed unchanged; this API does not claim durable replay.
        check(propose(run, ledger, seed, command, instance, quote("new", 0, 5)).ledger().balance() == 15);
    }

    static void rejectedProposals()
    {
        SliceInventory seed = inventory();
        RunLifecycle run = run(7, RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE);
        FatePointLedger ledger = new FatePointLedger(20, List.of());
        for (SlicePurchaseProposal.Quote quote : List.of(quote("a", 0, 1), quote("a", 2, 1),
            quote("new", 1, 1), quote("destroyed", 1, 1), quote("locked", 0, 1), quote("new", 0, 21)))
        { rejects(() -> propose(run, ledger, seed, UUID.randomUUID(), UUID.randomUUID(), quote)); }
        for (RunLifecycle.State state : RunLifecycle.State.values())
        {
            if (state == RunLifecycle.State.ACTIVE) { continue; }
            rejects(() -> propose(run(7, state, RunLifecycle.Obligation.NONE), ledger, seed,
                UUID.randomUUID(), UUID.randomUUID(), quote("new", 0, 1)));
        }
        for (RunLifecycle.Obligation obligation : RunLifecycle.Obligation.values())
        {
            if (obligation == RunLifecycle.Obligation.NONE) { continue; }
            rejects(() -> propose(run(7, RunLifecycle.State.ACTIVE, obligation), ledger, seed,
                UUID.randomUUID(), UUID.randomUUID(), quote("new", 0, 1)));
        }
        rejects(() -> SlicePurchaseProposal.propose(run, ledger, seed,
            new RunIdentity("foreign", UUID.randomUUID()), UUID.randomUUID(), 7,
            UUID.randomUUID(), quote("new", 0, 1)));
        rejects(() -> SlicePurchaseProposal.propose(run, ledger, seed, run.identity(), UUID.randomUUID(), 6,
            UUID.randomUUID(), quote("new", 0, 1)));
        rejects(() -> propose(run, ledger, seed, UUID.randomUUID(), seed.activeWheel().slices().get(0).id(),
            quote("new", 0, 1)));
        List<Slice> full = new ArrayList<>(seed.activeWheel().slices());
        while (full.size() < CAPACITY) { full.add(Slice.special(UUID.randomUUID(), Kind.TAINT, 4)); }
        SliceInventory capacity = new SliceInventory(new ActiveWheel(full), List.of(), SatchelTier.BASE,
            seed.unlockedActivities(), seed.acquisitionHistory());
        rejects(() -> propose(run, ledger, capacity, UUID.randomUUID(), UUID.randomUUID(), quote("new", 0, 1)));
        try
        {
            propose(run(Long.MAX_VALUE, RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE),
                ledger, seed, UUID.randomUUID(), UUID.randomUUID(), quote("new", 0, 1));
            throw new AssertionError("Overflow accepted");
        }
        catch (ArithmeticException expected) { }
        rejects(() -> new SlicePurchaseProposal.Quote("new", "q", "v", 0, PurchaseKind.FIRST_INSTANCE, 0));
        rejects(() -> new SlicePurchaseProposal.Quote("new", "q", "v", 1, PurchaseKind.FIRST_INSTANCE, 1));
        check(ledger.entries().isEmpty() && seed.activeWheel().slices().size() == 5);
    }

    private static SlicePurchaseProposal propose(RunLifecycle run, FatePointLedger ledger,
        SliceInventory inventory, UUID command, UUID instance, SlicePurchaseProposal.Quote quote)
    {
        return SlicePurchaseProposal.propose(run, ledger, inventory, run.identity(), command,
            run.revision(), instance, quote);
    }

    private static SlicePurchaseProposal.Quote quote(String activity, long ordinal, long amount)
    {
        return new SlicePurchaseProposal.Quote(activity, "fixture-" + activity, "fixture-v1", amount,
            ordinal == 0 ? PurchaseKind.FIRST_INSTANCE : PurchaseKind.PAID_DUPLICATE, ordinal);
    }

    private static RunLifecycle run(long revision, RunLifecycle.State state, RunLifecycle.Obligation obligation)
    { return new RunLifecycle(new RunIdentity("fixture", UUID.randomUUID()), revision, state, obligation); }

    private static SliceInventory inventory()
    {
        List<Slice> slices = new ArrayList<>();
        for (String activity : List.of("a", "a", "b", "b", "c"))
        { slices.add(Slice.ordinary(UUID.randomUUID(), activity, Enhancement.BASE)); }
        return new SliceInventory(new ActiveWheel(slices), List.of(), SatchelTier.BASE,
            Set.of("a", "b", "c", "new", "destroyed"), Map.of("a", new AcquisitionHistory(0),
                "b", new AcquisitionHistory(0), "c", new AcquisitionHistory(0), "destroyed", new AcquisitionHistory(4)));
    }

    private static void rejects(Runnable action)
    {
        try { action.run(); }
        catch (IllegalArgumentException expected) { return; }
        throw new AssertionError("Invalid proposal accepted");
    }

    private static void check(boolean condition)
    { if (!condition) { throw new AssertionError("Purchase invariant failed"); } }
}
