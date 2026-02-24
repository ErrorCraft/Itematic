package net.errorcraft.itematic.mixin.client.gui.screen.recipebook;

import net.minecraft.client.gui.screen.recipebook.GhostRecipe;
import net.minecraft.recipe.display.SlotDisplay;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.context.ContextParameterMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GhostRecipe.class)
public interface GhostRecipeAccessor {
    @Invoker("addInputs")
    void itematic$addInputs(Slot slot, ContextParameterMap context, SlotDisplay display);
}
