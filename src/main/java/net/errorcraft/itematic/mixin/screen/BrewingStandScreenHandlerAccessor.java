package net.errorcraft.itematic.mixin.screen;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.BrewingStandMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BrewingStandMenu.class)
public interface BrewingStandScreenHandlerAccessor {
    @Accessor("BOTTLE_SLOT_START")
    static int inputSlotStart() {
        throw new AssertionError();
    }

    @Accessor("INGREDIENT_SLOT")
    static int ingredientSlot() {
        throw new AssertionError();
    }

    @Accessor("brewingStand")
    Container itematic$inventory();
}
