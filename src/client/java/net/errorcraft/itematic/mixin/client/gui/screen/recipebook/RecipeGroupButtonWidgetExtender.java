package net.errorcraft.itematic.mixin.client.gui.screen.recipebook;

import net.errorcraft.itematic.access.client.recipebook.RecipeBookWidgetTabAccess;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.client.gui.screen.recipebook.RecipeGroupButtonWidget;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(RecipeGroupButtonWidget.class)
public class RecipeGroupButtonWidgetExtender {
    @Redirect(
        method = "renderIcons",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screen/recipebook/RecipeBookWidget$Tab;primaryIcon()Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack primaryIcon(RecipeBookWidget.Tab instance) {
        ClientWorld world = MinecraftClient.getInstance().world;
        if (world == null) {
            return ItemStack.EMPTY;
        }

        return ((RecipeBookWidgetTabAccess)(Object) instance).itematic$primaryIconItem(world.itematic$getItemAccess());
    }

    @Redirect(
        method = "renderIcons",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screen/recipebook/RecipeBookWidget$Tab;secondaryIcon()Ljava/util/Optional;"
        )
    )
    private Optional<ItemStack> secondaryIcon(RecipeBookWidget.Tab instance) {
        ClientWorld world = MinecraftClient.getInstance().world;
        if (world == null) {
            return Optional.empty();
        }

        return ((RecipeBookWidgetTabAccess)(Object) instance).itematic$secondaryIconItem(world.itematic$getItemAccess());
    }
}
