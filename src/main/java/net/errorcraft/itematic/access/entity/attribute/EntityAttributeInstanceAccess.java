package net.errorcraft.itematic.access.entity.attribute;

public interface EntityAttributeInstanceAccess {
    default double itematic$getValue(double base) {
        return 0.0d;
    }
}
