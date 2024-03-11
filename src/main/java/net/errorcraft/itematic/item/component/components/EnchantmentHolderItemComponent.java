package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;

public record EnchantmentHolderItemComponent(RegistryEntry<Item> grindingTransformsInto) implements ItemComponent<EnchantmentHolderItemComponent> {
    public static final Codec<EnchantmentHolderItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        RegistryFixedCodec.of(RegistryKeys.ITEM).fieldOf("grinding_transforms_into").forGetter(EnchantmentHolderItemComponent::grindingTransformsInto)
    ).apply(instance, EnchantmentHolderItemComponent::new));

    @Override
    public ItemComponentType<EnchantmentHolderItemComponent> type() {
        return ItemComponentTypes.ENCHANTMENT_HOLDER;
    }

    @Override
    public Codec<EnchantmentHolderItemComponent> codec() {
        return CODEC;
    }

    @Override
    public void addComponents(ComponentMap.Builder builder) {
        builder.add(DataComponentTypes.STORED_ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT);
    }

    public static EnchantmentHolderItemComponent of(RegistryEntry<Item> grindingTransformsInto) {
        return new EnchantmentHolderItemComponent(grindingTransformsInto);
    }
}
