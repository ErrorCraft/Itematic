package net.errorcraft.itematic.world.modification.type;

import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.item.placement.fluid.FluidDrainer;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.errorcraft.itematic.world.modification.WorldModification;
import net.errorcraft.itematic.world.modification.WorldModificationType;
import net.errorcraft.itematic.world.modification.WorldModificationTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.world.RaycastContext;

import java.util.Optional;

public class DrainFluidWorldModification implements WorldModification {
    public static final DrainFluidWorldModification INSTANCE = new DrainFluidWorldModification();
    public static final MapCodec<DrainFluidWorldModification> CODEC = MapCodec.unit(INSTANCE);

    private DrainFluidWorldModification() {}

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
