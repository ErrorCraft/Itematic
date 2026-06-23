package net.errorcraft.itematic.entity.initializer.initializers;

import net.errorcraft.itematic.entity.initializer.EntityInitializer;
import net.errorcraft.itematic.util.context.ItematicContextParameters;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public record DecorationEntityInitializer<T extends AbstractDecorationEntity>(Creator<T> creator) implements EntityInitializer<T> {
    public static <T extends AbstractDecorationEntity> EntityInitializer<T> of(Creator<T> creator) {
        return new DecorationEntityInitializer<>(creator);
    }

    @Override
    public T create(ActionContext context, SpawnReason reason) {
        BlockPos pos = context.get(ItematicContextParameters.INTERACTED_POSITION, BlockPos::ofFloored);
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

        if (!entity.canStayAttached()) {
            return null;
        }

        entity.onPlace();
        return entity;
    }

    private boolean mayPlace(ActionContext context, BlockPos pos, Direction facing) {
        if (context.world().isOutOfHeightLimit(pos)) {
            return false;
        }

        PlayerEntity player = context.get(LootContextParameters.THIS_ENTITY, PlayerEntity.class);
        ItemStack usedStack = context.getOrDefault(LootContextParameters.TOOL, ItemStack.EMPTY);
        return player == null || player.canPlaceOn(pos, facing, usedStack);
    }

    @FunctionalInterface
    public interface Creator<T extends AbstractDecorationEntity> {
        @Nullable
        T create(World world, BlockPos pos, Direction facing);
    }
}
