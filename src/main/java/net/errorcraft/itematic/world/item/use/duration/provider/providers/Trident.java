package net.errorcraft.itematic.world.item.use.duration.provider.providers;

import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.use.duration.UseDuration;
import net.errorcraft.itematic.world.item.use.duration.provider.UseDurationProvider;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.OptionalInt;

public class Trident implements UseDurationProvider {
    public static final Trident INSTANCE = new Trident();
    public static final MapCodec<Trident> CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<ByteBuf, Trident> STREAM_CODEC = StreamCodec.unit(INSTANCE);
    public static final Type<Trident> TYPE = new Type<>(CODEC, STREAM_CODEC);

    private Trident() {}

    @Override
    public Type<?> type() {
        return TYPE;
    }

    @Override
    public OptionalInt get(ItemStack stack, LivingEntity user) {
        if (mayStartUsing(stack, user)) {
            return OptionalInt.of(UseDuration.INDEFINITE);
        }

        return OptionalInt.empty();
    }

    private static boolean mayStartUsing(ItemStack stack, LivingEntity user) {
        if (EnchantmentHelper.getTridentSpinAttackStrength(stack, user) > 0.0f && !user.isInWaterOrRain()) {
            return false;
        }

        return stack.itematic$getBehavior(ItemBehaviorType.DAMAGEABLE)
            .map(c -> c.isUsable(stack))
            .orElse(true);
    }
}
