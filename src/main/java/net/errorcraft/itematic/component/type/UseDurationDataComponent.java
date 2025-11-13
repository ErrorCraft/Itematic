package net.errorcraft.itematic.component.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.use.provider.IntegerProvider;
import net.errorcraft.itematic.item.use.provider.providers.ConstantIntegerProvider;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.Hand;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.World;

import java.util.Optional;

public record UseDurationDataComponent(Optional<IntegerProvider> ticks) {
    public static final UseDurationDataComponent INDEFINITE = new UseDurationDataComponent(Optional.empty());
    public static final MapCodec<UseDurationDataComponent> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        IntegerProvider.CODEC.optionalFieldOf("ticks").forGetter(UseDurationDataComponent::ticks)
    ).apply(instance, UseDurationDataComponent::new));
    public static final Codec<UseDurationDataComponent> CODEC = Codec.withAlternative(
        MAP_CODEC.codec(),
        Codecs.POSITIVE_INT,
        UseDurationDataComponent::new
    );
    public static final PacketCodec<RegistryByteBuf, UseDurationDataComponent> PACKET_CODEC = PacketCodecs.optional(IntegerProvider.PACKET_CODEC)
        .xmap(UseDurationDataComponent::new, UseDurationDataComponent::ticks);
    public static final int INDEFINITE_USE_DURATION = -1;

    public UseDurationDataComponent(IntegerProvider ticks) {
        this(Optional.of(ticks));
    }

    public UseDurationDataComponent(int ticks) {
        this(new ConstantIntegerProvider(ticks));
    }

    public int ticks(ItemStack stack, LivingEntity user) {
        return this.ticks.map(useDuration -> useDuration.get(stack, user))
            .map(useDuration -> {
                if (useDuration.isPresent()) {
                    return useDuration.getAsInt();
                }
                return 0;
            })
            .orElse(INDEFINITE_USE_DURATION);
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
