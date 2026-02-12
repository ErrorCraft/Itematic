package net.errorcraft.itematic.component.type;

import com.mojang.serialization.Codec;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;

public record UseRemainderDataComponent(ItemStack remainder) {
    public static final Codec<UseRemainderDataComponent> CODEC = ItemStack.CODEC.xmap(
        UseRemainderDataComponent::new,
        UseRemainderDataComponent::remainder
    );
    public static final PacketCodec<RegistryByteBuf, UseRemainderDataComponent> PACKET_CODEC = ItemStack.PACKET_CODEC.xmap(
        UseRemainderDataComponent::new,
        UseRemainderDataComponent::remainder
    );

    public ItemStack convert(ItemStack stack, int oldCount, boolean inCreativeMode, RemainderConsumer inserter) {
        if (inCreativeMode) {
            return stack;
        }

        if (stack.getCount() >= oldCount) {
            return stack;
        }

        ItemStack remainder = this.remainder.copy();
        if (stack.isEmpty()) {
            return remainder;
        }

        inserter.apply(remainder);
        return stack;
    }

    @FunctionalInterface
    public interface RemainderConsumer {
        void apply(ItemStack stack);
    }
}
