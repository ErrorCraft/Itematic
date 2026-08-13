package net.errorcraft.itematic.world.item.holder.rule.rules;

import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.errorcraft.itematic.world.item.holder.rule.ItemHolderRule;
import net.errorcraft.itematic.world.item.holder.rule.ItemHolderRuleType;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.math.Fraction;

public class RejectItemHolderRule implements ItemHolderRule {
    public static final RejectItemHolderRule INSTANCE = new RejectItemHolderRule();
    public static final MapCodec<RejectItemHolderRule> CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<ByteBuf, RejectItemHolderRule> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    private RejectItemHolderRule() {}

    @Override
    public ItemHolderRuleType<?> type() {
        return ItemHolderRuleType.REJECT;
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
