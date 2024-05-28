package net.errorcraft.itematic.mixin.fluid;

import net.errorcraft.itematic.access.fluid.FluidAccess;
import net.minecraft.fluid.Fluid;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Fluid.class)
public class FluidExtender implements FluidAccess {
}
