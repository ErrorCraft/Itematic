package net.errorcraft.itematic.access.world.entity.ai.attributes;

public interface AttributeInstanceAccess {
    default double itematic$getValue(double base) {
        return 0.0d;
    }
}
