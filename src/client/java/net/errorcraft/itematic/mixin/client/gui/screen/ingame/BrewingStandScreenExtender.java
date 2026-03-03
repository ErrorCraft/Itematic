package net.errorcraft.itematic.mixin.client.gui.screen.ingame;

import net.errorcraft.itematic.access.screen.BrewingStandScreenHandlerAccess;
import net.errorcraft.itematic.screen.BrewingStandMenuDelegate;
import net.minecraft.client.gui.screen.ingame.BrewingStandScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.recipebook.RecipeBookProvider;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(BrewingStandScreen.class)
public abstract class BrewingStandScreenExtender extends HandledScreen<BrewingStandMenuDelegate> implements RecipeBookProvider {
    public BrewingStandScreenExtender(BrewingStandMenuDelegate handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @ModifyVariable(
        method = "drawBackground",
        ordinal = 2,
        at = @At("STORE:FIRST")
    )
    private int useDirectXPosition(int original) {
        return this.x;
    }

    @ModifyConstant(
        method = "drawBackground",
        constant = @Constant(
            floatValue = 400.0f
        )
    )
    private float useRecipeForBrewingTime(float original) {
        return ((BrewingStandScreenHandlerAccess) this.handler).itematic$maxBrewingTime();
    }
}
