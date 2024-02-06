package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public record RepairableItemComponent(TagKey<Item> items) implements ItemComponent<RepairableItemComponent> {
    public static final Codec<RepairableItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        TagKey.unprefixedCodec(RegistryKeys.ITEM).fieldOf("items").forGetter(RepairableItemComponent::items)
    ).apply(instance, RepairableItemComponent::new));

    @Override
    public ItemComponentType<RepairableItemComponent> type() {
        return ItemComponentTypes.REPAIRABLE;
    }

    @Override
    public Codec<RepairableItemComponent> codec() {
        return CODEC;
    }

    public static RepairableItemComponent of(TagKey<Item> items) {
        return new RepairableItemComponent(items);
    }
}
