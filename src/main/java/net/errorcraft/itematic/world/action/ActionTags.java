package net.errorcraft.itematic.world.action;

import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ActionTags {
    public static final TagKey<ActionEntry> USE_HOE_ON_BLOCK = of("use_hoe_on_block");
    public static final TagKey<ActionEntry> USE_SHOVEL_ON_BLOCK = of("use_shovel_on_block");

    private ActionTags() {}

    private static TagKey<ActionEntry> of(String id) {
        return TagKey.of(ItematicRegistryKeys.ACTION, new Identifier(id));
    }
}
