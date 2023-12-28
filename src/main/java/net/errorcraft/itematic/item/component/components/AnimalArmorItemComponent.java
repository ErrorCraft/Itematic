package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.armor.ArmorMaterial;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.minecraft.item.AnimalArmorItem;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;

public record AnimalArmorItemComponent(RegistryEntry<ArmorMaterial> material, AnimalArmorItem.Type armorType) implements ItemComponent {
    public static final Codec<AnimalArmorItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        RegistryFixedCodec.of(ItematicRegistryKeys.ARMOR_MATERIAL).fieldOf("material").forGetter(AnimalArmorItemComponent::material),
        StringIdentifiable.createCodec(AnimalArmorItem.Type::values).fieldOf("type").forGetter(AnimalArmorItemComponent::armorType)
    ).apply(instance, AnimalArmorItemComponent::new));

    @Override
    public ItemComponentType<?> type() {
        return ItemComponentTypes.ANIMAL_ARMOR;
    }

    @Override
    public Codec<? extends ItemComponent> codec() {
        return CODEC;
    }

    public Identifier textureId() {
        return this.armorType.itematic$textureId(this.material);
    }

    public static AnimalArmorItemComponent of(RegistryEntry<ArmorMaterial> material, AnimalArmorItem.Type armorType) {
        return new AnimalArmorItemComponent(material, armorType);
    }
}
