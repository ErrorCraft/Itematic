package net.errorcraft.itematic.mixin.client.gui.screens.inventory;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.BrewingStandScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BrewingStandScreen.class)
public interface BrewingStandScreenAccessor {
    @Invoker("renderBg")
    void itematic$renderBg(GuiGraphics graphics, float a, int xm, int ym);
}
