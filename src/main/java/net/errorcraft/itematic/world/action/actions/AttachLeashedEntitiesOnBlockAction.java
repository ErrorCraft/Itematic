package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.item.LeadItem;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public record AttachLeashedEntitiesOnBlockAction() implements Action<AttachLeashedEntitiesOnBlockAction> {
    public static final AttachLeashedEntitiesOnBlockAction INSTANCE = new AttachLeashedEntitiesOnBlockAction();
    public static final MapCodec<AttachLeashedEntitiesOnBlockAction> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public ActionType<AttachLeashedEntitiesOnBlockAction> type() {
        return ActionTypes.ATTACH_LEASHED_ENTITIES_ON_BLOCK;
    }

    @Override
    public boolean execute(ActionContext context) {
        BlockPos pos = context.blockPos(ActionContextParameter.TARGET);
        ServerWorld world = context.world();
        if (!world.getBlockState(pos).isIn(BlockTags.FENCES)) {
            return false;
        }
        context.player(ActionContextParameter.THIS)
            .ifPresent(player -> LeadItem.attachHeldMobsToBlock(player, world, pos));
        return true;
    }
}
