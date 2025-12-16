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
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.network.codec.PacketCodec;
import org.apache.commons.lang3.math.Fraction;

public record FractionWithOccupancyHeldItemsItemHolderRule(Fraction fraction) implements ItemHolderRule {
    public static final MapCodec<FractionWithOccupancyHeldItemsItemHolderRule> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        ItemHolderItemComponent.CAPACITY_CODEC.fieldOf("fraction").forGetter(FractionWithOccupancyHeldItemsItemHolderRule::fraction)
    ).apply(instance, FractionWithOccupancyHeldItemsItemHolderRule::new));
    public static final PacketCodec<ByteBuf, FractionWithOccupancyHeldItemsItemHolderRule> PACKET_CODEC = PacketCodecUtil.FRACTION.xmap(FractionWithOccupancyHeldItemsItemHolderRule::new, FractionWithOccupancyHeldItemsItemHolderRule::fraction);

    public static FractionWithOccupancyHeldItemsItemHolderRule of(Fraction fraction) {
        return new FractionWithOccupancyHeldItemsItemHolderRule(fraction);
    }

    @Override
    public ItemHolderRuleType<?> type() {
        return ItemHolderRuleTypes.FRACTION_WITH_OCCUPANCY_HELD_ITEMS;
    }

    @Override
    public Fraction occupancy(ItemStack stack) {
        if (!stack.itematic$hasComponent(ItemComponentTypes.ITEM_HOLDER)) {
            return this.fraction;
        }
        BundleContentsComponent bundleContents = stack.get(DataComponentTypes.BUNDLE_CONTENTS);
        if (bundleContents == null) {
            return this.fraction;
        }
        return this.fraction.add(bundleContents.getOccupancy());
    }

    @Override
    public boolean canOccupy(ItemStack stack) {
        return true;
    }
}
