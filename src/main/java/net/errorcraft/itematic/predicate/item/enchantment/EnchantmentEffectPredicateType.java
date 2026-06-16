package net.errorcraft.itematic.predicate.item.enchantment;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.predicate.item.enchantment.numerical.EnchantmentValueEffectPredicate;
import net.errorcraft.itematic.registry.ItematicRegistries;
import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registry;

public record EnchantmentEffectPredicateType<T extends EnchantmentEffectPredicate>(Codec<T> codec, PacketCodec<RegistryByteBuf, T> packetCodec) {
    public static final Codec<EnchantmentEffectPredicateType<?>> CODEC = ItematicRegistries.ENCHANTMENT_EFFECT_PREDICATE_TYPE.getCodec();
    public static final PacketCodec<RegistryByteBuf, EnchantmentEffectPredicateType<?>> PACKET_CODEC = PacketCodecs.registryValue(ItematicRegistryKeys.ENCHANTMENT_EFFECT_PREDICATE_TYPE);
    public static final EnchantmentEffectPredicateType<EnchantmentValueEffectPredicate.TridentSpinAttackStrength> TRIDENT_SPIN_ATTACK_STRENGTH = register("trident_spin_attack_strength", new EnchantmentEffectPredicateType<>(EnchantmentValueEffectPredicate.TridentSpinAttackStrength.CODEC, EnchantmentValueEffectPredicate.TridentSpinAttackStrength.PACKET_CODEC));

    public static void init() {}

    private static <T extends EnchantmentEffectPredicate> EnchantmentEffectPredicateType<T> register(String id, EnchantmentEffectPredicateType<T> type) {
        return Registry.register(ItematicRegistries.ENCHANTMENT_EFFECT_PREDICATE_TYPE, id, type);
    }
}
