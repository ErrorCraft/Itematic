package net.errorcraft.itematic.world.level.material;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.codec.RegistryCodecs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.material.Fluid;

public class Fluids {
    public static final Codec<Holder<Fluid>> CODEC = RegistryCodecs.holder(Registries.FLUID);
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<Fluid>> STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.FLUID);

    private Fluids() {}
}
