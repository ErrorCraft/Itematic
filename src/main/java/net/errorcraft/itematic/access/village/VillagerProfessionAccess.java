package net.errorcraft.itematic.access.village;

import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;

public interface VillagerProfessionAccess {
    default TagKey<Item> gatherableItemsTag() {
        return null;
    }
}
