package net.errorcraft.itematic.mixin.client.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.item.TooltipData;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.DynamicRegistryManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(DrawContext.class)
public class DrawContextExtender {
    @Shadow
    @Final
    private MinecraftClient client;

    @Redirect(
        method = "drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isItemBarVisible()Z"
        )
    )
    private boolean isItemBarVisibleUseDynamicRegistry(ItemStack instance) {
        return instance.itematic$isItemBarVisible(this.registryManager());
    }

    @Redirect(
        method = "drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;getItemBarStep()I"
        )
    )
    private int getItemBarStepUseDynamicRegistry(ItemStack instance) {
        return instance.itematic$itemBarStep(this.registryManager());
    }

    @Redirect(
        method = "drawItemTooltip",
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
        if (this.client.world == null) {
            return null;
        }
        return this.client.world.getRegistryManager();
    }
}
