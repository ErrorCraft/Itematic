package net.errorcraft.itematic.registry;

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
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class ItematicRegistryKeys {
    public static final RegistryKey<Registry<ItemComponentType<?>>> ITEM_COMPONENT_TYPE = RegistryKey.ofRegistry(Identifier.ofVanilla("item_component_type"));
    public static final RegistryKey<Registry<DispenseBehavior>> DISPENSE_BEHAVIOR = RegistryKey.ofRegistry(Identifier.ofVanilla("dispense_behavior"));
    public static final RegistryKey<Registry<ItemEvent>> ITEM_EVENT = RegistryKey.ofRegistry(Identifier.ofVanilla("item_event"));
    public static final RegistryKey<Registry<ActionType<?>>> ACTION_TYPE = RegistryKey.ofRegistry(Identifier.ofVanilla("action_type"));
    public static final RegistryKey<Registry<ItemGroupEntryProvider>> ITEM_GROUP_ENTRY_PROVIDER = RegistryKey.ofRegistry(Identifier.ofVanilla("item_group_entry_provider"));
    public static final RegistryKey<Registry<Trade>> TRADE = RegistryKey.ofRegistry(Identifier.ofVanilla("trade"));
    public static final RegistryKey<Registry<ActionEntry>> ACTION = RegistryKey.ofRegistry(Identifier.ofVanilla("action"));
    public static final RegistryKey<Registry<SequenceHandlerType<?>>> SEQUENCE_HANDLER_TYPE = RegistryKey.ofRegistry(Identifier.ofVanilla("sequence_handler_type"));
    public static final RegistryKey<Registry<SmithingTemplate>> SMITHING_TEMPLATE = RegistryKey.ofRegistry(Identifier.ofVanilla("smithing_template"));
    public static final RegistryKey<Registry<BlockPickerType<?>>> BLOCK_PICKER_TYPE = RegistryKey.ofRegistry(Identifier.ofVanilla("block_picker_type"));
    public static final RegistryKey<Registry<TradeModifierType<?>>> TRADE_MODIFIER_TYPE = RegistryKey.ofRegistry(Identifier.ofVanilla("trade_modifier_type"));
    public static final RegistryKey<Registry<IntegerProviderType<?>>> INTEGER_PROVIDER_TYPE = RegistryKey.ofRegistry(Identifier.ofVanilla("integer_provider_type"));
    public static final RegistryKey<Registry<ItemHolderRuleType<?>>> ITEM_HOLDER_RULE_TYPE = RegistryKey.ofRegistry(Identifier.ofVanilla("item_holder_rule_type"));
    public static final RegistryKey<Registry<ShooterMethodType<?>>> SHOOTER_METHOD_TYPE = RegistryKey.ofRegistry(Identifier.ofVanilla("shooter_method_type"));

    private ItematicRegistryKeys() {}
}
