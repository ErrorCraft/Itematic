package net.errorcraft.itematic.access.entity;

import net.minecraft.util.StringIdentifiable;

public interface EquipmentSlotAccess extends StringIdentifiable {
    @Override
    default String asString() {
        return "";
    }
}
