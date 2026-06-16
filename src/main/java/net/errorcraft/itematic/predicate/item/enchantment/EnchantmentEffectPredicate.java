package net.errorcraft.itematic.predicate.item.enchantment;

import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.errorcraft.itematic.network.codec.PacketCodecUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.util.math.random.Random;

import java.util.Map;

public interface EnchantmentEffectPredicate {
    Codec<Map<EnchantmentEffectPredicateType<?>, EnchantmentEffectPredicate>> CODEC = Codec.dispatchedMap(EnchantmentEffectPredicateType.CODEC, EnchantmentEffectPredicateType::codec);
    PacketCodec<RegistryByteBuf, Map<EnchantmentEffectPredicateType<?>, EnchantmentEffectPredicate>> PACKET_CODEC = PacketCodecUtil.dispatchedMap(
        Object2ObjectOpenHashMap::new,
        EnchantmentEffectPredicateType.PACKET_CODEC,
        EnchantmentEffectPredicateType::packetCodec
    );
    boolean test(ItemStack stack, Random random);
}
