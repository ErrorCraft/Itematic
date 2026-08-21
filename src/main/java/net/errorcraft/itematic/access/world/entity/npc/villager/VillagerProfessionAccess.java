package net.errorcraft.itematic.access.world.entity.npc.villager;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.Nullable;

public interface VillagerProfessionAccess {
    @Nullable
    default TagKey<Item> itematic$gatherableItems() {
        return null;
    }
    default void itematic$setGatherableItems(TagKey<Item> gatherableItems) {}
}
