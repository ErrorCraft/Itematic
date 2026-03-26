package net.errorcraft.itematic.item.placement;

import net.errorcraft.itematic.item.ItemResult;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public abstract class Placer {
    protected final ItemStack stack;
    protected final ItemStackExchanger stackExchanger;
    protected final World world;
    protected final BlockPos blockPos;
    protected final BlockState blockState;
    @Nullable
    protected final PlayerEntity player;

    protected Placer(ItemStack stack, ItemStackExchanger stackExchanger, World world, BlockPos blockPos, BlockState blockState, @Nullable PlayerEntity player) {
        this.stack = stack;
        this.stackExchanger = stackExchanger;
        this.world = world;
        this.blockPos = blockPos;
        this.blockState = blockState;
        this.player = player;
    }

    public abstract ItemResult place();

    protected void tryDecrementStack() {
        this.stack.decrementUnlessCreative(1, this.player);
    }
}
