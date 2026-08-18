package net.errorcraft.itematic.world.item.holder.rule;

import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.core.registries.ItematicBuiltInRegistries;
import net.errorcraft.itematic.core.registries.ItematicRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.math.Fraction;

public interface ItemHolderRule {
    MapCodec<ItemHolderRule> CODEC = ItematicBuiltInRegistries.ITEM_HOLDER_RULE_TYPE.byNameCodec()
        .dispatchMap(ItemHolderRule::type, ItemHolderRuleType::codec);
    StreamCodec<RegistryFriendlyByteBuf, ItemHolderRule> STREAM_CODEC = ByteBufCodecs.registry(ItematicRegistries.ITEM_HOLDER_RULE_TYPE).dispatch(ItemHolderRule::type, ItemHolderRuleType::streamCodec);

    ItemHolderRuleType<?> type();
    Fraction occupancy(ItemStack stack);
    boolean canOccupy(ItemStack stack);
}
