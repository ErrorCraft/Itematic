package net.errorcraft.itematic.mixin.world.inventory;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.inventory.ShulkerBoxSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ShulkerBoxSlot.class)
public class ShulkerBoxSlotExtender {
    @WrapOperation(
        method = "mayPlace",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/Item;canFitInsideContainerItems()Z"
        )
    )
    private boolean useStackAwareVersion(Item instance, Operation<Boolean> original, ItemStack itemStack) {
        return itemStack.itematic$canFitInsideContainerItems();
    }
}
