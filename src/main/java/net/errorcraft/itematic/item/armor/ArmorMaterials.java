package net.errorcraft.itematic.item.armor;

import net.minecraft.registry.Registerable;
import net.minecraft.util.Identifier;

public class ArmorMaterials {
    private ArmorMaterials() {}

    public static void bootstrap(Registerable<ArmorMaterial> registerable) {
        registerable.register(ArmorMaterialKeys.LEATHER, new ArmorMaterial(new Identifier("leather")));
        registerable.register(ArmorMaterialKeys.CHAINMAIL, new ArmorMaterial(new Identifier("chainmail")));
        registerable.register(ArmorMaterialKeys.IRON, new ArmorMaterial(new Identifier("iron")));
        registerable.register(ArmorMaterialKeys.GOLD, new ArmorMaterial(new Identifier("gold")));
        registerable.register(ArmorMaterialKeys.DIAMOND, new ArmorMaterial(new Identifier("diamond")));
        registerable.register(ArmorMaterialKeys.NETHERITE, new ArmorMaterial(new Identifier("netherite")));
        registerable.register(ArmorMaterialKeys.TURTLE, new ArmorMaterial(new Identifier("turtle")));
    }
}
