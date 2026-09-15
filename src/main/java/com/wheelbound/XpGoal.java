package com.wheelbound;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/** Percent weights sum to 100. The million-XP wedge is deliberately only 1%. */
enum XpGoal
{
    TEN_THOUSAND(10_000, 35, 0x338A48), TWENTY_FIVE_THOUSAND(25_000, 27, 0x337CB5),
    FIFTY_THOUSAND(50_000, 20, 0xA8A332), HUNDRED_THOUSAND(100_000, 10, 0xC79827),
    QUARTER_MILLION(250_000, 5, 0xCC752B), HALF_MILLION(500_000, 2, 0xBD4D2F), MILLION(1_000_000, 1, 0xAF3038);

    final int xp;
    final int weight;
    final java.awt.Color color;
    XpGoal(int xp, int weight, int color) { this.xp = xp; this.weight = weight; this.color = new java.awt.Color(color); }
    WheelEntry entry()
    {
        return new WheelEntry(name(), String.format(Locale.US, "Gain %,d XP", xp), null, weight);
    }
    static List<WheelEntry> entries()
    {
        return Arrays.stream(values()).map(XpGoal::entry).collect(Collectors.toUnmodifiableList());
    }
}
