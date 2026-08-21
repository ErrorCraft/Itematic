package net.errorcraft.itematic.access.world.entity.ai.attributes;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;

public interface AttributeSupplierAccess {
    default double itematic$getValue(Holder<Attribute> attribute, double base) {
        return 0.0d;
    }
}
