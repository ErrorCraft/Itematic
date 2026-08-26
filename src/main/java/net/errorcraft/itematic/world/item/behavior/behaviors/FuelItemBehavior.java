package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.item.ItemStackTemplates;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.core.Holder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;

import java.util.Optional;

public record FuelItemBehavior(int ticks, Optional<ItemStackTemplate> remainder) implements ItemBehavior<FuelItemBehavior> {
    public static final Codec<FuelItemBehavior> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ExtraCodecs.POSITIVE_INT.fieldOf("ticks").forGetter(FuelItemBehavior::ticks),
        ItemStackTemplate.CODEC.optionalFieldOf("remainder").forGetter(FuelItemBehavior::remainder)
    ).apply(instance, FuelItemBehavior::new));

    public static FuelItemBehavior of(int ticks) {
        return new FuelItemBehavior(ticks, Optional.empty());
    }

    public static FuelItemBehavior of(int ticks, Holder<Item> remainder) {
        return new FuelItemBehavior(ticks, Optional.of(ItemStackTemplates.of(remainder)));
    }

    @Override
    public ItemBehaviorType<FuelItemBehavior> type() {
        return ItemBehaviorType.FUEL;
    }
}
