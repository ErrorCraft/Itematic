package net.errorcraft.itematic.access.village;

import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;
import org.jetbrains.annotations.Nullable;

public interface VillagerProfessionAccess {
    @Nullable
    default TagKey<Item> itematic$gatherableItemsTag() {
        return null;
    }
}
