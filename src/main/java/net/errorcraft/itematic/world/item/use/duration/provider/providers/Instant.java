package net.errorcraft.itematic.world.item.use.duration.provider.providers;

import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.errorcraft.itematic.world.item.use.duration.UseDuration;
import net.errorcraft.itematic.world.item.use.duration.provider.UseDurationProvider;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.OptionalInt;

public class Instant implements UseDurationProvider {
    public static final Instant INSTANCE = new Instant();
    public static final MapCodec<Instant> CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<ByteBuf, Instant> STREAM_CODEC = StreamCodec.unit(INSTANCE);
    public static final Type<Instant> TYPE = new Type<>(CODEC, STREAM_CODEC);

    private Instant() {}

    @Override
    public Type<?> type() {
        return TYPE;
    }

    @Override
    public OptionalInt get(ItemStack stack, LivingEntity user) {
        return OptionalInt.of(UseDuration.INSTANT);
    }
}
