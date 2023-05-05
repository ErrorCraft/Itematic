package net.errorcraft.itematic.item.armor;

import net.minecraft.registry.Registerable;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class ArmorMaterials {
    public static final RegistryKey<Registry<ArmorMaterial>> ARMOR_MATERIAL_KEY = RegistryKey.ofRegistry(new Identifier("armor_material"));

    private ArmorMaterials() {}

    public static void bootstrap(Registerable<ArmorMaterial> registerable) {
        registerable.register(ArmorMaterialKeys.IRON, new ArmorMaterial(new Identifier("iron")));
    }
}
