package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.util.SetCodec;
import net.errorcraft.itematic.world.ItemResult;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.use.duration.UseDuration;
import net.errorcraft.itematic.world.item.use.duration.provider.UseDurationProvider;
import net.errorcraft.itematic.world.item.use.duration.provider.providers.ConstantUseDurationProvider;
import net.errorcraft.itematic.world.item.use.duration.provider.providers.IndefiniteUseDurationProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.component.UseEffects;
import net.minecraft.world.item.component.UseRemainder;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

import java.util.Optional;
import java.util.Set;

public record UseableItemBehavior(Optional<UseDuration> ticks, ItemUseAnimation animation, Optional<ItemStack> remainder, UseEffects effects, Set<Pass> passes) implements ItemBehavior<UseableItemBehavior> {
    public static final Codec<UseableItemBehavior> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        UseDuration.CODEC.optionalFieldOf("ticks").forGetter(UseableItemBehavior::ticks),
        ItemUseAnimation.CODEC.optionalFieldOf("animation", ItemUseAnimation.NONE).forGetter(UseableItemBehavior::animation),
        ItemStack.CODEC.optionalFieldOf("remainder").forGetter(UseableItemBehavior::remainder),
        UseEffects.CODEC.optionalFieldOf("effects", UseEffects.DEFAULT).forGetter(UseableItemBehavior::effects),
        SetCodec.forEnum(Pass.CODEC).optionalFieldOf("passes", Pass.DEFAULT_PASSES).forGetter(UseableItemBehavior::passes)
    ).apply(instance, UseableItemBehavior::new));

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public ItemBehaviorType<UseableItemBehavior> type() {
        return ItemBehaviorType.USEABLE;
    }

    @Override
    public ItemResult use(Level world, Player user, InteractionHand hand, ItemStack stack, ItemStackExchanger stackExchanger) {
        if (this.isUnuseable(Pass.NORMAL)) {
            return ItemResult.PASS;
        }

        return tryStartUsing(world, user, hand, stack);
    }

    @Override
    public ItemResult useOnBlock(UseOnContext context, ItemStackExchanger stackExchanger) {
        if (this.isUnuseable(Pass.BLOCK)) {
            return ItemResult.PASS;
        }

        return tryStartUsing(context.getLevel(), context.getPlayer(), context.getHand(), context.getItemInHand());
    }

    @Override
    public ItemResult useOnEntity(Player user, LivingEntity target, InteractionHand hand, ItemStack stack, ItemStackExchanger stackExchanger) {
        if (this.isUnuseable(Pass.ENTITY)) {
            return ItemResult.PASS;
        }

        return tryStartUsing(user.level(), user, hand, stack);
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        this.ticks.ifPresent(ticks -> builder.set(ItematicDataComponents.USE_DURATION, ticks));
        builder.set(ItematicDataComponents.USE_ANIMATION, this.animation);
        this.remainder.ifPresent(remainder -> builder.set(DataComponents.USE_REMAINDER, new UseRemainder(remainder)));
        builder.set(DataComponents.USE_EFFECTS, this.effects);
    }

    private boolean isUnuseable(Pass pass) {
        return !this.passes.contains(pass);
    }

    private static ItemResult tryStartUsing(Level world, Player user, InteractionHand hand, ItemStack stack) {
        if (!stack.itematic$mayStartUsing(world, user, hand, stack)) {
            return ItemResult.PASS;
        }

        UseDuration useDuration = stack.get(ItematicDataComponents.USE_DURATION);
        if (useDuration == null) {
            return ItemResult.CONSUME;
        }

        if (useDuration.startUsing(stack, user, hand)) {
            return ItemResult.CONSUME;
        }

        return ItemResult.PASS;
    }

    public static class Builder {
        @Nullable
        private UseDurationProvider ticks;
        private ItemUseAnimation animation = ItemUseAnimation.NONE;
        @Nullable
        private Holder<Item> remainder;
        private Set<Pass> passes = Pass.DEFAULT_PASSES;
        private UseEffects effects = UseEffects.DEFAULT;

        private Builder() {}

        public UseableItemBehavior build() {
            return new UseableItemBehavior(
                Optional.ofNullable(this.ticks).map(UseDuration::new),
                this.animation,
                Optional.ofNullable(this.remainder).map(ItemStack::new),
                this.effects,
                this.passes
            );
        }

        public Builder useFor(int ticks) {
            this.ticks = new ConstantUseDurationProvider(ticks);
            return this;
        }

        public Builder useFor(UseDurationProvider ticks) {
            this.ticks = ticks;
            return this;
        }

        public Builder useIndefinitely() {
            this.ticks = IndefiniteUseDurationProvider.INSTANCE;
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

        public Builder passes(Pass... passes) {
            this.passes = Set.of(passes);
            return this;
        }
    }

    public enum Pass implements StringRepresentable {
        NORMAL("normal"),
        BLOCK("block"),
        ENTITY("entity");
        public static final Set<Pass> DEFAULT_PASSES = Set.of(NORMAL);
        public static final Codec<Pass> CODEC = StringRepresentable.fromEnum(Pass::values);

        private final String name;

        Pass(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }
}
