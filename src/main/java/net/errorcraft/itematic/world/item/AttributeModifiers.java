package net.errorcraft.itematic.world.item;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.waypoints.Waypoint;

public class AttributeModifiers {
    private AttributeModifiers() {}

    public static ItemAttributeModifiers armor(ArmorMaterial material, ArmorType type) {
        Identifier attributeId = Identifier.withDefaultNamespace("armor." + type.getName());
        EquipmentSlotGroup slot = EquipmentSlotGroup.bySlot(type.getSlot());
        ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
        builder.add(
            Attributes.ARMOR,
            new AttributeModifier(
                attributeId,
                material.defense().get(type),
                AttributeModifier.Operation.ADD_VALUE
            ),
            slot
        );
        double toughness = material.toughness();
        if (toughness > 0.0d) {
            builder.add(
                Attributes.ARMOR_TOUGHNESS,
                new AttributeModifier(
                    attributeId,
                    toughness,
                    AttributeModifier.Operation.ADD_VALUE
                ),
                slot
            );
        }

        double knockbackResistance = material.knockbackResistance();
        if (knockbackResistance > 0.0d) {
            builder.add(
                Attributes.KNOCKBACK_RESISTANCE,
                new AttributeModifier(
                    attributeId,
                    knockbackResistance,
                    AttributeModifier.Operation.ADD_VALUE
                ),
                slot
            );
        }

        return builder.build();
    }

    public static ItemAttributeModifiers hideFromLocatorBar() {
        return ItemAttributeModifiers.builder()
            .add(
                Attributes.WAYPOINT_TRANSMIT_RANGE,
                Waypoint.WAYPOINT_TRANSMIT_RANGE_HIDE_MODIFIER,
                EquipmentSlotGroup.HEAD,
                ItemAttributeModifiers.Display.hidden()
            )
            .build();
    }
}
