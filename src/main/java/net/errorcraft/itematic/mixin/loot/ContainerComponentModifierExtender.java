package net.errorcraft.itematic.mixin.loot;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.access.loot.ContainerComponentModifierAccess;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.ContainerComponentModifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.stream.Stream;

@Mixin(ContainerComponentModifier.class)
public interface ContainerComponentModifierExtender<T> extends ContainerComponentModifierAccess<T> {
    @Shadow
    T apply(T component, Stream<ItemStack> contents);

    @Redirect(
        method = "apply(Lnet/minecraft/item/ItemStack;Ljava/lang/Object;Ljava/util/stream/Stream;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/loot/ContainerComponentModifier;apply(Ljava/lang/Object;Ljava/util/stream/Stream;)Ljava/lang/Object;"
        )
    )
    private T useStackAwareVersion(ContainerComponentModifier<T> instance, T component, Stream<ItemStack> newContents, @Local(argsOnly = true) ItemStack stack) {
        return this.itematic$apply(stack, component, newContents);
    }

    default T itematic$apply(ItemStack stack, T component, Stream<ItemStack> newContents) {
        return this.apply(component, newContents);
    }
}
