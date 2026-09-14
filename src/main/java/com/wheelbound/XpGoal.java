package com.wheelbound;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/** Percent weights sum to 100. The million-XP wedge is deliberately only 1%. */
enum XpGoal
{
    TEN_THOUSAND(10_000, 35), TWENTY_FIVE_THOUSAND(25_000, 27),
    FIFTY_THOUSAND(50_000, 20), HUNDRED_THOUSAND(100_000, 10),
    QUARTER_MILLION(250_000, 5), HALF_MILLION(500_000, 2), MILLION(1_000_000, 1);

    final int xp;
    final int weight;
    XpGoal(int xp, int weight) { this.xp = xp; this.weight = weight; }
    WheelEntry entry()
    {
        return new WheelEntry(name(), String.format(Locale.US, "Gain %,d XP", xp), null, weight);
    }
    static List<WheelEntry> entries()
    {
        return Arrays.stream(values()).map(XpGoal::entry).collect(Collectors.toUnmodifiableList());
    }
}
