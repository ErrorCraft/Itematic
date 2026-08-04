package net.errorcraft.itematic.mixin.client.gui.screen.ingame;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.BrewingStandScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BrewingStandScreen.class)
public interface BrewingStandScreenAccessor {
    @Invoker("renderBg")
    void itematic$drawBackground(GuiGraphics context, float delta, int mouseX, int mouseY);
}
