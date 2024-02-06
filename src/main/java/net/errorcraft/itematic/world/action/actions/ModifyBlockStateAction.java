package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.block.BlockStateUtil;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Property;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Map;

public record ModifyBlockStateAction(ActionContextParameter position, Map<String, String> properties, boolean pushEntitiesUpwards) implements Action<ModifyBlockStateAction> {
    public static final Codec<ModifyBlockStateAction> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ActionContextParameter.CODEC.fieldOf("position").forGetter(ModifyBlockStateAction::position),
        Codec.unboundedMap(Codec.STRING, Codec.STRING).fieldOf("properties").forGetter(ModifyBlockStateAction::properties),
        Codecs.createStrictOptionalFieldCodec(Codec.BOOL, "push_entities_upwards", false).forGetter(ModifyBlockStateAction::pushEntitiesUpwards)
    ).apply(instance, ModifyBlockStateAction::new));

    @Override
    public ActionType<ModifyBlockStateAction> type() {
        return ActionTypes.MODIFY_BLOCK_STATE;
    }

    @Override
    public boolean execute(ActionContext context) {
        ServerWorld world = context.world();
        BlockPos pos = context.blockPos(this.position);
        BlockState state = world.getBlockState(pos);
        BlockState newState = state;
        StateManager<Block, BlockState> stateManager = state.getBlock().getStateManager();
        for (String key : this.properties.keySet()) {
            Property<?> property = stateManager.getProperty(key);
            if (property == null) {
                continue;
            }
            newState = BlockStateUtil.with(newState, property, this.properties.get(key));
        }
        if (state == newState) {
            return false;
        }
        if (this.pushEntitiesUpwards) {
            Block.pushEntitiesUpBeforeBlockChange(state, newState, world, pos);
        }
        world.setBlockState(pos, newState);
        return true;
    }

    public static Builder builder(ActionContextParameter position) {
        return new Builder(position);
    }

    public static final class Builder {
        private final ActionContextParameter position;
        private final Map<String, String> properties = new HashMap<>();
        private boolean pushEntitiesUpwards = false;

        private Builder(ActionContextParameter position) {
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
