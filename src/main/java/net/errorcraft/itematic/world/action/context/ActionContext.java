package net.errorcraft.itematic.world.action.context;

import net.errorcraft.itematic.loot.context.ItematicLootContextTypes;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameters;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public record ActionContext(ServerWorld world, Optional<Entity> target, Vec3d position, Direction side, ItemStack stack) {
    public static Builder builder(ServerWorld world) {
        return new Builder(world);
    }

    public static Builder builder(ServerWorld world, ItemStack stack) {
        return new Builder(world).stack(stack);
    }

    public Optional<PlayerEntity> player() {
        if (this.target.isEmpty()) {
            return Optional.empty();
        }
        if (this.target.get() instanceof PlayerEntity playerEntity) {
            return Optional.of(playerEntity);
        }
        return Optional.empty();
    }

    public BlockPos blockPos() {
        return BlockPos.ofFloored(this.position);
    }

    public LootContext lootContext() {
        LootContextParameterSet set = new LootContextParameterSet.Builder(this.world)
            .add(LootContextParameters.THIS_ENTITY, this.target.orElse(null))
            .add(LootContextParameters.ORIGIN, this.position)
            .add(LootContextParameters.TOOL, this.stack)
            .build(ItematicLootContextTypes.ACTION);
        return new LootContext.Builder(set).build(Optional.empty());
    }

    public static ActionContext of(ServerWorld world, @Nullable Entity target, BlockPos pos, Direction side, ItemStack stack) {
        return new ActionContext(world, Optional.ofNullable(target), Vec3d.ofBottomCenter(pos), side, stack);
    }

    public static class Builder {
        private final ServerWorld world;
        private final Map<ActionContextParameter, Entity> entities = new HashMap<>();
        private final Map<ActionContextParameter, Vec3d> positions = new HashMap<>();
        private Direction side = Direction.UP;
        private ItemStack stack = ItemStack.EMPTY;

        private Builder(ServerWorld world) {
            this.world = world;
        }

        public ActionContext build(ActionContextParameters parameters) {
            return new ActionContext(this.world, this.entity(parameters.entity()), this.position(parameters.position()), this.side, this.stack);
        }

        public LootContext createLootContext(ActionContextParameters parameters) {
            LootContextParameterSet set = new LootContextParameterSet.Builder(this.world)
                .add(LootContextParameters.THIS_ENTITY, this.entities.get(parameters.entity()))
                .add(LootContextParameters.ORIGIN, this.position(parameters.position()))
                .add(LootContextParameters.TOOL, this.stack)
                .build(ItematicLootContextTypes.ACTION);
            return new LootContext.Builder(set).build(Optional.empty());
        }

        public Builder entity(ActionContextParameter parameter, Entity entity) {
            this.entities.put(parameter, entity);
            return this;
        }

        public Builder entityPosition(ActionContextParameter parameter, Entity entity) {
            this.entities.put(parameter, entity);
            if (entity != null) {
                this.positions.put(parameter, entity.getPos());
            }
            return this;
        }

        public Builder position(ActionContextParameter parameter, BlockPos position) {
            return this.position(parameter, position.toCenterPos());
        }

        public Builder position(ActionContextParameter parameter, Vec3d position) {
            if (position == null) {
                throw new IllegalArgumentException("Position was null");
            }
            this.positions.put(parameter, position);
            return this;
        }

        public Builder side(Direction side) {
            if (side == null) {
                throw new IllegalArgumentException("Side was null");
            }
            this.side = side;
            return this;
        }

        public Builder stack(ItemStack stack) {
            if (stack == null) {
                throw new IllegalArgumentException("Item stack was null");
            }
            this.stack = stack;
            return this;
        }

        private Optional<Entity> entity(ActionContextParameter parameter) {
            return Optional.ofNullable(this.entities.get(parameter));
        }

        private Vec3d position(ActionContextParameter parameter) {
            Vec3d position = this.positions.get(parameter);
            if (position == null) {
                return this.world.getSpawnPos().toCenterPos();
            }
            return position;
        }
    }
}
