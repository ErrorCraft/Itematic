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
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Map;

public record ModifyBlockStateAction(ActionContextParameter position, Map<String, String> properties) implements Action {
    public static final Codec<ModifyBlockStateAction> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ActionContextParameter.CODEC.fieldOf("position").forGetter(ModifyBlockStateAction::position),
        Codec.unboundedMap(Codec.STRING, Codec.STRING).fieldOf("properties").forGetter(ModifyBlockStateAction::properties)
    ).apply(instance, ModifyBlockStateAction::new));

    @Override
    public ActionType<?> type() {
        return ActionTypes.MODIFY_BLOCK_STATE;
    }

    @Override
    public boolean execute(ActionContext context) {
        BlockPos pos = context.blockPos(this.position);
        BlockState state = context.world().getBlockState(pos);
        StateManager<Block, BlockState> stateManager = state.getBlock().getStateManager();

        boolean applied = false;
        for (String key : this.properties.keySet()) {
            Property<?> property = stateManager.getProperty(key);
            if (property == null) {
                continue;
            }
            state = BlockStateUtil.with(state, property, this.properties.get(key));
            applied = true;
        }
        if (applied) {
            context.world().setBlockState(pos, state);
        }
        return applied;
    }

    public static Builder builder(ActionContextParameter position) {
        return new Builder(position);
    }

    public static final class Builder {
        private final ActionContextParameter position;
        private final Map<String, String> properties = new HashMap<>();

        private Builder(ActionContextParameter position) {
            this.position = position;
        }

        public ModifyBlockStateAction build() {
            return new ModifyBlockStateAction(this.position, this.properties);
        }

        public <T extends Comparable<T>> Builder property(Property<T> property, T value) {
            this.properties.put(property.getName(), property.name(value));
            return this;
        }
    }
}
