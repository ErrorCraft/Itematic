package net.errorcraft.itematic.mixin.world.item.component;

import net.errorcraft.itematic.access.world.item.component.ContainerComponentAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ContainerComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.stream.Stream;

@Mixin(ContainerComponent.class)
public interface ContainerComponentExtender<T extends ContainerComponent<T>> extends ContainerComponentAccess<T> {
    @Shadow
    T copyWithContents(Stream<ItemStack> newContents);

    @Override
    default T itematic$copyWithContents(ItemStack stack, Stream<ItemStack> newContents) {
        return this.copyWithContents(newContents);
    }
}
