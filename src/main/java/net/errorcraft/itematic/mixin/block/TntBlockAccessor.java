package net.errorcraft.itematic.mixin.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TntBlock;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(TntBlock.class)
public interface TntBlockAccessor {
    @Invoker("prime")
    static boolean primeTnt(Level world, BlockPos pos, @Nullable LivingEntity igniter) {
        throw new AssertionError();
    }
}
