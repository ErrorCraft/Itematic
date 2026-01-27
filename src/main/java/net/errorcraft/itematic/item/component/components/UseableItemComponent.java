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
import net.errorcraft.itematic.serialization.ItematicCodecs;
import net.errorcraft.itematic.util.UseActionUtil;
import net.minecraft.component.ComponentMap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.Hand;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

import java.util.Set;

public record UseableItemComponent(UseDurationDataComponent ticks, UseAction animation, Set<Pass> passes) implements ItemComponent<UseableItemComponent> {
    public static final Codec<UseableItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        UseDurationDataComponent.MAP_CODEC.forGetter(UseableItemComponent::ticks),
        UseActionUtil.CODEC.optionalFieldOf("animation", UseAction.NONE).forGetter(UseableItemComponent::animation),
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

        UseDurationDataComponent useDurationDataComponent = stack.get(ItematicDataComponentTypes.USE_DURATION);
        if (useDurationDataComponent == null) {
            return ItemResult.PASS;
        }

        if (useDurationDataComponent.startUsing(world, user, hand, stack)) {
            return ItemResult.CONSUME;
        }

        return ItemResult.PASS;
    }

    @Override
    public void addComponents(ComponentMap.Builder builder) {
        builder.add(ItematicDataComponentTypes.USE_DURATION, this.ticks);
        builder.add(ItematicDataComponentTypes.USE_ANIMATION, this.animation);
    }

    private boolean isUnuseable(Pass pass) {
        return !this.passes.contains(pass);
    }

    public static class Builder {
        private IntegerProvider ticks;
        private UseAction animation = UseAction.NONE;
        private Set<Pass> passes = Pass.DEFAULT_PASSES;

        private Builder() {}

        public UseableItemComponent build() {
            return new UseableItemComponent(
                this.ticks == null ? UseDurationDataComponent.INDEFINITE : new UseDurationDataComponent(this.ticks),
                this.animation,
                this.passes
            );
        }

        public Builder ticks(int ticks) {
            this.ticks = new ConstantIntegerProvider(ticks);
            return this;
        }

        public Builder ticks(IntegerProvider ticks) {
            this.ticks = ticks;
            return this;
        }

        public Builder animation(UseAction animation) {
            this.animation = animation;
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
