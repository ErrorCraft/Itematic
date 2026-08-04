package net.errorcraft.itematic.mixin.client.gui.screen.ingame;

import net.errorcraft.itematic.access.screen.BrewingStandScreenHandlerAccess;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.BrewingStandScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.BrewingStandMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(BrewingStandScreen.class)
public abstract class BrewingStandScreenExtender extends AbstractContainerScreen<BrewingStandMenu> {
    public BrewingStandScreenExtender(BrewingStandMenu handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    @ModifyVariable(
        method = "renderBg",
        ordinal = 2,
        at = @At("STORE:FIRST")
    )
    private int useDirectXPosition(int original) {
        return this.leftPos;
    }

    @ModifyConstant(
        method = "renderBg",
        constant = @Constant(
            floatValue = 400.0f
        )
    )
    private float useRecipeForBrewingTime(float original) {
        return ((BrewingStandScreenHandlerAccess) this.menu).itematic$maxBrewingTime();
    }
}
