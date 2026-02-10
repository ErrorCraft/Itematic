package net.errorcraft.itematic.component.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.SharedConstants;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.dynamic.Codecs;

public record UseCooldownDataComponent(float seconds) {
    public static final Codec<UseCooldownDataComponent> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
        Codecs.POSITIVE_FLOAT.fieldOf("seconds").forGetter(UseCooldownDataComponent::seconds)
    ).apply(instance, UseCooldownDataComponent::new));
    public static final PacketCodec<ByteBuf, UseCooldownDataComponent> PACKET_CODEC = PacketCodecs.FLOAT.xmap(
        UseCooldownDataComponent::new,
        UseCooldownDataComponent::seconds
    );

    public int ticks() {
        return (int)(this.seconds * SharedConstants.TICKS_PER_SECOND);
    }

    public void set(ItemStack stack, LivingEntity user) {
        if (user instanceof PlayerEntity player) {
            player.getItemCooldownManager().set(stack.getItem(), this.ticks());
        }
    }
}
