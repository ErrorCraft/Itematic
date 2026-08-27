package net.errorcraft.itematic.world.item.holder.rule.rules;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.errorcraft.itematic.network.codec.ItematicStreamCodecs;
import net.errorcraft.itematic.world.item.behavior.behaviors.ItemHolderItemBehavior;
import net.errorcraft.itematic.world.item.holder.rule.ItemHolderRule;
import net.errorcraft.itematic.world.item.holder.rule.ItemHolderRuleType;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemInstance;
import org.apache.commons.lang3.math.Fraction;

public record FractionItemHolderRule(Fraction fraction) implements ItemHolderRule {
    public static final MapCodec<FractionItemHolderRule> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        ItemHolderItemBehavior.CAPACITY_CODEC.fieldOf("fraction").forGetter(FractionItemHolderRule::fraction)
    ).apply(instance, FractionItemHolderRule::new));
    public static final StreamCodec<ByteBuf, FractionItemHolderRule> STREAM_CODEC = ItematicStreamCodecs.FRACTION.map(FractionItemHolderRule::new, FractionItemHolderRule::fraction);

    public static FractionItemHolderRule of(Fraction fraction) {
        return new FractionItemHolderRule(fraction);
    }

    @Override
    public ItemHolderRuleType<?> type() {
        return ItemHolderRuleType.FRACTION;
    }

    @Override
    public DataResult<Fraction> occupancy(ItemInstance item) {
        return DataResult.success(this.fraction);
    }

    @Override
    public boolean canOccupy(ItemInstance item) {
        return true;
    }
}
