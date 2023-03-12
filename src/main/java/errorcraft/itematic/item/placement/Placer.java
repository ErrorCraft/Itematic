package errorcraft.itematic.item.placement;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class Placer {
    protected final ItemStack stack;
    protected final World world;
    protected final BlockPos blockPos;
    protected final BlockState blockState;
    protected final PlayerEntity player;

    protected Placer(ItemStack stack, World world, BlockPos blockPos, BlockState blockState, PlayerEntity player) {
        this.stack = stack;
        this.world = world;
        this.blockPos = blockPos;
        this.blockState = blockState;
        this.player = player;
    }

    public abstract TypedActionResult<ItemStack> place();
}
