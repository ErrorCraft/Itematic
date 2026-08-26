package net.errorcraft.itematic.world.entity.initializer.initializers;

import net.errorcraft.itematic.util.context.ItematicContextKeys;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.entity.initializer.EntityInitializer;
import net.errorcraft.itematic.world.item.ItemStacks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jspecify.annotations.Nullable;

public record HangingEntityInitializer<T extends HangingEntity>(Creator<T> creator) implements EntityInitializer<T> {
    public static <T extends HangingEntity> EntityInitializer<T> of(Creator<T> creator) {
        return new HangingEntityInitializer<>(creator);
    }

    @Override
    public @Nullable T create(ActionContext context, EntitySpawnReason reason) {
        BlockPos pos = context.get(ItematicContextKeys.INTERACTED_POSITION, BlockPos::containing);
        if (pos == null) {
            return null;
        }

        Direction facing = context.getOrDefault(ItematicContextKeys.SIDE, Direction.UP);
        if (!this.mayPlace(context, pos, facing)) {
            return null;
        }

        T entity = this.creator.create(context.level(), pos, facing);
        if (entity == null) {
            return null;
        }

        if (!entity.survives()) {
            return null;
        }

        entity.playPlacementSound();
        return entity;
    }

    private boolean mayPlace(ActionContext context, BlockPos pos, Direction facing) {
        if (context.level().isOutsideBuildHeight(pos)) {
            return false;
        }

        Player player = context.get(LootContextParams.THIS_ENTITY, Player.class);
        ItemStack usedStack = context.getOrDefault(LootContextParams.TOOL, ItemStacks::fromItemInstance, ItemStack.EMPTY);
        return player == null || player.mayUseItemAt(pos, facing, usedStack);
    }

    @FunctionalInterface
    public interface Creator<T extends HangingEntity> {
        @Nullable
        T create(Level level, BlockPos pos, Direction facing);
    }
}
