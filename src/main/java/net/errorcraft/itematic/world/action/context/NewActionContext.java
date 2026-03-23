package net.errorcraft.itematic.world.action.context;

import net.errorcraft.itematic.item.placement.block.picker.BlockPicker;
import net.errorcraft.itematic.util.context.ItematicContextParameters;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AutomaticItemPlacementContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootWorldContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.function.CommandFunctionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.context.ContextParameter;
import net.minecraft.util.context.ContextParameterMap;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class NewActionContext {
    private final ServerWorld world;
    private final ContextParameterMap parameters;
    private final ItemStackExchanger stackExchanger;

    private NewActionContext(ServerWorld world, ContextParameterMap parameters, ItemStackExchanger stackExchanger) {
        this.world = world;
        this.parameters = parameters;
        this.stackExchanger = stackExchanger;
    }

    public static Builder builder(ServerWorld world) {
        return new Builder(world);
    }

    public Builder extend() {
        return new Builder(this.world, this.parameters);
    }

    public ServerWorld world() {
        return this.world;
    }

    public ItemStackExchanger stackExchanger() {
        return this.stackExchanger;
    }

    public <T> boolean has(ContextParameter<T> parameter) {
        return this.parameters.contains(parameter);
    }

    @Nullable
    public <T> T get(ContextParameter<T> parameter) {
        return this.parameters.getNullable(parameter);
    }

    @Nullable
    public <T, U extends T> U get(ContextParameter<T> parameter, Class<U> clazz) {
        T value = this.get(parameter);
        if (clazz.isInstance(value)) {
            return clazz.cast(value);
        }

        return null;
    }

    public <T> T getOrDefault(ContextParameter<T> parameter, T defaultValue) {
        return this.parameters.getOrDefault(parameter, defaultValue);
    }

    @Nullable
    public BlockPos getBlockPos(ContextParameter<Vec3d> parameter) {
        Vec3d pos = this.get(parameter);
        if (pos == null) {
            return null;
        }

        return BlockPos.ofFloored(pos);
    }

    public ItemStack resultStack() {
        return this.stackExchanger.result();
    }

    public void exchangeStack(ItemStack stack) {
        this.stackExchanger.exchange(stack);
    }

    public LootContext lootContext() {
        LootWorldContext context = new LootWorldContext(
            this.world,
            this.parameters,
            Map.of(),
            0.0f
        );
        return new LootContext.Builder(context).build(Optional.empty());
    }

    public ServerCommandSource commandSource(CommandFunctionManager functionManager) {
        ServerCommandSource source = functionManager.getScheduledCommandSource();
        Entity entity = this.get(LootContextParameters.THIS_ENTITY);
        if (entity != null) {
            source = source.withEntity(entity);
        }

        Vec3d position = this.get(LootContextParameters.ORIGIN);
        if (position != null) {
            source = source.withPosition(position);
        }

        return source;
    }


    @Nullable
    public ItemPlacementContext blockPlaceContext(PositionTarget position, BlockPicker<?> block) {
        Vec3d pos = this.get(position.parameter());
        if (pos == null) {
            return null;
        }

        Direction side = this.get(ItematicContextParameters.SIDE);
        if (side == null) {
            return null;
        }

        ItemPlacementContext placeContext = this.blockPlaceContext(pos, side);
        return block.placementContext(placeContext);
    }

    private ItemPlacementContext blockPlaceContext(Vec3d pos, Direction side) {
        BlockPos blockPos = BlockPos.ofFloored(pos);
        Entity entity = this.get(LootContextParameters.THIS_ENTITY);
        if (entity != null) {
            return new ItemPlacementContext(
                this.world,
                entity instanceof PlayerEntity player ? player : null,
                this.get(ItematicContextParameters.HAND),
                this.getOrDefault(LootContextParameters.TOOL, ItemStack.EMPTY),
                new BlockHitResult(
                    pos,
                    side,
                    blockPos,
                    false
                )
            );
        }

        Direction useSide = this.world.isAir(blockPos.down()) ? side : Direction.UP;
        return new AutomaticItemPlacementContext(
            this.world,
            blockPos,
            side,
            this.getOrDefault(LootContextParameters.TOOL, ItemStack.EMPTY),
            useSide
        );
    }

    public static class Builder {
        private final ServerWorld world;
        private ItemStackExchanger stackExchanger = ItemStackExchanger.EMPTY;
        private final ContextParameterMap.Builder parameters = new ContextParameterMap.Builder();

        private Builder(ServerWorld world) {
            this.world = world;
        }

        private Builder(ServerWorld world, ContextParameterMap parameters) {
            this(world);
            this.parameters.itematic$copy(parameters);
        }

        public NewActionContext build() {
            return new NewActionContext(
                this.world,
                this.parameters.itematic$build(),
                this.stackExchanger
            );
        }

        public Builder stackExchanger(ItemStackExchanger stackExchanger) {
            this.stackExchanger = stackExchanger;
            return this;
        }

        public Builder possibleStackExchanger(@Nullable LivingEntity consumingEntity, ItemStack initialStack) {
            if (consumingEntity == null) {
                return this;
            }

            return this.stackExchanger(consumingEntity, initialStack);
        }

        public Builder stackExchanger(LivingEntity consumingEntity, ItemStack initialStack) {
            this.stackExchanger = ItemStackExchanger.forEntity(consumingEntity, initialStack);
            return this;
        }

        public Builder stackExchanger(Direction side, Vec3d pos, ItemStack initialStack) {
            this.stackExchanger = ItemStackExchanger.forDispenser(this.world, side, pos, initialStack);
            return this;
        }

        public <T> Builder add(ContextParameter<T> parameter, T value) {
            this.parameters.add(parameter, value);
            return this;
        }

        public <T> Builder addOptional(ContextParameter<T> parameter, @Nullable T value) {
            this.parameters.addNullable(parameter, value);
            return this;
        }

        public <T, U> Builder addOptional(ContextParameter<T> parameter, @Nullable U value, Function<@NotNull U, T> mapper) {
            if (value == null) {
                return this;
            }

            this.addOptional(parameter, mapper.apply(value));
            return this;
        }
    }
}
