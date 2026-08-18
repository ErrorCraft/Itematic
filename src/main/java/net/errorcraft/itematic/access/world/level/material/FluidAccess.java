package net.errorcraft.itematic.access.world.level.material;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.Nullable;

public interface FluidAccess {
    @Nullable
    default ResourceKey<Item> itematic$getBucketItemId() {
        return null;
    }
}
