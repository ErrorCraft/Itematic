package net.errorcraft.itematic.registry;

import net.errorcraft.itematic.mixin.core.registries.BuiltInRegistriesAccessor;
import net.errorcraft.itematic.village.trade.modifier.TradeModifierType;
import net.errorcraft.itematic.village.trade.modifier.TradeModifierTypes;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.sequence.handler.SequenceHandlerType;
import net.errorcraft.itematic.world.action.sequence.handler.SequenceHandlerTypes;
import net.errorcraft.itematic.world.entity.spawn.rule.EntitySpawnRuleType;
import net.errorcraft.itematic.world.item.ItemEvent;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.group.entry.ItemGroupEntryType;
import net.errorcraft.itematic.world.item.holder.rule.ItemHolderRuleType;
import net.errorcraft.itematic.world.item.placement.block.picker.BlockPickerType;
import net.errorcraft.itematic.world.item.smithing.template.SmithingTemplate;
import net.errorcraft.itematic.world.item.smithing.template.SmithingTemplates;
import net.errorcraft.itematic.world.item.use.duration.provider.UseDurationProviderType;
import net.errorcraft.itematic.world.item.weapon.melee.behavior.MeleeWeaponComponents;
import net.errorcraft.itematic.world.item.weapon.shooter.method.ShooterMethodType;
import net.errorcraft.itematic.world.modification.WorldModificationType;
import net.errorcraft.itematic.world.modification.WorldModificationTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;

public class ItematicRegistries {
    public static final Registry<ItemBehaviorType<?>> ITEM_BEHAVIOR_TYPE = BuiltInRegistriesAccessor.registerSimple(ItematicRegistryKeys.ITEM_BEHAVIOR_TYPE, r -> ItemBehaviorType.USEABLE);
    public static final Registry<ItemEvent> ITEM_EVENT = BuiltInRegistriesAccessor.registerSimple(ItematicRegistryKeys.ITEM_EVENT, r -> ItemEvent.USE);
    public static final Registry<ActionType<?>> ACTION_TYPE = BuiltInRegistriesAccessor.registerSimple(ItematicRegistryKeys.ACTION_TYPE, r -> ActionTypes.MODIFY_ITEM);
    public static final Registry<SequenceHandlerType<?>> SEQUENCE_HANDLER_TYPE = BuiltInRegistriesAccessor.registerSimple(ItematicRegistryKeys.SEQUENCE_HANDLER_TYPE, r -> SequenceHandlerTypes.UNCHECKED);
    public static final Registry<SmithingTemplate> SMITHING_TEMPLATE = BuiltInRegistriesAccessor.registerSimple(ItematicRegistryKeys.SMITHING_TEMPLATE, r -> SmithingTemplates.TRIM_PATTERN);
    public static final Registry<BlockPickerType<?>> BLOCK_PICKER_TYPE = BuiltInRegistriesAccessor.registerSimple(ItematicRegistryKeys.BLOCK_PICKER_TYPE, r -> BlockPickerType.SIMPLE);
    public static final Registry<TradeModifierType<?>> TRADE_MODIFIER_TYPE = BuiltInRegistriesAccessor.registerSimple(ItematicRegistryKeys.TRADE_MODIFIER_TYPE, r -> TradeModifierTypes.ENCHANT_WITH_LEVELS);
    public static final Registry<UseDurationProviderType<?>> USE_DURATION_PROVIDER_TYPE = BuiltInRegistriesAccessor.registerSimple(ItematicRegistryKeys.USE_DURATION_PROVIDER_TYPE, r -> UseDurationProviderType.CONSTANT);
    public static final Registry<ItemHolderRuleType<?>> ITEM_HOLDER_RULE_TYPE = BuiltInRegistriesAccessor.registerSimple(ItematicRegistryKeys.ITEM_HOLDER_RULE_TYPE, r -> ItemHolderRuleType.REJECT);
    public static final Registry<ShooterMethodType<?>> SHOOTER_METHOD_TYPE = BuiltInRegistriesAccessor.registerSimple(ItematicRegistryKeys.SHOOTER_METHOD_TYPE, r -> ShooterMethodType.DIRECT);
    public static final Registry<WorldModificationType<?>> WORLD_MODIFICATION_TYPE = BuiltInRegistriesAccessor.registerSimple(ItematicRegistryKeys.WORLD_MODIFICATION_TYPE, r -> WorldModificationTypes.DRAIN_FLUID);
    public static final Registry<EntitySpawnRuleType<?>> ENTITY_SPAWN_RULE_TYPE = BuiltInRegistriesAccessor.registerSimple(ItematicRegistryKeys.ENTITY_SPAWN_RULE_TYPE, r -> EntitySpawnRuleType.DISCARD);
    public static final Registry<DataComponentType<?>> MELEE_WEAPON_COMPONENT_TYPE = BuiltInRegistriesAccessor.registerSimple(ItematicRegistryKeys.MELEE_WEAPON_COMPONENT_TYPE, r -> MeleeWeaponComponents.SMASHING);
    public static final Registry<ItemGroupEntryType<?>> ITEM_GROUP_ENTRY_TYPE = BuiltInRegistriesAccessor.registerSimple(ItematicRegistryKeys.ITEM_GROUP_ENTRY_TYPE, r -> ItemGroupEntryType.STACK);

    private ItematicRegistries() {}
}
