package net.errorcraft.itematic.component.type;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.use.provider.IntegerProvider;
import net.errorcraft.itematic.item.use.provider.providers.ConstantIntegerProvider;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.util.Hand;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.World;

public record UseDurationDataComponent(IntegerProvider ticks) {
    private static final Codec<UseDurationDataComponent> FULL_CODEC = IntegerProvider.CODEC.xmap(
        UseDurationDataComponent::new,
        UseDurationDataComponent::ticks
    );
    public static final Codec<UseDurationDataComponent> CODEC = Codec.withAlternative(
        FULL_CODEC,
        Codecs.POSITIVE_INT,
        UseDurationDataComponent::new
    );
    public static final PacketCodec<RegistryByteBuf, UseDurationDataComponent> PACKET_CODEC = IntegerProvider.PACKET_CODEC.xmap(
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

    public boolean startUsing(World world, PlayerEntity user, Hand hand, ItemStack stack) {
        int ticks = this.ticks(stack, user);
        if (ticks == 0) {
            return false;
        }

        if (ticks == INDEFINITE_USE_DURATION) {
            ItemUsage.consumeHeldItem(world, user, hand);
        } else {
            user.itematic$startUsingHand(hand, ticks);
        }

        return true;
    }
}
