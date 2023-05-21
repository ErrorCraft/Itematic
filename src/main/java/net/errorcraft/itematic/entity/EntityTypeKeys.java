package net.errorcraft.itematic.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class EntityTypeKeys {
    public static final RegistryKey<EntityType<?>> COW = of("cow");
    public static final RegistryKey<EntityType<?>> EGG = of("egg");
    public static final RegistryKey<EntityType<?>> ENDER_PEARL = of("ender_pearl");
    public static final RegistryKey<EntityType<?>> ENDERMAN = of("enderman");
    public static final RegistryKey<EntityType<?>> EXPERIENCE_BOTTLE = of("experience_bottle");
    public static final RegistryKey<EntityType<?>> PIG = of("pig");
    public static final RegistryKey<EntityType<?>> SNOWBALL = of("snowball");

    private EntityTypeKeys() {}

    private static RegistryKey<EntityType<?>> of(String id) {
        return RegistryKey.of(RegistryKeys.ENTITY_TYPE, new Identifier(id));
    }
}
