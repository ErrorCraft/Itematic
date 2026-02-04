package net.errorcraft.itematic.item.use.provider.providers;

import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.use.provider.IntegerProvider;
import net.errorcraft.itematic.item.use.provider.IntegerProviderType;
import net.errorcraft.itematic.item.use.provider.IntegerProviderTypes;
import net.minecraft.SharedConstants;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.MathHelper;

import java.util.OptionalInt;

public class PlayableIntegerProvider implements IntegerProvider {
    public static final PlayableIntegerProvider INSTANCE = new PlayableIntegerProvider();
    public static final MapCodec<PlayableIntegerProvider> CODEC = MapCodec.unit(INSTANCE);
    public static final PacketCodec<ByteBuf, PlayableIntegerProvider> PACKET_CODEC = PacketCodec.unit(INSTANCE);

    private PlayableIntegerProvider() {}

    @Override
    public IntegerProviderType<?> type() {
        return IntegerProviderTypes.PLAYABLE;
    }

    @Override
    public OptionalInt get(ItemStack stack, LivingEntity user) {
        return stack.itematic$getBehavior(ItemComponentTypes.PLAYABLE)
            .flatMap(component -> component.instrument(stack, user.getRegistryManager()))
            .map(RegistryEntry::value)
            .map(instrument -> OptionalInt.of(MathHelper.floor(instrument.useDuration() * SharedConstants.TICKS_PER_SECOND)))
            .orElseGet(OptionalInt::empty);
    }
}
