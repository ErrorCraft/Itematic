package net.errorcraft.itematic.mixin.client.gui.screens.inventory;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.minecraft.client.gui.screens.inventory.LoomScreen;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LoomScreen.class)
public class LoomScreenExtender {
    @WrapOperation(
        method = "extractBackground",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;getItem()Lnet/minecraft/world/item/Item;"
        )
    )
    @Nullable
    private Item getItemUseNull(ItemStack instance, Operation<Item> original) {
        return null;
    }

    @WrapOperation(
        method = "extractBackground",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/BannerItem;getColor()Lnet/minecraft/world/item/DyeColor;"
        )
    )
    private DyeColor getColorUseDataComponent(BannerItem instance, Operation<DyeColor> original, @Local(name = "resultSlot") Slot resultSlot) {
        return resultSlot.getItem()
            .getOrDefault(ItematicDataComponents.BANNER_BASE_COLOR, DyeColor.WHITE);
    }
}
