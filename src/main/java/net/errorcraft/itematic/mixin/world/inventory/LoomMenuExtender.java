package net.errorcraft.itematic.mixin.world.inventory;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.minecraft.world.inventory.LoomMenu;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(LoomMenu.class)
public class LoomMenuExtender {
    @ModifyConstant(
        method = "quickMoveStack",
        constant = @Constant(
            classValue = BannerItem.class
        )
    )
    private boolean instanceOfBannerItemCheckBannerBaseColorDataComponent(Object reference, Class<BannerItem> clazz, @Local(name = "stack") ItemStack stack) {
        return stack.has(ItematicDataComponents.BANNER_BASE_COLOR);
    }

    @Mixin(targets = "net/minecraft/world/inventory/LoomMenu$3")
    public static class BannerSlotExtender {
        @ModifyConstant(
            method = "mayPlace",
            constant = @Constant(
                classValue = BannerItem.class
            )
        )
        private boolean instanceOfBannerItemCheckBannerBaseColorDataComponent(Object reference, Class<BannerItem> clazz, ItemStack itemStack) {
            return itemStack.has(ItematicDataComponents.BANNER_BASE_COLOR);
        }
    }
}
