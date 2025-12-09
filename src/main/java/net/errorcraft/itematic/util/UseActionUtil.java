package net.errorcraft.itematic.util;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.UseAction;
import net.minecraft.util.function.ValueLists;

import java.util.function.IntFunction;

public class UseActionUtil {
    public static final Codec<UseAction> CODEC = StringIdentifiable.createCodec(UseAction::values);
    private static final IntFunction<UseAction> INT_TO_ACTION = ValueLists.createIdToValueFunction(UseAction::ordinal, UseAction.values(), ValueLists.OutOfBoundsHandling.ZERO);
    public static final PacketCodec<ByteBuf, UseAction> PACKET_CODEC = PacketCodecs.indexed(INT_TO_ACTION, UseAction::ordinal);

    private UseActionUtil() {}
}
