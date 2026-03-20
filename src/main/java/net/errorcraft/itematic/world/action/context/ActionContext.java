package net.errorcraft.itematic.world.action.context;

import net.errorcraft.itematic.item.ItemStackConsumer;
import net.errorcraft.itematic.util.PositionUtil;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AutomaticItemPlacementContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ActionContext {
    private final Builder builder;
    private final ServerWorld world;
    protected final Map<ActionContextParameter, Entity> entities;
    protected final Map<ActionContextParameter, Vec3d> positions;
    protected Direction side = Direction.UP;
    protected ItemStack stack = ItemStack.EMPTY;
    protected ItemStackConsumer resultStackConsumer;
    protected EquipmentSlot slot;

    public ActionContext(ServerWorld world) {
        this(world, new HashMap<>(), new HashMap<>());
    }

    private ActionContext(ServerWorld world, Map<ActionContextParameter, Entity> entities, Map<ActionContextParameter, Vec3d> positions) {
        this.world = world;
        this.entities = entities;
        this.positions = positions;
        this.builder = null;
    }

    private ActionContext(Builder builder) {
        this.builder = builder;
        this.world = builder.world;
        this.entities = new HashMap<>(builder.entities);
        this.positions = new HashMap<>(builder.positions);
        this.side = builder.side;
        this.stack = builder.stack;
        this.resultStackConsumer = builder.resultStackConsumer;
        this.slot = builder.slot;
    }

    public static Builder builder(ServerWorld world) {
        return new Builder(world);
    }

    public static Builder builder(ServerWorld world, ItemStack stack, ItemStackConsumer resultStackConsumer) {
        return builder(world)
            .stack(stack)
            .resultStackConsumer(resultStackConsumer);
    }

    public static Builder builder(ServerWorld world, ItemStack stack, ItemStackConsumer resultStackConsumer, EquipmentSlot slot) {
        return builder(world, stack, resultStackConsumer)
            .slot(slot);
    }

    public static Builder builder(ServerWorld world, ItemStack stack, ItemStackConsumer resultStackConsumer, Hand hand) {
        if (hand == null) {
            return builder(world, stack, resultStackConsumer);
        }

        return builder(world, stack, resultStackConsumer, LivingEntity.getSlotForHand(hand));
    }

    public ItemUsageContext createItemUsageContext(ActionContextParameter position) {
        return new ItemUsageContext(
            this.world,
            this.player(ActionContextParameter.THIS).orElse(null),
            this.hand(),
            this.stack(),
            new BlockHitResult(
                this.position(position),
                this.side(),
                this.blockPos(position),
                false
            )
        );
    }

    public ItemPlacementContext createItemPlacementContext(ActionContextParameter position, RegistryEntry<Block> block) {
        if (this.entity(ActionContextParameter.THIS).isPresent()) {
            ItemPlacementContext placementContext = new ItemPlacementContext(this.createItemUsageContext(position));
            return block.value().itematic$placementContext(placementContext);
        }
        BlockPos pos = this.blockPos(position);
        Direction useSide = this.world.isAir(pos.down()) ? this.side : Direction.UP;
        return new AutomaticItemPlacementContext(this.world, pos, this.side, this.stack, useSide);
    }

    public ServerWorld world() {
        return this.world;
    }

    public Optional<Entity> entity(ActionContextParameter parameter) {
        return Optional.ofNullable(this.entities.get(parameter));
    }

    public Optional<LivingEntity> livingEntity(ActionContextParameter parameter) {
        return this.entity(parameter).map(entity -> {
            if (entity instanceof LivingEntity livingEntity) {
                return livingEntity;
            }
            return null;
        });
    }

    public Optional<PlayerEntity> player(ActionContextParameter parameter) {
        return this.entity(parameter).map(entity -> {
            if (entity instanceof PlayerEntity player) {
                return player;
            }
            return null;
        });
    }

    public Vec3d position(ActionContextParameter parameter) {
        Vec3d position = this.positions.get(parameter);
        if (position == null) {
            return this.world.getSpawnPos().toCenterPos();
        }
        return position;
    }

    public BlockPos blockPos(ActionContextParameter parameter) {
        return BlockPos.ofFloored(this.position(parameter));
    }

    public Direction side() {
        return this.side;
    }

    public ItemStack stack() {
        return this.stack;
    }

    public ItemStackConsumer resultStackConsumer() {
        return this.resultStackConsumer;
    }

    public void setResultStack(ItemStack stack) {
        if (this.resultStackConsumer == null) {
            return;
        }
        this.resultStackConsumer.set(stack);
    }

    public Optional<EquipmentSlot> slot() {
        return Optional.ofNullable(this.slot);
    }

    public Hand hand() {
        if (this.slot == null) {
            return null;
        }
        return switch (this.slot) {
            case MAINHAND -> Hand.MAIN_HAND;
            case OFFHAND -> Hand.OFF_HAND;
            default -> null;
        };
    }

    public static class Builder {
        private final ServerWorld world;
        private final Map<ActionContextParameter, Entity> entities = new HashMap<>();
        private final Map<ActionContextParameter, Vec3d> positions = new HashMap<>();
        private Direction side = Direction.UP;
        private ItemStack stack = ItemStack.EMPTY;
        private ItemStackConsumer resultStackConsumer;
        private EquipmentSlot slot;

        private Builder(ServerWorld world) {
            this.world = world;
        }

        public ActionContext build() {
            return new ActionContext(this);
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
            return this.position(parameter, Vec3d.ofBottomCenter(position));
        }

        public Builder position(ActionContextParameter parameter, Position position) {
            return this.position(parameter, PositionUtil.vec3d(position));
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

        public Builder resultStackConsumer(ItemStackConsumer resultStackConsumer) {
            this.resultStackConsumer = resultStackConsumer;
            return this;
        }

        public Builder slot(EquipmentSlot slot) {
            this.slot = slot;
            return this;
        }

        public Builder hand(Hand hand) {
            this.slot = LivingEntity.getSlotForHand(hand);
            return this;
        }
    }
}
