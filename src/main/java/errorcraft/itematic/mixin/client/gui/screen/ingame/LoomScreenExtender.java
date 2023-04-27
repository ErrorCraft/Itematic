package errorcraft.itematic.mixin.client.gui.screen.ingame;

import errorcraft.itematic.item.ItemKeys;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.LoomScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.LoomScreenHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Environment(EnvType.CLIENT)
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
        RegistryEntry<Item> entry = this.client.world.getRegistryManager().get(RegistryKeys.ITEM).entryOf(ItemKeys.GRAY_BANNER);
        return new ItemStack(entry);
    }
}
