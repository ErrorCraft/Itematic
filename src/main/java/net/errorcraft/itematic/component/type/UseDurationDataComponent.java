package net.errorcraft.itematic.component.type;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.use.provider.IntegerProvider;
import net.errorcraft.itematic.item.use.provider.providers.ConstantIntegerProvider;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.Level;

public record UseDurationDataComponent(IntegerProvider ticks) {
    private static final Codec<UseDurationDataComponent> FULL_CODEC = IntegerProvider.CODEC.xmap(
        UseDurationDataComponent::new,
        UseDurationDataComponent::ticks
    );
    public static final Codec<UseDurationDataComponent> CODEC = Codec.withAlternative(
        FULL_CODEC,
        ExtraCodecs.POSITIVE_INT,
        UseDurationDataComponent::new
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, UseDurationDataComponent> PACKET_CODEC = IntegerProvider.PACKET_CODEC.map(
        UseDurationDataComponent::new,
        UseDurationDataComponent::ticks
    );
    public static final int INDEFINITE_USE_DURATION = -1;

    private UseDurationDataComponent(int ticks) {
        this(new ConstantIntegerProvider(ticks));
    }

    public int ticks(ItemStack stack, LivingEntity user) {
        return this.ticks.get(stack, user).orElse(0);
    }

    public boolean startUsing(Level world, Player user, InteractionHand hand, ItemStack stack) {
        int ticks = this.ticks(stack, user);
        if (ticks == 0) {
            return false;
        }

        if (ticks == INDEFINITE_USE_DURATION) {
            ItemUtils.startUsingInstantly(world, user, hand);
        } else {
            user.itematic$startUsingHand(hand, ticks);
        }

        return true;
    }
}
