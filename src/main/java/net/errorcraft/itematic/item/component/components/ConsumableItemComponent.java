package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.component.type.UseDurationDataComponent;
import net.errorcraft.itematic.item.ItemStackConsumer;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.event.ItemEvents;
import net.errorcraft.itematic.mixin.component.type.ConsumableComponentAccessor;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.consume.UseAction;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public record ConsumableItemComponent(boolean hasConsumeParticles, RegistryEntry<SoundEvent> sound) implements ItemComponent<ConsumableItemComponent> {
    public static final Codec<ConsumableItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.BOOL.optionalFieldOf("has_consume_particles", true).forGetter(ConsumableItemComponent::hasConsumeParticles),
        SoundEvent.ENTRY_CODEC.optionalFieldOf("sound", SoundEvents.ENTITY_GENERIC_EAT).forGetter(ConsumableItemComponent::sound)
    ).apply(instance, ConsumableItemComponent::new));
    private static final float CONSUME_EFFECTS_THRESHOLD = ConsumableComponentAccessor.consumeEffectsThreshold();

    public static ConsumableItemComponent of(boolean hasConsumeParticles, RegistryEntry<SoundEvent> sound) {
        return new ConsumableItemComponent(hasConsumeParticles, sound);
    }

    public static Builder builder(ConsumableComponent consumable) {
        return new Builder(consumable.getConsumeTicks())
            .useAnimation(consumable.useAction())
            .consumeSound(consumable.sound())
            .hasConsumeParticles(consumable.hasConsumeParticles());
    }

    @Override
    public ItemComponentType<ConsumableItemComponent> type() {
        return ItemComponentTypes.CONSUMABLE;
    }

    @Override
    public Codec<ConsumableItemComponent> codec() {
        return CODEC;
    }

    @Override
    public void using(ItemStack stack, World world, LivingEntity user, int usedTicks, int remainingUseTicks) {
        ConsumableComponent consumable = stack.get(DataComponentTypes.CONSUMABLE);
        if (consumable != null && shouldSpawnParticlesAndPlaySounds(usedTicks, remainingUseTicks)) {
            consumable.spawnParticlesAndPlaySound(user.getRandom(), user, stack, 5);
        }
    }

    @Override
    public void addComponents(ComponentMap.Builder builder) {
        builder.add(DataComponentTypes.CONSUMABLE, new ConsumableComponent(0.0f, UseAction.NONE, this.sound, this.hasConsumeParticles, List.of()));
    }

    public void consume(LivingEntity user, ItemStack stack, ItemStackConsumer resultStackConsumer, World world, Hand hand) {
        if (!(user instanceof PlayerEntity player)) {
            return;
        }

        if (world instanceof ServerWorld serverWorld) {
            ActionContext context = ActionContext.builder(serverWorld, stack, resultStackConsumer, hand)
                .entityPosition(ActionContextParameter.THIS, user)
                .build();
            stack.itematic$invokeEvent(ItemEvents.CONSUME_ITEM, context);
        }

        stack.decrementUnlessCreative(1, user);
        if (player instanceof ServerPlayerEntity serverPlayer) {
            Criteria.CONSUME_ITEM.trigger(serverPlayer, stack);
        }

        player.incrementStat(Stats.USED.itematic$getOrCreateStat(stack.getRegistryEntry()));
    }

    private static boolean shouldSpawnParticlesAndPlaySounds(int usedTicks, int remainingUseTicks) {
        boolean isValidTime = remainingUseTicks != UseDurationDataComponent.INDEFINITE_USE_DURATION && usedTicks > (usedTicks + remainingUseTicks) * CONSUME_EFFECTS_THRESHOLD;
        return isValidTime && usedTicks % 4 == 0;
    }

    public static class Builder {
        private final int useDuration;
        private UseAction useAnimation;
        private FoodItemComponent food;
        private RegistryEntry<Item> remainder;
        private boolean hasConsumeParticles = true;
        private RegistryEntry<SoundEvent> consumeSound = SoundEvents.ENTITY_GENERIC_EAT;

        private Builder(int useDuration) {
            this.useDuration = useDuration;
        }

        public ItemComponent<?>[] build() {
            Set<ItemComponent<?>> behavior = new HashSet<>();
            behavior.add(UseableItemComponent.builder()
                .ticks(this.useDuration)
                .animation(this.useAnimation)
                .remainder(this.remainder)
                .build()
            );
            behavior.add(ConsumableItemComponent.of(this.hasConsumeParticles, this.consumeSound));
            if (this.food != null) {
                behavior.add(this.food);
            }

            return behavior.toArray(ItemComponent<?>[]::new);
        }

        public Builder food(FoodComponent food) {
            this.food = FoodItemComponent.of(food);
            return this;
        }

        public Builder useAnimation(UseAction animation) {
            this.useAnimation = animation;
            return this;
        }

        public Builder remainder(RegistryEntry<Item> resultItem) {
            this.remainder = Objects.requireNonNull(resultItem);
            return this;
        }

        public Builder noConsumeParticles() {
            this.hasConsumeParticles = false;
            return this;
        }

        public Builder consumeSound(RegistryEntry<SoundEvent> consumeSound) {
            this.consumeSound = Objects.requireNonNull(consumeSound);
            return this;
        }

        public Builder hasConsumeParticles(boolean hasConsumeParticles) {
            this.hasConsumeParticles = hasConsumeParticles;
            return this;
        }
    }
}
