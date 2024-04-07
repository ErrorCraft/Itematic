package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.armor.ArmorMaterial;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentSet;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.AnimalArmorItem;
import net.minecraft.item.ArmorItem;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;

import java.util.Optional;

public record ArmorItemComponent(RegistryEntry<ArmorMaterial> material, Optional<AnimalArmorItem.Type> armorType) implements ItemComponent<ArmorItemComponent> {
    public static final Codec<ArmorItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        RegistryFixedCodec.of(ItematicRegistryKeys.ARMOR_MATERIAL).fieldOf("material").forGetter(ArmorItemComponent::material),
        StringIdentifiable.createCodec(AnimalArmorItem.Type::values).optionalFieldOf("armor_type").forGetter(ArmorItemComponent::armorType)
    ).apply(instance, ArmorItemComponent::new));

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
        if (!this.material.hasKeyAndValue()) {
            return;
        }
        components.get(ItemComponentTypes.EQUIPMENT)
            .ifPresent(c -> this.material.value().addAttributes(builder, c.slot()));
    }

    public Identifier textureId() {
        return this.armorType.map(type -> type.itematic$textureId(this.material))
            .orElseGet(() -> this.textureId(false));
    }

    public Identifier textureId(boolean secondLayer) {
        return this.material.value().textureId(secondLayer);
    }

    public static ArmorItemComponent of(RegistryEntry<ArmorMaterial> material) {
        return new ArmorItemComponent(material, Optional.empty());
    }

    public static ArmorItemComponent of(RegistryEntry<ArmorMaterial> material, AnimalArmorItem.Type armorType) {
        return new ArmorItemComponent(material, Optional.of(armorType));
    }

    public static ItemComponent<?>[] of(ArmorItem.Type type, int damageFactor, RegistryEntry<ArmorMaterial> material, RegistryEntry<SoundEvent> equipSound) {
        return new ItemComponent<?>[] {
            MaxStackSizeItemComponent.of(1),
            DamageableItemComponent.of(type.getMaxDamage(damageFactor)),
            EquipmentItemComponent.of(type.getEquipmentSlot(), true, equipSound),
            of(material)
        };
    }

    public static ItemComponent<?>[] of(int durability, EquipmentSlot slot, RegistryEntry<ArmorMaterial> material, RegistryEntry<SoundEvent> equipSound) {
        return new ItemComponent<?>[] {
            MaxStackSizeItemComponent.of(1),
            DamageableItemComponent.of(durability),
            EquipmentItemComponent.of(slot, true, equipSound),
            of(material)
        };
    }

    public static ItemComponent<?>[] ofAnimal(RegistryEntry<ArmorMaterial> material, RegistryEntry<SoundEvent> equipSound, AnimalArmorItem.Type type) {
        return new ItemComponent<?>[] {
            MaxStackSizeItemComponent.of(1),
            EquipmentItemComponent.of(EquipmentSlot.BODY, false, equipSound),
            of(material, type)
        };
    }
}
