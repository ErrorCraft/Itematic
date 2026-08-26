package net.errorcraft.itematic.mixin.client.gui.screens.recipebook;

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
public class RecipeBookTabButtonExtender {
    @Redirect(
        method = "extractIcon",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screens/recipebook/RecipeBookComponent$TabInfo;primaryIcon()Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack primaryIcon(RecipeBookComponent.TabInfo instance) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            return ItemStack.EMPTY;
        }

        return instance.itematic$primaryIconItem(level.itematic$itemAccess());
    }

    @Redirect(
        method = "extractIcon",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screens/recipebook/RecipeBookComponent$TabInfo;secondaryIcon()Ljava/util/Optional;"
        )
    )
    private Optional<ItemStack> secondaryIcon(RecipeBookComponent.TabInfo instance) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            return Optional.empty();
        }

        return instance.itematic$secondaryIconItem(level.itematic$itemAccess());
    }
}
