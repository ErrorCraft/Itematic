package net.errorcraft.itematic.component.type;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.holder.rule.ItemHolderRules;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import java.util.List;

public record BundleContentsComponentExtraFields(ItemHolderRules rules) {
    public static final BundleContentsComponentExtraFields DEFAULT = new BundleContentsComponentExtraFields(new ItemHolderRules(List.of()));
    public static final Codec<BundleContentsComponentExtraFields> CODEC = ItemHolderRules.CODEC.xmap(BundleContentsComponentExtraFields::new, BundleContentsComponentExtraFields::rules);
    public static final StreamCodec<RegistryFriendlyByteBuf, BundleContentsComponentExtraFields> PACKET_CODEC = ItemHolderRules.PACKET_CODEC.map(BundleContentsComponentExtraFields::new, BundleContentsComponentExtraFields::rules);
}
