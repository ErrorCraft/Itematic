package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.food.VillagerFood;

public record VillagerFoodItemBehavior(VillagerFood food) implements ItemBehavior<VillagerFoodItemBehavior> {
    public static final Codec<VillagerFoodItemBehavior> CODEC = VillagerFood.CODEC.xmap(
        VillagerFoodItemBehavior::new,
        VillagerFoodItemBehavior::food
    );

    public static VillagerFoodItemBehavior of(int nutrition) {
        return new VillagerFoodItemBehavior(new VillagerFood(nutrition));
    }

    @Override
    public ItemBehaviorType<VillagerFoodItemBehavior> type() {
        return ItemBehaviorType.VILLAGER_FOOD;
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.VILLAGER_FOOD, this.food);
    }
}
