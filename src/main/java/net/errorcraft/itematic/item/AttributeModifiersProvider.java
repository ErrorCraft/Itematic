package net.errorcraft.itematic.item;

import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class AttributeModifiersProvider {
    public static final Codec<AttributeModifiersProvider> CODEC = AttributeModifiersComponent.Entry.CODEC.listOf().xmap(AttributeModifiersProvider::new, provider -> provider.entries);
    public static final AttributeModifiersProvider EMPTY = new AttributeModifiersProvider(List.of());
    private final List<AttributeModifiersComponent.Entry> entries;

    private AttributeModifiersProvider() {
        this(new ArrayList<>());
    }

    private AttributeModifiersProvider(List<AttributeModifiersComponent.Entry> entries) {
        this.entries = entries;
    }

    public void addTo(ComponentMap.Builder builder) {
        if (this.entries.isEmpty()) {
            return;
        }

        builder.add(DataComponentTypes.ATTRIBUTE_MODIFIERS, new AttributeModifiersComponent(this.entries, true));
    }

    private void add(RegistryEntry<EntityAttribute> attribute, EntityAttributeModifier modifier, AttributeModifierSlot slot) {
        this.entries.add(new AttributeModifiersComponent.Entry(attribute, modifier, slot));
    }

    public static AttributeModifiersProvider armor(ArmorMaterial material, EquipmentType type) {
        Identifier attributeId = Identifier.ofVanilla("armor." + type.getName());
        AttributeModifierSlot slot = AttributeModifierSlot.forEquipmentSlot(type.getEquipmentSlot());
        AttributeModifiersProvider provider = new AttributeModifiersProvider();
        provider.add(
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
            provider.add(
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
            provider.add(
                EntityAttributes.KNOCKBACK_RESISTANCE,
                new EntityAttributeModifier(
                    attributeId,
                    knockbackResistance,
                    EntityAttributeModifier.Operation.ADD_VALUE
                ),
                slot
            );
        }

        return provider;
    }
}
