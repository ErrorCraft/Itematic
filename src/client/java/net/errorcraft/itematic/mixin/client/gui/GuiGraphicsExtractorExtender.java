package net.errorcraft.itematic.mixin.client.gui;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.errorcraft.itematic.client.resources.item.bar.ItemBarStyle;
import net.errorcraft.itematic.client.resources.item.bar.ItemBarStyleManager;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.state.gui.GuiRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix3x2fStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiGraphicsExtractor.class)
public abstract class GuiGraphicsExtractorExtender {
    @Shadow
    public abstract void blitSprite(RenderPipeline renderPipeline, Identifier location, int x, int y, int width, int height, int color);

    @Unique
    private ItemBarStyleManager itemBarStyles;

    @Inject(
        method = "<init>(Lnet/minecraft/client/Minecraft;Lorg/joml/Matrix3x2fStack;Lnet/minecraft/client/renderer/state/gui/GuiRenderState;II)V",
        at = @At("TAIL")
    )
    private void setItemBarStyles(Minecraft minecraft, Matrix3x2fStack pose, GuiRenderState guiRenderState, int mouseX, int mouseY, CallbackInfo info) {
        this.itemBarStyles = minecraft.itematic$itemBarStyles();
    }

    @WrapMethod(
        method = "itemBar"
    )
    private void useDataComponent(ItemStack itemStack, int x, int y, Operation<Void> original) {
        Identifier itemBarStyleId = itemStack.get(ItematicDataComponents.ITEM_BAR_STYLE);
        if (itemBarStyleId == null) {
            return;
        }

        ItemBarStyle itemBarStyle = this.itemBarStyles.get(itemBarStyleId);
        if (itemBarStyle == null) {
            return;
        }

        if (!itemBarStyle.isVisible(itemStack)) {
            return;
        }

        this.blitSprite(
            RenderPipelines.GUI_TEXTURED,
            itemBarStyle.progressTexture(itemStack),
            x,
            y,
            16,
            16,
            itemBarStyle.color(itemStack)
        );
    }
}
