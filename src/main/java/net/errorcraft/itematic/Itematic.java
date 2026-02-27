package net.errorcraft.itematic;

import net.errorcraft.itematic.component.ItematicDataComponentTypes;
import net.errorcraft.itematic.item.color.ItemColorTypes;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.event.ItemEvents;
import net.errorcraft.itematic.item.holder.rule.ItemHolderRuleTypes;
import net.errorcraft.itematic.item.model.override.ModelOverrides;
import net.errorcraft.itematic.item.placement.block.picker.BlockPickerTypes;
import net.errorcraft.itematic.item.pointer.Pointers;
import net.errorcraft.itematic.item.shooter.method.ShooterMethodTypes;
import net.errorcraft.itematic.item.smithing.template.SmithingTemplateTypes;
import net.errorcraft.itematic.item.use.provider.IntegerProviderTypes;
import net.errorcraft.itematic.loot.context.ItematicLootContextParameters;
import net.errorcraft.itematic.loot.context.ItematicLootContextTypes;
import net.errorcraft.itematic.loot.function.ItematicItemModifierTypes;
import net.errorcraft.itematic.loot.predicate.ItematicPredicateTypes;
import net.errorcraft.itematic.recipe.ItematicRecipeSerializers;
import net.errorcraft.itematic.recipe.ItematicRecipeTypes;
import net.errorcraft.itematic.screen.ItematicScreenHandlerTypes;
import net.errorcraft.itematic.village.trade.modifier.TradeModifierTypes;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.sequence.handler.SequenceHandlerTypes;
import net.fabricmc.api.ModInitializer;

public class Itematic implements ModInitializer {
    @Override
    public void onInitialize() {
        ItemComponentTypes.init();
        ItemColorTypes.init();
        ItemEvents.init();
        ActionTypes.init();
        ItematicLootContextTypes.init();
        ModelOverrides.init();
        Pointers.init();
        SequenceHandlerTypes.init();
        ItematicPredicateTypes.init();
        ItematicLootContextParameters.init();
        SmithingTemplateTypes.init();
        ItematicRecipeSerializers.init();
        BlockPickerTypes.init();
        ItematicItemModifierTypes.init();
        TradeModifierTypes.init();
        ItematicDataComponentTypes.init();
        IntegerProviderTypes.init();
        ItemHolderRuleTypes.init();
        ShooterMethodTypes.init();
        ItematicRecipeTypes.init();
        ItematicScreenHandlerTypes.init();
    }
}
