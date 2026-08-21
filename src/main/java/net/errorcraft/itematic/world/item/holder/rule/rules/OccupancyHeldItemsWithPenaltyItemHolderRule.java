package net.errorcraft.itematic.world.item.holder.rule.rules;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.errorcraft.itematic.network.codec.ItematicStreamCodecs;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.behavior.behaviors.ItemHolderItemBehavior;
import net.errorcraft.itematic.world.item.holder.rule.ItemHolderRule;
import net.errorcraft.itematic.world.item.holder.rule.ItemHolderRuleType;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.math.Fraction;

public record OccupancyHeldItemsWithPenaltyItemHolderRule(Fraction penalty) implements ItemHolderRule {
    public static final MapCodec<OccupancyHeldItemsWithPenaltyItemHolderRule> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        ItemHolderItemBehavior.CAPACITY_CODEC.fieldOf("penalty").forGetter(OccupancyHeldItemsWithPenaltyItemHolderRule::penalty)
    ).apply(instance, OccupancyHeldItemsWithPenaltyItemHolderRule::new));
    public static final StreamCodec<ByteBuf, OccupancyHeldItemsWithPenaltyItemHolderRule> STREAM_CODEC = ItematicStreamCodecs.FRACTION.map(OccupancyHeldItemsWithPenaltyItemHolderRule::new, OccupancyHeldItemsWithPenaltyItemHolderRule::penalty);

    public static OccupancyHeldItemsWithPenaltyItemHolderRule of(Fraction fraction) {
        return new OccupancyHeldItemsWithPenaltyItemHolderRule(fraction);
    }

    @Override
    public ItemHolderRuleType<?> type() {
        return ItemHolderRuleType.OCCUPANCY_HELD_ITEMS_WITH_PENALTY;
    }

    @Override
    public Fraction occupancy(ItemStack stack) {
        return stack.itematic$getBehavior(ItemBehaviorType.ITEM_HOLDER)
            .map(c -> c.occupancy(stack))
            .map(this.penalty::add)
            .orElse(this.penalty);
    }

    @Override
    public boolean canOccupy(ItemStack stack) {
        return true;
    }
}
