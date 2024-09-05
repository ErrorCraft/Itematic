package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.ItemStackConsumer;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.World;

public record UseableItemComponent(int ticks) implements ItemComponent<UseableItemComponent> {
    public static final Codec<UseableItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codecs.POSITIVE_INT.fieldOf("ticks").forGetter(UseableItemComponent::ticks)
    ).apply(instance, UseableItemComponent::new));

    public static UseableItemComponent of(int ticks) {
        return new UseableItemComponent(ticks);
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
        if (stack.itematic$mayStartUsing(world, user, hand, stack)) {
            return ItemUsage.consumeHeldItem(world, user, hand).getResult();
        }
        return ActionResult.PASS;
    }
}
