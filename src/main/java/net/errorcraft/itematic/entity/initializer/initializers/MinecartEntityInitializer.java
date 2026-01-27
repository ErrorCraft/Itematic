package net.errorcraft.itematic.entity.initializer.initializers;

import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.entity.initializer.EntityInitializer;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.RailShape;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public record MinecartEntityInitializer<T extends AbstractMinecartEntity>(EntityType<T> type, Creator<T> creator) implements EntityInitializer<T> {
    @Override
    public T create(ActionContext context, SpawnReason reason) {
        return this.create(context.world(), context.blockPos(ActionContextParameter.TARGET), context.stack());
    }

    public static <U extends AbstractMinecartEntity> MapCodec<EntityInitializer<U>> createCodec(EntityType<U> type, Creator<U> creator) {
        return MapCodec.unit(new MinecartEntityInitializer<>(type, creator));
    }

    private T create(ServerWorld world, BlockPos pos, ItemStack stack) {
        BlockState blockState = world.getBlockState(pos);
        if (!blockState.isIn(BlockTags.RAILS)) {
            return null;
        }
        RailShape railShape = blockState.getBlock() instanceof AbstractRailBlock railBlock ? blockState.get(railBlock.getShapeProperty()) : RailShape.NORTH_SOUTH;
        double verticalOffset = railShape.isAscending() ? 0.5d : 0.0d;
        T entity = this.creator.create(world, pos.getX() + 0.5d, pos.getY() + verticalOffset + 0.0625d, pos.getZ() + 0.5d);
        Text customName = stack.get(DataComponentTypes.CUSTOM_NAME);
        if (customName != null) {
            entity.setCustomName(customName);
        }
        return entity;
    }

    @FunctionalInterface
    public interface Creator<T extends AbstractMinecartEntity> {
        T create(World world, double x, double y, double z);
    }
}
