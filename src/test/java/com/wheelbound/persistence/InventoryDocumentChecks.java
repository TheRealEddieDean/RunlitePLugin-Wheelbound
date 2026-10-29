package com.wheelbound.persistence;

import com.wheelbound.domain.*;
import java.util.*;

/** Dependency-free aggregate checks for environments without the Gradle distribution. */
public final class InventoryDocumentChecks
{
    public static void main(String[] args)
    {
        List<ActiveWheel.Slice> active = new ArrayList<>();
        Map<String, SliceInventory.AcquisitionHistory> history = new HashMap<>();
        for (String activity : List.of("mining", "mining", "fishing", "fishing", "combat"))
        {
            active.add(ActiveWheel.Slice.ordinary(UUID.randomUUID(), activity, ActiveWheel.Enhancement.SECOND));
            history.put(activity, new SliceInventory.AcquisitionHistory(2));
        }
        history.put("questing", new SliceInventory.AcquisitionHistory(4));
        history.put("destroyed", new SliceInventory.AcquisitionHistory(9));
        Set<String> unlocks = new HashSet<>(history.keySet()); unlocks.add("unlock-only");
        SliceInventory inventory = new SliceInventory(new ActiveWheel(active),
            List.of(ActiveWheel.Slice.ordinary(UUID.randomUUID(), "questing", ActiveWheel.Enhancement.FOURTH)),
            SliceInventory.SatchelTier.THIRD, unlocks, history);
        RunLifecycle run = new RunLifecycle(new RunIdentity("account:profile", UUID.randomUUID()), 0,
            RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE);
        RunDocument document = new RunDocument(run, "v1", List.of(), Optional.empty(), Optional.of(inventory));
        if (document.inventory().orElseThrow() != inventory
            || document.inventory().get().classifyNewPurchase("destroyed").duplicateOrdinal() != 10
            || document.inventory().get().classifyNewPurchase("unlock-only").kind() != SliceInventory.PurchaseKind.FIRST_INSTANCE
            || document.inventory().get().stored().get(0).enhancement() != ActiveWheel.Enhancement.FOURTH
            || new RunDocument(run, "v1").inventory().isPresent())
        { throw new AssertionError("Inventory availability or lifetime evidence lost"); }
        try
        {
            new RunDocument(run, "v1", List.of(), Optional.empty(), null);
            throw new AssertionError("Null optional accepted");
        }
        catch (NullPointerException expected) { }
        System.out.println("InventoryDocumentChecks passed");
    }
}
