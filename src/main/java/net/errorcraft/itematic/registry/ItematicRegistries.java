package net.errorcraft.itematic.registry;

import net.errorcraft.itematic.item.color.ItemColorType;
import net.errorcraft.itematic.item.color.ItemColorTypes;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.dispense.behavior.DispenseBehaviors;
import net.errorcraft.itematic.item.event.ItemEvent;
import net.errorcraft.itematic.item.event.ItemEvents;
import net.errorcraft.itematic.item.model.override.ModelOverride;
import net.errorcraft.itematic.item.model.override.ModelOverrides;
import net.errorcraft.itematic.item.pointer.Pointer;
import net.errorcraft.itematic.item.pointer.Pointers;
import net.errorcraft.itematic.item.smithing.template.SmithingTemplateType;
import net.errorcraft.itematic.item.smithing.template.SmithingTemplateTypes;
import net.errorcraft.itematic.mixin.registry.RegistriesAccessor;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.sequence.handler.SequenceHandlerType;
import net.errorcraft.itematic.world.action.sequence.handler.SequenceHandlerTypes;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.registry.Registry;

public class ItematicRegistries {
    public static final Registry<ItemComponentType<?>> ITEM_COMPONENT_TYPE = RegistriesAccessor.create(ItematicRegistryKeys.ITEM_COMPONENT_TYPE, r -> ItemComponentTypes.USE_DURATION);
    public static final Registry<ItemColorType<?>> ITEM_COLOR_TYPE = RegistriesAccessor.create(ItematicRegistryKeys.ITEM_COLOR_TYPE, r -> ItemColorTypes.DYEABLE);
    public static final Registry<DispenserBehavior> DISPENSE_BEHAVIOR = RegistriesAccessor.create(ItematicRegistryKeys.DISPENSE_BEHAVIOR, r -> DispenseBehaviors.ITEM);
    public static final Registry<ItemEvent> ITEM_EVENT = RegistriesAccessor.create(ItematicRegistryKeys.ITEM_EVENT, r -> ItemEvents.USE);
    public static final Registry<ActionType<?>> ACTION_TYPE = RegistriesAccessor.create(ItematicRegistryKeys.ACTION_TYPE, r -> ActionTypes.MODIFY_ITEM);
    public static final Registry<ModelOverride> MODEL_OVERRIDE = RegistriesAccessor.create(ItematicRegistryKeys.MODEL_OVERRIDE, r -> ModelOverrides.LEFT_HANDED);
    public static final Registry<Pointer> POINTER = RegistriesAccessor.create(ItematicRegistryKeys.POINTER, r -> Pointers.SPAWN_LOCATION);
    public static final Registry<SequenceHandlerType<?>> SEQUENCE_HANDLER_TYPE = RegistriesAccessor.create(ItematicRegistryKeys.SEQUENCE_HANDLER_TYPE, r -> SequenceHandlerTypes.UNCHECKED);
    public static final Registry<SmithingTemplateType<?>> SMITHING_TEMPLATE_TYPE = RegistriesAccessor.create(ItematicRegistryKeys.SMITHING_TEMPLATE_TYPE, r -> SmithingTemplateTypes.TRIM_PATTERN);

    private ItematicRegistries() {}
}
