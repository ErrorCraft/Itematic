package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.mixin.block.TntBlockAccessor;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;

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
        BlockPos pos = context.get(this.position.parameter(), BlockPos::ofFloored);
        if (pos == null) {
            return false;
        }

        ServerWorld world = context.world();
        PlayerEntity player = context.get(LootContextParameters.THIS_ENTITY, PlayerEntity.class);
        if (TntBlockAccessor.primeTnt(world, pos, player)) {
            world.removeBlock(pos, false);
            return true;
        }

        if (player != null && !world.getGameRules().getBoolean(GameRules.TNT_EXPLODES)) {
            player.sendMessage(Text.translatable("block.minecraft.tnt.disabled"), true);
        }

        return false;
    }
}
