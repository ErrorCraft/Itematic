package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Repairable;

public record RepairableItemComponent(HolderSet<Item> items) implements ItemComponent<RepairableItemComponent> {
    public static final Codec<RepairableItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        RegistryCodecs.homogeneousList(Registries.ITEM).fieldOf("items").forGetter(RepairableItemComponent::items)
    ).apply(instance, RepairableItemComponent::new));

    public static RepairableItemComponent of(HolderSet<Item> items) {
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
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.REPAIRABLE, new Repairable(this.items));
    }
}
