package net.errorcraft.itematic.item.armor;

import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class ArmorMaterialKeys {
    public static final RegistryKey<ArmorMaterial> IRON = register("iron");

    private ArmorMaterialKeys() {}

    private static RegistryKey<ArmorMaterial> register(String name) {
        return RegistryKey.of(ArmorMaterials.ARMOR_MATERIAL_KEY, new Identifier(name));
    }
}
