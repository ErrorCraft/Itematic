package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.core.Holder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import java.util.Optional;

public record FuelItemComponent(int ticks, Optional<ItemStack> remainder) implements ItemComponent<FuelItemComponent> {
    public static final Codec<FuelItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ExtraCodecs.POSITIVE_INT.fieldOf("ticks").forGetter(FuelItemComponent::ticks),
        ItemStack.CODEC.optionalFieldOf("remainder").forGetter(FuelItemComponent::remainder)
    ).apply(instance, FuelItemComponent::new));

    public static FuelItemComponent of(int ticks) {
        return new FuelItemComponent(ticks, Optional.empty());
    }

    public static FuelItemComponent of(int ticks, Holder<Item> remainder) {
        return new FuelItemComponent(ticks, Optional.of(new ItemStack(remainder)));
    }

    @Override
    public ItemComponentType<FuelItemComponent> type() {
        return ItemComponentTypes.FUEL;
    }

    @Override
    public Codec<FuelItemComponent> codec() {
        return CODEC;
    }
}
