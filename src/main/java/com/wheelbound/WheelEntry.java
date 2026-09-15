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
    final String source;
    final BufferedImage sourceIcon;

    WheelEntry(String id, String label, BufferedImage icon, int weight)
    {
        this(id, label, icon, weight, null, null);
    }

    WheelEntry(String id, String label, BufferedImage icon, int weight, String source, BufferedImage sourceIcon)
    {
        this.source = source; this.sourceIcon = sourceIcon;
        if (weight <= 0) { throw new IllegalArgumentException("Weight must be positive"); }
        this.id = Objects.requireNonNull(id);
        this.label = Objects.requireNonNull(label);
        this.icon = icon;
        this.weight = weight;
    }

    /** Presentation grouping happens after mode-specific eligibility and CA task filtering. */
    static java.util.List<WheelEntry> groupRaids(java.util.List<WheelEntry> entries)
    {
        java.util.Map<String, WheelEntry> grouped = new java.util.LinkedHashMap<>();
        for (WheelEntry entry : entries)
        {
            String name = entry.label;
            for (String raid : java.util.List.of("Chambers of Xeric", "Theatre of Blood", "Tombs of Amascut"))
            {
                if (name.startsWith(raid)) { name = raid; break; }
            }
            String id = name.equals("Chambers of Xeric") ? "CHAMBERS_OF_XERIC"
                : name.equals("Theatre of Blood") ? "THEATRE_OF_BLOOD"
                : name.equals("Tombs of Amascut") ? "TOMBS_OF_AMASCUT" : entry.id;
            grouped.putIfAbsent(id, id.equals(entry.id) && name.equals(entry.label) ? entry
                : new WheelEntry(id, name, entry.icon, entry.weight, entry.source, entry.sourceIcon));
        }
        return java.util.List.copyOf(grouped.values());
    }

    String wheelLabel()
    {
        switch (label)
        {
            case "Chambers of Xeric": return "CoX";
            case "Theatre of Blood": return "ToB";
            case "Tombs of Amascut": return "ToA";
            case "Dagannoth Supreme": return "DK Supreme";
            case "Dagannoth Prime": return "DK Prime";
            case "Dagannoth Rex": return "DK Rex";
            case "The Corrupted Gauntlet": return "Corrupted G.";
            case "Thermonuclear Smoke Devil": return "Thermy";
            case "Grotesque Guardians": return "Guardians";
            case "Phantom Muspah": return "Muspah";
            case "Alchemical Hydra": return "Hydra";
            case "Phosani's Nightmare": return "Phosani";
            case "Corporeal Beast": return "Corp";
            case "Commander Zilyana": return "Zilyana";
            case "General Graardor": return "Graardor";
            case "Chaos Elemental": return "Chaos Ele";
            case "Chaos Fanatic": return "Fanatic";
            case "Crazy Archaeologist": return "Crazy Arch.";
            case "Deranged Archaeologist": return "Deranged A.";
            case "The Royal Titans": return "Royal Titans";
            case "Doom of Mokhaiotl": return "Doom";
            case "Shellbane Gryphon": return "Gryphon";
            default: return label.startsWith("The ") ? label.substring(4) : label;
        }
    }
}
