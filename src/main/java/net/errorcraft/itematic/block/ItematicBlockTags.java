package net.errorcraft.itematic.block;

import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ItematicBlockTags {
    public static final TagKey<Block> END_CRYSTAL_SPAWNABLE_ON = of("end_crystal_spawnable_on");
    public static final TagKey<Block> SWORD_SUPER_EFFICIENT = of("sword_super_efficient");
    public static final TagKey<Block> SHEARS_EFFICIENT = of("shears_efficient");
    public static final TagKey<Block> SHEARS_SLIGHTLY_EFFICIENT = of("shears_slightly_efficient");
    public static final TagKey<Block> SHEARS_SUPER_EFFICIENT = of("shears_super_efficient");

    private ItematicBlockTags() {}

    private static TagKey<Block> of(String id) {
        return TagKey.of(RegistryKeys.BLOCK, new Identifier(id));
    }
}
