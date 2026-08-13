package net.errorcraft.itematic;

import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.loot.function.ItematicItemModifierTypes;
import net.errorcraft.itematic.loot.predicate.ItematicPredicateTypes;
import net.errorcraft.itematic.predicate.entity.ItematicEntitySubPredicateTypes;
import net.errorcraft.itematic.recipe.ItematicRecipeSerializers;
import net.errorcraft.itematic.recipe.ItematicRecipeTypes;
import net.errorcraft.itematic.recipe.book.ItematicRecipeBookCategories;
import net.errorcraft.itematic.recipe.display.ItematicRecipeDisplaySerializers;
import net.errorcraft.itematic.recipe.display.slot.ItematicSlotDisplaySerializers;
import net.errorcraft.itematic.util.context.ItematicContextParameters;
import net.errorcraft.itematic.util.context.ItematicContextTypes;
import net.errorcraft.itematic.village.trade.modifier.TradeModifierTypes;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.sequence.handler.SequenceHandlerTypes;
import net.errorcraft.itematic.world.entity.spawn.rule.EntitySpawnRuleType;
import net.errorcraft.itematic.world.item.ItemEvent;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.holder.rule.ItemHolderRuleType;
import net.errorcraft.itematic.world.item.placement.block.picker.BlockPickerType;
import net.errorcraft.itematic.world.item.smithing.template.SmithingTemplates;
import net.errorcraft.itematic.world.item.use.duration.provider.UseDurationProviderType;
import net.errorcraft.itematic.world.item.weapon.melee.behavior.MeleeWeaponComponents;
import net.errorcraft.itematic.world.item.weapon.shooter.method.ShooterMethodType;
import net.errorcraft.itematic.world.modification.WorldModificationTypes;
import net.fabricmc.api.ModInitializer;

public class Itematic implements ModInitializer {
    @Override
    public void onInitialize() {
        ItemBehaviorType.init();
        ItemEvent.init();
        ActionTypes.init();
        ItematicContextTypes.init();
        SequenceHandlerTypes.init();
        ItematicPredicateTypes.init();
        ItematicContextParameters.init();
        SmithingTemplates.init();
        BlockPickerType.init();
        ItematicItemModifierTypes.init();
        TradeModifierTypes.init();
        ItematicDataComponents.init();
        UseDurationProviderType.init();
        ItemHolderRuleType.init();
        ShooterMethodType.init();
        ItematicRecipeTypes.init();
        ItematicRecipeSerializers.init();
        ItematicRecipeBookCategories.init();
        ItematicRecipeDisplaySerializers.init();
        ItematicSlotDisplaySerializers.init();
        ItematicEntitySubPredicateTypes.init();
        WorldModificationTypes.init();
        EntitySpawnRuleType.init();
        MeleeWeaponComponents.init();
    }
}
