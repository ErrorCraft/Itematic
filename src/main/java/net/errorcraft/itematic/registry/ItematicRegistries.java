package net.errorcraft.itematic.registry;

import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.mixin.registry.RegistriesAccessor;
import net.minecraft.registry.Registry;

public class ItematicRegistries {
    public static final Registry<ItemComponentType<?>> ITEM_COMPONENT_TYPE = RegistriesAccessor.create(ItematicRegistryKeys.ITEM_COMPONENT_TYPE, r -> ItemComponentTypes.USE_DURATION);

    private ItematicRegistries() {}
}
