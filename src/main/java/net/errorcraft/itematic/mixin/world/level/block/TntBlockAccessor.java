package net.errorcraft.itematic.mixin.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TntBlock;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(TntBlock.class)
public interface TntBlockAccessor {
    @Invoker("prime")
    static boolean prime(Level level, BlockPos pos, @Nullable LivingEntity igniter) {
        throw new AssertionError();
    }
}
