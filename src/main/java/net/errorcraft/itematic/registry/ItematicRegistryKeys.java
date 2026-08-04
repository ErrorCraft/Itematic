package net.errorcraft.itematic.registry;

import net.errorcraft.itematic.entity.spawn.rule.EntitySpawnRuleType;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.dispense.behavior.DispenseBehavior;
import net.errorcraft.itematic.item.event.ItemEvent;
import net.errorcraft.itematic.item.group.entry.provider.ItemGroupEntryProvider;
import net.errorcraft.itematic.item.holder.rule.ItemHolderRuleType;
import net.errorcraft.itematic.item.placement.block.picker.BlockPickerType;
import net.errorcraft.itematic.item.shooter.method.ShooterMethodType;
import net.errorcraft.itematic.item.smithing.template.SmithingTemplate;
import net.errorcraft.itematic.item.use.provider.IntegerProviderType;
import net.errorcraft.itematic.village.trade.Trade;
import net.errorcraft.itematic.village.trade.modifier.TradeModifierType;
import net.errorcraft.itematic.world.action.ActionEntry;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.sequence.handler.SequenceHandlerType;
import net.errorcraft.itematic.world.modification.WorldModificationType;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

public class ItematicRegistryKeys {
    public static final ResourceKey<Registry<ItemComponentType<?>>> ITEM_COMPONENT_TYPE = ResourceKey.createRegistryKey(Identifier.withDefaultNamespace("item_component_type"));
    public static final ResourceKey<Registry<DispenseBehavior>> DISPENSE_BEHAVIOR = ResourceKey.createRegistryKey(Identifier.withDefaultNamespace("dispense_behavior"));
    public static final ResourceKey<Registry<ItemEvent>> ITEM_EVENT = ResourceKey.createRegistryKey(Identifier.withDefaultNamespace("item_event"));
    public static final ResourceKey<Registry<ActionType<?>>> ACTION_TYPE = ResourceKey.createRegistryKey(Identifier.withDefaultNamespace("action_type"));
    public static final ResourceKey<Registry<ItemGroupEntryProvider>> ITEM_GROUP_ENTRY_PROVIDER = ResourceKey.createRegistryKey(Identifier.withDefaultNamespace("item_group_entry_provider"));
    public static final ResourceKey<Registry<Trade>> TRADE = ResourceKey.createRegistryKey(Identifier.withDefaultNamespace("trade"));
    public static final ResourceKey<Registry<ActionEntry>> ACTION = ResourceKey.createRegistryKey(Identifier.withDefaultNamespace("action"));
    public static final ResourceKey<Registry<SequenceHandlerType<?>>> SEQUENCE_HANDLER_TYPE = ResourceKey.createRegistryKey(Identifier.withDefaultNamespace("sequence_handler_type"));
    public static final ResourceKey<Registry<SmithingTemplate>> SMITHING_TEMPLATE = ResourceKey.createRegistryKey(Identifier.withDefaultNamespace("smithing_template"));
    public static final ResourceKey<Registry<BlockPickerType<?>>> BLOCK_PICKER_TYPE = ResourceKey.createRegistryKey(Identifier.withDefaultNamespace("block_picker_type"));
    public static final ResourceKey<Registry<TradeModifierType<?>>> TRADE_MODIFIER_TYPE = ResourceKey.createRegistryKey(Identifier.withDefaultNamespace("trade_modifier_type"));
    public static final ResourceKey<Registry<IntegerProviderType<?>>> INTEGER_PROVIDER_TYPE = ResourceKey.createRegistryKey(Identifier.withDefaultNamespace("integer_provider_type"));
    public static final ResourceKey<Registry<ItemHolderRuleType<?>>> ITEM_HOLDER_RULE_TYPE = ResourceKey.createRegistryKey(Identifier.withDefaultNamespace("item_holder_rule_type"));
    public static final ResourceKey<Registry<ShooterMethodType<?>>> SHOOTER_METHOD_TYPE = ResourceKey.createRegistryKey(Identifier.withDefaultNamespace("shooter_method_type"));
    public static final ResourceKey<Registry<WorldModificationType<?>>> WORLD_MODIFICATION_TYPE = ResourceKey.createRegistryKey(Identifier.withDefaultNamespace("world_modification_type"));
    public static final ResourceKey<Registry<EntitySpawnRuleType<?>>> ENTITY_SPAWN_RULE_TYPE = ResourceKey.createRegistryKey(Identifier.withDefaultNamespace("entity_spawn_rule_type"));
    public static final ResourceKey<Registry<DataComponentType<?>>> MELEE_WEAPON_COMPONENT_TYPE = ResourceKey.createRegistryKey(Identifier.withDefaultNamespace("melee_weapon_component_type"));

    private ItematicRegistryKeys() {}
}
