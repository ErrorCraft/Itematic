package net.errorcraft.itematic.access.village;

import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;
import org.jetbrains.annotations.Nullable;

public interface VillagerProfessionAccess {
    @Nullable
    default TagKey<Item> itematic$gatherableItems() {
        return null;
    }
    default void itematic$setGatherableItems(TagKey<Item> gatherableItems) {}
}
