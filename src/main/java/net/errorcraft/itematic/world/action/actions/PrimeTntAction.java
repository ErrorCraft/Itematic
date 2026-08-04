package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.mixin.block.TntBlockAccessor;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public record PrimeTntAction(PositionTarget position) implements Action<PrimeTntAction> {
    public static final MapCodec<PrimeTntAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        PositionTarget.CODEC.fieldOf("position").forGetter(PrimeTntAction::position)
    ).apply(instance, PrimeTntAction::new));

    public static PrimeTntAction of(PositionTarget position) {
        return new PrimeTntAction(position);
    }

    @Override
    public ActionType<PrimeTntAction> type() {
        return ActionTypes.PRIME_TNT;
    }

    @Override
    public boolean execute(ActionContext context) {
        if (!(context.world() instanceof ServerLevel world)) {
            return false;
        }

        BlockPos pos = context.get(this.position.contextParam(), BlockPos::containing);
        if (pos == null) {
            return false;
        }

        Player player = context.get(LootContextParams.THIS_ENTITY, Player.class);
        if (TntBlockAccessor.primeTnt(world, pos, player)) {
            world.removeBlock(pos, false);
            return true;
        }

        if (player != null && !world.getGameRules().get(GameRules.TNT_EXPLODES)) {
            player.displayClientMessage(Component.translatable("block.minecraft.tnt.disabled"), true);
        }

        return false;
    }
}
