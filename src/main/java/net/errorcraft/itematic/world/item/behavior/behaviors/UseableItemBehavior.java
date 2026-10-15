package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.world.item.ItemStackTemplates;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.use.duration.UseDuration;
import net.errorcraft.itematic.world.item.use.duration.provider.UseDurationProvider;
import net.errorcraft.itematic.world.item.use.duration.provider.providers.Constant;
import net.errorcraft.itematic.world.item.use.duration.provider.providers.Indefinite;
import net.errorcraft.itematic.world.item.use.duration.provider.providers.Instant;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.UseEffects;
import net.minecraft.world.item.component.UseRemainder;
import org.jspecify.annotations.Nullable;

import java.util.Optional;
import java.util.Set;

public record UseableItemBehavior(UseDuration ticks, ItemUseAnimation animation, Optional<ItemStackTemplate> remainder, UseEffects effects, Set<UseDuration.Pass> passes) implements ItemBehavior<UseableItemBehavior> {
    public static final Codec<UseableItemBehavior> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        UseDuration.CODEC.optionalFieldOf("ticks", new UseDuration(Instant.INSTANCE)).forGetter(UseableItemBehavior::ticks),
        ItemUseAnimation.CODEC.optionalFieldOf("animation", ItemUseAnimation.NONE).forGetter(UseableItemBehavior::animation),
        ItemStackTemplate.CODEC.optionalFieldOf("remainder").forGetter(UseableItemBehavior::remainder),
        UseEffects.CODEC.optionalFieldOf("effects", UseEffects.DEFAULT).forGetter(UseableItemBehavior::effects),
        UseDuration.Pass.SET_CODEC.optionalFieldOf("passes", UseDuration.Pass.DEFAULT).forGetter(UseableItemBehavior::passes)
    ).apply(instance, UseableItemBehavior::new));

    public static UseableItemBehavior of(Consumable consumable) {
        return new UseableItemBehavior(
            new UseDuration(consumable.consumeTicks()),
            consumable.animation(),
            Optional.empty(),
            UseEffects.DEFAULT,
            UseDuration.Pass.DEFAULT
        );
    }

    public static UseableItemBehavior of(Consumable consumable, Holder<Item> remainder) {
        return new UseableItemBehavior(
            new UseDuration(consumable.consumeTicks()),
            consumable.animation(),
            Optional.of(ItemStackTemplates.of(remainder)),
            UseEffects.DEFAULT,
            UseDuration.Pass.DEFAULT
        );
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public ItemBehaviorType<UseableItemBehavior> type() {
        return ItemBehaviorType.USEABLE;
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(ItematicDataComponents.USE_DURATION, this.ticks);
        builder.set(ItematicDataComponents.USE_ANIMATION, this.animation);
        this.remainder.ifPresent(remainder -> builder.set(DataComponents.USE_REMAINDER, new UseRemainder(remainder)));
        builder.set(DataComponents.USE_EFFECTS, this.effects);
        builder.set(ItematicDataComponents.USE_PASSES, this.passes);
    }

    public static class Builder {
        private UseDurationProvider ticks = Instant.INSTANCE;
        private ItemUseAnimation animation = ItemUseAnimation.NONE;
        @Nullable
        private Holder<Item> remainder;
        private Set<UseDuration.Pass> passes = UseDuration.Pass.DEFAULT;
        private UseEffects effects = UseEffects.DEFAULT;

        private Builder() {}

        public UseableItemBehavior build() {
            return new UseableItemBehavior(
                new UseDuration(this.ticks),
                this.animation,
                Optional.ofNullable(this.remainder).map(ItemStackTemplates::of),
                this.effects,
                this.passes
            );
        }

        public Builder useFor(int ticks) {
            this.ticks = new Constant(ticks);
            return this;
        }

        public Builder useFor(UseDurationProvider ticks) {
            this.ticks = ticks;
            return this;
        }

        public Builder useIndefinitely() {
            this.ticks = Indefinite.INSTANCE;
            return this;
        }

        public Builder animation(ItemUseAnimation animation) {
            this.animation = animation;
            return this;
        }

        public Builder remainder(@Nullable Holder<Item> remainder) {
            this.remainder = remainder;
            return this;
        }

        public Builder effects(UseEffects effects) {
            this.effects = effects;
            return this;
        }

        public Builder passes(UseDuration.Pass... passes) {
            this.passes = Set.of(passes);
            return this;
        }
    }
}
