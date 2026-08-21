package net.errorcraft.itematic.world.item.use.duration.provider.providers;

import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.use.duration.provider.UseDurationProvider;
import net.errorcraft.itematic.world.item.use.duration.provider.UseDurationProviderType;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.OptionalInt;

public class ShooterUseDurationProvider implements UseDurationProvider {
    public static final ShooterUseDurationProvider INSTANCE = new ShooterUseDurationProvider();
    public static final MapCodec<ShooterUseDurationProvider> CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<ByteBuf, ShooterUseDurationProvider> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    private ShooterUseDurationProvider() {}

    @Override
    public UseDurationProviderType<?> type() {
        return UseDurationProviderType.SHOOTER;
    }

    @Override
    public OptionalInt get(ItemStack stack, LivingEntity user) {
        if (user.itematic$getAmmunition(stack).isEmpty()) {
            return OptionalInt.empty();
        }

        return stack.itematic$getBehavior(ItemBehaviorType.SHOOTER)
            .map(shooter -> shooter.useDuration(stack, user))
            .orElseGet(OptionalInt::empty);
    }
}
