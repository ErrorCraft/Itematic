package net.errorcraft.itematic.access.entity.attribute;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;

public interface DefaultAttributeContainerAccess {
    default double itematic$getValue(Holder<Attribute> attribute, double base) {
        return 0.0d;
    }
}
