package net.errorcraft.itematic.world.item.holder.rule.rules;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.errorcraft.itematic.world.item.holder.rule.ItemHolderRule;
import net.errorcraft.itematic.world.item.holder.rule.ItemHolderRuleType;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemInstance;
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
    public DataResult<Fraction> occupancy(ItemInstance item) {
        return DataResult.error(() -> "Reject item holder rule does not have occupancy");
    }

    @Override
    public boolean canOccupy(ItemInstance item) {
        return false;
    }
}
