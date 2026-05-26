package net.errorcraft.itematic.fluid;

import net.errorcraft.itematic.util.context.ItematicContextParameters;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public class FluidUtil {
    private FluidUtil() {}

    @Nullable
    public static BlockPos getPlacementPosition(ActionContext context, PositionTarget position) {
        BlockPos pos = context.get(position.parameter(), BlockPos::ofFloored);
        if (pos == null) {
            return null;
        }

        if (!canPlaceAt(context, pos)) {
            return null;
        }

        return pos;
    }

    private static boolean canPlaceAt(ActionContext context, BlockPos pos) {
        Entity placer = context.get(LootContextParameters.THIS_ENTITY);
        if (placer == null) {
            return true;
        }

        if (!context.world().canEntityModifyAt(placer, pos)) {
            return false;
        }

        Direction direction = context.getOrDefault(ItematicContextParameters.SIDE, Direction.UP);
        return !(placer instanceof PlayerEntity player) ||
            player.canPlaceOn(
                pos.offset(direction),
                direction,
                context.getOrDefault(LootContextParameters.TOOL, ItemStack.EMPTY)
            );
    }
}
