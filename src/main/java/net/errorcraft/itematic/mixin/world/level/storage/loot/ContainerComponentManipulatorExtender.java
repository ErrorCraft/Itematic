package net.errorcraft.itematic.mixin.world.level.storage.loot;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.access.world.level.storage.loot.ContainerComponentManipulatorAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.ContainerComponentManipulator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.stream.Stream;

@Mixin(ContainerComponentManipulator.class)
public interface ContainerComponentManipulatorExtender<T> extends ContainerComponentManipulatorAccess<T> {
    @Shadow
    T setContents(T component, Stream<ItemStack> contents);

    @WrapOperation(
        method = "setContents(Lnet/minecraft/world/item/ItemStack;Ljava/lang/Object;Ljava/util/stream/Stream;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/storage/loot/ContainerComponentManipulator;setContents(Ljava/lang/Object;Ljava/util/stream/Stream;)Ljava/lang/Object;"
        )
    )
    private T useStackAwareVersion(ContainerComponentManipulator<T> instance, T component, Stream<ItemStack> newContents, Operation<T> original, @Local(name = "itemStack", argsOnly = true) ItemStack itemStack) {
        return this.itematic$setContents(itemStack, component, newContents);
    }

    default T itematic$setContents(ItemStack stack, T component, Stream<ItemStack> newContents) {
        return this.setContents(component, newContents);
    }
}
