package net.errorcraft.itematic.item.use.provider.providers;

import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.use.provider.IntegerProvider;
import net.errorcraft.itematic.item.use.provider.IntegerProviderType;
import net.errorcraft.itematic.item.use.provider.IntegerProviderTypes;
import net.minecraft.SharedConstants;
import net.minecraft.core.Holder;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import java.util.OptionalInt;

public class PlayableIntegerProvider implements IntegerProvider {
    public static final PlayableIntegerProvider INSTANCE = new PlayableIntegerProvider();
    public static final MapCodec<PlayableIntegerProvider> CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<ByteBuf, PlayableIntegerProvider> PACKET_CODEC = StreamCodec.unit(INSTANCE);

    private PlayableIntegerProvider() {}

    @Override
    public IntegerProviderType<?> type() {
        return IntegerProviderTypes.PLAYABLE;
    }

    @Override
    public OptionalInt get(ItemStack stack, LivingEntity user) {
        return stack.itematic$getBehavior(ItemComponentTypes.PLAYABLE)
            .flatMap(component -> component.instrument(stack, user.registryAccess()))
            .map(Holder::value)
            .map(instrument -> OptionalInt.of(Mth.floor(instrument.useDuration() * SharedConstants.TICKS_PER_SECOND)))
            .orElseGet(OptionalInt::empty);
    }
}
