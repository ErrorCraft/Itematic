package net.errorcraft.itematic.item.use.provider.providers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.use.provider.IntegerProvider;
import net.errorcraft.itematic.item.use.provider.IntegerProviderType;
import net.errorcraft.itematic.item.use.provider.IntegerProviderTypes;
import net.errorcraft.itematic.predicate.item.ItemPredicates;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import java.util.OptionalInt;

public record ConditionIntegerProvider(IntegerProvider amount, ItemPredicate condition) implements IntegerProvider {
    public static final MapCodec<ConditionIntegerProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        IntegerProvider.CODEC.fieldOf("amount").forGetter(ConditionIntegerProvider::amount),
        ItemPredicate.CODEC.fieldOf("condition").forGetter(ConditionIntegerProvider::condition)
    ).apply(instance, ConditionIntegerProvider::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, ConditionIntegerProvider> PACKET_CODEC = StreamCodec.composite(
        IntegerProvider.PACKET_CODEC, ConditionIntegerProvider::amount,
        ItemPredicates.PACKET_CODEC, ConditionIntegerProvider::condition,
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
