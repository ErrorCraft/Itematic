package net.errorcraft.itematic.access.entity.attribute;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import org.jetbrains.annotations.Nullable;

public interface AttributeContainerAccess {
    default double itematic$getValue(Holder<Attribute> attribute, @Nullable Double possibleBase) {
        return 0.0d;
    }
}
