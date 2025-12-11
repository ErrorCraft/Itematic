package net.errorcraft.itematic.item.holder.rule.rules;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.errorcraft.itematic.item.holder.rule.ItemHolderRule;
import net.errorcraft.itematic.item.holder.rule.ItemHolderRuleType;
import net.errorcraft.itematic.item.holder.rule.ItemHolderRuleTypes;
import net.errorcraft.itematic.network.codec.PacketCodecUtil;
import net.errorcraft.itematic.serialization.ItematicCodecs;
import net.minecraft.item.ItemStack;
import net.minecraft.network.codec.PacketCodec;
import org.apache.commons.lang3.math.Fraction;

public record FractionItemHolderRule(Fraction fraction) implements ItemHolderRule {
    public static final MapCodec<FractionItemHolderRule> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        ItematicCodecs.POSITIVE_FRACTION.fieldOf("fraction").forGetter(FractionItemHolderRule::fraction)
    ).apply(instance, FractionItemHolderRule::new));
    public static final PacketCodec<ByteBuf, FractionItemHolderRule> PACKET_CODEC = PacketCodecUtil.FRACTION.xmap(FractionItemHolderRule::new, FractionItemHolderRule::fraction);

    public static FractionItemHolderRule of(int numerator, int denominator) {
        return new FractionItemHolderRule(Fraction.getFraction(numerator, denominator));
    }

    @Override
    public ItemHolderRuleType<?> type() {
        return ItemHolderRuleTypes.FRACTION;
    }

    @Override
    public Fraction occupancy(ItemStack stack) {
        return this.fraction;
    }

    @Override
    public boolean canOccupy(ItemStack stack) {
        return true;
    }
}
