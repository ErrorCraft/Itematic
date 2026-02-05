package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentSet;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.serialization.ItematicCodecs;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.AnimalArmorItem;
import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;

public record ArmorItemComponent(int defense, double toughness, double knockbackResistance, Identifier attributeId) implements ItemComponent<ArmorItemComponent> {
    public static final Codec<ArmorItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codecs.NON_NEGATIVE_INT.fieldOf("defense").forGetter(ArmorItemComponent::defense),
        ItematicCodecs.NON_NEGATIVE_DOUBLE.fieldOf("toughness").forGetter(ArmorItemComponent::toughness),
        ItematicCodecs.NON_NEGATIVE_DOUBLE.fieldOf("knockback_resistance").forGetter(ArmorItemComponent::knockbackResistance),
        Identifier.CODEC.fieldOf("attribute_id").forGetter(ArmorItemComponent::attributeId)
    ).apply(instance, ArmorItemComponent::new));

    public static ArmorItemComponent of(ArmorMaterial material, EquipmentType type) {
        return new ArmorItemComponent(
            material.defense().get(type),
            material.toughness(),
            material.knockbackResistance(),
            Identifier.ofVanilla("armor." + type.getName())
        );
    }

    public static ItemComponent<?>[] from(ArmorMaterial material, EquipmentType type) {
        return new ItemComponent<?>[] {
            StackableItemComponent.of(1),
            DamageableItemComponent.of(type.getMaxDamage(material.durability())),
            EquipmentItemComponent.of(material, type),
            of(material, type)
        };
    }

    public static ItemComponent<?>[] from(ArmorMaterial material, EquipmentType type, AnimalArmorItem.Type animalType) {
        return new ItemComponent<?>[] {
            StackableItemComponent.of(1),
            EquipmentItemComponent.of(material, type, animalType),
            of(material, type)
        };
    }

    public static ItemComponent<?>[] fromDamageable(ArmorMaterial material, EquipmentType type, AnimalArmorItem.Type animalType) {
        return new ItemComponent<?>[] {
            StackableItemComponent.of(1),
            DamageableItemComponent.of(type.getMaxDamage(material.durability()), animalType.itematic$breakSound()),
            EquipmentItemComponent.of(material, type, animalType),
            of(material, type)
        };
    }

    @Override
    public ItemComponentType<ArmorItemComponent> type() {
        return ItemComponentTypes.ARMOR;
    }

    @Override
    public Codec<ArmorItemComponent> codec() {
        return CODEC;
    }

    @Override
    public void addAttributeModifiers(AttributeModifiersComponent.Builder builder, ItemComponentSet components) {
        AttributeModifierSlot slot = components.get(ItemComponentTypes.EQUIPMENT)
            .map(EquipmentItemComponent::equippable)
            .map(EquippableComponent::slot)
            .map(AttributeModifierSlot::forEquipmentSlot)
            .orElse(AttributeModifierSlot.ARMOR);
        builder.add(
            EntityAttributes.ARMOR,
            new EntityAttributeModifier(
                this.attributeId,
                this.defense,
                EntityAttributeModifier.Operation.ADD_VALUE
            ),
            slot
        );
        builder.add(
            EntityAttributes.ARMOR_TOUGHNESS,
            new EntityAttributeModifier(
                this.attributeId,
                this.toughness,
                EntityAttributeModifier.Operation.ADD_VALUE
            ),
            slot
        );
        if (this.knockbackResistance > 0.0d) {
            builder.add(
                EntityAttributes.KNOCKBACK_RESISTANCE,
                new EntityAttributeModifier(
                    this.attributeId,
                    this.knockbackResistance,
                    EntityAttributeModifier.Operation.ADD_VALUE
                ),
                slot
            );
        }
    }
}
