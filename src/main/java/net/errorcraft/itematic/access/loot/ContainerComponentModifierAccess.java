package net.errorcraft.itematic.access.loot;

import net.minecraft.item.ItemStack;

import java.util.stream.Stream;

public interface ContainerComponentModifierAccess<T> {
    T itematic$create(ItemStack stack, T component, Stream<ItemStack> newContents);
}
