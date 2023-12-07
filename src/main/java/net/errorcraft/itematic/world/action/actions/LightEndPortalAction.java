package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.EndPortalFrameBlock;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldEvents;

public record LightEndPortalAction(ActionContextParameter position) implements Action {
    public static final Codec<LightEndPortalAction> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ActionContextParameter.CODEC.fieldOf("position").forGetter(LightEndPortalAction::position)
    ).apply(instance, LightEndPortalAction::new));
    private static final int PORTAL_SIZE = 3;

    @Override
    public ActionType<?> type() {
        return ActionTypes.LIGHT_END_PORTAL;
    }

    @Override
    public boolean execute(ActionContext context) {
        ServerWorld world = context.world();
        BlockPos pos = context.blockPos(this.position);
        BlockPattern.Result result = EndPortalFrameBlock.getCompletedFramePattern()
            .searchAround(world, pos);
        if (result == null) {
            return false;
        }

        BlockPos endPortalStartPos = result.getFrontTopLeft().add(-PORTAL_SIZE, 0, -PORTAL_SIZE);
        for (int x = 0; x < PORTAL_SIZE; x++) {
            for (int z = 0; z < PORTAL_SIZE; z++) {
                world.setBlockState(endPortalStartPos.add(x, 0, z), Blocks.END_PORTAL.getDefaultState(), Block.NOTIFY_LISTENERS);
            }
        }

        world.syncGlobalEvent(WorldEvents.END_PORTAL_OPENED, endPortalStartPos.add(1, 0, 1), 0);
        return true;
    }

    public static LightEndPortalAction of(ActionContextParameter position) {
        return new LightEndPortalAction(position);
    }
}
