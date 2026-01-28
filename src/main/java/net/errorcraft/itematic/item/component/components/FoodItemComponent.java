package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.ItemStackConsumer;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.World;

public record FoodItemComponent(int nutrition, float saturation, boolean alwaysEdible) implements ItemComponent<FoodItemComponent> {
    public static final Codec<FoodItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codecs.NON_NEGATIVE_INT.fieldOf("nutrition").forGetter(FoodItemComponent::nutrition),
        Codec.FLOAT.fieldOf("saturation").forGetter(FoodItemComponent::saturation),
        Codec.BOOL.optionalFieldOf("always_edible", false).forGetter(FoodItemComponent::alwaysEdible)
    ).apply(instance, FoodItemComponent::new));

    // todo fix effects
    public static FoodItemComponent of(FoodComponent food) {
        return new FoodItemComponent(food.nutrition(), food.saturation(), food.canAlwaysEat());
    }

    @Override
    public ItemComponentType<FoodItemComponent> type() {
        return ItemComponentTypes.FOOD;
    }

    @Override
    public Codec<FoodItemComponent> codec() {
        return CODEC;
    }

    @Override
    public void finishUsing(World world, LivingEntity user, ItemStack stack, int usedTicks, ItemStackConsumer resultStackConsumer) {
        FoodComponent food = stack.get(DataComponentTypes.FOOD);
        if (food != null) {
            user.itematic$eatFood(world, stack, food, resultStackConsumer);
        }
    }

    @Override
    public void addComponents(ComponentMap.Builder builder) {
        builder.add(DataComponentTypes.FOOD, new FoodComponent(this.nutrition, this.saturation, this.alwaysEdible));
    }

    // todo fix???
    public boolean mayStartUsing(PlayerEntity user) {
        return user.canConsume(this.alwaysEdible);
    }
}
