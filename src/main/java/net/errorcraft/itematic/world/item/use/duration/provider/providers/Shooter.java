package net.errorcraft.itematic.world.item.use.duration.provider.providers;

import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.world.item.use.duration.provider.UseDurationProvider;
import net.errorcraft.itematic.world.item.weapon.shooter.method.ShooterMethod;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.OptionalInt;

public class Shooter implements UseDurationProvider {
    public static final Shooter INSTANCE = new Shooter();
    public static final MapCodec<Shooter> CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<ByteBuf, Shooter> STREAM_CODEC = StreamCodec.unit(INSTANCE);
    public static final Type<Shooter> TYPE = new Type<>(CODEC, STREAM_CODEC);

    private Shooter() {}

    @Override
    public Type<?> type() {
        return TYPE;
    }

    @Override
    public OptionalInt get(ItemStack stack, LivingEntity user) {
        if (user.itematic$getAmmunition(stack).isEmpty()) {
            return OptionalInt.empty();
        }

        ShooterMethod<?> method = stack.get(ItematicDataComponents.SHOOTER_METHOD);
        if (method == null) {
            return OptionalInt.empty();
        }

        return method.useDuration(stack, user);
    }
}
