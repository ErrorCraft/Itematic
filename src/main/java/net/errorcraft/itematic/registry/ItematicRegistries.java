package net.errorcraft.itematic.registry;

import net.errorcraft.itematic.entity.spawn.rule.EntitySpawnRuleType;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.event.ItemEvent;
import net.errorcraft.itematic.item.event.ItemEvents;
import net.errorcraft.itematic.item.holder.rule.ItemHolderRuleType;
import net.errorcraft.itematic.item.holder.rule.ItemHolderRuleTypes;
import net.errorcraft.itematic.item.placement.block.picker.BlockPickerType;
import net.errorcraft.itematic.item.placement.block.picker.BlockPickerTypes;
import net.errorcraft.itematic.item.shooter.method.ShooterMethodType;
import net.errorcraft.itematic.item.shooter.method.ShooterMethodTypes;
import net.errorcraft.itematic.item.smithing.template.SmithingTemplate;
import net.errorcraft.itematic.item.smithing.template.SmithingTemplates;
import net.errorcraft.itematic.item.use.provider.IntegerProviderType;
import net.errorcraft.itematic.item.use.provider.IntegerProviderTypes;
import net.errorcraft.itematic.mixin.registry.RegistriesAccessor;
import net.errorcraft.itematic.predicate.item.enchantment.EnchantmentEffectPredicateType;
import net.errorcraft.itematic.village.trade.modifier.TradeModifierType;
import net.errorcraft.itematic.village.trade.modifier.TradeModifierTypes;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.sequence.handler.SequenceHandlerType;
import net.errorcraft.itematic.world.action.sequence.handler.SequenceHandlerTypes;
import net.errorcraft.itematic.world.modification.WorldModificationType;
import net.errorcraft.itematic.world.modification.WorldModificationTypes;
import net.minecraft.registry.Registry;

public class ItematicRegistries {
    public static final Registry<ItemComponentType<?>> ITEM_COMPONENT_TYPE = RegistriesAccessor.create(ItematicRegistryKeys.ITEM_COMPONENT_TYPE, r -> ItemComponentTypes.USEABLE);
    public static final Registry<ItemEvent> ITEM_EVENT = RegistriesAccessor.create(ItematicRegistryKeys.ITEM_EVENT, r -> ItemEvents.USE);
    public static final Registry<ActionType<?>> ACTION_TYPE = RegistriesAccessor.create(ItematicRegistryKeys.ACTION_TYPE, r -> ActionTypes.MODIFY_ITEM);
    public static final Registry<SequenceHandlerType<?>> SEQUENCE_HANDLER_TYPE = RegistriesAccessor.create(ItematicRegistryKeys.SEQUENCE_HANDLER_TYPE, r -> SequenceHandlerTypes.UNCHECKED);
    public static final Registry<SmithingTemplate> SMITHING_TEMPLATE = RegistriesAccessor.create(ItematicRegistryKeys.SMITHING_TEMPLATE, r -> SmithingTemplates.TRIM_PATTERN);
    public static final Registry<BlockPickerType<?>> BLOCK_PICKER_TYPE = RegistriesAccessor.create(ItematicRegistryKeys.BLOCK_PICKER_TYPE, r -> BlockPickerTypes.SIMPLE);
    public static final Registry<TradeModifierType<?>> TRADE_MODIFIER_TYPE = RegistriesAccessor.create(ItematicRegistryKeys.TRADE_MODIFIER_TYPE, r -> TradeModifierTypes.ENCHANT_WITH_LEVELS);
    public static final Registry<IntegerProviderType<?>> INTEGER_PROVIDER_TYPE = RegistriesAccessor.create(ItematicRegistryKeys.INTEGER_PROVIDER_TYPE, r -> IntegerProviderTypes.CONSTANT);
    public static final Registry<ItemHolderRuleType<?>> ITEM_HOLDER_RULE_TYPE = RegistriesAccessor.create(ItematicRegistryKeys.ITEM_HOLDER_RULE_TYPE, r -> ItemHolderRuleTypes.REJECT);
    public static final Registry<ShooterMethodType<?>> SHOOTER_METHOD_TYPE = RegistriesAccessor.create(ItematicRegistryKeys.SHOOTER_METHOD_TYPE, r -> ShooterMethodTypes.DIRECT);
    public static final Registry<WorldModificationType<?>> WORLD_MODIFICATION_TYPE = RegistriesAccessor.create(ItematicRegistryKeys.WORLD_MODIFICATION_TYPE, r -> WorldModificationTypes.DRAIN_FLUID);
    public static final Registry<EntitySpawnRuleType<?>> ENTITY_SPAWN_RULE_TYPE = RegistriesAccessor.create(ItematicRegistryKeys.ENTITY_SPAWN_RULE_TYPE, r -> EntitySpawnRuleType.DISCARD);
    public static final Registry<EnchantmentEffectPredicateType<?>> ENCHANTMENT_EFFECT_PREDICATE_TYPE = RegistriesAccessor.create(ItematicRegistryKeys.ENCHANTMENT_EFFECT_PREDICATE_TYPE, r -> EnchantmentEffectPredicateType.TRIDENT_SPIN_ATTACK_STRENGTH);

    private ItematicRegistries() {}
}
