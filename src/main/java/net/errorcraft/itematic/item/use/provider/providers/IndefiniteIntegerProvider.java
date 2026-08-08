package net.errorcraft.itematic.item.use.provider.providers;

import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.errorcraft.itematic.item.use.provider.IntegerProvider;
import net.errorcraft.itematic.item.use.provider.IntegerProviderType;
import net.errorcraft.itematic.item.use.provider.IntegerProviderTypes;
import net.errorcraft.itematic.world.item.component.UseDuration;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.OptionalInt;

public class IndefiniteIntegerProvider implements IntegerProvider {
    public static final IndefiniteIntegerProvider INSTANCE = new IndefiniteIntegerProvider();
    public static final MapCodec<IndefiniteIntegerProvider> CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<ByteBuf, IndefiniteIntegerProvider> PACKET_CODEC = StreamCodec.unit(INSTANCE);

    private IndefiniteIntegerProvider() {}

    @Override
    public IntegerProviderType<?> type() {
        return IntegerProviderTypes.INDEFINITE;
    }

    @Override
    public OptionalInt get(ItemStack stack, LivingEntity user) {
        return OptionalInt.of(UseDuration.INDEFINITE);
    }
}
