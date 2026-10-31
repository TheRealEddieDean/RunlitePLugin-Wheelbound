package com.wheelbound.persistence;

import com.wheelbound.domain.MasterSelectionCreationReceipt;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/** Read-only exact-request recovery. Absence never authorizes a new selection. */
public final class MasterCreationRecovery
{
    private MasterCreationRecovery() { }

    public static Optional<MasterSelectionCreationReceipt> find(RunDocument current,
        MasterSelectionCreationReceipt request)
    {
        Objects.requireNonNull(current, "current");
        Objects.requireNonNull(request, "request");
        if (!current.lifecycle().identity().equals(request.identity()))
        { throw new IllegalArgumentException("Foreign Master creation request"); }
        UUID id = request.transactionId();
        if (current.masterCreations().isPresent()
            && current.masterCreations().get().receipts().containsKey(id))
        {
            return Optional.of(current.masterCreations().get().replay(request,
                current.lifecycle().identity(), current.lifecycle().revision()));
        }
        if (current.masterResolutions().filter(h -> h.receipts().containsKey(id)).isPresent()
            || current.receipts().stream().anyMatch(r -> r.commandId().equals(id))
            || current.grandFate().filter(s -> s.commandId().equals(id)).isPresent()
            || current.grandActivations().filter(h -> h.receipts().containsKey(id)).isPresent()
            || current.grandProgress().filter(p -> p.evidence().values().stream()
                .anyMatch(e -> e.receiptId().equals(id))).isPresent()
            || current.pendingMasterSelection().filter(p -> p.transactionId().equals(id)).isPresent())
        { throw new IllegalArgumentException("Transaction exists without a matching original creation"); }
        return Optional.empty();
    }
}
