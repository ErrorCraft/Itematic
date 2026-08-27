package net.errorcraft.itematic.mixin.client.gui.screens.inventory;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.BrewingStandScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BrewingStandScreen.class)
public interface BrewingStandScreenAccessor {
    @Invoker("extractBackground")
    void itematic$extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a);
}
