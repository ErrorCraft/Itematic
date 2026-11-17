package net.errorcraft.itematic.world.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BrushItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public record BlockBrusher(int sweepCount) {
    public static final Codec<BlockBrusher> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ExtraCodecs.POSITIVE_INT.fieldOf("sweep_count").forGetter(BlockBrusher::sweepCount)
    ).apply(instance, BlockBrusher::new));
    public static final StreamCodec<ByteBuf, BlockBrusher> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.VAR_INT, BlockBrusher::sweepCount,
        BlockBrusher::new
    );
    public static final ScopedValue<Integer> USED_TICKS = ScopedValue.newInstance();
    public static final ScopedValue<BlockBrusher> BRUSH = ScopedValue.newInstance();
    private static final BrushItem DUMMY = new BrushItem(new Item.Properties());

    public void brush(ItemStack stack, Level level, LivingEntity user, int usedTicks) {
        ScopedValue
            .where(USED_TICKS, usedTicks)
            .where(BRUSH, this)
            .run(() -> DUMMY.onUseTick(level, user, stack, 1));
    }
}
