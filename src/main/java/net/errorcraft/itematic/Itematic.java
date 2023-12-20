package net.errorcraft.itematic;

import net.errorcraft.itematic.item.color.ItemColorTypes;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.dispense.behavior.DispenseBehaviors;
import net.errorcraft.itematic.item.event.ItemEvents;
import net.errorcraft.itematic.item.model.override.ModelOverrides;
import net.errorcraft.itematic.item.pointer.Pointers;
import net.errorcraft.itematic.item.smithing.template.SmithingTemplateTypes;
import net.errorcraft.itematic.loot.context.ItematicLootContextParameters;
import net.errorcraft.itematic.loot.context.ItematicLootContextTypes;
import net.errorcraft.itematic.loot.predicate.ItematicPredicateTypes;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.sequence.handler.SequenceHandlerTypes;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.dispenser.DispenserBehavior;

public class Itematic implements ModInitializer {
    @Override
    public void onInitialize() {
        ItemComponentTypes.init();
        ItemColorTypes.init();
        DispenserBehavior.registerDefaults(); // Force load DispenserBehavior before loading the registry, so we don't get a NullPointerException
        DispenseBehaviors.init();
        ItemEvents.init();
        ActionTypes.init();
        ItematicLootContextTypes.init();
        ModelOverrides.init();
        Pointers.init();
        SequenceHandlerTypes.init();
        ItematicPredicateTypes.init();
        ItematicLootContextParameters.init();
        SmithingTemplateTypes.init();
    }
}
