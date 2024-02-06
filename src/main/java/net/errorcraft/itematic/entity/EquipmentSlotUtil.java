package net.errorcraft.itematic.entity;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import org.jetbrains.annotations.Nullable;

public class EquipmentSlotUtil {
    private EquipmentSlotUtil() {}

    @Nullable
    public static ArmorItem.Type armorType(EquipmentSlot slot) {
        return switch (slot) {
            case HEAD -> ArmorItem.Type.HELMET;
            case CHEST -> ArmorItem.Type.CHESTPLATE;
            case LEGS -> ArmorItem.Type.LEGGINGS;
            case FEET -> ArmorItem.Type.BOOTS;
            case BODY -> ArmorItem.Type.BODY;
            default -> null;
        };
    }
}
