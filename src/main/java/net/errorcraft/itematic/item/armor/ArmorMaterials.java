package net.errorcraft.itematic.item.armor;

import net.minecraft.registry.Registerable;
import net.minecraft.util.Identifier;

public class ArmorMaterials {
    private ArmorMaterials() {}

    public static void bootstrap(Registerable<ArmorMaterial> registerable) {
        registerable.register(ArmorMaterialKeys.LEATHER, ArmorMaterial.from(new Identifier("leather"), net.minecraft.item.ArmorMaterials.LEATHER));
        registerable.register(ArmorMaterialKeys.CHAINMAIL, ArmorMaterial.from(new Identifier("chainmail"), net.minecraft.item.ArmorMaterials.CHAIN));
        registerable.register(ArmorMaterialKeys.IRON, ArmorMaterial.from(new Identifier("iron"), net.minecraft.item.ArmorMaterials.IRON));
        registerable.register(ArmorMaterialKeys.GOLD, ArmorMaterial.from(new Identifier("gold"), net.minecraft.item.ArmorMaterials.GOLD));
        registerable.register(ArmorMaterialKeys.DIAMOND, ArmorMaterial.from(new Identifier("diamond"), net.minecraft.item.ArmorMaterials.DIAMOND));
        registerable.register(ArmorMaterialKeys.NETHERITE, ArmorMaterial.from(new Identifier("netherite"), net.minecraft.item.ArmorMaterials.NETHERITE));
        registerable.register(ArmorMaterialKeys.TURTLE, ArmorMaterial.from(new Identifier("turtle"), net.minecraft.item.ArmorMaterials.TURTLE));
        registerable.register(ArmorMaterialKeys.ARMADILLO, ArmorMaterial.from(new Identifier("armadillo"), net.minecraft.item.ArmorMaterials.ARMADILLO));
    }
}
