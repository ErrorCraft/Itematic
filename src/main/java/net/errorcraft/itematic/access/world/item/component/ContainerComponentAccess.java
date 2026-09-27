package net.errorcraft.itematic.access.world.item.component;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ContainerComponent;

import java.util.stream.Stream;

public interface ContainerComponentAccess<T extends ContainerComponent<T>> {
    default T itematic$copyWithContents(ItemStack stack, Stream<ItemStack> newContents) {
        throw new AssertionError("Implemented via mixin");
    }
}
