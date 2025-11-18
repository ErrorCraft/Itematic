package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.component.ItematicDataComponentTypes;
import net.errorcraft.itematic.component.type.UseDurationDataComponent;
import net.errorcraft.itematic.item.ItemStackConsumer;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.use.provider.IntegerProvider;
import net.errorcraft.itematic.serialization.ItematicCodecs;
import net.minecraft.component.ComponentMap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.world.World;

import java.util.Set;

public record UseableItemComponent(UseDurationDataComponent ticks, Set<Pass> passes) implements ItemComponent<UseableItemComponent> {
    public static final Codec<UseableItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        UseDurationDataComponent.MAP_CODEC.forGetter(UseableItemComponent::ticks),
        ItematicCodecs.setCodec(Pass.CODEC).optionalFieldOf("passes", Pass.DEFAULT_PASSES).forGetter(UseableItemComponent::passes)
    ).apply(instance, UseableItemComponent::new));

    public static UseableItemComponent of(int ticks) {
        return new UseableItemComponent(new UseDurationDataComponent(ticks), Pass.DEFAULT_PASSES);
    }

    public static UseableItemComponent of(int ticks, Pass... passes) {
        return new UseableItemComponent(new UseDurationDataComponent(ticks), Set.of(passes));
    }

    public static UseableItemComponent of(IntegerProvider ticks) {
        return new UseableItemComponent(new UseDurationDataComponent(ticks), Pass.DEFAULT_PASSES);
    }

    public static UseableItemComponent of(IntegerProvider ticks, Pass... passes) {
        return new UseableItemComponent(new UseDurationDataComponent(ticks), Set.of(passes));
    }

    public static UseableItemComponent indefinite() {
        return new UseableItemComponent(UseDurationDataComponent.INDEFINITE, Pass.DEFAULT_PASSES);
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
    public ActionResult use(World world, PlayerEntity user, Hand hand, ItemStack stack, ItemStackConsumer resultStackConsumer) {
        if (this.isUnuseable(Pass.NORMAL)) {
            return ActionResult.PASS;
        }
        return tryStartUsing(world, user, hand, stack);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context, ItemStackConsumer resultStackConsumer) {
        if (this.isUnuseable(Pass.BLOCK)) {
            return ActionResult.PASS;
        }
        return tryStartUsing(context.getWorld(), context.getPlayer(), context.getHand(), context.getStack());
    }

    @Override
    public ActionResult useOnEntity(PlayerEntity user, LivingEntity target, Hand hand, ItemStack stack, ItemStackConsumer resultStackConsumer) {
        if (this.isUnuseable(Pass.ENTITY)) {
            return ActionResult.PASS;
        }
        return tryStartUsing(user.getWorld(), user, hand, stack);
    }

    private static ActionResult tryStartUsing(World world, PlayerEntity user, Hand hand, ItemStack stack) {
        if (!stack.itematic$mayStartUsing(world, user, hand, stack)) {
            return ActionResult.PASS;
        }
        UseDurationDataComponent useDurationDataComponent = stack.get(ItematicDataComponentTypes.USE_DURATION);
        if (useDurationDataComponent == null) {
            return ActionResult.PASS;
        }
        if (useDurationDataComponent.startUsing(world, user, hand, stack)) {
            return ActionResult.CONSUME;
        }
        return ActionResult.PASS;
    }

    @Override
    public void addComponents(ComponentMap.Builder builder) {
        builder.add(ItematicDataComponentTypes.USE_DURATION, this.ticks);
    }

    private boolean isUnuseable(Pass pass) {
        return !this.passes.contains(pass);
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
