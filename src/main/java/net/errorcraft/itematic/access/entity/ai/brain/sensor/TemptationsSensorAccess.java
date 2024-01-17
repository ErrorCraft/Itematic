package net.errorcraft.itematic.access.entity.ai.brain.sensor;

import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;

public interface TemptationsSensorAccess {
    default void itematic$setTemptations(TagKey<Item> temptations) {}
}
