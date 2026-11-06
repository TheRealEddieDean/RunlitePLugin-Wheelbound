package com.wheelbound.domain;

import java.util.ArrayList;
import java.util.List;

/** Dependency-free payload checks; fixture IDs are not an approved quest catalog. */
public final class QuestFateOffersChecks
{
    public static void main(String[] args)
    {
        for (QuestFateOffers.Capacity capacity : QuestFateOffers.Capacity.values())
        {
            ArrayList<String> source = new ArrayList<>(
                List.of("quest.a", "quest.b", "quest.c", "quest.d", "quest.e")
                    .subList(0, capacity.count()));
            QuestFateOffers offers = new QuestFateOffers(capacity, "v1", source);
            QuestFateOffers restored = new QuestFateOffers(capacity, "v1", List.copyOf(source));
            check(offers.capacity() == capacity && offers.catalogVersion().equals("v1"));
            check(offers.equals(restored) && restored.equals(offers)
                && offers.hashCode() == restored.hashCode());
            for (String id : source)
            {
                QuestFateOffers.Selection selected = offers.select(id);
                check(selected.offers() == offers && selected.questId().equals(id));
                QuestFateOffers.Selection replay = new QuestFateOffers.Selection(restored, id);
                check(selected.equals(replay) && selected.hashCode() == replay.hashCode());
                check(!selected.equals(null) && !selected.equals(offers));
            }
            check(!offers.select(source.get(0)).equals(offers.select(source.get(1))));
            ArrayList<String> reversed = new ArrayList<>(source);
            java.util.Collections.reverse(reversed);
            QuestFateOffers reordered = new QuestFateOffers(capacity, "v1", reversed);
            QuestFateOffers versioned = new QuestFateOffers(capacity, "v2", source);
            check(!offers.equals(reordered) && !offers.equals(versioned));
            check(!offers.select("quest.a").equals(reordered.select("quest.a")));
            check(!offers.select("quest.a").equals(versioned.select("quest.a")));
            ArrayList<String> changed = new ArrayList<>(source);
            changed.set(1, "quest.foreign");
            check(!offers.select("quest.a").equals(
                new QuestFateOffers(capacity, "v1", changed).select("quest.a")));
            ArrayList<String> duplicate = new ArrayList<>(source);
            duplicate.set(1, duplicate.get(0));
            invalid(() -> new QuestFateOffers(capacity, "v1", duplicate));
            invalid(() -> new QuestFateOffers(capacity, "v1", source.subList(1, source.size())));
            ArrayList<String> extra = new ArrayList<>(source);
            extra.add("quest.extra");
            invalid(() -> new QuestFateOffers(capacity, "v1", extra));
            for (String bad : new String[] {null, "", " ", " quest.a", "quest.a ", "\tquest.a"})
            {
                invalid(() -> offers.select(bad));
                invalid(() -> new QuestFateOffers(capacity, bad, source));
                ArrayList<String> malformed = new ArrayList<>(source);
                malformed.set(0, bad);
                invalid(() -> new QuestFateOffers(capacity, "v1", malformed));
            }
            invalid(() -> offers.select("quest.absent"));
            invalid(() -> new QuestFateOffers(capacity, "v1", List.of()));
            invalid(() -> new QuestFateOffers(capacity, "v1", null));
            try { offers.questIds().clear(); throw new AssertionError("Mutable snapshot"); }
            catch (UnsupportedOperationException expected) { }
            QuestFateOffers.Selection selection = offers.select("quest.a");
            source.clear();
            check(offers.equals(restored) && selection.equals(restored.select("quest.a")));
            check(!offers.equals(null) && !offers.equals("offers"));
        }
        invalid(() -> new QuestFateOffers(null, "v1", List.of("a", "b", "c")));
        invalid(() -> new QuestFateOffers.Selection(null, "quest.a"));
        System.out.println("QuestFateOffersChecks PASSED");
    }

    private static void check(boolean condition)
    { if (!condition) { throw new AssertionError("Frozen quest offers mismatch"); } }

    private static void invalid(Runnable action)
    {
        try { action.run(); }
        catch (IllegalArgumentException | NullPointerException expected) { return; }
        throw new AssertionError("Invalid quest payload accepted");
    }
}
