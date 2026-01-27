package net.errorcraft.itematic.mixin.client.gui;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.access.client.MinecraftClientAccess;
import net.errorcraft.itematic.client.item.bar.ItemBarStyleLoader;
import net.errorcraft.itematic.component.ItematicDataComponentTypes;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Function;

@Mixin(DrawContext.class)
public abstract class DrawContextExtender {
    @Shadow
    public abstract void drawGuiTexture(Function<Identifier, RenderLayer> function, Identifier identifier, int i, int j, int k, int l, int m);

    @Unique
    private ItemBarStyleLoader itemBarStyles;

    @Inject(
        method = "<init>(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;)V",
        at = @At("TAIL")
    )
    private void setItemBarStyles(MinecraftClient client, MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers, CallbackInfo info) {
        this.itemBarStyles = ((MinecraftClientAccess) client).itematic$itemBarStyles();
    }

    @ModifyExpressionValue(
        method = "drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V",
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
        return this.itemBarStyles.get(itemBarStyleId).map(itemBarStyle -> itemBarStyle.isVisible(stack))
            .orElse(false);
    }

    @Inject(
        method = "drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;getItemBarStep()I"
        )
    )
    private void renderItemBarFromDataComponent(TextRenderer textRenderer, ItemStack stack, int x, int y, String countOverride, CallbackInfo info) {
        Identifier itemBarStyleId = stack.get(ItematicDataComponentTypes.ITEM_BAR_STYLE);
        if (itemBarStyleId == null) {
            return;
        }

        this.itemBarStyles.get(itemBarStyleId).ifPresent(itemBarStyle -> this.drawGuiTexture(
            RenderLayer::getGuiTextured,
            itemBarStyle.progressTexture(stack),
            x,
            y,
            16,
            16,
            itemBarStyle.color(stack)
        ));
    }

    @Redirect(
        method = "drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/DrawContext;fill(Lnet/minecraft/client/render/RenderLayer;IIIII)V"
        ),
        slice = @Slice(
            from = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/item/ItemStack;isItemBarVisible()Z"
            ),
            to = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/client/network/ClientPlayerEntity;getItemCooldownManager()Lnet/minecraft/entity/player/ItemCooldownManager;"
            )
        )
    )
    private void doNotRenderOriginalItemBar(DrawContext instance, RenderLayer layer, int x1, int y1, int x2, int y2, int color) {}
}
