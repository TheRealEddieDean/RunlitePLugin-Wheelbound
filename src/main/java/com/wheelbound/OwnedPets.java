package com.wheelbound;

import java.util.HashSet;
import java.util.Set;
import net.runelite.api.Client;
import net.runelite.api.EnumComposition;
import net.runelite.api.gameval.VarPlayerID;
import net.runelite.client.game.ItemVariationMapping;

/** Probita's unlocked-pet bitmasks, including pets currently reclaimable. */
final class OwnedPets
{
    static final int PET_ITEMS = 985;

    static Set<Integer> read(Client client)
    {
        EnumComposition pets = client.getEnum(PET_ITEMS);
        if (pets == null || pets.getKeys() == null || pets.getKeys().length == 0)
        { throw new IllegalStateException("Pet data unavailable"); }
        int[] flags = {client.getVarpValue(VarPlayerID.PET_INSURANCE_BITMASK1),
            client.getVarpValue(VarPlayerID.PET_INSURANCE_BITMASK2),
            client.getVarpValue(VarPlayerID.PET_INSURANCE_BITMASK3)};
        Set<Integer> owned = new HashSet<>();
        for (int index : pets.getKeys())
        {
            if (index >= 0 && index < 93 && (flags[index / 31] & (1 << (index % 31))) != 0)
            { owned.add(ItemVariationMapping.map(pets.getIntValue(index))); }
        }
        return Set.copyOf(owned);
    }

    static boolean contains(Set<Integer> owned, PetDefinition pet)
    { return owned.contains(ItemVariationMapping.map(pet.itemId)); }
}
