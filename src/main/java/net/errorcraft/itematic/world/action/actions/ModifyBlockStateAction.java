package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public record ModifyBlockStateAction(PositionTarget position, BlockStateComponent properties, boolean pushEntitiesUpwards) implements Action<ModifyBlockStateAction> {
    public static final MapCodec<ModifyBlockStateAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        PositionTarget.CODEC.fieldOf("position").forGetter(ModifyBlockStateAction::position),
        BlockStateComponent.CODEC.fieldOf("properties").forGetter(ModifyBlockStateAction::properties),
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
    public boolean execute(ActionContext context) {
        World world = context.world();
        BlockPos pos = context.getBlockPos(this.position.parameter());
        if (pos == null) {
            return false;
        }

        BlockState currentState = world.getBlockState(pos);
        BlockState newState = this.properties.applyToState(currentState);
        if (newState == currentState) {
            return false;
        }

        if (this.pushEntitiesUpwards) {
            Block.pushEntitiesUpBeforeBlockChange(currentState, newState, world, pos);
        }

        world.setBlockState(pos, newState);
        return true;
    }

    public static final class Builder {
        private final PositionTarget position;
        private final Map<String, String> properties = new HashMap<>();
        private boolean pushEntitiesUpwards = false;

        private Builder(PositionTarget position) {
            this.position = position;
        }

        public ModifyBlockStateAction build() {
            return new ModifyBlockStateAction(
                this.position,
                new BlockStateComponent(this.properties),
                this.pushEntitiesUpwards
            );
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
