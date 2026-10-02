package net.errorcraft.itematic.world.item.component;

import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.HashMap;
import java.util.Map;

public class BlockItemStatePropertiesBuilder {
    private final Map<String, String> properties = new HashMap<>();

    private BlockItemStatePropertiesBuilder() {}

    public static BlockItemStatePropertiesBuilder create() {
        return new BlockItemStatePropertiesBuilder();
    }

    public BlockItemStateProperties build() {
        return new BlockItemStateProperties(this.properties);
    }

    public <T extends Comparable<T>> BlockItemStatePropertiesBuilder property(Property<T> property, T value) {
        this.properties.put(property.getName(), property.getName(value));
        return this;
    }
}
