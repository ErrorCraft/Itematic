package net.errorcraft.itematic.item.use.provider.providers;

import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.use.provider.IntegerProvider;
import net.errorcraft.itematic.item.use.provider.IntegerProviderType;
import net.errorcraft.itematic.item.use.provider.IntegerProviderTypes;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import java.util.OptionalInt;

public class ShooterIntegerProvider implements IntegerProvider {
    public static final ShooterIntegerProvider INSTANCE = new ShooterIntegerProvider();
    public static final MapCodec<ShooterIntegerProvider> CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<ByteBuf, ShooterIntegerProvider> PACKET_CODEC = StreamCodec.unit(INSTANCE);

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

        return stack.itematic$getBehavior(ItemComponentTypes.SHOOTER)
            .map(shooter -> shooter.useDuration(stack, user))
            .orElseGet(OptionalInt::empty);
    }
}
