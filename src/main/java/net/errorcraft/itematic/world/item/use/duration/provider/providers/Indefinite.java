package net.errorcraft.itematic.world.item.use.duration.provider.providers;

import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.errorcraft.itematic.world.item.use.duration.UseDuration;
import net.errorcraft.itematic.world.item.use.duration.provider.UseDurationProvider;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.OptionalInt;

public class Indefinite implements UseDurationProvider {
    public static final Indefinite INSTANCE = new Indefinite();
    public static final MapCodec<Indefinite> CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<ByteBuf, Indefinite> STREAM_CODEC = StreamCodec.unit(INSTANCE);
    public static final Type<Indefinite> TYPE = new Type<>(CODEC, STREAM_CODEC);

    private Indefinite() {}

    @Override
    public Type<?> type() {
        return TYPE;
    }

    @Override
    public OptionalInt get(ItemStack stack, LivingEntity user) {
        return OptionalInt.of(UseDuration.INDEFINITE);
    }
}
