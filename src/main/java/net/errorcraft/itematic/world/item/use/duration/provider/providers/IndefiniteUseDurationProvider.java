package net.errorcraft.itematic.world.item.use.duration.provider.providers;

import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.errorcraft.itematic.world.item.use.duration.UseDuration;
import net.errorcraft.itematic.world.item.use.duration.provider.UseDurationProvider;
import net.errorcraft.itematic.world.item.use.duration.provider.UseDurationProviderType;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.OptionalInt;

public class IndefiniteUseDurationProvider implements UseDurationProvider {
    public static final IndefiniteUseDurationProvider INSTANCE = new IndefiniteUseDurationProvider();
    public static final MapCodec<IndefiniteUseDurationProvider> CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<ByteBuf, IndefiniteUseDurationProvider> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    private IndefiniteUseDurationProvider() {}

    @Override
    public UseDurationProviderType<?> type() {
        return UseDurationProviderType.INDEFINITE;
    }

    @Override
    public OptionalInt get(ItemStack stack, LivingEntity user) {
        return OptionalInt.of(UseDuration.INDEFINITE);
    }
}
