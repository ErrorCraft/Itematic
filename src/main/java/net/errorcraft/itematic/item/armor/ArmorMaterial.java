package net.errorcraft.itematic.item.armor;

import com.google.common.collect.ImmutableMultimap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.entity.EquipmentSlotUtil;
import net.errorcraft.itematic.mixin.item.ArmorItemAccessor;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
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

    public void addAttributes(EquipmentSlot slot, ImmutableMultimap.Builder<RegistryEntry<EntityAttribute>, EntityAttributeModifier> attributeModifiers) {
        UUID uuid = EQUIPMENT_SLOT_ATTRIBUTE_UUIDS.get(slot);
        int defense = this.defense(slot);
        if (defense > 0) {
            attributeModifiers.put(EntityAttributes.GENERIC_ARMOR, new EntityAttributeModifier(uuid, "Armor modifier", defense, EntityAttributeModifier.Operation.ADDITION));
        }
        attributeModifiers.put(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, new EntityAttributeModifier(uuid, "Armor toughness", this.toughness, EntityAttributeModifier.Operation.ADDITION));
        if (this.knockbackResistance > 0.0d) {
            attributeModifiers.put(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, new EntityAttributeModifier(uuid, "Armor knockback resistance", this.knockbackResistance, EntityAttributeModifier.Operation.ADDITION));
        }
    }

    public static ArmorMaterial from(Identifier assetId, RegistryEntry<net.minecraft.item.ArmorMaterial> material) {
        return new ArmorMaterial(assetId, material.value().defense(), material.value().getToughness(), material.value().getKnockbackResistance());
    }
}
