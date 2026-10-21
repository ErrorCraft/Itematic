package net.errorcraft.itematic;

import net.errorcraft.itematic.advancements.predicates.entity.ItematicEntitySubPredicates;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.core.component.provider.number.floats.FloatProviders;
import net.errorcraft.itematic.core.component.provider.number.ints.IntProviders;
import net.errorcraft.itematic.core.registries.ItematicBuiltInRegistries;
import net.errorcraft.itematic.util.context.ItematicContextKeys;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.sequence.handler.SequenceHandlerType;
import net.errorcraft.itematic.world.entity.spawn.rule.EntitySpawnRuleTypes;
import net.errorcraft.itematic.world.item.ItemEvent;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.holder.rule.ItemHolderRuleType;
import net.errorcraft.itematic.world.item.placement.block.picker.BlockPickerType;
import net.errorcraft.itematic.world.item.smithing.template.SmithingTemplates;
import net.errorcraft.itematic.world.item.use.duration.provider.UseDurationProviderTypes;
import net.errorcraft.itematic.world.item.weapon.melee.behavior.MeleeWeaponComponents;
import net.errorcraft.itematic.world.item.weapon.shooter.method.ShooterMethods;
import net.errorcraft.itematic.world.level.levelgen.feature.stateproviders.ItematicBlockStateProviderTypes;
import net.errorcraft.itematic.world.level.modification.WorldModificationTypes;
import net.errorcraft.itematic.world.level.storage.loot.functions.ItematicItemModifiers;
import net.errorcraft.itematic.world.level.storage.loot.predicates.ItematicPredicates;
import net.errorcraft.itematic.world.level.storage.loot.providers.number.floats.ItematicContextFloatProviderTypes;
import net.errorcraft.itematic.world.level.storage.loot.providers.number.ints.ItematicContextIntProviderTypes;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.registries.BuiltInRegistries;

public class Itematic implements ModInitializer {
    @Override
    public void onInitialize() {
        ItemBehaviorType.init();
        ItemEvent.init();
        ActionType.init();
        SequenceHandlerType.init();
        ItematicPredicates.bootstrap(BuiltInRegistries.LOOT_CONDITION_TYPE);
        ItematicContextKeys.init();
        SmithingTemplates.init();
        BlockPickerType.init();
        ItematicItemModifiers.bootstrap(BuiltInRegistries.LOOT_FUNCTION_TYPE);
        ItematicDataComponents.init();
        UseDurationProviderTypes.bootstrap(ItematicBuiltInRegistries.USE_DURATION_PROVIDER_TYPE);
        ItemHolderRuleType.init();
        ShooterMethods.bootstrap(ItematicBuiltInRegistries.SHOOTER_METHOD);
        WorldModificationTypes.bootstrap(ItematicBuiltInRegistries.WORLD_MODIFICATION_TYPE);
        EntitySpawnRuleTypes.bootstrap(ItematicBuiltInRegistries.ENTITY_SPAWN_RULE_TYPE);
        MeleeWeaponComponents.init();
        ItematicEntitySubPredicates.bootstrap(BuiltInRegistries.ENTITY_SUB_PREDICATE_TYPE);
        ItematicBlockStateProviderTypes.bootstrap(BuiltInRegistries.BLOCK_STATE_PROVIDER_TYPE);
        IntProviders.bootstrap(ItematicBuiltInRegistries.DATA_COMPONENT_CONTEXT_INT_PROVIDER_TYPE);
        ItematicContextIntProviderTypes.bootstrap(BuiltInRegistries.CONTEXT_INT_PROVIDER_TYPE);
        FloatProviders.bootstrap(ItematicBuiltInRegistries.DATA_COMPONENT_CONTEXT_FLOAT_PROVIDER_TYPE);
        ItematicContextFloatProviderTypes.bootstrap(BuiltInRegistries.CONTEXT_FLOAT_PROVIDER_TYPE);
    }
}
