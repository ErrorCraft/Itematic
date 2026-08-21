package net.errorcraft.itematic.mixin.client.gui.screens.recipebook;

import net.minecraft.client.gui.screens.recipebook.OverlayRecipeComponent;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

public interface OverlayRecipeComponentAccessor {
    @Mixin(OverlayRecipeComponent.OverlayRecipeButton.class)
    interface OverlayRecipeButtonAccessor {
        @Invoker("createGridPos")
        static OverlayRecipeComponent.OverlayRecipeButton.Pos createGridPos(int gridXPos, int gridYPos, List<ItemStack> itemStacks) {
            throw new AssertionError();
        }
    }
}
