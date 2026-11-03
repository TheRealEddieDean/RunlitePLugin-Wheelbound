package com.wheelbound.persistence;

import java.util.Objects;
import java.util.Optional;

/** Read-only exact-response recovery; absence never authorizes drawing or admission. */
public final class MasterRetryRecovery
{
    private MasterRetryRecovery() { }

    /** Requires the original immutable payload, not a newly sampled replacement.
     * Returns the retained response even when the current head has advanced or paused.
     * This performs no storage access or current-evidence authentication.
     */
    public static Optional<MasterRetryOperationProposal> find(RunDocument current,
        MasterRetryOperationProposal request)
    {
        Objects.requireNonNull(current, "current");
        Objects.requireNonNull(request, "request");
        if (!current.lifecycle().identity().equals(request.identity()))
        { throw new IllegalArgumentException("Foreign Master retry request"); }
        for (var receipt : current.receipts())
        {
            if (!receipt.commandId().equals(request.commandId())) { continue; }
            var retained = receipt.masterRetry();
            if (retained == null || !retained.equals(request))
            { throw new IllegalArgumentException("Retry command reused for different request"); }
            return Optional.of(retained.validateRetained(current));
        }
        var id = request.commandId();
        if (current.masterCreations().filter(h -> h.receipts().containsKey(id)).isPresent()
            || current.masterResolutions().filter(h -> h.receipts().containsKey(id)).isPresent()
            || current.grandFate().filter(s -> s.commandId().equals(id)).isPresent()
            || current.grandActivations().filter(h -> h.receipts().containsKey(id)).isPresent()
            || current.grandProgress().filter(p -> p.evidence().values().stream()
                .anyMatch(e -> e.receiptId().equals(id))).isPresent()
            || current.pendingMasterSelection().filter(p -> p.transactionId().equals(id)).isPresent())
        { throw new IllegalArgumentException("Command exists without original retry response"); }
        return Optional.empty();
    }
}
