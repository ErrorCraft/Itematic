package net.errorcraft.itematic;

import net.errorcraft.itematic.advancements.criterion.ItematicEntitySubPredicates;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.util.context.ItematicContextKeySets;
import net.errorcraft.itematic.util.context.ItematicContextKeys;
import net.errorcraft.itematic.village.trade.modifier.TradeModifierTypes;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.sequence.handler.SequenceHandlerTypes;
import net.errorcraft.itematic.world.entity.spawn.rule.EntitySpawnRuleType;
import net.errorcraft.itematic.world.inventory.ItematicMenuTypes;
import net.errorcraft.itematic.world.item.ItemEvent;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.crafting.ItematicRecipeBookCategories;
import net.errorcraft.itematic.world.item.crafting.ItematicRecipeSerializers;
import net.errorcraft.itematic.world.item.crafting.ItematicRecipeTypes;
import net.errorcraft.itematic.world.item.crafting.display.ItematicRecipeDisplays;
import net.errorcraft.itematic.world.item.crafting.display.ItematicSlotDisplays;
import net.errorcraft.itematic.world.item.holder.rule.ItemHolderRuleType;
import net.errorcraft.itematic.world.item.placement.block.picker.BlockPickerType;
import net.errorcraft.itematic.world.item.smithing.template.SmithingTemplates;
import net.errorcraft.itematic.world.item.use.duration.provider.UseDurationProviderType;
import net.errorcraft.itematic.world.item.weapon.melee.behavior.MeleeWeaponComponents;
import net.errorcraft.itematic.world.item.weapon.shooter.method.ShooterMethodType;
import net.errorcraft.itematic.world.level.storage.loot.functions.ItematicItemModifierTypes;
import net.errorcraft.itematic.world.level.storage.loot.predicates.ItematicPredicateTypes;
import net.errorcraft.itematic.world.modification.WorldModificationTypes;
import net.fabricmc.api.ModInitializer;

public class Itematic implements ModInitializer {
    @Override
    public void onInitialize() {
        ItemBehaviorType.init();
        ItemEvent.init();
        ActionTypes.init();
        ItematicContextKeySets.init();
        SequenceHandlerTypes.init();
        ItematicPredicateTypes.init();
        ItematicContextKeys.init();
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
        ItematicRecipeDisplays.init();
        ItematicSlotDisplays.init();
        ItematicEntitySubPredicates.init();
        WorldModificationTypes.init();
        EntitySpawnRuleType.init();
        MeleeWeaponComponents.init();
        ItematicMenuTypes.init();
    }
}
