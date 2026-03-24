package net.errorcraft.itematic.entity.initializer.initializers;

import net.errorcraft.itematic.entity.initializer.EntityInitializer;
import net.errorcraft.itematic.util.context.ItematicContextParameters;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public record DecorationEntityInitializer<T extends AbstractDecorationEntity>(Creator<T> creator, PlacementChecker checker) implements EntityInitializer<T> {
    public static EntityInitializer<PaintingEntity> ofPainting() {
        return new DecorationEntityInitializer<>(
            (world, pos, facing) -> PaintingEntity.placePainting(world, pos, facing).orElse(null),
            DecorationEntityInitializer::mayPlacePainting
        );
    }

    public static <T extends ItemFrameEntity> EntityInitializer<T> ofItemFrame(Creator<T> creator) {
        return new DecorationEntityInitializer<>(
            creator,
            DecorationEntityInitializer::mayPlaceItemFrame
        );
    }

    @Override
    public T create(ActionContext context, SpawnReason reason) {
        BlockPos pos = context.getBlockPos(ItematicContextParameters.INTERACTED_POSITION);
        if (pos == null) {
            return null;
        }

        Direction side = context.getOrDefault(ItematicContextParameters.SIDE, Direction.UP);
        return this.create(
            context.world(),
            context.get(LootContextParameters.THIS_ENTITY, PlayerEntity.class),
            pos,
            side,
            context.getOrDefault(LootContextParameters.TOOL, ItemStack.EMPTY)
        );
    }

    private T create(ServerWorld world, PlayerEntity player, BlockPos pos, Direction facing, ItemStack stack) {
        if (player == null || !this.checker.mayPlace(player, pos, facing, stack)) {
            return null;
        }

        T entity = this.creator.create(world, pos, facing);
        if (entity == null) {
            return null;
        }

        EntityType.loadFromEntityNbt(
            world,
            player,
            entity,
            stack.getOrDefault(DataComponentTypes.ENTITY_DATA, NbtComponent.DEFAULT)
        );
        if (!entity.canStayAttached()) {
            return null;
        }

        entity.onPlace();
        return entity;
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
