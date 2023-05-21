package net.errorcraft.itematic;

import net.errorcraft.itematic.item.color.ItemColorTypes;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.dispense.behavior.DispenseBehaviors;
import net.fabricmc.api.ModInitializer;

public class Itematic implements ModInitializer {
    @Override
    public void onInitialize() {
        ItemComponentTypes.init();
        ItemColorTypes.init();
        DispenseBehaviors.init();
    }
}
