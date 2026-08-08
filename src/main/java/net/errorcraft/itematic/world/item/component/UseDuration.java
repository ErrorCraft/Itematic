package net.errorcraft.itematic.world.item.component;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.use.provider.IntegerProvider;
import net.errorcraft.itematic.item.use.provider.providers.ConstantIntegerProvider;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public record UseDuration(IntegerProvider ticks) {
    private static final Codec<UseDuration> FULL_CODEC = IntegerProvider.CODEC.xmap(
        UseDuration::new,
        UseDuration::ticks
    );
    public static final Codec<UseDuration> CODEC = Codec.withAlternative(
        FULL_CODEC,
        ExtraCodecs.POSITIVE_INT,
        UseDuration::new
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, UseDuration> PACKET_CODEC = IntegerProvider.PACKET_CODEC.map(
        UseDuration::new,
        UseDuration::ticks
    );
    public static final int INDEFINITE = -1;

    private UseDuration(int ticks) {
        this(new ConstantIntegerProvider(ticks));
    }

    public int ticks(ItemStack stack, LivingEntity user) {
        return this.ticks.get(stack, user).orElse(0);
    }

    public boolean startUsing(ItemStack stack, LivingEntity user, InteractionHand hand) {
        int ticks = this.ticks(stack, user);
        if (ticks == 0) {
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
