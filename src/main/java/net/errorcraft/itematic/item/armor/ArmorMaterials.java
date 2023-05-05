package net.errorcraft.itematic.item.armor;

import net.minecraft.registry.Registerable;
import net.minecraft.util.Identifier;

public class ArmorMaterials {
    private ArmorMaterials() {}

    public static void bootstrap(Registerable<ArmorMaterial> registerable) {
        registerable.register(ArmorMaterialKeys.IRON, new ArmorMaterial(new Identifier("iron")));
    }
}
