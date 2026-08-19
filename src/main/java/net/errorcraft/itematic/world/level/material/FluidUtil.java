package net.errorcraft.itematic.world.level.material;

import net.errorcraft.itematic.util.context.ItematicContextKeys;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jspecify.annotations.Nullable;

public class FluidUtil {
    private FluidUtil() {}

    @Nullable
    public static BlockPos getPlacementPosition(ActionContext context, PositionTarget position) {
        BlockPos pos = context.get(position.contextParam(), BlockPos::containing);
        if (pos == null) {
            return null;
        }

        if (!mayPlaceAt(context, pos)) {
            return null;
        }

        return pos;
    }

    private static boolean mayPlaceAt(ActionContext context, BlockPos pos) {
        Entity placer = context.get(LootContextParams.THIS_ENTITY);
        if (placer == null) {
            return true;
        }

        if (!context.world().mayInteract(placer, pos)) {
            return false;
        }

        Direction direction = context.getOrDefault(ItematicContextKeys.SIDE, Direction.UP);
        return !(placer instanceof Player player) ||
            player.mayUseItemAt(
                pos.relative(direction),
                direction,
                context.getOrDefault(LootContextParams.TOOL, ItemStack.EMPTY)
            );
    }
}
