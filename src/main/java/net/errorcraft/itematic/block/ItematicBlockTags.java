package net.errorcraft.itematic.block;

import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ItematicBlockTags {
    public static final TagKey<Block> END_CRYSTAL_SPAWNABLE_ON = of("end_crystal_spawnable_on");

    private ItematicBlockTags() {}

    private static TagKey<Block> of(String id) {
        return TagKey.of(RegistryKeys.BLOCK, new Identifier(id));
    }
}
