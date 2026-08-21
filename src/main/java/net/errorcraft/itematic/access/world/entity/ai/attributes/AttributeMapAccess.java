package net.errorcraft.itematic.access.world.entity.ai.attributes;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import org.jspecify.annotations.Nullable;

public interface AttributeMapAccess {
    default double itematic$getValue(Holder<Attribute> attribute, @Nullable Double possibleBase) {
        return 0.0d;
    }
}
