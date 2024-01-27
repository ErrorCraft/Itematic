package net.errorcraft.itematic.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class EnchantmentKeys {
    public static final RegistryKey<Enchantment> PROTECTION = of("protection");
    public static final RegistryKey<Enchantment> FIRE_PROTECTION = of("fire_protection");
    public static final RegistryKey<Enchantment> FEATHER_FALLING = of("feather_falling");
    public static final RegistryKey<Enchantment> BLAST_PROTECTION = of("blast_protection");
    public static final RegistryKey<Enchantment> PROJECTILE_PROTECTION = of("projectile_protection");
    public static final RegistryKey<Enchantment> RESPIRATION = of("respiration");
    public static final RegistryKey<Enchantment> AQUA_AFFINITY = of("aqua_affinity");
    public static final RegistryKey<Enchantment> THORNS = of("thorns");
    public static final RegistryKey<Enchantment> DEPTH_STRIDER = of("depth_strider");
    public static final RegistryKey<Enchantment> FROST_WALKER = of("frost_walker");
    public static final RegistryKey<Enchantment> BINDING_CURSE = of("binding_curse");
    public static final RegistryKey<Enchantment> SOUL_SPEED = of("soul_speed");
    public static final RegistryKey<Enchantment> SWIFT_SNEAK = of("swift_sneak");
    public static final RegistryKey<Enchantment> SHARPNESS = of("sharpness");
    public static final RegistryKey<Enchantment> SMITE = of("smite");
    public static final RegistryKey<Enchantment> BANE_OF_ARTHROPODS = of("bane_of_arthropods");
    public static final RegistryKey<Enchantment> KNOCKBACK = of("knockback");
    public static final RegistryKey<Enchantment> FIRE_ASPECT = of("fire_aspect");
    public static final RegistryKey<Enchantment> LOOTING = of("looting");
    public static final RegistryKey<Enchantment> SWEEPING_EDGE = of("sweeping_edge");
    public static final RegistryKey<Enchantment> EFFICIENCY = of("efficiency");
    public static final RegistryKey<Enchantment> SILK_TOUCH = of("silk_touch");
    public static final RegistryKey<Enchantment> UNBREAKING = of("unbreaking");
    public static final RegistryKey<Enchantment> FORTUNE = of("fortune");
    public static final RegistryKey<Enchantment> POWER = of("power");
    public static final RegistryKey<Enchantment> PUNCH = of("punch");
    public static final RegistryKey<Enchantment> FLAME = of("flame");
    public static final RegistryKey<Enchantment> INFINITY = of("infinity");
    public static final RegistryKey<Enchantment> LUCK_OF_THE_SEA = of("luck_of_the_sea");
    public static final RegistryKey<Enchantment> LURE = of("lure");
    public static final RegistryKey<Enchantment> LOYALTY = of("loyalty");
    public static final RegistryKey<Enchantment> IMPALING = of("impaling");
    public static final RegistryKey<Enchantment> RIPTIDE = of("riptide");
    public static final RegistryKey<Enchantment> CHANNELING = of("channeling");
    public static final RegistryKey<Enchantment> MULTISHOT = of("multishot");
    public static final RegistryKey<Enchantment> QUICK_CHARGE = of("quick_charge");
    public static final RegistryKey<Enchantment> PIERCING = of("piercing");
    public static final RegistryKey<Enchantment> MENDING = of("mending");
    public static final RegistryKey<Enchantment> VANISHING_CURSE = of("vanishing_curse");

    private EnchantmentKeys() {}

    private static RegistryKey<Enchantment> of(String id) {
        return RegistryKey.of(RegistryKeys.ENCHANTMENT, new Identifier(id));
    }
}
