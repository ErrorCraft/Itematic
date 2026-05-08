package net.errorcraft.itematic.item.bucket.modification.type;

import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.item.bucket.modification.WorldModification;
import net.errorcraft.itematic.item.bucket.modification.WorldModificationType;
import net.errorcraft.itematic.item.bucket.modification.WorldModificationTypes;
import net.errorcraft.itematic.item.placement.fluid.FluidDrainer;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.item.ItemStack;
import net.minecraft.world.RaycastContext;

import java.util.Optional;

public class DrainFluid implements WorldModification {
    public static final DrainFluid INSTANCE = new DrainFluid();
    public static final MapCodec<DrainFluid> CODEC = MapCodec.unit(INSTANCE);

    private DrainFluid() {}

    @Override
    public WorldModificationType<?> type() {
        return WorldModificationTypes.DRAIN_FLUID;
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
    public RaycastContext.FluidHandling fluidHandling() {
        return RaycastContext.FluidHandling.SOURCE_ONLY;
    }
}
