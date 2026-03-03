package net.errorcraft.itematic.mixin.client.gui.screen.recipebook;

import net.minecraft.client.gui.screen.recipebook.RecipeAlternativesWidget;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

public interface RecipeAlternativesWidgetAccessor {
    @Mixin(RecipeAlternativesWidget.AlternativeButtonWidget.class)
    interface AlternativeButtonWidgetAccessor {
        @Invoker("slot")
        static RecipeAlternativesWidget.AlternativeButtonWidget.InputSlot slot(int x, int y, List<ItemStack> stacks) {
            throw new AssertionError();
        }
    }
}
