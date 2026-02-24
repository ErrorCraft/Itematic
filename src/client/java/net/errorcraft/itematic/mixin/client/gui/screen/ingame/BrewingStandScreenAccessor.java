package net.errorcraft.itematic.mixin.client.gui.screen.ingame;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.BrewingStandScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BrewingStandScreen.class)
public interface BrewingStandScreenAccessor {
    @Invoker("drawBackground")
    void itematic$drawBackground(DrawContext context, float delta, int mouseX, int mouseY);
}
