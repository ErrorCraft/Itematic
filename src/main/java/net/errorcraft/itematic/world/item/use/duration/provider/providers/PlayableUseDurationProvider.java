package net.errorcraft.itematic.world.item.use.duration.provider.providers;

import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.use.duration.provider.UseDurationProvider;
import net.errorcraft.itematic.world.item.use.duration.provider.UseDurationProviderType;
import net.minecraft.SharedConstants;
import net.minecraft.core.Holder;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.OptionalInt;

public class PlayableUseDurationProvider implements UseDurationProvider {
    public static final PlayableUseDurationProvider INSTANCE = new PlayableUseDurationProvider();
    public static final MapCodec<PlayableUseDurationProvider> CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<ByteBuf, PlayableUseDurationProvider> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    private PlayableUseDurationProvider() {}

    @Override
    public UseDurationProviderType<?> type() {
        return UseDurationProviderType.PLAYABLE;
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
