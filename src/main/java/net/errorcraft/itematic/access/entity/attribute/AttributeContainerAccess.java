package net.errorcraft.itematic.access.entity.attribute;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.entry.RegistryEntry;
import org.jetbrains.annotations.Nullable;

public interface AttributeContainerAccess {
    double itematic$getTrueBaseValue(RegistryEntry<EntityAttribute> attribute);
    double itematic$getValue(RegistryEntry<EntityAttribute> attribute, @Nullable Double possibleBase);
}
