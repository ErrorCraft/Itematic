package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.world.item.ItemStackTemplates;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.component.CookingFuel;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;

import java.util.Optional;

public record CookingFuelItemBehavior(CookingFuel fuel, Optional<ItemStackTemplate> remainder) implements ItemBehavior<CookingFuelItemBehavior> {
    public static final Codec<CookingFuelItemBehavior> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        MapCodec.assumeMapUnsafe(CookingFuel.CODEC).forGetter(CookingFuelItemBehavior::fuel),
        ItemStackTemplate.CODEC.optionalFieldOf("remainder").forGetter(CookingFuelItemBehavior::remainder)
    ).apply(instance, CookingFuelItemBehavior::new));

    public static CookingFuelItemBehavior of(ResourceKey<NumberProvider> burnTime) {
        return new CookingFuelItemBehavior(
            new CookingFuel(burnTime, NumberProviders.COOKING_DEFAULT_SPEED_MULTIPLIER),
            Optional.empty()
        );
    }

    public static CookingFuelItemBehavior of(ResourceKey<NumberProvider> burnTime, Holder<Item> remainder) {
        return new CookingFuelItemBehavior(
            new CookingFuel(burnTime, NumberProviders.COOKING_DEFAULT_SPEED_MULTIPLIER),
            Optional.of(ItemStackTemplates.of(remainder))
        );
    }

    @Override
    public ItemBehaviorType<CookingFuelItemBehavior> type() {
        return ItemBehaviorType.COOKING_FUEL;
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.COOKING_FUEL, this.fuel);
        this.remainder.ifPresent(remainder -> builder.set(ItematicDataComponents.COOKING_FUEL_REMAINDER, remainder));
    }
}
