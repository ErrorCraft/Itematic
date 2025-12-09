package net.errorcraft.itematic.item.use.provider.providers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.use.provider.IntegerProvider;
import net.errorcraft.itematic.item.use.provider.IntegerProviderType;
import net.errorcraft.itematic.item.use.provider.IntegerProviderTypes;
import net.errorcraft.itematic.predicate.item.ItemPredicateUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.predicate.item.ItemPredicate;

import java.util.OptionalInt;

public record ConditionIntegerProvider(IntegerProvider amount, ItemPredicate condition) implements IntegerProvider {
    public static final MapCodec<ConditionIntegerProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        IntegerProvider.CODEC.fieldOf("amount").forGetter(ConditionIntegerProvider::amount),
        ItemPredicate.CODEC.fieldOf("condition").forGetter(ConditionIntegerProvider::condition)
    ).apply(instance, ConditionIntegerProvider::new));
    public static final PacketCodec<RegistryByteBuf, ConditionIntegerProvider> PACKET_CODEC = PacketCodec.tuple(
        IntegerProvider.PACKET_CODEC, ConditionIntegerProvider::amount,
        ItemPredicateUtil.PACKET_CODEC, ConditionIntegerProvider::condition,
        ConditionIntegerProvider::new
    );

    @Override
    public IntegerProviderType<?> type() {
        return IntegerProviderTypes.CONDITION;
    }

    @Override
    public OptionalInt get(ItemStack stack, LivingEntity user) {
        if (!this.condition.test(stack)) {
            return OptionalInt.empty();
        }
        return this.amount.get(stack, user);
    }
}
