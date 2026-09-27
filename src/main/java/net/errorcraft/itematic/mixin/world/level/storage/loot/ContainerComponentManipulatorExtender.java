package net.errorcraft.itematic.mixin.world.level.storage.loot;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ContainerComponent;
import net.minecraft.world.level.storage.loot.ContainerComponentManipulator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.stream.Stream;

@Mixin(ContainerComponentManipulator.class)
public class ContainerComponentManipulatorExtender<T extends ContainerComponent<T>> {
    @WrapOperation(
        method = "setContents(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/component/ContainerComponent;Ljava/util/stream/Stream;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/component/ContainerComponent;copyWithContents(Ljava/util/stream/Stream;)Lnet/minecraft/world/item/component/ContainerComponent;"
        )
    )
    private T useStackAwareVersion(ContainerComponent<T> instance, Stream<ItemStack> itemStackStream, Operation<T> original, @Local(name = "itemStack", argsOnly = true) ItemStack itemStack) {
        return instance.itematic$copyWithContents(itemStack, itemStackStream);
    }
}
