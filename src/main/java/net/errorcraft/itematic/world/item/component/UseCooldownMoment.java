package net.errorcraft.itematic.world.item.component;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;

import java.util.function.IntFunction;

public enum UseCooldownMoment implements StringRepresentable {
    ON_START(0, "on_start"),
    ON_END(1, "on_end");

    public static final Codec<UseCooldownMoment> CODEC = StringRepresentable.fromEnum(UseCooldownMoment::values);
    private static final IntFunction<UseCooldownMoment> BY_ID = ByIdMap.continuous(
        UseCooldownMoment::id,
        values(),
        ByIdMap.OutOfBoundsStrategy.ZERO
    );
    public static final StreamCodec<ByteBuf, UseCooldownMoment> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, UseCooldownMoment::id);
    public static final UseCooldownMoment DEFAULT = ON_END;

    private final int id;
    private final String name;

    UseCooldownMoment(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int id() {
        return this.id;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
