package net.errorcraft.itematic.registry;

import net.errorcraft.itematic.item.armor.ArmorMaterial;
import net.errorcraft.itematic.item.color.ItemColorType;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.event.ItemEvent;
import net.errorcraft.itematic.item.group.entry.provider.ItemGroupEntryProvider;
import net.errorcraft.itematic.item.model.override.ModelOverride;
import net.errorcraft.itematic.item.pointer.Pointer;
import net.errorcraft.itematic.village.trade.Trade;
import net.errorcraft.itematic.world.action.ActionEntry;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.sequence.handler.SequenceHandlerType;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class ItematicRegistryKeys {
    public static final RegistryKey<Registry<ItemComponentType<?>>> ITEM_COMPONENT_TYPE = RegistryKey.ofRegistry(new Identifier("item_component_type"));
    public static final RegistryKey<Registry<ArmorMaterial>> ARMOR_MATERIAL = RegistryKey.ofRegistry(new Identifier("armor_material"));
    public static final RegistryKey<Registry<ItemColorType<?>>> ITEM_COLOR_TYPE = RegistryKey.ofRegistry(new Identifier("item_color_type"));
    public static final RegistryKey<Registry<DispenserBehavior>> DISPENSE_BEHAVIOR = RegistryKey.ofRegistry(new Identifier("dispense_behavior"));
    public static final RegistryKey<Registry<ItemEvent>> ITEM_EVENT = RegistryKey.ofRegistry(new Identifier("item_event"));
    public static final RegistryKey<Registry<ActionType<?>>> ACTION_TYPE = RegistryKey.ofRegistry(new Identifier("action_type"));
    public static final RegistryKey<Registry<ItemGroupEntryProvider>> ITEM_GROUP_ENTRY_PROVIDER = RegistryKey.ofRegistry(new Identifier("item_group_entry_provider"));
    public static final RegistryKey<Registry<ModelOverride>> MODEL_OVERRIDE = RegistryKey.ofRegistry(new Identifier("model_override"));
    public static final RegistryKey<Registry<Pointer>> POINTER = RegistryKey.ofRegistry(new Identifier("pointer"));
    public static final RegistryKey<Registry<Trade>> TRADE = RegistryKey.ofRegistry(new Identifier("trade"));
    public static final RegistryKey<Registry<ActionEntry>> ACTION = RegistryKey.ofRegistry(new Identifier("action"));
    public static final RegistryKey<Registry<SequenceHandlerType<?>>> SEQUENCE_HANDLER_TYPE = RegistryKey.ofRegistry(new Identifier("sequence_handler_type"));

    private ItematicRegistryKeys() {}
}
