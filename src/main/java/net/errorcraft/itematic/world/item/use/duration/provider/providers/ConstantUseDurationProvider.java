package net.errorcraft.itematic.world.item.use.duration.provider.providers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.errorcraft.itematic.world.item.use.duration.provider.UseDurationProvider;
import net.errorcraft.itematic.world.item.use.duration.provider.UseDurationProviderType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import java.util.OptionalInt;

public record ConstantUseDurationProvider(int value) implements UseDurationProvider {
    public static final MapCodec<ConstantUseDurationProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        ExtraCodecs.POSITIVE_INT.fieldOf("amount").forGetter(ConstantUseDurationProvider::value)
    ).apply(instance, ConstantUseDurationProvider::new));
    public static final StreamCodec<ByteBuf, ConstantUseDurationProvider> STREAM_CODEC = ByteBufCodecs.VAR_INT.map(ConstantUseDurationProvider::new, ConstantUseDurationProvider::value);

    @Override
    public UseDurationProviderType<?> type() {
        return UseDurationProviderType.CONSTANT;
    }

    @Override
    public OptionalInt get(ItemStack stack, LivingEntity user) {
        return OptionalInt.of(this.value);
    }
}
