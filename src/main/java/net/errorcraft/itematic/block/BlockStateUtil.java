package net.errorcraft.itematic.block;

import net.minecraft.block.BlockState;
import net.minecraft.state.property.Property;

public class BlockStateUtil {
    private BlockStateUtil() {}

    public static <T extends Comparable<T>> BlockState with(BlockState state, Property<T> property, String name) {
        return property.parse(name)
            .map((value) -> state.with(property, value))
            .orElse(state);
    }
}
