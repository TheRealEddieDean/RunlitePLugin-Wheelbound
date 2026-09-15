package com.wheelbound;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/** Profile-local custom lists. Stable IDs keep duplicate labels and checkbox state independent. */
final class CustomWheels
{
    static final String KEY = "customWheels";
    private int version = 1;
    private List<CustomWheel> wheels = new ArrayList<>();

    static final class Item
    {
        String id = UUID.randomUUID().toString();
        String name;
        boolean enabled = true;
        Item(String name) { this.name = name; }
    }

    static final class CustomWheel
    {
        String id = UUID.randomUUID().toString();
        String name;
        List<Item> items = new ArrayList<>();
        CustomWheel(String name) { this.name = name; }
        List<WheelEntry> entries()
        { return items.stream().map(i -> new WheelEntry("CUSTOM_" + i.id, i.name, null, 1)).collect(Collectors.toList()); }
        String exclusions()
        { return items.stream().filter(i -> !i.enabled).map(i -> "CUSTOM_" + i.id).collect(Collectors.joining(",")); }
        @Override public String toString() { return "Custom: " + name; }
    }

    List<CustomWheel> wheels() { return List.copyOf(wheels); }
    CustomWheel find(String id) { return wheels.stream().filter(w -> w.id.equals(id)).findFirst().orElse(null); }
    CustomWheel create(String name)
    {
        String clean = text(name, 80);
        if (wheels.stream().anyMatch(w -> w.name.equalsIgnoreCase(clean)))
        { throw new IllegalArgumentException("A custom wheel already has that name."); }
        CustomWheel wheel = new CustomWheel(clean); wheels.add(wheel); return wheel;
    }
    void add(CustomWheel wheel, String name) { wheel.items.add(new Item(text(name, 200))); }
    void remove(CustomWheel wheel) { wheels.remove(wheel); }
    String save() { return new Gson().toJson(this); }

    static CustomWheels load(String saved)
    {
        if (saved == null || saved.isBlank()) { return new CustomWheels(); }
        try
        {
            CustomWheels data = new Gson().fromJson(saved, CustomWheels.class);
            if (data == null || data.version != 1 || data.wheels == null) { throw new IllegalArgumentException(); }
            Set<String> ids = new HashSet<>();
            for (CustomWheel wheel : data.wheels)
            {
                if (wheel == null || wheel.items == null || !validId(wheel.id) || !ids.add(wheel.id))
                { throw new IllegalArgumentException(); }
                wheel.name = text(wheel.name, 80);
                for (Item item : wheel.items)
                {
                    if (item == null || !validId(item.id) || !ids.add(item.id)) { throw new IllegalArgumentException(); }
                    item.name = text(item.name, 200);
                }
            }
            return data;
        }
        catch (RuntimeException ex)
        { throw new IllegalArgumentException("Saved custom wheels could not be read. Your saved data has been kept.", ex); }
    }

    private static boolean validId(String id)
    { return id != null && id.matches("[a-zA-Z0-9-]+") && id.length() <= 80; }
    private static String text(String name, int limit)
    {
        if (name == null || name.isBlank()) { throw new IllegalArgumentException("Enter a name first."); }
        String clean = name.strip();
        if (clean.length() > limit) { throw new IllegalArgumentException("Use " + limit + " characters or fewer."); }
        if (clean.codePoints().anyMatch(Character::isISOControl))
        { throw new IllegalArgumentException("Use a single line of text."); }
        return clean;
    }
}
