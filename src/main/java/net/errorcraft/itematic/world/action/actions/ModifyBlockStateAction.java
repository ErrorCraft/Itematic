package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import java.util.HashMap;
import java.util.Map;

public record ModifyBlockStateAction(PositionTarget position, BlockItemStateProperties properties, boolean pushEntitiesUpwards) implements Action<ModifyBlockStateAction> {
    public static final MapCodec<ModifyBlockStateAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        PositionTarget.CODEC.fieldOf("position").forGetter(ModifyBlockStateAction::position),
        BlockItemStateProperties.CODEC.fieldOf("properties").forGetter(ModifyBlockStateAction::properties),
        Codec.BOOL.optionalFieldOf("push_entities_upwards", false).forGetter(ModifyBlockStateAction::pushEntitiesUpwards)
    ).apply(instance, ModifyBlockStateAction::new));

    public static Builder builder(PositionTarget position) {
        return new Builder(position);
    }

    @Override
    public ActionType<ModifyBlockStateAction> type() {
        return ActionType.MODIFY_BLOCK_STATE;
    }

    @Override
    public boolean execute(ActionContext context) {
        BlockPos pos = context.get(this.position.contextParam(), BlockPos::containing);
        if (pos == null) {
            return false;
        }

        Level world = context.level();
        BlockState currentState = world.getBlockState(pos);
        BlockState newState = this.properties.apply(currentState);
        if (newState == currentState) {
            return false;
        }

        if (this.pushEntitiesUpwards) {
            Block.pushEntitiesUp(currentState, newState, world, pos);
        }

        world.setBlockAndUpdate(pos, newState);
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
                new BlockItemStateProperties(this.properties),
                this.pushEntitiesUpwards
            );
        }

        public <T extends Comparable<T>> Builder property(Property<T> property, T value) {
            this.properties.put(property.getName(), property.getName(value));
            return this;
        }

        public Builder pushEntitiesUpwards() {
            this.pushEntitiesUpwards = true;
            return this;
        }
    }
}
