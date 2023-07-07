package net.errorcraft.itematic.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class EntityTypeKeys {
    public static final RegistryKey<EntityType<?>> ARROW = of("arrow");
    public static final RegistryKey<EntityType<?>> AXOLOTL = of("axolotl");
    public static final RegistryKey<EntityType<?>> COD = of("cod");
    public static final RegistryKey<EntityType<?>> COW = of("cow");
    public static final RegistryKey<EntityType<?>> EGG = of("egg");
    public static final RegistryKey<EntityType<?>> ENDER_PEARL = of("ender_pearl");
    public static final RegistryKey<EntityType<?>> ENDERMAN = of("enderman");
    public static final RegistryKey<EntityType<?>> EXPERIENCE_BOTTLE = of("experience_bottle");
    public static final RegistryKey<EntityType<?>> FIREWORK_ROCKET = of("firework_rocket");
    public static final RegistryKey<EntityType<?>> PIG = of("pig");
    public static final RegistryKey<EntityType<?>> POTION = of("potion");
    public static final RegistryKey<EntityType<?>> PUFFERFISH = of("pufferfish");
    public static final RegistryKey<EntityType<?>> SALMON = of("salmon");
    public static final RegistryKey<EntityType<?>> SNOWBALL = of("snowball");
    public static final RegistryKey<EntityType<?>> SPECTRAL_ARROW = of("spectral_arrow");
    public static final RegistryKey<EntityType<?>> TADPOLE = of("tadpole");
    public static final RegistryKey<EntityType<?>> TROPICAL_FISH = of("tropical_fish");

    private EntityTypeKeys() {}

    private static RegistryKey<EntityType<?>> of(String id) {
        return RegistryKey.of(RegistryKeys.ENTITY_TYPE, new Identifier(id));
    }
}
