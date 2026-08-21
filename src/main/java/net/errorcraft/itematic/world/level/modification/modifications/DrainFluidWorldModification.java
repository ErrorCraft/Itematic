package net.errorcraft.itematic.world.level.modification.modifications;

import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.errorcraft.itematic.world.item.placement.fluid.FluidDrainer;
import net.errorcraft.itematic.world.level.modification.WorldModification;
import net.errorcraft.itematic.world.level.modification.WorldModificationType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;

import java.util.Optional;

public class DrainFluidWorldModification implements WorldModification {
    public static final DrainFluidWorldModification INSTANCE = new DrainFluidWorldModification();
    public static final MapCodec<DrainFluidWorldModification> CODEC = MapCodec.unit(INSTANCE);

    private DrainFluidWorldModification() {}

    @Override
    public WorldModificationType<?> type() {
        return WorldModificationType.DRAIN_FLUID;
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
