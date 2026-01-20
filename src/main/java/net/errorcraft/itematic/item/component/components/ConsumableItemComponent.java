package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.ItemStackConsumer;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.event.ItemEvents;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public record ConsumableItemComponent(Optional<RegistryEntry<Item>> resultItem, boolean hasConsumeParticles, RegistryEntry<SoundEvent> sound) implements ItemComponent<ConsumableItemComponent> {
    private static final RegistryEntry<SoundEvent> DEFAULT_SOUND = Registries.SOUND_EVENT.getEntry(SoundEvents.ENTITY_GENERIC_EAT);
    public static final Codec<ConsumableItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        RegistryFixedCodec.of(RegistryKeys.ITEM).optionalFieldOf("result_item").forGetter(ConsumableItemComponent::resultItem),
        Codec.BOOL.optionalFieldOf("has_consume_particles", true).forGetter(ConsumableItemComponent::hasConsumeParticles),
        SoundEvent.ENTRY_CODEC.optionalFieldOf("sound", DEFAULT_SOUND).forGetter(ConsumableItemComponent::sound)
    ).apply(instance, ConsumableItemComponent::new));

    public static ConsumableItemComponent of(RegistryEntry<Item> resultItem, boolean hasConsumeParticles, RegistryEntry<SoundEvent> sound) {
        return new ConsumableItemComponent(Optional.ofNullable(resultItem), hasConsumeParticles, sound);
    }

    public static Builder builder(int useDuration) {
        return new Builder(useDuration);
    }

    public static Builder builder(FoodComponent food) {
        return new Builder(food.getEatTicks())
            .food(food)
            .useAnimation(UseAction.EAT);
    }

    @Override
    public ItemComponentType<ConsumableItemComponent> type() {
        return ItemComponentTypes.CONSUMABLE;
    }

    @Override
    public Codec<ConsumableItemComponent> codec() {
        return CODEC;
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
        this.resultItem.map(ItemStack::new)
            .map(resultStack -> ItemUsage.exchangeStack(stack, player, resultStack))
            .ifPresentOrElse(resultStackConsumer::set, () -> stack.decrementUnlessCreative(1, user));
        if (player instanceof ServerPlayerEntity serverPlayer) {
            Criteria.CONSUME_ITEM.trigger(serverPlayer, stack);
        }

        player.incrementStat(Stats.USED.itematic$getOrCreateStat(stack.getRegistryEntry()));
    }

    public static class Builder {
        private final int useDuration;
        private UseAction useAnimation;
        private FoodItemComponent food;
        private RegistryEntry<Item> resultItem;
        private boolean hasConsumeParticles = true;
        private RegistryEntry<SoundEvent> consumeSound = DEFAULT_SOUND;

        private Builder(int useDuration) {
            this.useDuration = useDuration;
        }

        public ItemComponent<?>[] build() {
            Set<ItemComponent<?>> behavior = new HashSet<>();
            behavior.add(UseableItemComponent.builder()
                .ticks(this.useDuration)
                .animation(this.useAnimation)
                .build()
            );
            behavior.add(ConsumableItemComponent.of(this.resultItem, this.hasConsumeParticles, this.consumeSound));
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

        public Builder resultItem(RegistryEntry<Item> resultItem) {
            this.resultItem = Objects.requireNonNull(resultItem);
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
    }
}
