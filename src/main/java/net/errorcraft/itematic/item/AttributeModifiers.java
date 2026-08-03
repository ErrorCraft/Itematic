package net.errorcraft.itematic.item;

import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.util.Identifier;
import net.minecraft.world.waypoint.Waypoint;

public class AttributeModifiers {
    private AttributeModifiers() {}

    public static AttributeModifiersComponent armor(ArmorMaterial material, EquipmentType type) {
        Identifier attributeId = Identifier.ofVanilla("armor." + type.getName());
        AttributeModifierSlot slot = AttributeModifierSlot.forEquipmentSlot(type.getEquipmentSlot());
        AttributeModifiersComponent.Builder builder = AttributeModifiersComponent.builder();
        builder.add(
            EntityAttributes.ARMOR,
            new EntityAttributeModifier(
                attributeId,
                material.defense().get(type),
                EntityAttributeModifier.Operation.ADD_VALUE
            ),
            slot
        );
        double toughness = material.toughness();
        if (toughness > 0.0d) {
            builder.add(
                EntityAttributes.ARMOR_TOUGHNESS,
                new EntityAttributeModifier(
                    attributeId,
                    toughness,
                    EntityAttributeModifier.Operation.ADD_VALUE
                ),
                slot
            );
        }

        double knockbackResistance = material.knockbackResistance();
        if (knockbackResistance > 0.0d) {
            builder.add(
                EntityAttributes.KNOCKBACK_RESISTANCE,
                new EntityAttributeModifier(
                    attributeId,
                    knockbackResistance,
                    EntityAttributeModifier.Operation.ADD_VALUE
                ),
                slot
            );
        }

        return builder.build();
    }

    public static AttributeModifiersComponent hideFromLocatorBar() {
        return AttributeModifiersComponent.builder()
            .add(
                EntityAttributes.WAYPOINT_TRANSMIT_RANGE,
                Waypoint.DISABLE_TRACKING,
                AttributeModifierSlot.HEAD,
                AttributeModifiersComponent.Display.getHidden()
            )
            .build();
    }
}
