package net.errorcraft.itematic.mixin.client.gui;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.errorcraft.itematic.client.item.bar.ItemBarStyleLoader;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.render.state.GuiRenderState;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix3x2fStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiGraphics.class)
public abstract class DrawContextExtender {
    @Shadow
    public abstract void blitSprite(RenderPipeline pipeline, Identifier sprite, int x, int y, int width, int height, int color);

    @Unique
    private ItemBarStyleLoader itemBarStyles;

    @Inject(
        method = "<init>(Lnet/minecraft/client/Minecraft;Lorg/joml/Matrix3x2fStack;Lnet/minecraft/client/gui/render/state/GuiRenderState;II)V",
        at = @At("TAIL")
    )
    private void setItemBarStyles(Minecraft client, Matrix3x2fStack matrices, GuiRenderState state, int mouseX, int mouseY, CallbackInfo info) {
        this.itemBarStyles = client.itematic$itemBarStyles();
    }

    @ModifyExpressionValue(
        method = "renderItemBar",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;isBarVisible()Z"
        )
    )
    private boolean useDataComponent(boolean original, @Local(argsOnly = true) ItemStack stack) {
        Identifier itemBarStyleId = stack.get(ItematicDataComponents.ITEM_BAR_STYLE);
        if (itemBarStyleId == null) {
            return false;
        }

        return this.itemBarStyles.get(itemBarStyleId)
            .map(itemBarStyle -> itemBarStyle.isVisible(stack))
            .orElse(false);
    }

    @Inject(
        method = "renderItemBar",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;getBarWidth()I"
        )
    )
    private void renderItemBarFromDataComponent(ItemStack stack, int x, int y, CallbackInfo info) {
        Identifier itemBarStyleId = stack.get(ItematicDataComponents.ITEM_BAR_STYLE);
        if (itemBarStyleId == null) {
            return;
        }

        this.itemBarStyles.get(itemBarStyleId).ifPresent(itemBarStyle -> this.blitSprite(
            RenderPipelines.GUI_TEXTURED,
            itemBarStyle.progressTexture(stack),
            x,
            y,
            16,
            16,
            itemBarStyle.color(stack)
        ));
    }

    @Redirect(
        method = "renderItemBar",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/GuiGraphics;fill(Lcom/mojang/blaze3d/pipeline/RenderPipeline;IIIII)V"
        )
    )
    private void doNotRenderOriginalItemBar(GuiGraphics instance, RenderPipeline pipeline, int x1, int y1, int x2, int y2, int color) {}
}
