package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.LeadItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public record AttachLeashedEntitiesOnBlockAction(PositionTarget position) implements Action<AttachLeashedEntitiesOnBlockAction> {
    public static final MapCodec<AttachLeashedEntitiesOnBlockAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        PositionTarget.CODEC.fieldOf("position").forGetter(AttachLeashedEntitiesOnBlockAction::position)
    ).apply(instance, AttachLeashedEntitiesOnBlockAction::new));

    public static AttachLeashedEntitiesOnBlockAction of(PositionTarget position) {
        return new AttachLeashedEntitiesOnBlockAction(position);
    }

    @Override
    public ActionType<AttachLeashedEntitiesOnBlockAction> type() {
        return ActionType.ATTACH_LEASHED_ENTITIES_ON_BLOCK;
    }

    @Override
    public boolean execute(ActionContext context) {
        BlockPos pos = context.get(this.position.contextParam(), BlockPos::containing);
        if (pos == null) {
            return false;
        }

        Level level = context.level();
        if (!level.getBlockState(pos).is(BlockTags.FENCES)) {
            return false;
        }

        if (context.get(LootContextParams.THIS_ENTITY) instanceof Player player) {
            return LeadItem.bindPlayerMobs(player, level, pos).consumesAction();
        }

        return false;
    }
}
