package net.errorcraft.itematic.tags;

import net.errorcraft.itematic.core.registries.ItematicRegistries;
import net.errorcraft.itematic.world.item.group.ItemGroup;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;

public class ItemGroupTags {
    public static final TagKey<ItemGroup> CREATIVE_MODE_GROUPS = of("creative_mode_groups");

    private ItemGroupTags() {}

    private static TagKey<ItemGroup> of(String id) {
        return TagKey.create(
            ItematicRegistries.ITEM_GROUP,
            Identifier.withDefaultNamespace(id)
        );
    }
}
