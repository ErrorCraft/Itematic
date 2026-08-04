package net.errorcraft.itematic.potion;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.alchemy.Potion;

public class PotionKeys {
    public static final ResourceKey<Potion> AWKWARD = of("awkward");
    public static final ResourceKey<Potion> FIRE_RESISTANCE = of("fire_resistance");
    public static final ResourceKey<Potion> HARMING = of("harming");
    public static final ResourceKey<Potion> HEALING = of("healing");
    public static final ResourceKey<Potion> INFESTED = of("infested");
    public static final ResourceKey<Potion> INVISIBILITY = of("invisibility");
    public static final ResourceKey<Potion> LEAPING = of("leaping");
    public static final ResourceKey<Potion> LONG_FIRE_RESISTANCE = of("long_fire_resistance");
    public static final ResourceKey<Potion> LONG_INVISIBILITY = of("long_invisibility");
    public static final ResourceKey<Potion> LONG_LEAPING = of("long_leaping");
    public static final ResourceKey<Potion> LONG_NIGHT_VISION = of("long_night_vision");
    public static final ResourceKey<Potion> LONG_POISON = of("long_poison");
    public static final ResourceKey<Potion> LONG_REGENERATION = of("long_regeneration");
    public static final ResourceKey<Potion> LONG_SLOWNESS = of("long_slowness");
    public static final ResourceKey<Potion> LONG_SLOW_FALLING = of("long_slow_falling");
    public static final ResourceKey<Potion> LONG_STRENGTH = of("long_strength");
    public static final ResourceKey<Potion> LONG_SWIFTNESS = of("long_swiftness");
    public static final ResourceKey<Potion> LONG_TURTLE_MASTER = of("long_turtle_master");
    public static final ResourceKey<Potion> LONG_WATER_BREATHING = of("long_water_breathing");
    public static final ResourceKey<Potion> LONG_WEAKNESS = of("long_weakness");
    public static final ResourceKey<Potion> MUNDANE = of("mundane");
    public static final ResourceKey<Potion> NIGHT_VISION = of("night_vision");
    public static final ResourceKey<Potion> OOZING = of("oozing");
    public static final ResourceKey<Potion> POISON = of("poison");
    public static final ResourceKey<Potion> REGENERATION = of("regeneration");
    public static final ResourceKey<Potion> SLOWNESS = of("slowness");
    public static final ResourceKey<Potion> SLOW_FALLING = of("slow_falling");
    public static final ResourceKey<Potion> STRENGTH = of("strength");
    public static final ResourceKey<Potion> STRONG_HARMING = of("strong_harming");
    public static final ResourceKey<Potion> STRONG_HEALING = of("strong_healing");
    public static final ResourceKey<Potion> STRONG_LEAPING = of("strong_leaping");
    public static final ResourceKey<Potion> STRONG_POISON = of("strong_poison");
    public static final ResourceKey<Potion> STRONG_REGENERATION = of("strong_regeneration");
    public static final ResourceKey<Potion> STRONG_SLOWNESS = of("strong_slowness");
    public static final ResourceKey<Potion> STRONG_STRENGTH = of("strong_strength");
    public static final ResourceKey<Potion> STRONG_SWIFTNESS = of("strong_swiftness");
    public static final ResourceKey<Potion> STRONG_TURTLE_MASTER = of("strong_turtle_master");
    public static final ResourceKey<Potion> SWIFTNESS = of("swiftness");
    public static final ResourceKey<Potion> THICK = of("thick");
    public static final ResourceKey<Potion> TURTLE_MASTER = of("turtle_master");
    public static final ResourceKey<Potion> WATER = of("water");
    public static final ResourceKey<Potion> WATER_BREATHING = of("water_breathing");
    public static final ResourceKey<Potion> WEAKNESS = of("weakness");
    public static final ResourceKey<Potion> WEAVING = of("weaving");
    public static final ResourceKey<Potion> WIND_CHARGED = of("wind_charged");

    private PotionKeys() {}

    private static ResourceKey<Potion> of(String id) {
        return ResourceKey.create(Registries.POTION, Identifier.withDefaultNamespace(id));
    }
}
