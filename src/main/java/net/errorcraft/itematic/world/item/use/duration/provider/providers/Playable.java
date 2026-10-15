package net.errorcraft.itematic.world.item.use.duration.provider.providers;

import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.use.duration.provider.UseDurationProvider;
import net.minecraft.SharedConstants;
import net.minecraft.core.Holder;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.OptionalInt;

public class Playable implements UseDurationProvider {
    public static final Playable INSTANCE = new Playable();
    public static final MapCodec<Playable> CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<ByteBuf, Playable> STREAM_CODEC = StreamCodec.unit(INSTANCE);
    public static final Type<Playable> TYPE = new Type<>(CODEC, STREAM_CODEC);

    private Playable() {}

    @Override
    public Type<?> type() {
        return TYPE;
    }

    @Override
    public OptionalInt get(ItemStack stack, LivingEntity user) {
        return stack.itematic$getBehavior(ItemBehaviorType.PLAYABLE)
            .flatMap(playable -> playable.instrument(stack))
            .map(Holder::value)
            .map(instrument -> OptionalInt.of(Mth.floor(instrument.useDuration() * SharedConstants.TICKS_PER_SECOND)))
            .orElseGet(OptionalInt::empty);
    }
}
