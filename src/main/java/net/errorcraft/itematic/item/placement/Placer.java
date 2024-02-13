package net.errorcraft.itematic.item.placement;

import net.errorcraft.itematic.item.ItemStackConsumer;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public abstract class Placer {
    protected final ItemStack stack;
    protected final ItemStackConsumer resultStackConsumer;
    protected final World world;
    protected final BlockPos blockPos;
    protected final BlockState blockState;
    @Nullable
    protected final PlayerEntity player;

    protected Placer(ItemStack stack, ItemStackConsumer resultStackConsumer, World world, BlockPos blockPos, BlockState blockState, @Nullable PlayerEntity player) {
        this.stack = stack;
        this.resultStackConsumer = resultStackConsumer;
        this.world = world;
        this.blockPos = blockPos;
        this.blockState = blockState;
        this.player = player;
    }

    public abstract ActionResult place();

    protected void tryDecrementStack() {
        this.stack.decrementUnlessCreative(1, this.player);
    }
}
