package net.errorcraft.itematic.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ItematicEntityTypeTags {
    public static final TagKey<EntityType<?>> PROFICIENT_WITH_GOLDEN_WEAPONS = of("proficient_with_golden_weapons");

    private ItematicEntityTypeTags() {}

    private static TagKey<EntityType<?>> of(String id) {
        return TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier(id));
    }
}
