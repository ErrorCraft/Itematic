package net.errorcraft.itematic.item.holder.rule.rules;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.ItemHolderItemComponent;
import net.errorcraft.itematic.item.holder.rule.ItemHolderRule;
import net.errorcraft.itematic.item.holder.rule.ItemHolderRuleType;
import net.errorcraft.itematic.item.holder.rule.ItemHolderRuleTypes;
import net.errorcraft.itematic.network.codec.PacketCodecUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.network.codec.PacketCodec;
import org.apache.commons.lang3.math.Fraction;

public record OccupancyHeldItemsWithPenaltyItemHolderRule(Fraction penalty) implements ItemHolderRule {
    public static final MapCodec<OccupancyHeldItemsWithPenaltyItemHolderRule> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        ItemHolderItemComponent.CAPACITY_CODEC.fieldOf("penalty").forGetter(OccupancyHeldItemsWithPenaltyItemHolderRule::penalty)
    ).apply(instance, OccupancyHeldItemsWithPenaltyItemHolderRule::new));
    public static final PacketCodec<ByteBuf, OccupancyHeldItemsWithPenaltyItemHolderRule> PACKET_CODEC = PacketCodecUtil.FRACTION.xmap(OccupancyHeldItemsWithPenaltyItemHolderRule::new, OccupancyHeldItemsWithPenaltyItemHolderRule::penalty);

    public static OccupancyHeldItemsWithPenaltyItemHolderRule of(Fraction fraction) {
        return new OccupancyHeldItemsWithPenaltyItemHolderRule(fraction);
    }

    @Override
    public ItemHolderRuleType<?> type() {
        return ItemHolderRuleTypes.OCCUPANCY_HELD_ITEMS_WITH_PENALTY;
    }

    @Override
    public Fraction occupancy(ItemStack stack) {
        return stack.itematic$getBehavior(ItemComponentTypes.ITEM_HOLDER)
            .map(c -> c.occupancy(stack))
            .map(this.penalty::add)
            .orElse(this.penalty);
    }

    @Override
    public boolean canOccupy(ItemStack stack) {
        return true;
    }
}
