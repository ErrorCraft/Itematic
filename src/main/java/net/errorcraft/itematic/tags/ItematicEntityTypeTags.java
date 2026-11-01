package net.errorcraft.itematic.tags;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class ItematicEntityTypeTags {
    public static final TagKey<EntityType<?>> CANNOT_INTERSECT_WITH_CUSHION = of("cannot_intersect_with_cushion");
    public static final TagKey<EntityType<?>> CARROT_ON_A_STICK_CONTROL_TARGETS = of("carrot_on_a_stick_control_targets");
    public static final TagKey<EntityType<?>> WARPED_FUNGUS_ON_A_STICK_CONTROL_TARGETS = of("warped_fungus_on_a_stick_control_targets");

    private ItematicEntityTypeTags() {}

    private static TagKey<EntityType<?>> of(String id) {
        return TagKey.create(Registries.ENTITY_TYPE, Identifier.withDefaultNamespace(id));
    }
}
