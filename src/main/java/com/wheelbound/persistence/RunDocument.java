package com.wheelbound.persistence;

import com.wheelbound.domain.RunLifecycle;
import java.util.Objects;

/** Version-one foundation snapshot, not the complete playable-mode aggregate. */
public final class RunDocument
{
    private final RunLifecycle lifecycle;
    private final String catalogVersion;

    public RunDocument(RunLifecycle lifecycle, String catalogVersion)
    {
        this.lifecycle = Objects.requireNonNull(lifecycle, "lifecycle");
        this.catalogVersion = Objects.requireNonNull(catalogVersion, "catalogVersion");
        if (catalogVersion.trim().isEmpty()) { throw new IllegalArgumentException("Empty catalog version"); }
    }

    public RunLifecycle lifecycle() { return lifecycle; }
    public String catalogVersion() { return catalogVersion; }
}
