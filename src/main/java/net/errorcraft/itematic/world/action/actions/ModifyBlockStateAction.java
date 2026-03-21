package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.block.BlockStateUtil;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.NewActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;
import java.util.Map;

public record ModifyBlockStateAction(PositionTarget position, Map<String, String> properties, boolean pushEntitiesUpwards) implements Action<ModifyBlockStateAction> {
    public static final MapCodec<ModifyBlockStateAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        PositionTarget.CODEC.fieldOf("position").forGetter(ModifyBlockStateAction::position),
        Codec.unboundedMap(Codec.STRING, Codec.STRING).fieldOf("properties").forGetter(ModifyBlockStateAction::properties),
        Codec.BOOL.optionalFieldOf("push_entities_upwards", false).forGetter(ModifyBlockStateAction::pushEntitiesUpwards)
    ).apply(instance, ModifyBlockStateAction::new));

    public static Builder builder(PositionTarget position) {
        return new Builder(position);
    }

    @Override
    public ActionType<ModifyBlockStateAction> type() {
        return ActionTypes.MODIFY_BLOCK_STATE;
    }

    @Override
    public boolean execute(NewActionContext context) {
        ServerWorld world = context.world();
        Vec3d pos = context.get(this.position.parameter());
        if (pos == null) {
            return false;
        }

        BlockPos blockPos = BlockPos.ofFloored(pos);
        BlockState currentState = world.getBlockState(blockPos);
        BlockState newState = this.modifyBlockState(currentState);
        if (newState == currentState) {
            return false;
        }

        if (this.pushEntitiesUpwards) {
            Block.pushEntitiesUpBeforeBlockChange(currentState, newState, world, blockPos);
        }

        world.setBlockState(blockPos, newState);
        return true;
    }

    private BlockState modifyBlockState(BlockState currentState) {
        BlockState newState = currentState;
        StateManager<Block, BlockState> stateManager = currentState.getBlock().getStateManager();
        for (String key : this.properties.keySet()) {
            Property<?> property = stateManager.getProperty(key);
            if (property == null) {
                continue;
            }

            newState = BlockStateUtil.with(newState, property, this.properties.get(key));
        }

        return newState;
    }

    public static final class Builder {
        private final PositionTarget position;
        private final Map<String, String> properties = new HashMap<>();
        private boolean pushEntitiesUpwards = false;

        private Builder(PositionTarget position) {
            this.position = position;
        }

        public ModifyBlockStateAction build() {
            return new ModifyBlockStateAction(this.position, this.properties, this.pushEntitiesUpwards);
        }

        public <T extends Comparable<T>> Builder property(Property<T> property, T value) {
            this.properties.put(property.getName(), property.name(value));
            return this;
        }

        public Builder pushEntitiesUpwards() {
            this.pushEntitiesUpwards = true;
            return this;
        }
    }
}
