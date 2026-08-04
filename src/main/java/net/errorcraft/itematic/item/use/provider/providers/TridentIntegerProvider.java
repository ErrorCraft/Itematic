package net.errorcraft.itematic.item.use.provider.providers;

import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.errorcraft.itematic.component.type.UseDurationDataComponent;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.use.provider.IntegerProvider;
import net.errorcraft.itematic.item.use.provider.IntegerProviderType;
import net.errorcraft.itematic.item.use.provider.IntegerProviderTypes;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import java.util.OptionalInt;

public class TridentIntegerProvider implements IntegerProvider {
    public static final TridentIntegerProvider INSTANCE = new TridentIntegerProvider();
    public static final MapCodec<TridentIntegerProvider> CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<ByteBuf, TridentIntegerProvider> PACKET_CODEC = StreamCodec.unit(INSTANCE);

    private TridentIntegerProvider() {}

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
        if (EnchantmentHelper.getTridentSpinAttackStrength(stack, user) > 0.0f && !user.isInWaterOrRain()) {
            return false;
        }

        return stack.itematic$getBehavior(ItemComponentTypes.DAMAGEABLE)
            .map(c -> c.isUsable(stack))
            .orElse(true);
    }
}
