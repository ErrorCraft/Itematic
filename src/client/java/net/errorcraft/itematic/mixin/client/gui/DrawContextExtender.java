package net.errorcraft.itematic.mixin.client.gui;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.errorcraft.itematic.client.item.bar.ItemBarStyleLoader;
import net.errorcraft.itematic.component.ItematicDataComponentTypes;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.render.state.GuiRenderState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.joml.Matrix3x2fStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DrawContext.class)
public abstract class DrawContextExtender {
    @Shadow
    public abstract void drawGuiTexture(RenderPipeline pipeline, Identifier sprite, int x, int y, int width, int height, int color);

    @Unique
    private ItemBarStyleLoader itemBarStyles;

    @Inject(
        method = "<init>(Lnet/minecraft/client/MinecraftClient;Lorg/joml/Matrix3x2fStack;Lnet/minecraft/client/gui/render/state/GuiRenderState;)V",
        at = @At("TAIL")
    )
    private void setItemBarStyles(MinecraftClient client, Matrix3x2fStack matrices, GuiRenderState state, CallbackInfo info) {
        this.itemBarStyles = client.itematic$itemBarStyles();
    }

    @ModifyExpressionValue(
        method = "drawItemBar",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isItemBarVisible()Z"
        )
    )
    private boolean useDataComponent(boolean original, @Local(argsOnly = true) ItemStack stack) {
        Identifier itemBarStyleId = stack.get(ItematicDataComponentTypes.ITEM_BAR_STYLE);
        if (itemBarStyleId == null) {
            return false;
        }

        return this.itemBarStyles.get(itemBarStyleId)
            .map(itemBarStyle -> itemBarStyle.isVisible(stack))
            .orElse(false);
    }

    @Inject(
        method = "drawItemBar",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;getItemBarStep()I"
        )
    )
    private void renderItemBarFromDataComponent(ItemStack stack, int x, int y, CallbackInfo info) {
        Identifier itemBarStyleId = stack.get(ItematicDataComponentTypes.ITEM_BAR_STYLE);
        if (itemBarStyleId == null) {
            return;
        }

        this.itemBarStyles.get(itemBarStyleId).ifPresent(itemBarStyle -> this.drawGuiTexture(
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
        method = "drawItemBar",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/DrawContext;fill(Lcom/mojang/blaze3d/pipeline/RenderPipeline;IIIII)V"
        ),
        slice = @Slice(
            from = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/item/ItemStack;isItemBarVisible()Z"
            )
        )
    )
    private void doNotRenderOriginalItemBar(DrawContext instance, RenderPipeline pipeline, int x1, int y1, int x2, int y2, int z) {}
}
