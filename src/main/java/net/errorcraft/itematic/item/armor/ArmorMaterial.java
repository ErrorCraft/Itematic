package net.errorcraft.itematic.item.armor;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.entity.EquipmentSlotUtil;
import net.errorcraft.itematic.mixin.item.ArmorItemAccessor;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ArmorItem;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public record ArmorMaterial(Identifier assetId, Map<ArmorItem.Type, Integer> defense, double toughness, double knockbackResistance) {
    public static final Codec<ArmorMaterial> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Identifier.CODEC.fieldOf("asset_id").forGetter(ArmorMaterial::assetId),
        Codec.simpleMap(ArmorItem.Type.CODEC, Codec.INT, StringIdentifiable.toKeyable(ArmorItem.Type.values())).fieldOf("defense").forGetter(ArmorMaterial::defense),
        Codec.DOUBLE.fieldOf("toughness").forGetter(ArmorMaterial::toughness),
        Codec.DOUBLE.fieldOf("knockback_resistance").forGetter(ArmorMaterial::knockbackResistance)
    ).apply(instance, ArmorMaterial::new));
    private static final Map<EquipmentSlot, UUID> EQUIPMENT_SLOT_ATTRIBUTE_UUIDS = ArmorItemAccessor.attributeModifiers().entrySet()
        .stream()
        .collect(Collectors.toMap(entry -> entry.getKey().getEquipmentSlot(), Map.Entry::getValue));

    public int defense(EquipmentSlot slot) {
        ArmorItem.Type type = EquipmentSlotUtil.armorType(slot);
        if (type == null) {
            return 0;
        }
        return this.defense.getOrDefault(type, 0);
    }

    public Identifier textureId(boolean secondLayer) {
        if (secondLayer) {
            this.assetId.withPath(path -> "models/armor/" + path + "_layer_2");
        }
        return this.assetId.withPath(path -> "models/armor/" + path + "_layer_1");
    }

    public void addAttributes(AttributeModifiersComponent.Builder builder, EquipmentSlot slot) {
        UUID uuid = EQUIPMENT_SLOT_ATTRIBUTE_UUIDS.get(slot);
        int defense = this.defense(slot);
        if (defense > 0) {
            builder.add(EntityAttributes.GENERIC_ARMOR, new EntityAttributeModifier(uuid, "Armor modifier", defense, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.forEquipmentSlot(slot));
        }
        builder.add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, new EntityAttributeModifier(uuid, "Armor toughness", this.toughness, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.forEquipmentSlot(slot));
        if (this.knockbackResistance > 0.0d) {
            builder.add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, new EntityAttributeModifier(uuid, "Armor knockback resistance", this.knockbackResistance, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.forEquipmentSlot(slot));
        }
    }

    public static ArmorMaterial from(Identifier assetId, RegistryEntry<net.minecraft.item.ArmorMaterial> material) {
        return new ArmorMaterial(assetId, material.value().defense(), material.value().toughness(), material.value().knockbackResistance());
    }
}
