package net.errorcraft.itematic.access.entity.attribute;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.entry.RegistryEntry;
import org.jetbrains.annotations.Nullable;

public interface AttributeContainerAccess {
    default double itematic$getValue(RegistryEntry<EntityAttribute> attribute, @Nullable Double possibleBase) {
        return 0.0d;
    }
}
