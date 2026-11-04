package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.food.FoodProperties;

public record FoodItemBehavior(FoodProperties food) implements ItemBehavior<FoodItemBehavior> {
    public static final Codec<FoodItemBehavior> CODEC = FoodProperties.DIRECT_CODEC.xmap(
        FoodItemBehavior::new,
        FoodItemBehavior::food
    );

    public static FoodItemBehavior of(FoodProperties food) {
        return new FoodItemBehavior(food);
    }

    @Override
    public ItemBehaviorType<FoodItemBehavior> type() {
        return ItemBehaviorType.FOOD;
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.FOOD, this.food);
    }
}
