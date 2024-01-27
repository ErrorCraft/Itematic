package net.errorcraft.itematic.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class EnchantmentTags {
    public static final TagKey<Enchantment> CURSED = of("cursed");
    public static final TagKey<Enchantment> ENCHANTING = of("enchanting");
    public static final TagKey<Enchantment> FORGING = of("forging");

    public static final TagKey<Enchantment> ARMOR_ENCHANTING = of("armor_enchanting");
    public static final TagKey<Enchantment> BOOTS_ENCHANTING = of("boots_enchanting");
    public static final TagKey<Enchantment> LEGGINGS_ENCHANTING = of("leggings_enchanting");
    public static final TagKey<Enchantment> CHESTPLATE_ENCHANTING = of("chestplate_enchanting");
    public static final TagKey<Enchantment> HELMET_ENCHANTING = of("helmet_enchanting");
    public static final TagKey<Enchantment> SWORD_ENCHANTING = of("sword_enchanting");
    public static final TagKey<Enchantment> TOOL_ENCHANTING = of("tool_enchanting");
    public static final TagKey<Enchantment> TOOL_FORGING = of("tool_forging");
    public static final TagKey<Enchantment> SHOVEL_ENCHANTING = of("shovel_enchanting");
    public static final TagKey<Enchantment> PICKAXE_ENCHANTING = of("pickaxe_enchanting");
    public static final TagKey<Enchantment> AXE_ENCHANTING = of("axe_enchanting");
    public static final TagKey<Enchantment> HOE_ENCHANTING = of("hoe_enchanting");
    public static final TagKey<Enchantment> BOW_ENCHANTING = of("bow_enchanting");
    public static final TagKey<Enchantment> CROSSBOW_ENCHANTING = of("crossbow_enchanting");

    public static final TagKey<Enchantment> ARMOR_FORGING = of("armor_forging");
    public static final TagKey<Enchantment> BOOTS_FORGING = of("boots_forging");
    public static final TagKey<Enchantment> LEGGINGS_FORGING = of("leggings_forging");
    public static final TagKey<Enchantment> CHESTPLATE_FORGING = of("chestplate_forging");
    public static final TagKey<Enchantment> HELMET_FORGING = of("helmet_forging");
    public static final TagKey<Enchantment> SWORD_FORGING = of("sword_forging");
    public static final TagKey<Enchantment> SHOVEL_FORGING = of("shovel_forging");
    public static final TagKey<Enchantment> PICKAXE_FORGING = of("pickaxe_forging");
    public static final TagKey<Enchantment> AXE_FORGING = of("axe_forging");
    public static final TagKey<Enchantment> HOE_FORGING = of("hoe_forging");
    public static final TagKey<Enchantment> BOW_FORGING = of("bow_forging");
    public static final TagKey<Enchantment> CROSSBOW_FORGING = of("crossbow_forging");
    public static final TagKey<Enchantment> STEERING_FORGING = of("steering_forging");

    private EnchantmentTags() {}

    private static TagKey<Enchantment> of(String id) {
        return TagKey.of(RegistryKeys.ENCHANTMENT, new Identifier(id));
    }
}
