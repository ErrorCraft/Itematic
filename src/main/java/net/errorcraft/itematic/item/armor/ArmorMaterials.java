package net.errorcraft.itematic.item.armor;

import net.minecraft.registry.Registerable;
import net.minecraft.util.Identifier;

public class ArmorMaterials {
    private ArmorMaterials() {}

    public static void bootstrap(Registerable<ArmorMaterial> registerable) {
        registerable.register(ArmorMaterialKeys.LEATHER, ArmorMaterial.from(Identifier.ofVanilla("leather"), net.minecraft.item.ArmorMaterials.LEATHER));
        registerable.register(ArmorMaterialKeys.CHAINMAIL, ArmorMaterial.from(Identifier.ofVanilla("chainmail"), net.minecraft.item.ArmorMaterials.CHAIN));
        registerable.register(ArmorMaterialKeys.IRON, ArmorMaterial.from(Identifier.ofVanilla("iron"), net.minecraft.item.ArmorMaterials.IRON));
        registerable.register(ArmorMaterialKeys.GOLD, ArmorMaterial.from(Identifier.ofVanilla("gold"), net.minecraft.item.ArmorMaterials.GOLD));
        registerable.register(ArmorMaterialKeys.DIAMOND, ArmorMaterial.from(Identifier.ofVanilla("diamond"), net.minecraft.item.ArmorMaterials.DIAMOND));
        registerable.register(ArmorMaterialKeys.NETHERITE, ArmorMaterial.from(Identifier.ofVanilla("netherite"), net.minecraft.item.ArmorMaterials.NETHERITE));
        registerable.register(ArmorMaterialKeys.TURTLE, ArmorMaterial.from(Identifier.ofVanilla("turtle"), net.minecraft.item.ArmorMaterials.TURTLE));
        registerable.register(ArmorMaterialKeys.ARMADILLO, ArmorMaterial.from(Identifier.ofVanilla("armadillo"), net.minecraft.item.ArmorMaterials.ARMADILLO));
    }
}
