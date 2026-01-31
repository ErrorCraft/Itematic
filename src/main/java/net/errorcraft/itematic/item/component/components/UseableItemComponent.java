package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.component.ItematicDataComponentTypes;
import net.errorcraft.itematic.component.type.UseDurationDataComponent;
import net.errorcraft.itematic.item.ItemResult;
import net.errorcraft.itematic.item.ItemStackConsumer;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.use.provider.IntegerProvider;
import net.errorcraft.itematic.item.use.provider.providers.ConstantIntegerProvider;
import net.errorcraft.itematic.item.use.provider.providers.IndefiniteIntegerProvider;
import net.errorcraft.itematic.serialization.ItematicCodecs;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.UseRemainderComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.consume.UseAction;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Hand;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.Set;

public record UseableItemComponent(Optional<UseDurationDataComponent> ticks, UseAction animation, Optional<ItemStack> remainder, Set<Pass> passes) implements ItemComponent<UseableItemComponent> {
    public static final Codec<UseableItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        UseDurationDataComponent.CODEC.optionalFieldOf("ticks").forGetter(UseableItemComponent::ticks),
        UseAction.CODEC.optionalFieldOf("animation", UseAction.NONE).forGetter(UseableItemComponent::animation),
        ItemStack.CODEC.optionalFieldOf("remainder").forGetter(UseableItemComponent::remainder),
        ItematicCodecs.setCodec(Pass.CODEC).optionalFieldOf("passes", Pass.DEFAULT_PASSES).forGetter(UseableItemComponent::passes)
    ).apply(instance, UseableItemComponent::new));

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public ItemComponentType<UseableItemComponent> type() {
        return ItemComponentTypes.USEABLE;
    }

    @Override
    public Codec<UseableItemComponent> codec() {
        return CODEC;
    }

    @Override
    public ItemResult use(World world, PlayerEntity user, Hand hand, ItemStack stack, ItemStackConsumer resultStackConsumer) {
        if (this.isUnuseable(Pass.NORMAL)) {
            return ItemResult.PASS;
        }

        return tryStartUsing(world, user, hand, stack);
    }

    @Override
    public ItemResult useOnBlock(ItemUsageContext context, ItemStackConsumer resultStackConsumer) {
        if (this.isUnuseable(Pass.BLOCK)) {
            return ItemResult.PASS;
        }

        return tryStartUsing(context.getWorld(), context.getPlayer(), context.getHand(), context.getStack());
    }

    @Override
    public ItemResult useOnEntity(PlayerEntity user, LivingEntity target, Hand hand, ItemStack stack, ItemStackConsumer resultStackConsumer) {
        if (this.isUnuseable(Pass.ENTITY)) {
            return ItemResult.PASS;
        }

        return tryStartUsing(user.getWorld(), user, hand, stack);
    }

    private static ItemResult tryStartUsing(World world, PlayerEntity user, Hand hand, ItemStack stack) {
        if (!stack.itematic$mayStartUsing(world, user, hand, stack)) {
            return ItemResult.PASS;
        }

        UseDurationDataComponent useDuration = stack.get(ItematicDataComponentTypes.USE_DURATION);
        if (useDuration == null) {
            return ItemResult.CONSUME;
        }

        if (useDuration.startUsing(world, user, hand, stack)) {
            return ItemResult.CONSUME;
        }

        return ItemResult.PASS;
    }

    @Override
    public void addComponents(ComponentMap.Builder builder) {
        this.ticks.ifPresent(ticks -> builder.add(ItematicDataComponentTypes.USE_DURATION, ticks));
        builder.add(ItematicDataComponentTypes.USE_ANIMATION, this.animation);
        this.remainder.ifPresent(remainder -> builder.add(DataComponentTypes.USE_REMAINDER, new UseRemainderComponent(remainder)));
    }

    private boolean isUnuseable(Pass pass) {
        return !this.passes.contains(pass);
    }

    public static class Builder {
        private IntegerProvider ticks;
        private UseAction animation = UseAction.NONE;
        private RegistryEntry<Item> remainder;
        private Set<Pass> passes = Pass.DEFAULT_PASSES;

        private Builder() {}

        public UseableItemComponent build() {
            return new UseableItemComponent(
                Optional.ofNullable(this.ticks).map(UseDurationDataComponent::new),
                this.animation,
                Optional.ofNullable(this.remainder).map(ItemStack::new),
                this.passes
            );
        }

        public Builder useFor(int ticks) {
            this.ticks = new ConstantIntegerProvider(ticks);
            return this;
        }

        public Builder useFor(IntegerProvider ticks) {
            this.ticks = ticks;
            return this;
        }

        public Builder useIndefinitely() {
            this.ticks = IndefiniteIntegerProvider.INSTANCE;
            return this;
        }

        public Builder animation(UseAction animation) {
            this.animation = animation;
            return this;
        }

        public Builder remainder(RegistryEntry<Item> remainder) {
            this.remainder = remainder;
            return this;
        }

        public Builder passes(Pass... passes) {
            this.passes = Set.of(passes);
            return this;
        }
    }

    public enum Pass implements StringIdentifiable {
        NORMAL("normal"),
        BLOCK("block"),
        ENTITY("entity");
        public static final Set<Pass> DEFAULT_PASSES = Set.of(NORMAL);
        public static final Codec<Pass> CODEC = StringIdentifiable.createCodec(Pass::values);

        private final String name;

        Pass(String name) {
            this.name = name;
        }

        @Override
        public String asString() {
            return this.name;
        }
    }
}
