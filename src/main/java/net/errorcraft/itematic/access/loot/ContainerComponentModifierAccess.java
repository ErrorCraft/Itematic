package net.errorcraft.itematic.access.loot;

import net.minecraft.world.item.ItemStack;

import java.util.stream.Stream;

public interface ContainerComponentModifierAccess<T> {
    T itematic$apply(ItemStack stack, T component, Stream<ItemStack> newContents);
}
