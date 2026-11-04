package net.errorcraft.itematic.world.level.block;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.codec.RegistryCodecs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.block.Block;

public class Blocks {
    public static final Codec<Holder<Block>> CODEC = RegistryCodecs.holder(Registries.BLOCK);
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<Block>> STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.BLOCK);

    private Blocks() {}
}
