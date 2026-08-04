package net.errorcraft.itematic.entity.initializer.initializers;

import net.errorcraft.itematic.entity.initializer.EntityInitializer;
import net.errorcraft.itematic.util.context.ItematicContextParameters;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jetbrains.annotations.Nullable;

public record DecorationEntityInitializer<T extends HangingEntity>(Creator<T> creator) implements EntityInitializer<T> {
    public static <T extends HangingEntity> EntityInitializer<T> of(Creator<T> creator) {
        return new DecorationEntityInitializer<>(creator);
    }

    @Override
    public T create(ActionContext context, EntitySpawnReason reason) {
        BlockPos pos = context.get(ItematicContextParameters.INTERACTED_POSITION, BlockPos::containing);
        if (pos == null) {
            return null;
        }

        Direction facing = context.getOrDefault(ItematicContextParameters.SIDE, Direction.UP);
        if (!this.mayPlace(context, pos, facing)) {
            return null;
        }

        T entity = this.creator.create(context.world(), pos, facing);
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
        if (context.world().isOutsideBuildHeight(pos)) {
            return false;
        }

        Player player = context.get(LootContextParams.THIS_ENTITY, Player.class);
        ItemStack usedStack = context.getOrDefault(LootContextParams.TOOL, ItemStack.EMPTY);
        return player == null || player.mayUseItemAt(pos, facing, usedStack);
    }

    @FunctionalInterface
    public interface Creator<T extends HangingEntity> {
        @Nullable
        T create(Level world, BlockPos pos, Direction facing);
    }
}
