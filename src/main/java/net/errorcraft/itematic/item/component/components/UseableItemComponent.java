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
import net.errorcraft.itematic.item.use.provider.providers.ConstantIntegerProvider;
import net.minecraft.component.ComponentMap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public record UseableItemComponent(UseDurationDataComponent ticks) implements ItemComponent<UseableItemComponent> {
    public static final Codec<UseableItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        UseDurationDataComponent.MAP_CODEC.forGetter(UseableItemComponent::ticks)
    ).apply(instance, UseableItemComponent::new));

    public static UseableItemComponent of(int ticks) {
        return new UseableItemComponent(new UseDurationDataComponent(new ConstantIntegerProvider(ticks)));
    }

    public static UseableItemComponent of(IntegerProvider ticks) {
        return new UseableItemComponent(new UseDurationDataComponent(ticks));
    }

    public static UseableItemComponent indefinite() {
        return new UseableItemComponent(UseDurationDataComponent.INDEFINITE);
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
}
