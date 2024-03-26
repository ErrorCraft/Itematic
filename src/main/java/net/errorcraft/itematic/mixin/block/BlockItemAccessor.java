package net.errorcraft.itematic.mixin.block;

import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockItem.class)
public interface BlockItemAccessor {
    @Invoker("copyComponentsToBlockEntity")
    static void copyComponentsToBlockEntity(World world, BlockPos pos, ItemStack stack) {
        throw new AssertionError();
    }
}
