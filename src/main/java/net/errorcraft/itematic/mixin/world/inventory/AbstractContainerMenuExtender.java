package net.errorcraft.itematic.mixin.world.inventory;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.errorcraft.itematic.world.item.behavior.behaviors.ItemHolderItemBehavior;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractContainerMenu.class)
public class AbstractContainerMenuExtender {
    @WrapOperation(
        method = "setSelectedBundleItemIndex",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/BundleItem;toggleSelectedItem(Lnet/minecraft/world/item/ItemStack;I)V"
        )
    )
    private void toggleSelectedItemUseItemBehavior(ItemStack stack, int selectedItem, Operation<Void> original) {
        ItemHolderItemBehavior.toggleSelectedItem(stack, selectedItem);
    }
}
