package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.util.dynamic.Codecs;

import java.util.Optional;

public record EnchantableItemComponent(int enchantability, Optional<RegistryEntry<Item>> transformsInto) implements ItemComponent<EnchantableItemComponent> {
    public static final Codec<EnchantableItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codecs.POSITIVE_INT.fieldOf("enchantability").forGetter(EnchantableItemComponent::enchantability),
        RegistryFixedCodec.of(RegistryKeys.ITEM).optionalFieldOf("transforms_into").forGetter(EnchantableItemComponent::transformsInto)
    ).apply(instance, EnchantableItemComponent::new));

    public static EnchantableItemComponent of(RegistryEntry<ArmorMaterial> material) {
        return of(material.value().enchantability());
    }

    public static EnchantableItemComponent of(ToolMaterial material) {
        return of(material.getEnchantability());
    }

    public static EnchantableItemComponent of(int enchantability) {
        return new EnchantableItemComponent(enchantability, Optional.empty());
    }

    public static EnchantableItemComponent ofTransforming(int enchantability, RegistryEntry<Item> item) {
        return new EnchantableItemComponent(enchantability, Optional.of(item));
    }

    @Override
    public ItemComponentType<EnchantableItemComponent> type() {
        return ItemComponentTypes.ENCHANTABLE;
    }

    @Override
    public Codec<EnchantableItemComponent> codec() {
        return CODEC;
    }
}
