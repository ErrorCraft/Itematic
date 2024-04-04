package net.errorcraft.itematic.mixin.block.dispenser;

import net.minecraft.block.dispenser.ShearsDispenserBehavior;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ShearsDispenserBehavior.class)
public interface ShearsDispenserBehaviorAccessor {
    @Invoker("tryShearBlock")
    static boolean tryShearBlock(ServerWorld world, BlockPos pos) {
        throw new AssertionError();
    }

    @Invoker("tryShearEntity")
    static boolean tryShearEntity(ServerWorld world, BlockPos pos) {
        throw new AssertionError();
    }
}
