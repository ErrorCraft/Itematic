package net.errorcraft.itematic.world.item.use.duration.provider.providers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.advancements.criterion.ItemPredicates;
import net.errorcraft.itematic.world.item.use.duration.provider.UseDurationProvider;
import net.errorcraft.itematic.world.item.use.duration.provider.UseDurationProviderType;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.OptionalInt;

public record ConditionUseDurationProvider(UseDurationProvider amount, ItemPredicate condition) implements UseDurationProvider {
    public static final MapCodec<ConditionUseDurationProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        UseDurationProvider.CODEC.fieldOf("amount").forGetter(ConditionUseDurationProvider::amount),
        ItemPredicate.CODEC.fieldOf("condition").forGetter(ConditionUseDurationProvider::condition)
    ).apply(instance, ConditionUseDurationProvider::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, ConditionUseDurationProvider> STREAM_CODEC = StreamCodec.composite(
        UseDurationProvider.STREAM_CODEC, ConditionUseDurationProvider::amount,
        ItemPredicates.STREAM_CODEC, ConditionUseDurationProvider::condition,
        ConditionUseDurationProvider::new
    );

    @Override
    public UseDurationProviderType<?> type() {
        return UseDurationProviderType.CONDITION;
    }

    @Override
    public OptionalInt get(ItemStack stack, LivingEntity user) {
        if (!this.condition.test(stack)) {
            return OptionalInt.empty();
        }

        return this.amount.get(stack, user);
    }
}
