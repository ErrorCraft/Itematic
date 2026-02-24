package net.errorcraft.itematic.potion;

import net.minecraft.potion.Potion;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class PotionKeys {
    public static final RegistryKey<Potion> AWKWARD = of("awkward");
    public static final RegistryKey<Potion> FIRE_RESISTANCE = of("fire_resistance");
    public static final RegistryKey<Potion> HARMING = of("harming");
    public static final RegistryKey<Potion> HEALING = of("healing");
    public static final RegistryKey<Potion> INFESTED = of("infested");
    public static final RegistryKey<Potion> INVISIBILITY = of("invisibility");
    public static final RegistryKey<Potion> LEAPING = of("leaping");
    public static final RegistryKey<Potion> LONG_FIRE_RESISTANCE = of("long_fire_resistance");
    public static final RegistryKey<Potion> LONG_INVISIBILITY = of("long_invisibility");
    public static final RegistryKey<Potion> LONG_LEAPING = of("long_leaping");
    public static final RegistryKey<Potion> LONG_NIGHT_VISION = of("long_night_vision");
    public static final RegistryKey<Potion> LONG_POISON = of("long_poison");
    public static final RegistryKey<Potion> LONG_REGENERATION = of("long_regeneration");
    public static final RegistryKey<Potion> LONG_SLOWNESS = of("long_slowness");
    public static final RegistryKey<Potion> LONG_SLOW_FALLING = of("long_slow_falling");
    public static final RegistryKey<Potion> LONG_STRENGTH = of("long_strength");
    public static final RegistryKey<Potion> LONG_SWIFTNESS = of("long_swiftness");
    public static final RegistryKey<Potion> LONG_TURTLE_MASTER = of("long_turtle_master");
    public static final RegistryKey<Potion> LONG_WATER_BREATHING = of("long_water_breathing");
    public static final RegistryKey<Potion> LONG_WEAKNESS = of("long_weakness");
    public static final RegistryKey<Potion> MUNDANE = of("mundane");
    public static final RegistryKey<Potion> NIGHT_VISION = of("night_vision");
    public static final RegistryKey<Potion> OOZING = of("oozing");
    public static final RegistryKey<Potion> POISON = of("poison");
    public static final RegistryKey<Potion> REGENERATION = of("regeneration");
    public static final RegistryKey<Potion> SLOWNESS = of("slowness");
    public static final RegistryKey<Potion> SLOW_FALLING = of("slow_falling");
    public static final RegistryKey<Potion> STRENGTH = of("strength");
    public static final RegistryKey<Potion> STRONG_HARMING = of("strong_harming");
    public static final RegistryKey<Potion> STRONG_HEALING = of("strong_healing");
    public static final RegistryKey<Potion> STRONG_LEAPING = of("strong_leaping");
    public static final RegistryKey<Potion> STRONG_POISON = of("strong_poison");
    public static final RegistryKey<Potion> STRONG_REGENERATION = of("strong_regeneration");
    public static final RegistryKey<Potion> STRONG_SLOWNESS = of("strong_slowness");
    public static final RegistryKey<Potion> STRONG_STRENGTH = of("strong_strength");
    public static final RegistryKey<Potion> STRONG_SWIFTNESS = of("strong_swiftness");
    public static final RegistryKey<Potion> STRONG_TURTLE_MASTER = of("strong_turtle_master");
    public static final RegistryKey<Potion> SWIFTNESS = of("swiftness");
    public static final RegistryKey<Potion> THICK = of("thick");
    public static final RegistryKey<Potion> TURTLE_MASTER = of("turtle_master");
    public static final RegistryKey<Potion> WATER = of("water");
    public static final RegistryKey<Potion> WATER_BREATHING = of("water_breathing");
    public static final RegistryKey<Potion> WEAKNESS = of("weakness");
    public static final RegistryKey<Potion> WEAVING = of("weaving");
    public static final RegistryKey<Potion> WIND_CHARGED = of("wind_charged");

    private PotionKeys() {}

    private static RegistryKey<Potion> of(String id) {
        return RegistryKey.of(RegistryKeys.POTION, Identifier.ofVanilla(id));
    }
}
