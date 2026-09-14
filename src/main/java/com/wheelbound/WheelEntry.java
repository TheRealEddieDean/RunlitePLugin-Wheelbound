package com.wheelbound;

import java.awt.image.BufferedImage;
import java.util.Objects;

/** Immutable presentation entry; the same weight drives selection and wedge geometry. */
final class WheelEntry
{
    final String id;
    final String label;
    final BufferedImage icon;
    final int weight;

    WheelEntry(String id, String label, BufferedImage icon, int weight)
    {
        if (weight <= 0) { throw new IllegalArgumentException("Weight must be positive"); }
        this.id = Objects.requireNonNull(id);
        this.label = Objects.requireNonNull(label);
        this.icon = icon;
        this.weight = weight;
    }
}
