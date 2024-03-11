package net.errorcraft.itematic.component;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.registry.entry.RegistryEntry;

public class PotionContentsComponentUtil {
    private PotionContentsComponentUtil() {}

    public static ItemStack setPotion(ItemStack stack, RegistryEntry<Potion> potion) {
        stack.set(DataComponentTypes.POTION_CONTENTS, new PotionContentsComponent(potion));
        return stack;
    }
}
