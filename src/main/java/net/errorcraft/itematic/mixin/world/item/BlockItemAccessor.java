package net.errorcraft.itematic.mixin.world.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockItem.class)
public interface BlockItemAccessor {
    @Invoker("updateBlockEntityComponents")
    static void updateBlockEntityComponents(Level level, BlockPos pos, ItemStack stack) {
        throw new AssertionError();
    }
}
