package net.errorcraft.itematic.item.holder.rule.rules;

import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.errorcraft.itematic.item.holder.rule.ItemHolderRule;
import net.errorcraft.itematic.item.holder.rule.ItemHolderRuleType;
import net.errorcraft.itematic.item.holder.rule.ItemHolderRuleTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.network.codec.PacketCodec;
import org.apache.commons.lang3.math.Fraction;

public record RejectItemHolderRule() implements ItemHolderRule {
    public static final RejectItemHolderRule INSTANCE = new RejectItemHolderRule();
    public static final MapCodec<RejectItemHolderRule> CODEC = MapCodec.unit(INSTANCE);
    public static final PacketCodec<ByteBuf, RejectItemHolderRule> PACKET_CODEC = PacketCodec.unit(INSTANCE);

    @Override
    public ItemHolderRuleType<?> type() {
        return ItemHolderRuleTypes.REJECT;
    }

    @Override
    public Fraction occupancy(ItemStack stack) {
        return Fraction.ZERO;
    }

    @Override
    public boolean canOccupy(ItemStack stack) {
        return false;
    }
}
