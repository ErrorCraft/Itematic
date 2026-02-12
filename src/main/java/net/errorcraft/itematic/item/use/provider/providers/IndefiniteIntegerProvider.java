package net.errorcraft.itematic.item.use.provider.providers;

import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.errorcraft.itematic.component.type.UseDurationDataComponent;
import net.errorcraft.itematic.item.use.provider.IntegerProvider;
import net.errorcraft.itematic.item.use.provider.IntegerProviderType;
import net.errorcraft.itematic.item.use.provider.IntegerProviderTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.codec.PacketCodec;

import java.util.OptionalInt;

public class IndefiniteIntegerProvider implements IntegerProvider {
    public static final IndefiniteIntegerProvider INSTANCE = new IndefiniteIntegerProvider();
    public static final MapCodec<IndefiniteIntegerProvider> CODEC = MapCodec.unit(INSTANCE);
    public static final PacketCodec<ByteBuf, IndefiniteIntegerProvider> PACKET_CODEC = PacketCodec.unit(INSTANCE);

    private IndefiniteIntegerProvider() {}

    @Override
    public IntegerProviderType<?> type() {
        return IntegerProviderTypes.INDEFINITE;
    }

    @Override
    public OptionalInt get(ItemStack stack, LivingEntity user) {
        return OptionalInt.of(UseDurationDataComponent.INDEFINITE_USE_DURATION);
    }
}
