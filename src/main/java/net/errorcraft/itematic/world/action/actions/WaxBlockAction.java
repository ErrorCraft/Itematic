package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.HoneycombItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public record WaxBlockAction(PositionTarget position) implements Action<WaxBlockAction> {
    public static final MapCodec<WaxBlockAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        PositionTarget.CODEC.fieldOf("position").forGetter(WaxBlockAction::position)
    ).apply(instance, WaxBlockAction::new));

    public static WaxBlockAction of(PositionTarget position) {
        return new WaxBlockAction(position);
    }

    @Override
    public ActionType<WaxBlockAction> type() {
        return ActionTypes.WAX_BLOCK;
    }

    @Override
    public boolean execute(ActionContext context) {
        BlockPos pos = context.get(this.position.contextParam(), BlockPos::containing);
        if (pos == null) {
            return false;
        }

        Level world = context.world();
        return HoneycombItem.getWaxed(world.getBlockState(pos))
            .map(state -> {
                Entity entity = context.get(LootContextParams.THIS_ENTITY);
                world.setBlock(pos, state, Block.UPDATE_ALL_IMMEDIATE);
                world.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(entity, state));
                world.levelEvent(LevelEvent.PARTICLES_AND_SOUND_WAX_ON, pos, 0);
                return true;
            })
            .orElse(false);
    }
}
