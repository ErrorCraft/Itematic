package net.errorcraft.itematic.mixin.client.gui.screen.recipebook;

import net.minecraft.client.gui.screens.recipebook.OverlayRecipeComponent;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

public interface RecipeAlternativesWidgetAccessor {
    @Mixin(OverlayRecipeComponent.OverlayRecipeButton.class)
    interface AlternativeButtonWidgetAccessor {
        @Invoker("createGridPos")
        static OverlayRecipeComponent.OverlayRecipeButton.Pos slot(int x, int y, List<ItemStack> stacks) {
            throw new AssertionError();
        }
    }
}
