package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public record TakeHoneyAction(PositionTarget position) implements Action<TakeHoneyAction> {
    public static final MapCodec<TakeHoneyAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        PositionTarget.CODEC.fieldOf("position").forGetter(TakeHoneyAction::position)
    ).apply(instance, TakeHoneyAction::new));

    public static TakeHoneyAction of(PositionTarget position) {
        return new TakeHoneyAction(position);
    }

    @Override
    public ActionType<TakeHoneyAction> type() {
        return ActionType.TAKE_HONEY;
    }

    @Override
    public boolean execute(ActionContext context) {
        BlockPos pos = context.get(this.position.contextParam(), BlockPos::containing);
        if (pos == null) {
            return false;
        }

        BlockState state = context.level().getBlockState(pos);
        if (!state.hasProperty(BeehiveBlock.HONEY_LEVEL)) {
            return false;
        }

        if (state.getValue(BeehiveBlock.HONEY_LEVEL) < BeehiveBlock.MAX_HONEY_LEVELS) {
            return false;
        }

        if (!(state.getBlock() instanceof BeehiveBlock beehiveBlock)) {
            return false;
        }

        beehiveBlock.releaseBeesAndResetHoneyLevel(
            context.level(),
            state,
            pos,
            context.get(LootContextParams.THIS_ENTITY) instanceof Player player ? player : null,
            BeehiveBlockEntity.BeeReleaseStatus.BEE_RELEASED
        );
        return true;
    }
}
