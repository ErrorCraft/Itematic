package net.errorcraft.itematic.access.entity.ai.goal;

import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;

public interface TemptGoalAccess {
    default void itematic$setItems(TagKey<Item> items) {}
}
