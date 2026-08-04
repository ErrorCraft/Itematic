package net.errorcraft.itematic.block;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ItematicBlockTags {
    public static final TagKey<Block> END_CRYSTAL_SPAWNABLE_ON = of("end_crystal_spawnable_on");
    public static final TagKey<Block> TILLABLE_INTO_FARMLAND = of("tillable_into_farmland");
    public static final TagKey<Block> FLATTENABLE_INTO_DIRT_PATH = of("flattenable_into_dirt_path");

    private ItematicBlockTags() {}

    private static TagKey<Block> of(String id) {
        return TagKey.create(Registries.BLOCK, Identifier.withDefaultNamespace(id));
    }
}
