package net.errorcraft.itematic.registry;

import net.errorcraft.itematic.item.armor.ArmorMaterial;
import net.errorcraft.itematic.item.color.ItemColorType;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class ItematicRegistryKeys {
    public static final RegistryKey<Registry<ItemComponentType<?>>> ITEM_COMPONENT_TYPE = RegistryKey.ofRegistry(new Identifier("item_component_type"));
    public static final RegistryKey<Registry<ArmorMaterial>> ARMOR_MATERIAL = RegistryKey.ofRegistry(new Identifier("armor_material"));
    public static final RegistryKey<Registry<ItemColorType<?>>> ITEM_COLOR_TYPE = RegistryKey.ofRegistry(new Identifier("item_color_type"));
    public static final RegistryKey<Registry<DispenserBehavior>> DISPENSE_BEHAVIOR = RegistryKey.ofRegistry(new Identifier("dispense_behavior"));

    private ItematicRegistryKeys() {}
}
