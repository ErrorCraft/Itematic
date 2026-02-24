package net.errorcraft.itematic.mixin.screen;

import net.minecraft.inventory.Inventory;
import net.minecraft.screen.BrewingStandScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BrewingStandScreenHandler.class)
public interface BrewingStandScreenHandlerAccessor {
    @Accessor("field_30763")
    static int inputSlotStart() {
        throw new AssertionError();
    }

    @Accessor("INGREDIENT_SLOT_ID")
    static int ingredientSlot() {
        throw new AssertionError();
    }

    @Accessor("inventory")
    Inventory itematic$inventory();
}
