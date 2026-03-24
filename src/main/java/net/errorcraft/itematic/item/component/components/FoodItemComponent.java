package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.event.ItemEvents;
import net.errorcraft.itematic.util.context.ItematicContextParameters;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public record FoodItemComponent(int nutrition, float saturation, boolean alwaysEdible) implements ItemComponent<FoodItemComponent> {
    public static final Codec<FoodItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codecs.NON_NEGATIVE_INT.fieldOf("nutrition").forGetter(FoodItemComponent::nutrition),
        Codec.FLOAT.fieldOf("saturation").forGetter(FoodItemComponent::saturation),
        Codec.BOOL.optionalFieldOf("always_edible", false).forGetter(FoodItemComponent::alwaysEdible)
    ).apply(instance, FoodItemComponent::new));

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
    public void finishUsing(World world, LivingEntity user, ItemStack stack, int usedTicks, ItemStackExchanger stackExchanger) {
        FoodComponent food = stack.get(DataComponentTypes.FOOD);
        if (user instanceof PlayerEntity player) {
            player.getHungerManager().eat(food);
            world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5f, MathHelper.nextBetween(user.getRandom(), 0.9f, 1.0f));
        }

        if (world instanceof ServerWorld serverWorld) {
            ActionContext context = ActionContext.builder(serverWorld)
                .stackExchanger(stackExchanger)
                .add(LootContextParameters.THIS_ENTITY, user)
                .add(LootContextParameters.ORIGIN, user.getPos())
                .add(LootContextParameters.TOOL, stack)
                .add(ItematicContextParameters.HAND, user.getActiveHand())
                .build();
            stack.itematic$invokeEvent(ItemEvents.EAT_ITEM, context);
        }
    }

    @Override
    public void addComponents(ComponentMap.Builder builder) {
        builder.add(DataComponentTypes.FOOD, new FoodComponent(this.nutrition, this.saturation, this.alwaysEdible));
    }

    public boolean mayStartUsing(PlayerEntity user, ItemStack stack) {
        FoodComponent food = stack.get(DataComponentTypes.FOOD);
        return food != null && user.canConsume(food.canAlwaysEat());
    }
}
