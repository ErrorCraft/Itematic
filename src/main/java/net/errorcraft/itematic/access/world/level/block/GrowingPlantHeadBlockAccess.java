package net.errorcraft.itematic.access.world.level.block;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.Nullable;

public interface GrowingPlantHeadBlockAccess {
    @Nullable
    default ResourceKey<Item> itematic$stemItemId() {
        return null;
    }
    default void itematic$setStemItemId(ResourceKey<Item> stemItemId) {}
}
