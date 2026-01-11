package net.errorcraft.itematic.item.holder.rule;

import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.registry.ItematicRegistries;
import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import org.apache.commons.lang3.math.Fraction;

public interface ItemHolderRule {
    MapCodec<ItemHolderRule> CODEC = ItematicRegistries.ITEM_HOLDER_RULE_TYPE.getCodec().dispatchMap("type", ItemHolderRule::type, ItemHolderRuleType::codec);
    PacketCodec<RegistryByteBuf, ItemHolderRule> PACKET_CODEC = PacketCodecs.registryValue(ItematicRegistryKeys.ITEM_HOLDER_RULE_TYPE).dispatch(ItemHolderRule::type, ItemHolderRuleType::packetCodec);

    ItemHolderRuleType<?> type();
    Fraction occupancy(ItemStack stack);
    boolean canOccupy(ItemStack stack);
}
