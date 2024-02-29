package net.errorcraft.itematic.access.loot.function;

import net.minecraft.potion.Potion;
import net.minecraft.registry.entry.RegistryEntryList;

public interface SetPotionLootFunctionAccess {
    void itematic$setPotions(RegistryEntryList<Potion> potions);
}
