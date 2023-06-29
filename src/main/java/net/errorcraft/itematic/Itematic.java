package net.errorcraft.itematic;

import net.errorcraft.itematic.item.color.ItemColorTypes;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.dispense.behavior.DispenseBehaviors;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.dispenser.DispenserBehavior;

public class Itematic implements ModInitializer {
    @Override
    public void onInitialize() {
        ItemComponentTypes.init();
        ItemColorTypes.init();
        DispenserBehavior.registerDefaults(); // Force load DispenserBehavior before loading the registry, so we don't get a NullPointerException
        DispenseBehaviors.init();
    }
}
