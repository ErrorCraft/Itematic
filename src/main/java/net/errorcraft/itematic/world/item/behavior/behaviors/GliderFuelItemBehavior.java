package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.component.GliderFuel;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProvider;
import net.minecraft.world.level.storage.loot.providers.number.floats.ResolvableFloat;

public record GliderFuelItemBehavior(GliderFuel gliderFuel) implements ItemBehavior<GliderFuelItemBehavior> {
    public static final Codec<GliderFuelItemBehavior> CODEC = GliderFuel.CODEC.xmap(
        GliderFuelItemBehavior::new,
        GliderFuelItemBehavior::gliderFuel
    );

    public static GliderFuelItemBehavior of(ResourceKey<ContextFloatProvider> constantlyAddedSpeed, ResourceKey<ContextFloatProvider> targetSpeed, ResourceKey<ContextFloatProvider> targetSpeedFactor) {
        return new GliderFuelItemBehavior(
            new GliderFuel(
                new ResolvableFloat.Reference(constantlyAddedSpeed),
                new ResolvableFloat.Reference(targetSpeed),
                new ResolvableFloat.Reference(targetSpeedFactor)
            )
        );
    }

    @Override
    public ItemBehaviorType<GliderFuelItemBehavior> type() {
        return ItemBehaviorType.GLIDER_FUEL;
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(ItematicDataComponents.GLIDER_FUEL, this.gliderFuel);
    }
}
