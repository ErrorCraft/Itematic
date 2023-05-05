package net.errorcraft.itematic.mixin.client.gui.screen.ingame;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.LoomScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.LoomScreenHandler;
import net.minecraft.text.Text;
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
            target = "net/minecraft/item/ItemStack"
        )
    )
    @SuppressWarnings("ConstantConditions")
    private ItemStack drawBannerNewItemStackUseRegistryEntry(ItemConvertible item) {
        return new ItemStack(this.client.world.getItem(ItemKeys.GRAY_BANNER));
    }
}
