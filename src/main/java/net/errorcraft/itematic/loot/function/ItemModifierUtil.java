package net.errorcraft.itematic.loot.function;

import net.errorcraft.itematic.access.loot.function.SetPotionLootFunctionAccess;
import net.errorcraft.itematic.mixin.loot.function.SetPotionLootFunctionAccessor;
import net.minecraft.loot.function.SetPotionLootFunction;
import net.minecraft.potion.Potion;
import net.minecraft.registry.entry.RegistryEntryList;

import java.util.List;

public class ItemModifierUtil {
    private ItemModifierUtil() {}

    public static SetPotionLootFunction setPotion(RegistryEntryList<Potion> potions) {
        SetPotionLootFunction itemModifier = SetPotionLootFunctionAccessor.create(List.of(), null);
        ((SetPotionLootFunctionAccess) itemModifier).itematic$setPotions(potions);
        return itemModifier;
    }
}
