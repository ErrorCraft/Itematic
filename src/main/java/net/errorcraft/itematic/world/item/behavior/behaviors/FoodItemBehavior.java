package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.util.context.ItematicContextKeys;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.errorcraft.itematic.world.item.ItemEvent;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public record FoodItemBehavior(int nutrition, float saturation, boolean alwaysEdible) implements ItemBehavior<FoodItemBehavior> {
    public static final Codec<FoodItemBehavior> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ExtraCodecs.NON_NEGATIVE_INT.fieldOf("nutrition").forGetter(FoodItemBehavior::nutrition),
        Codec.FLOAT.fieldOf("saturation").forGetter(FoodItemBehavior::saturation),
        Codec.BOOL.optionalFieldOf("always_edible", false).forGetter(FoodItemBehavior::alwaysEdible)
    ).apply(instance, FoodItemBehavior::new));

    public static FoodItemBehavior of(FoodProperties food) {
        return new FoodItemBehavior(food.nutrition(), food.saturation(), food.canAlwaysEat());
    }

    @Override
    public ItemBehaviorType<FoodItemBehavior> type() {
        return ItemBehaviorType.FOOD;
    }

    @Override
    public void finishUsing(Level level, LivingEntity user, ItemStack stack, int usedTicks, ItemStackExchanger stackExchanger) {
        FoodProperties food = stack.get(DataComponents.FOOD);
        if (user instanceof Player player) {
            player.getFoodData().eat(food);
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_BURP, SoundSource.PLAYERS, 0.5f, Mth.randomBetween(user.getRandom(), 0.9f, 1.0f));
        }

        if (level instanceof ServerLevel serverLevel) {
            ActionContext context = ActionContext.builder(serverLevel)
                .stackExchanger(stackExchanger)
                .add(LootContextParams.THIS_ENTITY, user)
                .add(LootContextParams.ORIGIN, user.position())
                .add(LootContextParams.TOOL, stack)
                .add(ItematicContextKeys.HAND, user.getUsedItemHand())
                .build();
            stack.itematic$invokeEvent(ItemEvent.EAT_ITEM, context);
        }
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.FOOD, new FoodProperties(this.nutrition, this.saturation, this.alwaysEdible));
    }

    public boolean mayStartUsing(Player user, ItemStack stack) {
        FoodProperties food = stack.get(DataComponents.FOOD);
        return food != null && user.canEat(food.canAlwaysEat());
    }
}
