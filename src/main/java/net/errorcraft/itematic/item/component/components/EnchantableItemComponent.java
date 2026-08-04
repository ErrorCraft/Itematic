package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.enchantment.Enchantable;
import net.minecraft.world.item.equipment.ArmorMaterial;
import java.util.Optional;

public record EnchantableItemComponent(int enchantability, Optional<Holder<Item>> transformsInto) implements ItemComponent<EnchantableItemComponent> {
    public static final Codec<EnchantableItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ExtraCodecs.POSITIVE_INT.fieldOf("enchantability").forGetter(EnchantableItemComponent::enchantability),
        RegistryFixedCodec.create(Registries.ITEM).optionalFieldOf("transforms_into").forGetter(EnchantableItemComponent::transformsInto)
    ).apply(instance, EnchantableItemComponent::new));

    public static EnchantableItemComponent of(ArmorMaterial material) {
        return of(material.enchantmentValue());
    }

    public static EnchantableItemComponent of(ToolMaterial material) {
        return of(material.enchantmentValue());
    }

    public static EnchantableItemComponent of(int enchantability) {
        return new EnchantableItemComponent(enchantability, Optional.empty());
    }

    public static EnchantableItemComponent ofTransforming(int enchantability, Holder<Item> item) {
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

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.ENCHANTABLE, new Enchantable(this.enchantability));
    }
}
