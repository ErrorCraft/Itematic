package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.component.BrewingFuel;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;

public record BrewingFuelItemBehavior(BrewingFuel fuel) implements ItemBehavior<BrewingFuelItemBehavior> {
    public static final Codec<BrewingFuelItemBehavior> CODEC = BrewingFuel.CODEC.xmap(
        BrewingFuelItemBehavior::new,
        BrewingFuelItemBehavior::fuel
    );

    public static BrewingFuelItemBehavior of(ResourceKey<NumberProvider> uses) {
        return new BrewingFuelItemBehavior(
            new BrewingFuel(uses, NumberProviders.COOKING_DEFAULT_SPEED_MULTIPLIER)
        );
    }

    @Override
    public ItemBehaviorType<BrewingFuelItemBehavior> type() {
        return ItemBehaviorType.BREWING_FUEL;
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.BREWING_FUEL, this.fuel);
    }
}
