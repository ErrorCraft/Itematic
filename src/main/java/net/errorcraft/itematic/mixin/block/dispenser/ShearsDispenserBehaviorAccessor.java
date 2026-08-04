package net.errorcraft.itematic.mixin.block.dispenser;

import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.ShearsDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ShearsDispenseItemBehavior.class)
public interface ShearsDispenserBehaviorAccessor {
    @Invoker("tryShearBeehive")
    static boolean tryShearBlock(ServerLevel world, ItemStack itemStack, BlockPos blockPos) {
        throw new AssertionError();
    }

    @Invoker("tryShearEntity")
    static boolean tryShearEntity(ServerLevel world, BlockPos pos, ItemStack shears) {
        throw new AssertionError();
    }
}
