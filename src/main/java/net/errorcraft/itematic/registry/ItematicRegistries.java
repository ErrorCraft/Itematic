package net.errorcraft.itematic.registry;

import net.errorcraft.itematic.item.color.ItemColorType;
import net.errorcraft.itematic.item.color.ItemColorTypes;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.dispense.behavior.DispenseBehaviors;
import net.errorcraft.itematic.mixin.registry.RegistriesAccessor;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.registry.Registry;

public class ItematicRegistries {
    public static final Registry<ItemComponentType<?>> ITEM_COMPONENT_TYPE = RegistriesAccessor.create(ItematicRegistryKeys.ITEM_COMPONENT_TYPE, r -> ItemComponentTypes.USE_DURATION);
    public static final Registry<ItemColorType<?>> ITEM_COLOR_TYPE = RegistriesAccessor.create(ItematicRegistryKeys.ITEM_COLOR_TYPE, r -> ItemColorTypes.DYEABLE);
    public static final Registry<DispenserBehavior> DISPENSE_BEHAVIOR = RegistriesAccessor.create(ItematicRegistryKeys.DISPENSE_BEHAVIOR, r -> DispenseBehaviors.ITEM);

    private ItematicRegistries() {}
}
