package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.ItemEvent;
import net.errorcraft.itematic.mixin.component.type.ConsumableComponentAccessor;
import net.errorcraft.itematic.util.context.ItematicContextParameters;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.errorcraft.itematic.world.item.use.duration.UseDuration;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public record ConsumableItemBehavior(boolean hasConsumeParticles, Holder<SoundEvent> sound) implements ItemBehavior<ConsumableItemBehavior> {
    public static final Codec<ConsumableItemBehavior> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.BOOL.optionalFieldOf("has_consume_particles", true).forGetter(ConsumableItemBehavior::hasConsumeParticles),
        SoundEvent.CODEC.optionalFieldOf("sound", SoundEvents.GENERIC_EAT).forGetter(ConsumableItemBehavior::sound)
    ).apply(instance, ConsumableItemBehavior::new));
    private static final float CONSUME_EFFECTS_THRESHOLD = ConsumableComponentAccessor.consumeEffectsThreshold();

    public static ConsumableItemBehavior of(boolean hasConsumeParticles, Holder<SoundEvent> sound) {
        return new ConsumableItemBehavior(hasConsumeParticles, sound);
    }

    public static Builder builder(Consumable consumable) {
        return new Builder(consumable.consumeTicks())
            .useAnimation(consumable.animation())
            .consumeSound(consumable.sound())
            .hasConsumeParticles(consumable.hasConsumeParticles());
    }

    @Override
    public ItemBehaviorType<ConsumableItemBehavior> type() {
        return ItemBehaviorType.CONSUMABLE;
    }

    @Override
    public Codec<ConsumableItemBehavior> codec() {
        return CODEC;
    }

    @Override
    public void using(ItemStack stack, Level world, LivingEntity user, int usedTicks, int remainingUseTicks) {
        Consumable consumable = stack.get(DataComponents.CONSUMABLE);
        if (consumable != null && shouldSpawnParticlesAndPlaySounds(usedTicks, remainingUseTicks)) {
            consumable.emitParticlesAndSounds(user.getRandom(), user, stack, 5);
        }
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.CONSUMABLE, new Consumable(0.0f, ItemUseAnimation.NONE, this.sound, this.hasConsumeParticles, List.of()));
    }

    public void consume(LivingEntity user, ItemStack stack, ItemStackExchanger stackExchanger, Level world, InteractionHand hand) {
        if (world instanceof ServerLevel serverWorld) {
            ActionContext context = ActionContext.builder(serverWorld)
                .stackExchanger(stackExchanger)
                .add(LootContextParams.THIS_ENTITY, user)
                .add(LootContextParams.ORIGIN, user.position())
                .add(LootContextParams.TOOL, stack)
                .add(ItematicContextParameters.HAND, hand)
                .build();
            stack.itematic$invokeEvent(ItemEvent.CONSUME_ITEM, context);
        }

        stack.consume(1, user);
        if (user instanceof Player player) {
            this.consumeForPlayer(player, stack);
        }
    }

    private void consumeForPlayer(Player player, ItemStack stack) {
        if (player instanceof ServerPlayer serverPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, stack);
        }

        player.awardStat(Stats.ITEM_USED.itematic$getOrCreateStat(stack.getItemHolder()));
    }

    private static boolean shouldSpawnParticlesAndPlaySounds(int usedTicks, int remainingUseTicks) {
        boolean isValidTime = remainingUseTicks != UseDuration.INDEFINITE && usedTicks > (usedTicks + remainingUseTicks) * CONSUME_EFFECTS_THRESHOLD;
        return isValidTime && usedTicks % 4 == 0;
    }

    public static class Builder {
        private final int useDuration;
        private ItemUseAnimation useAnimation;
        private FoodItemBehavior food;
        private Holder<Item> remainder;
        private boolean hasConsumeParticles = true;
        private Holder<SoundEvent> consumeSound = SoundEvents.GENERIC_EAT;

        private Builder(int useDuration) {
            this.useDuration = useDuration;
        }

        public ItemBehavior<?>[] build() {
            Set<ItemBehavior<?>> behavior = new HashSet<>();
            behavior.add(UseableItemBehavior.builder()
                .useFor(this.useDuration)
                .animation(this.useAnimation)
                .remainder(this.remainder)
                .build()
            );
            behavior.add(ConsumableItemBehavior.of(this.hasConsumeParticles, this.consumeSound));
            if (this.food != null) {
                behavior.add(this.food);
            }

            return behavior.toArray(ItemBehavior<?>[]::new);
        }

        public Builder food(FoodProperties food) {
            this.food = FoodItemBehavior.of(food);
            return this;
        }

        public Builder useAnimation(ItemUseAnimation animation) {
            this.useAnimation = animation;
            return this;
        }

        public Builder remainder(Holder<Item> resultItem) {
            this.remainder = Objects.requireNonNull(resultItem);
            return this;
        }

        public Builder noConsumeParticles() {
            this.hasConsumeParticles = false;
            return this;
        }

        public Builder consumeSound(Holder<SoundEvent> consumeSound) {
            this.consumeSound = Objects.requireNonNull(consumeSound);
            return this;
        }

        public Builder hasConsumeParticles(boolean hasConsumeParticles) {
            this.hasConsumeParticles = hasConsumeParticles;
            return this;
        }
    }
}
