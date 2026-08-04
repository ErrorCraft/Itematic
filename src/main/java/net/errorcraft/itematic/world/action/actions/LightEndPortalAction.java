package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EndPortalFrameBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.pattern.BlockPattern;

public record LightEndPortalAction(PositionTarget position) implements Action<LightEndPortalAction> {
    public static final MapCodec<LightEndPortalAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        PositionTarget.CODEC.fieldOf("position").forGetter(LightEndPortalAction::position)
    ).apply(instance, LightEndPortalAction::new));
    private static final int PORTAL_SIZE = 3;

    public static LightEndPortalAction of(PositionTarget position) {
        return new LightEndPortalAction(position);
    }

    @Override
    public ActionType<LightEndPortalAction> type() {
        return ActionTypes.LIGHT_END_PORTAL;
    }

    @Override
    public boolean execute(ActionContext context) {
        BlockPos pos = context.get(this.position.contextParam(), BlockPos::containing);
        if (pos == null) {
            return false;
        }

        Level world = context.world();
        BlockPattern.BlockPatternMatch result = EndPortalFrameBlock.getOrCreatePortalShape()
            .find(world, pos);
        if (result == null) {
            return false;
        }

        BlockPos endPortalStartPos = result.getFrontTopLeft().offset(-PORTAL_SIZE, 0, -PORTAL_SIZE);
        for (int x = 0; x < PORTAL_SIZE; x++) {
            for (int z = 0; z < PORTAL_SIZE; z++) {
                world.setBlock(endPortalStartPos.offset(x, 0, z), Blocks.END_PORTAL.defaultBlockState(), Block.UPDATE_CLIENTS);
            }
        }

        world.globalLevelEvent(LevelEvent.SOUND_END_PORTAL_SPAWN, endPortalStartPos.offset(1, 0, 1), 0);
        return true;
    }
}
