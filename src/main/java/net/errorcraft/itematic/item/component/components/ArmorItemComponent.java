package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.armor.ArmorMaterial;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;

public record ArmorItemComponent(RegistryEntry<ArmorMaterial> material) implements ItemComponent {
    public static final Codec<ArmorItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        RegistryFixedCodec.of(ItematicRegistryKeys.ARMOR_MATERIAL).fieldOf("material").forGetter(ArmorItemComponent::material)
    ).apply(instance, ArmorItemComponent::new));

    @Override
    public ItemComponentType<?> getType() {
        return ItemComponentTypes.ARMOR;
    }

    @Override
    public Codec<? extends ItemComponent> getCodec() {
        return CODEC;
    }

    public static ItemComponent[] from(ArmorMaterials material, ArmorItem.Type type, RegistryEntry<ArmorMaterial> materialEntry) {
        return new ItemComponent[] {
            new DamageableItemComponent(material.getDurability(type)),
            new EquipmentItemComponent(type.getEquipmentSlot(), true),
            new ArmorItemComponent(materialEntry)
        };
    }
}
