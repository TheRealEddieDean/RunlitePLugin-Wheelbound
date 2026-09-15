package com.wheelbound;

/** Sidebar wheel registry. Each wheel owns its options and manual exclusions. */
enum WheelType
{
    BOSSING("Bossing", "excludedBosses"),
    SKILLING("Skilling", "excludedSkills"),
    COMBAT_ACHIEVEMENTS("Combat Achievements", "excludedCaEncounters"),
    PET_HUNTING("Pet Hunting", "excludedPets");

    final String title, exclusionKey;
    WheelType(String title, String exclusionKey) { this.title = title; this.exclusionKey = exclusionKey; }
    static WheelType fromSaved(String value)
    {
        for (WheelType type : values()) { if (type.title.equals(value)) { return type; } }
        return BOSSING;
    }
    @Override public String toString() { return title; }
}
