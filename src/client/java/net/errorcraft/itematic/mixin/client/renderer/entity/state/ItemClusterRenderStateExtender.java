package net.errorcraft.itematic.mixin.client.renderer.entity.state;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.renderer.entity.state.ItemClusterRenderState;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemClusterRenderState.class)
public class ItemClusterRenderStateExtender {
    @WrapOperation(
        method = "getSeedForItemStack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z"
        )
    )
    private static boolean isEmptyCheckInteractableStack(ItemStack instance, Operation<Boolean> original) {
        return instance.itematic$cannotBeInteractedWith();
    }

    @WrapOperation(
        method = "getSeedForItemStack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/Item;getId(Lnet/minecraft/world/item/Item;)I"
        )
    )
    private static int getRawIdUseHolder(Item item, Operation<Integer> original, ItemStack itemStack) {
        return itemStack.typeHolder().itematic$rawId();
    }
}
