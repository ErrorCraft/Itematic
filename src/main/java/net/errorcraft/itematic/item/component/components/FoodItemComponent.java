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

import java.util.List;
import java.util.Optional;

public record FoodItemComponent(int nutrition, float saturation, boolean alwaysEdible, List<FoodComponent.StatusEffectEntry> effects) implements ItemComponent<FoodItemComponent> {
    public static final Codec<FoodItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codecs.NON_NEGATIVE_INT.fieldOf("nutrition").forGetter(FoodItemComponent::nutrition),
        Codec.FLOAT.fieldOf("saturation").forGetter(FoodItemComponent::saturation),
        Codec.BOOL.optionalFieldOf("always_edible", false).forGetter(FoodItemComponent::alwaysEdible),
        FoodComponent.StatusEffectEntry.CODEC.listOf().optionalFieldOf("effects", List.of()).forGetter(FoodItemComponent::effects)
    ).apply(instance, FoodItemComponent::new));

    public static FoodItemComponent of(FoodComponent food) {
        return new FoodItemComponent(food.nutrition(), food.saturation(), food.canAlwaysEat(), food.effects());
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
        builder.add(DataComponentTypes.FOOD, new FoodComponent(this.nutrition, this.saturation, false, 1.0f, Optional.empty(), this.effects));
    }

    public boolean mayStartUsing(PlayerEntity user) {
        return user.canConsume(this.alwaysEdible);
    }
}
