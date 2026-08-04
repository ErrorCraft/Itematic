package net.errorcraft.itematic.access.village;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

public interface VillagerProfessionAccess {
    @Nullable
    default TagKey<Item> itematic$gatherableItems() {
        return null;
    }
    default void itematic$setGatherableItems(TagKey<Item> gatherableItems) {}
}
