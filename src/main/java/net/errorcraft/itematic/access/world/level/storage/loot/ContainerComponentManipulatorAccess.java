package net.errorcraft.itematic.access.world.level.storage.loot;

import net.minecraft.world.item.ItemStack;

import java.util.stream.Stream;

public interface ContainerComponentManipulatorAccess<T> {
    T itematic$setContents(ItemStack stack, T component, Stream<ItemStack> newContents);
}
