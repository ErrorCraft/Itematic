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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.ItemEnchantments;

public record EnchantmentHolderItemComponent(Holder<Item> grindingTransformsInto) implements ItemComponent<EnchantmentHolderItemComponent> {
    public static final Codec<EnchantmentHolderItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        RegistryFixedCodec.create(Registries.ITEM).fieldOf("grinding_transforms_into").forGetter(EnchantmentHolderItemComponent::grindingTransformsInto)
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
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY);
    }

    public static EnchantmentHolderItemComponent of(Holder<Item> grindingTransformsInto) {
        return new EnchantmentHolderItemComponent(grindingTransformsInto);
    }
}
