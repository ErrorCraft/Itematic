package net.errorcraft.itematic.world.item.use.duration.provider.providers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.errorcraft.itematic.world.item.use.duration.provider.UseDurationProvider;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import java.util.OptionalInt;

public record Constant(int ticks) implements UseDurationProvider {
    public static final MapCodec<Constant> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        ExtraCodecs.POSITIVE_INT.fieldOf("amount").forGetter(Constant::ticks)
    ).apply(instance, Constant::new));
    public static final StreamCodec<ByteBuf, Constant> STREAM_CODEC = ByteBufCodecs.VAR_INT.map(Constant::new, Constant::ticks);
    public static final Type<Constant> TYPE = new Type<>(CODEC, STREAM_CODEC);

    @Override
    public Type<?> type() {
        return TYPE;
    }

    @Override
    public OptionalInt get(ItemStack stack, LivingEntity user) {
        return OptionalInt.of(this.ticks);
    }
}
