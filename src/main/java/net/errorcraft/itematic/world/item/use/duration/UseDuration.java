package net.errorcraft.itematic.world.item.use.duration;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.world.item.use.duration.provider.UseDurationProvider;
import net.errorcraft.itematic.world.item.use.duration.provider.providers.ConstantUseDurationProvider;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public record UseDuration(UseDurationProvider ticks) {
    private static final Codec<UseDuration> FULL_CODEC = UseDurationProvider.CODEC.xmap(
        UseDuration::new,
        UseDuration::ticks
    );
    public static final Codec<UseDuration> CODEC = Codec.withAlternative(
        FULL_CODEC,
        ExtraCodecs.POSITIVE_INT,
        UseDuration::new
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, UseDuration> STREAM_CODEC = UseDurationProvider.STREAM_CODEC.map(
        UseDuration::new,
        UseDuration::ticks
    );
    public static final int NONE = 0;
    public static final int INDEFINITE = -1;

    private UseDuration(int ticks) {
        this(new ConstantUseDurationProvider(ticks));
    }

    public int ticks(ItemStack stack, LivingEntity user) {
        return this.ticks.get(stack, user).orElse(NONE);
    }

    public boolean startUsing(ItemStack stack, LivingEntity user, InteractionHand hand) {
        int ticks = this.ticks(stack, user);
        if (ticks == NONE) {
            return false;
        }

        if (ticks == INDEFINITE) {
            user.startUsingItem(hand);
        } else {
            user.itematic$startUsingItem(hand, ticks);
        }

        return true;
    }
}
