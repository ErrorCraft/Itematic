package net.errorcraft.itematic.mixin.core.dispenser;

import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.ShearsDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ShearsDispenseItemBehavior.class)
public interface ShearsDispenseItemBehaviorAccessor {
    @Invoker("tryShearBeehive")
    static boolean tryShearBeehive(ServerLevel level, ItemStack tool, BlockPos pos) {
        throw new AssertionError();
    }

    @Invoker("tryShearEntity")
    static boolean tryShearEntity(ServerLevel level, BlockPos pos, ItemStack tool) {
        throw new AssertionError();
    }
}
