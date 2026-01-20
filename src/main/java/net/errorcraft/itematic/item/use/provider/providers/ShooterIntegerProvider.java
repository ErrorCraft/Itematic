package net.errorcraft.itematic.item.use.provider.providers;

import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.use.provider.IntegerProvider;
import net.errorcraft.itematic.item.use.provider.IntegerProviderType;
import net.errorcraft.itematic.item.use.provider.IntegerProviderTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.codec.PacketCodec;

import java.util.OptionalInt;

public class ShooterIntegerProvider implements IntegerProvider {
    public static final ShooterIntegerProvider INSTANCE = new ShooterIntegerProvider();
    public static final MapCodec<ShooterIntegerProvider> CODEC = MapCodec.unit(INSTANCE);
    public static final PacketCodec<ByteBuf, ShooterIntegerProvider> PACKET_CODEC = PacketCodec.unit(INSTANCE);

    private ShooterIntegerProvider() {}

    @Override
    public IntegerProviderType<?> type() {
        return IntegerProviderTypes.SHOOTER;
    }

    @Override
    public OptionalInt get(ItemStack stack, LivingEntity user) {
        if (user.itematic$getAmmunition(stack).isEmpty()) {
            return OptionalInt.empty();
        }

        return stack.itematic$getComponent(ItemComponentTypes.SHOOTER)
            .map(shooter -> shooter.useDuration(stack, user))
            .orElseGet(OptionalInt::empty);
    }
}
