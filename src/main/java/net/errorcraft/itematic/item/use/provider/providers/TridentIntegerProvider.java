package net.errorcraft.itematic.item.use.provider.providers;

import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.errorcraft.itematic.component.type.UseDurationDataComponent;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.use.provider.IntegerProvider;
import net.errorcraft.itematic.item.use.provider.IntegerProviderType;
import net.errorcraft.itematic.item.use.provider.IntegerProviderTypes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.server.world.ServerWorld;

import java.util.OptionalInt;

public record TridentIntegerProvider() implements IntegerProvider {
    public static final TridentIntegerProvider INSTANCE = new TridentIntegerProvider();
    public static final MapCodec<TridentIntegerProvider> CODEC = MapCodec.unit(INSTANCE);
    public static final PacketCodec<ByteBuf, TridentIntegerProvider> PACKET_CODEC = PacketCodec.unit(INSTANCE);

    @Override
    public IntegerProviderType<?> type() {
        return IntegerProviderTypes.TRIDENT;
    }

    @Override
    public OptionalInt get(ItemStack stack, LivingEntity user) {
        if (mayStartUsing(stack, user)) {
            return OptionalInt.of(UseDurationDataComponent.INDEFINITE_USE_DURATION);
        }
        return OptionalInt.empty();
    }

    private static boolean mayStartUsing(ItemStack stack, LivingEntity user) {
        if (user.getWorld() instanceof ServerWorld serverWorld && EnchantmentHelper.getTridentSpinAttackStrength(serverWorld, stack, user) > 0.0f && !user.isTouchingWaterOrRain()) {
            return false;
        }

        return stack.itematic$getComponent(ItemComponentTypes.DAMAGEABLE)
            .map(c -> c.isUsable(stack))
            .orElse(true);
    }
}
