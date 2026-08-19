package net.errorcraft.itematic.tags;

import net.errorcraft.itematic.core.registries.ItematicRegistries;
import net.errorcraft.itematic.world.action.ActionEntry;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;

public class ActionTags {
    public static final TagKey<ActionEntry> USE_HOE_ON_BLOCK = of("use_hoe_on_block");
    public static final TagKey<ActionEntry> USE_SHOVEL_ON_BLOCK = of("use_shovel_on_block");

    private ActionTags() {}

    private static TagKey<ActionEntry> of(String id) {
        return TagKey.create(ItematicRegistries.ACTION, Identifier.withDefaultNamespace(id));
    }
}
