package net.errorcraft.itematic.item.use.provider.providers;

import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.errorcraft.itematic.component.type.UseDurationDataComponent;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.ShooterItemComponent;
import net.errorcraft.itematic.item.use.provider.IntegerProvider;
import net.errorcraft.itematic.item.use.provider.IntegerProviderType;
import net.errorcraft.itematic.item.use.provider.IntegerProviderTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.codec.PacketCodec;

import java.util.Optional;
import java.util.OptionalInt;

public record ShooterIntegerProvider() implements IntegerProvider {
    public static final ShooterIntegerProvider INSTANCE = new ShooterIntegerProvider();
    public static final MapCodec<ShooterIntegerProvider> CODEC = MapCodec.unit(INSTANCE);
    public static final PacketCodec<ByteBuf, ShooterIntegerProvider> PACKET_CODEC = PacketCodec.unit(INSTANCE);

    @Override
    public IntegerProviderType<?> type() {
        return IntegerProviderTypes.SHOOTER;
    }

    @Override
    public OptionalInt get(ItemStack stack, LivingEntity user) {
        Optional<ShooterItemComponent> shooter = stack.itematic$getComponent(ItemComponentTypes.SHOOTER);
        if (shooter.isEmpty()) {
            return OptionalInt.empty();
        }
        if (shooter.get().isCharged(stack)) {
            return OptionalInt.empty();
        }
        if (user.itematic$getAmmunition(stack).isEmpty()) {
            return OptionalInt.empty();
        }
        return OptionalInt.of(UseDurationDataComponent.INDEFINITE_USE_DURATION);
    }
}
