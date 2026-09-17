package com.wheelbound;

import java.util.List;
import java.util.Random;

final class WheelSelection
{
    private WheelSelection() {}

    static int totalWeight(List<WheelEntry> entries)
    {
        int total = 0;
        for (WheelEntry entry : entries) { total = Math.addExact(total, entry.weight); }
        return total;
    }

    static int select(List<WheelEntry> entries, Random random)
    {
        int total = totalWeight(entries);
        return total == 0 ? -1 : indexAt(entries, random.nextInt(total));
    }

    static int indexAt(List<WheelEntry> entries, double ticket)
    {
        if (ticket < 0 || ticket >= totalWeight(entries)) { return -1; }
        for (int i = 0; i < entries.size(); i++)
        {
            ticket -= entries.get(i).weight;
            if (ticket < 0) { return i; }
        }
        return -1;
    }

    static double center(List<WheelEntry> entries, int selected)
    {
        double before = 0;
        for (int i = 0; i < selected; i++) { before += entries.get(i).weight; }
        return (before + entries.get(selected).weight / 2.0) * 360 / totalWeight(entries);
    }

    static double targetAngle(double start, List<WheelEntry> entries, int selected)
    {
        double desired = center(entries, selected) - 90;
        return start + ((desired - start) % 360 + 360) % 360 + 5 * 360;
    }
}
