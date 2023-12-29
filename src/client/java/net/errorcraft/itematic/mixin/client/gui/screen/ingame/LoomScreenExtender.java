package net.errorcraft.itematic.mixin.client.gui.screen.ingame;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.BannerPatternHolderItemComponent;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.LoomScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.BannerItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.LoomScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LoomScreen.class)
public abstract class LoomScreenExtender extends HandledScreen<LoomScreenHandler> {
    public LoomScreenExtender(LoomScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Redirect(
        method = "drawBanner",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"
        )
    )
    @SuppressWarnings("ConstantConditions")
    private ItemStack drawBannerNewItemStackUseRegistryEntry(ItemConvertible item) {
        return this.client.world.itematic$createStack(ItemKeys.GRAY_BANNER);
    }

    @Redirect(
        method = "onInventoryChanged",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"
        )
    )
    private Item getItemUseNull(ItemStack instance) {
        return null;
    }

    @Redirect(
        method = "onInventoryChanged",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/BannerItem;getColor()Lnet/minecraft/util/DyeColor;"
        )
    )
    private DyeColor getColor(BannerItem instance, @Local(ordinal = 0) ItemStack outputStack) {
        return outputStack.itematic$getComponent(ItemComponentTypes.BANNER_PATTERN_HOLDER)
            .flatMap(BannerPatternHolderItemComponent::color)
            .orElse(DyeColor.WHITE);
    }
}
