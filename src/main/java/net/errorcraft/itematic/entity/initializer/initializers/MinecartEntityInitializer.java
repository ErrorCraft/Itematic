package net.errorcraft.itematic.entity.initializer.initializers;

import net.errorcraft.itematic.entity.initializer.EntityInitializer;
import net.errorcraft.itematic.util.context.ItematicContextParameters;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.RailShape;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public record MinecartEntityInitializer<T extends AbstractMinecartEntity>(EntityType<T> type) implements EntityInitializer<T> {
    @Override
    public T create(ActionContext context, SpawnReason reason) {
        ServerWorld world = context.world();
        BlockPos pos = context.getBlockPos(ItematicContextParameters.INTERACTED_POSITION);
        if (pos == null) {
            return null;
        }

        BlockState blockState = world.getBlockState(pos);
        if (!blockState.isIn(BlockTags.RAILS)) {
            return null;
        }

        RailShape railShape = blockState.getBlock() instanceof AbstractRailBlock railBlock ? blockState.get(railBlock.getShapeProperty()) : RailShape.NORTH_SOUTH;
        double verticalOffset = railShape.isAscending() ? 0.5d : 0.0d;
        ItemStack stack = context.getOrDefault(LootContextParameters.TOOL, ItemStack.EMPTY);
        T entity = AbstractMinecartEntity.create(
            world,
            pos.getX() + 0.5d,
            pos.getY() + verticalOffset + 0.0625d,
            pos.getZ() + 0.5d,
            this.type,
            SpawnReason.SPAWN_ITEM_USE,
            stack,
            context.get(LootContextParameters.THIS_ENTITY, PlayerEntity.class)
        );
        if (entity == null) {
            return null;
        }

        if (AbstractMinecartEntity.areMinecartImprovementsEnabled(world)) {
            for (Entity otherEntity : world.getOtherEntities(null, entity.getBoundingBox())) {
                if (otherEntity instanceof AbstractMinecartEntity) {
                    return null;
                }
            }
        }

        Text customName = stack.get(DataComponentTypes.CUSTOM_NAME);
        if (customName != null) {
            entity.setCustomName(customName);
        }

        return entity;
    }
}
