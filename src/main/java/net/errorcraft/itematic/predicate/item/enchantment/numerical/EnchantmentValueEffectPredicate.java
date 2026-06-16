package net.errorcraft.itematic.predicate.item.enchantment.numerical;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.mixin.enchantment.EnchantmentHelperAccessor;
import net.errorcraft.itematic.predicate.NumberRangeUtil;
import net.errorcraft.itematic.predicate.item.enchantment.NumericalEnchantmentEffectPredicate;
import net.minecraft.component.ComponentType;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.enchantment.effect.EnchantmentValueEffect;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.util.math.random.Random;
import org.apache.commons.lang3.mutable.MutableFloat;

public abstract class EnchantmentValueEffectPredicate extends NumericalEnchantmentEffectPredicate<EnchantmentValueEffect> {
    public EnchantmentValueEffectPredicate(float baseValue, NumberRangeUtil.FloatRange range) {
        super(baseValue, range);
    }

    public static TridentSpinAttackStrength tridentSpinAttackStrength(float expectedValue) {
        return new TridentSpinAttackStrength(0.0f, NumberRangeUtil.FloatRange.exactly(expectedValue));
    }

    @Override
    public float getValue(float baseValue, ItemStack stack, Random random) {
        MutableFloat value = new MutableFloat(this.baseValue);
        EnchantmentHelperAccessor.forEachEnchantment(
            stack,
            (enchantment, level) -> enchantment.value().modifyValue(
                this.componentType(),
                random,
                level,
                value
            )
        );
        return value.floatValue();
    }

    public static class TridentSpinAttackStrength extends EnchantmentValueEffectPredicate {
        public static final Codec<TridentSpinAttackStrength> CODEC = createCodec(EnchantmentValueEffectPredicate.TridentSpinAttackStrength::new);
        public static final PacketCodec<RegistryByteBuf, TridentSpinAttackStrength> PACKET_CODEC = createPacketCodec(EnchantmentValueEffectPredicate.TridentSpinAttackStrength::new);

        public TridentSpinAttackStrength(float baseValue, NumberRangeUtil.FloatRange range) {
            super(baseValue, range);
        }

        @Override
        public ComponentType<EnchantmentValueEffect> componentType() {
            return EnchantmentEffectComponentTypes.TRIDENT_SPIN_ATTACK_STRENGTH;
        }
    }
}
