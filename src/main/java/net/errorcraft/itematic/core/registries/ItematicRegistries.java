package net.errorcraft.itematic.core.registries;

import net.errorcraft.itematic.core.dispenser.behavior.DispenseBehavior;
import net.errorcraft.itematic.world.action.ActionEntry;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.sequence.handler.SequenceHandlerType;
import net.errorcraft.itematic.world.entity.spawn.rule.EntitySpawnRuleType;
import net.errorcraft.itematic.world.item.ItemEvent;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.group.entry.ItemGroupEntryProvider;
import net.errorcraft.itematic.world.item.group.entry.ItemGroupEntryType;
import net.errorcraft.itematic.world.item.holder.rule.ItemHolderRuleType;
import net.errorcraft.itematic.world.item.placement.block.picker.BlockPickerType;
import net.errorcraft.itematic.world.item.smithing.template.SmithingTemplate;
import net.errorcraft.itematic.world.item.trading.Trade;
import net.errorcraft.itematic.world.item.trading.modifier.TradeModifierType;
import net.errorcraft.itematic.world.item.use.duration.provider.UseDurationProviderType;
import net.errorcraft.itematic.world.item.weapon.shooter.method.ShooterMethodType;
import net.errorcraft.itematic.world.level.modification.WorldModificationType;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

public class ItematicRegistries {
    public static final ResourceKey<Registry<ItemBehaviorType<?>>> ITEM_BEHAVIOR_TYPE = of("item_behavior_type");
    public static final ResourceKey<Registry<DispenseBehavior>> DISPENSE_BEHAVIOR = of("dispense_behavior");
    public static final ResourceKey<Registry<ItemEvent>> ITEM_EVENT = of("item_event");
    public static final ResourceKey<Registry<ActionType<?>>> ACTION_TYPE = of("action_type");
    public static final ResourceKey<Registry<ItemGroupEntryProvider>> ITEM_GROUP_ENTRY_PROVIDER = of("item_group_entry_provider");
    public static final ResourceKey<Registry<Trade>> TRADE = of("trade");
    public static final ResourceKey<Registry<ActionEntry>> ACTION = of("action");
    public static final ResourceKey<Registry<SequenceHandlerType<?>>> SEQUENCE_HANDLER_TYPE = of("sequence_handler_type");
    public static final ResourceKey<Registry<SmithingTemplate>> SMITHING_TEMPLATE = of("smithing_template");
    public static final ResourceKey<Registry<BlockPickerType<?>>> BLOCK_PICKER_TYPE = of("block_picker_type");
    public static final ResourceKey<Registry<TradeModifierType<?>>> TRADE_MODIFIER_TYPE = of("trade_modifier_type");
    public static final ResourceKey<Registry<UseDurationProviderType<?>>> USE_DURATION_PROVIDER_TYPE = of("use_duration_provider_type");
    public static final ResourceKey<Registry<ItemHolderRuleType<?>>> ITEM_HOLDER_RULE_TYPE = of("item_holder_rule_type");
    public static final ResourceKey<Registry<ShooterMethodType<?>>> SHOOTER_METHOD_TYPE = of("shooter_method_type");
    public static final ResourceKey<Registry<WorldModificationType<?>>> WORLD_MODIFICATION_TYPE = of("world_modification_type");
    public static final ResourceKey<Registry<EntitySpawnRuleType<?>>> ENTITY_SPAWN_RULE_TYPE = of("entity_spawn_rule_type");
    public static final ResourceKey<Registry<DataComponentType<?>>> MELEE_WEAPON_COMPONENT_TYPE = of("melee_weapon_component_type");
    public static final ResourceKey<Registry<ItemGroupEntryType<?>>> ITEM_GROUP_ENTRY_TYPE = of("item_group_entry_type");

    private ItematicRegistries() {}

    private static <T> ResourceKey<Registry<T>> of(String name) {
        return ResourceKey.createRegistryKey(Identifier.withDefaultNamespace(name));
    }
}
