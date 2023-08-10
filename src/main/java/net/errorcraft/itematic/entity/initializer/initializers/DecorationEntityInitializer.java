package net.errorcraft.itematic.entity.initializer.initializers;

import net.errorcraft.itematic.entity.initializer.EntityInitializer;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public record DecorationEntityInitializer<T extends AbstractDecorationEntity>(EntityType<T> type, Creator<T> creator, PlacementChecker checker) implements EntityInitializer<T> {
    @Override
    public T create(ActionContext context) {
        Direction side = context.side();
        return this.create(context.world(), context.player().orElse(null), context.blockPos(), side, context.stack());
    }

    private T create(ServerWorld world, PlayerEntity player, BlockPos pos, Direction facing, ItemStack stack) {
        if (player == null || !this.checker.mayPlace(player, pos, facing, stack)) {
            return null;
        }
        T entity = this.creator.create(world, pos, facing);
        if (entity == null) {
            return null;
        }
        EntityType.loadFromEntityNbt(world, player, entity, stack.getNbt());
        if (!entity.canStayAttached()) {
            return null;
        }
        entity.onPlace();
        return entity;
    }

    public static EntityInitializer<PaintingEntity> createPainting(EntityType<PaintingEntity> type) {
        return create(type, ((world, pos, facing) -> PaintingEntity.placePainting(world, pos, facing).orElse(null)), DecorationEntityInitializer::mayPlacePainting);
    }

    public static <U extends ItemFrameEntity> EntityInitializer<U> createItemFrame(EntityType<U> type, Creator<U> creator) {
        return create(type, creator, DecorationEntityInitializer::mayPlaceItemFrame);
    }

    private static <U extends AbstractDecorationEntity> EntityInitializer<U> create(EntityType<U> type, Creator<U> creator, PlacementChecker checker) {
        return new DecorationEntityInitializer<>(type, creator, checker);
    }

    private static boolean mayPlacePainting(PlayerEntity player, BlockPos pos, Direction facing, ItemStack stack) {
        return !facing.getAxis().isVertical() && player.canPlaceOn(pos, facing, stack);
    }

    private static boolean mayPlaceItemFrame(PlayerEntity player, BlockPos pos, Direction facing, ItemStack stack) {
        return !player.getWorld().isOutOfHeightLimit(pos) && player.canPlaceOn(pos, facing, stack);
    }

    @FunctionalInterface
    public interface Creator<T extends AbstractDecorationEntity> {
        @Nullable
        T create(World world, BlockPos pos, Direction facing);
    }

    @FunctionalInterface
    public interface PlacementChecker {
        boolean mayPlace(PlayerEntity player, BlockPos pos, Direction facing, ItemStack stack);
    }
}
