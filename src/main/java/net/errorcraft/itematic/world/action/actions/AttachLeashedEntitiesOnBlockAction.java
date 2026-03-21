package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.NewActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.LeadItem;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public record AttachLeashedEntitiesOnBlockAction(PositionTarget position) implements Action<AttachLeashedEntitiesOnBlockAction> {
    public static final MapCodec<AttachLeashedEntitiesOnBlockAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        PositionTarget.CODEC.fieldOf("position").forGetter(AttachLeashedEntitiesOnBlockAction::position)
    ).apply(instance, AttachLeashedEntitiesOnBlockAction::new));

    public static AttachLeashedEntitiesOnBlockAction of(PositionTarget position) {
        return new AttachLeashedEntitiesOnBlockAction(position);
    }

    @Override
    public ActionType<AttachLeashedEntitiesOnBlockAction> type() {
        return ActionTypes.ATTACH_LEASHED_ENTITIES_ON_BLOCK;
    }

    @Override
    public boolean execute(NewActionContext context) {
        BlockPos pos = context.getBlockPos(this.position.parameter());
        ServerWorld world = context.world();
        if (!world.getBlockState(pos).isIn(BlockTags.FENCES)) {
            return false;
        }

        if (context.get(LootContextParameters.THIS_ENTITY) instanceof PlayerEntity player) {
            return LeadItem.attachHeldMobsToBlock(player, world, pos).isAccepted();
        }

        return false;
    }
}
