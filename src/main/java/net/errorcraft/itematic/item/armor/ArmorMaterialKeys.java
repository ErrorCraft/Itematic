package net.errorcraft.itematic.item.armor;

import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class ArmorMaterialKeys {
    public static final RegistryKey<ArmorMaterial> LEATHER = register("leather");
    public static final RegistryKey<ArmorMaterial> CHAINMAIL = register("chainmail");
    public static final RegistryKey<ArmorMaterial> IRON = register("iron");
    public static final RegistryKey<ArmorMaterial> GOLD = register("gold");
    public static final RegistryKey<ArmorMaterial> DIAMOND = register("diamond");
    public static final RegistryKey<ArmorMaterial> NETHERITE = register("netherite");
    public static final RegistryKey<ArmorMaterial> TURTLE = register("turtle");

    private ArmorMaterialKeys() {}

    private static RegistryKey<ArmorMaterial> register(String name) {
        return RegistryKey.of(ItematicRegistryKeys.ARMOR_MATERIAL, new Identifier(name));
    }
}
