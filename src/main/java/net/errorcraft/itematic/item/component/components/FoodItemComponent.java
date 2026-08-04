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

public record FoodItemComponent(int nutrition, float saturation, boolean alwaysEdible) implements ItemComponent<FoodItemComponent> {
    public static final Codec<FoodItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ExtraCodecs.NON_NEGATIVE_INT.fieldOf("nutrition").forGetter(FoodItemComponent::nutrition),
        Codec.FLOAT.fieldOf("saturation").forGetter(FoodItemComponent::saturation),
        Codec.BOOL.optionalFieldOf("always_edible", false).forGetter(FoodItemComponent::alwaysEdible)
    ).apply(instance, FoodItemComponent::new));

    public static FoodItemComponent of(FoodProperties food) {
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
    public void finishUsing(Level world, LivingEntity user, ItemStack stack, int usedTicks, ItemStackExchanger stackExchanger) {
        FoodProperties food = stack.get(DataComponents.FOOD);
        if (user instanceof Player player) {
            player.getFoodData().eat(food);
            world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_BURP, SoundSource.PLAYERS, 0.5f, Mth.randomBetween(user.getRandom(), 0.9f, 1.0f));
        }

        if (world instanceof ServerLevel serverWorld) {
            ActionContext context = ActionContext.builder(serverWorld)
                .stackExchanger(stackExchanger)
                .add(LootContextParams.THIS_ENTITY, user)
                .add(LootContextParams.ORIGIN, user.position())
                .add(LootContextParams.TOOL, stack)
                .add(ItematicContextParameters.HAND, user.getUsedItemHand())
                .build();
            stack.itematic$invokeEvent(ItemEvents.EAT_ITEM, context);
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
