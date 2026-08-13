package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Repairable;

public record RepairableItemBehavior(HolderSet<Item> items) implements ItemBehavior<RepairableItemBehavior> {
    public static final Codec<RepairableItemBehavior> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        RegistryCodecs.homogeneousList(Registries.ITEM).fieldOf("items").forGetter(RepairableItemBehavior::items)
    ).apply(instance, RepairableItemBehavior::new));

    public static RepairableItemBehavior of(HolderSet<Item> items) {
        return new RepairableItemBehavior(items);
    }

    @Override
    public ItemBehaviorType<RepairableItemBehavior> type() {
        return ItemBehaviorType.REPAIRABLE;
    }

    @Override
    public Codec<RepairableItemBehavior> codec() {
        return CODEC;
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.REPAIRABLE, new Repairable(this.items));
    }
}
