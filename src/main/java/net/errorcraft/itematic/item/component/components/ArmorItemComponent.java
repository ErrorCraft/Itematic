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
import net.minecraft.sound.SoundEvent;

public record ArmorItemComponent(RegistryEntry<ArmorMaterial> material) implements ItemComponent {
    public static final Codec<ArmorItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        RegistryFixedCodec.of(ItematicRegistryKeys.ARMOR_MATERIAL).fieldOf("material").forGetter(ArmorItemComponent::material)
    ).apply(instance, ArmorItemComponent::new));

    @Override
    public ItemComponentType<?> type() {
        return ItemComponentTypes.ARMOR;
    }

    @Override
    public Codec<? extends ItemComponent> codec() {
        return CODEC;
    }

    public static ItemComponent[] from(ArmorMaterials armorMaterial, ArmorItem.Type type, RegistryEntry<ArmorMaterial> material, RegistryEntry<SoundEvent> equipSound) {
        return new ItemComponent[] {
            new DamageableItemComponent(armorMaterial.getDurability(type)),
            new EquipmentItemComponent(type.getEquipmentSlot(), true, equipSound),
            new ArmorItemComponent(material)
        };
    }
}
