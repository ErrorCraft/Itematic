package net.errorcraft.itematic.mixin.client.gui.screen.ingame;

import net.minecraft.client.gui.screen.ingame.BrewingStandScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.BrewingStandScreenHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(BrewingStandScreen.class)
public abstract class BrewingStandScreenExtender extends HandledScreen<BrewingStandScreenHandler> {
    public BrewingStandScreenExtender(BrewingStandScreenHandler handler, PlayerInventory inventory, Text title) {
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
}
