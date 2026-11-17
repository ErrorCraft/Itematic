package net.errorcraft.itematic.world.level.modification.modifications;

import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.errorcraft.itematic.world.item.placement.fluid.FluidDrainer;
import net.errorcraft.itematic.world.level.modification.WorldModification;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;

import java.util.Optional;

public class DrainFluid implements WorldModification {
    public static final DrainFluid INSTANCE = new DrainFluid();
    public static final MapCodec<DrainFluid> CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<ByteBuf, DrainFluid> STREAM_CODEC = StreamCodec.unit(INSTANCE);
    public static final Type<DrainFluid> TYPE = new Type<>(CODEC, STREAM_CODEC);

    private DrainFluid() {}

    @Override
    public Type<?> type() {
        return TYPE;
    }

    @Override
    public Optional<ItemStack> modify(ActionContext context, PositionTarget position, boolean mayOffset) {
        ItemStack drainedStack = new FluidDrainer(context, position).drain();
        if (drainedStack == null) {
            return Optional.empty();
        }

        return Optional.of(drainedStack);
    }

    @Override
    public ClipContext.Fluid fluidHandling() {
        return ClipContext.Fluid.SOURCE_ONLY;
    }
}
