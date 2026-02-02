package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.RepairableComponent;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryCodecs;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntryList;

public record RepairableItemComponent(RegistryEntryList<Item> items) implements ItemComponent<RepairableItemComponent> {
    public static final Codec<RepairableItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        RegistryCodecs.entryList(RegistryKeys.ITEM).fieldOf("items").forGetter(RepairableItemComponent::items)
    ).apply(instance, RepairableItemComponent::new));

    public static RepairableItemComponent of(RegistryEntryList<Item> items) {
        return new RepairableItemComponent(items);
    }

    @Override
    public ItemComponentType<RepairableItemComponent> type() {
        return ItemComponentTypes.REPAIRABLE;
    }

    @Override
    public Codec<RepairableItemComponent> codec() {
        return CODEC;
    }

    @Override
    public void addComponents(ComponentMap.Builder builder) {
        builder.add(DataComponentTypes.REPAIRABLE, new RepairableComponent(this.items));
    }
}
