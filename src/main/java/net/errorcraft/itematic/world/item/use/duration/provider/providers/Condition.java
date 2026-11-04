package net.errorcraft.itematic.world.item.use.duration.provider.providers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.advancements.predicates.ItemPredicates;
import net.errorcraft.itematic.world.item.use.duration.provider.UseDurationProvider;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.OptionalInt;

public record Condition(UseDurationProvider amount, ItemPredicate condition) implements UseDurationProvider {
    public static final MapCodec<Condition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        UseDurationProvider.CODEC.fieldOf("amount").forGetter(Condition::amount),
        ItemPredicate.CODEC.fieldOf("condition").forGetter(Condition::condition)
    ).apply(instance, Condition::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, Condition> STREAM_CODEC = StreamCodec.composite(
        UseDurationProvider.STREAM_CODEC, Condition::amount,
        ItemPredicates.STREAM_CODEC, Condition::condition,
        Condition::new
    );
    public static final Type<Condition> TYPE = new Type<>(CODEC, STREAM_CODEC);

    @Override
    public Type<?> type() {
        return TYPE;
    }

    @Override
    public OptionalInt get(ItemStack stack, LivingEntity user) {
        if (!this.condition.test(stack)) {
            return OptionalInt.empty();
        }

        return this.amount.get(stack, user);
    }
}
