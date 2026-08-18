package net.errorcraft.itematic.mixin.client.gui.screen.recipebook;

import net.errorcraft.itematic.access.client.recipebook.RecipeBookWidgetTabAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeBookTabButton;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(RecipeBookTabButton.class)
public class RecipeGroupButtonWidgetExtender {
    @Redirect(
        method = "renderIcon",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screens/recipebook/RecipeBookComponent$TabInfo;primaryIcon()Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack primaryIcon(RecipeBookComponent.TabInfo instance) {
        ClientLevel world = Minecraft.getInstance().level;
        if (world == null) {
            return ItemStack.EMPTY;
        }

        return ((RecipeBookWidgetTabAccess)(Object) instance).itematic$primaryIconItem(world.itematic$itemAccess());
    }

    @Redirect(
        method = "renderIcon",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screens/recipebook/RecipeBookComponent$TabInfo;secondaryIcon()Ljava/util/Optional;"
        )
    )
    private Optional<ItemStack> secondaryIcon(RecipeBookComponent.TabInfo instance) {
        ClientLevel world = Minecraft.getInstance().level;
        if (world == null) {
            return Optional.empty();
        }

        return ((RecipeBookWidgetTabAccess)(Object) instance).itematic$secondaryIconItem(world.itematic$itemAccess());
    }
}
