package net.errorcraft.itematic.world.action.context;

import net.errorcraft.itematic.util.context.ItematicContextKeys;
import net.errorcraft.itematic.world.item.placement.block.picker.BlockPicker;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.ServerFunctionManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.context.ContextKey;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class ActionContext {
    private final Level level;
    private final ContextMap parameters;
    private final ItemStackExchanger stackExchanger;

    private ActionContext(Level level, ContextMap parameters, ItemStackExchanger stackExchanger) {
        this.level = level;
        this.parameters = parameters;
        this.stackExchanger = stackExchanger;
    }

    public static Builder builder(Level level) {
        return new Builder(level);
    }

    public Builder extend() {
        return new Builder(this);
    }

    public Level level() {
        return this.level;
    }

    @Nullable
    public <T> T get(ContextKey<T> parameter) {
        return this.parameters.getOptional(parameter);
    }

    @Nullable
    public <T, U extends T> U get(ContextKey<T> parameter, Class<U> clazz) {
        T value = this.get(parameter);
        if (clazz.isInstance(value)) {
            return clazz.cast(value);
        }

        return null;
    }

    @Nullable
    public <T, U> U get(ContextKey<T> parameter, Function<T, U> mapper) {
        T value = this.get(parameter);
        if (value == null) {
            return null;
        }

        return mapper.apply(value);
    }

    public <T> T getOrDefault(ContextKey<T> parameter, T defaultValue) {
        return this.parameters.getOrDefault(parameter, defaultValue);
    }

    public ItemStack resultStack() {
        return this.stackExchanger.result();
    }

    public void exchangeStack(ItemStack stack) {
        this.stackExchanger.exchange(stack);
    }

    @Nullable
    public LootContext lootContext() {
        if (!(this.level instanceof ServerLevel serverLevel)) {
            return null;
        }

        LootParams params = new LootParams(
            serverLevel,
            this.parameters,
            Map.of(),
            0.0f
        );
        return new LootContext.Builder(params).create(Optional.empty());
    }

    public CommandSourceStack commandSource(ServerFunctionManager functionManager, Optional<LootContext.EntityTarget> entity, Optional<PositionTarget> position) {
        CommandSourceStack source = functionManager.getGameLoopSender();
        source = entity.map(LootContext.EntityTarget::contextParam)
            .map(this::get)
            .map(source::withEntity)
            .orElse(source);
        source = position.map(PositionTarget::contextParam)
            .map(this::get)
            .map(source::withPosition)
            .orElse(source);
        return source;
    }

    @Nullable
    public BlockPlaceContext blockPlaceContext(PositionTarget position, BlockPicker<?> block) {
        Vec3 pos = this.get(position.contextParam());
        if (pos == null) {
            return null;
        }

        Direction side = this.get(ItematicContextKeys.SIDE);
        if (side == null) {
            return null;
        }

        BlockPlaceContext placeContext = this.blockPlaceContext(pos, side);
        return block.placementContext(placeContext);
    }

    private BlockPlaceContext blockPlaceContext(Vec3 pos, Direction side) {
        BlockPos blockPos = BlockPos.containing(pos);
        Entity entity = this.get(LootContextParams.THIS_ENTITY);
        if (entity != null) {
            return new BlockPlaceContext(
                this.level,
                entity instanceof Player player ? player : null,
                this.getOrDefault(ItematicContextKeys.HAND, InteractionHand.MAIN_HAND),
                this.getOrDefault(LootContextParams.TOOL, ItemStack.EMPTY),
                new BlockHitResult(
                    pos,
                    side,
                    blockPos,
                    false
                )
            );
        }

        Direction useSide = this.level.isEmptyBlock(blockPos.below()) ? side : Direction.UP;
        return new DirectionalPlaceContext(
            this.level,
            blockPos,
            side,
            this.getOrDefault(LootContextParams.TOOL, ItemStack.EMPTY),
            useSide
        );
    }

    public static class Builder {
        private final Level level;
        private ItemStackExchanger stackExchanger = ItemStackExchanger.EMPTY;
        private final ContextMap.Builder parameters = new ContextMap.Builder();

        private Builder(Level level) {
            this.level = level;
        }

        private Builder(ActionContext currentContext) {
            this.level = currentContext.level;
            this.stackExchanger = currentContext.stackExchanger;
            this.parameters.itematic$copy(currentContext.parameters);
        }

        public ActionContext build() {
            return new ActionContext(
                this.level,
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

        public Builder stackExchanger(Direction side, Vec3 pos, ItemStack initialStack) {
            this.stackExchanger = ItemStackExchanger.forDispenser(this.level, side, pos, initialStack);
            return this;
        }

        public <T> Builder add(ContextKey<T> parameter, T value) {
            this.parameters.withParameter(parameter, value);
            return this;
        }

        public <T> Builder addOptional(ContextKey<T> parameter, @Nullable T value) {
            this.parameters.withOptionalParameter(parameter, value);
            return this;
        }

        public <T, U> Builder addOptional(ContextKey<T> parameter, @Nullable U value, Function<U, T> mapper) {
            if (value == null) {
                return this;
            }

            return this.addOptional(parameter, mapper.apply(value));
        }
    }
}
