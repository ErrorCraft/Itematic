package net.errorcraft.itematic.mixin.world.item.component;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemContainerContents.class)
public class ItemContainerContentsExtender {
    @WrapOperation(
        method = {
            "fromItems",
            "findLastNonEmptySlot"
        },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z"
        )
    )
    private static boolean isEmptyCheckInteractableStack(ItemStack instance, Operation<Boolean> original) {
        return instance.itematic$cannotBeInteractedWith();
    }
}
