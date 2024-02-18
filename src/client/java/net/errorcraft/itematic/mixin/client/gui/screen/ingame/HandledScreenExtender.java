package net.errorcraft.itematic.mixin.client.gui.screen.ingame;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.item.TooltipData;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(HandledScreen.class)
public class HandledScreenExtender extends Screen {
    protected HandledScreenExtender(Text title) {
        super(title);
    }

    @Redirect(
        method = "drawMouseoverTooltip",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;getTooltipData()Ljava/util/Optional;"
        )
    )
    private Optional<TooltipData> getTooltipDataUseDynamicRegistry(ItemStack instance) {
        return instance.itematic$tooltipData(this.registryManager());
    }

    @Unique
    private DynamicRegistryManager registryManager() {
        if (this.client == null) {
            return null;
        }
        if (this.client.world == null) {
            return null;
        }
        return this.client.world.getRegistryManager();
    }
}
