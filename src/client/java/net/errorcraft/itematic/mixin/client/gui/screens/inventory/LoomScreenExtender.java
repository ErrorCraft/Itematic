package net.errorcraft.itematic.mixin.client.gui.screens.inventory;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.behavior.behaviors.BannerPatternHolderItemBehavior;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.LoomScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.LoomMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LoomScreen.class)
public abstract class LoomScreenExtender extends AbstractContainerScreen<LoomMenu> {
    public LoomScreenExtender(LoomMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Redirect(
        method = "renderBg",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;getItem()Lnet/minecraft/world/item/Item;"
        )
    )
    @Nullable
    private Item getItemUseNull(ItemStack instance) {
        return null;
    }

    @Redirect(
        method = "renderBg",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/BannerItem;getColor()Lnet/minecraft/world/item/DyeColor;"
        )
    )
    private DyeColor getColorUseItemBehavior(BannerItem instance, @Local(ordinal = 3) Slot outputSlot) {
        return outputSlot.getItem()
            .itematic$getBehavior(ItemBehaviorType.BANNER_PATTERN_HOLDER)
            .flatMap(BannerPatternHolderItemBehavior::color)
            .orElse(DyeColor.WHITE);
    }
}
