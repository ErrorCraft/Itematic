package net.errorcraft.itematic.access.entity.attribute;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.entry.RegistryEntry;

public interface DefaultAttributeContainerAccess {
    default double itematic$getValue(RegistryEntry<EntityAttribute> attribute, double base) {
        return 0.0d;
    }
}
