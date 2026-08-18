package net.errorcraft.itematic.access.world.level.block.state;

import net.minecraft.core.component.DataComponentMap;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;

public interface BlockBehaviourAccess {
    default ResourceKey<Item> itematic$asItemId() {
        throw new AssertionError("Implemented via mixin");
    }
    default void itematic$setAsItemId(ResourceKey<Item> itemId) {}
    default void itematic$addComponents(DataComponentMap.Builder builder) {}
    default BlockPlaceContext itematic$blockPlaceContext(BlockPlaceContext context) {
        return context;
    }
}
