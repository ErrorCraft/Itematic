package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.block.TntBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

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
        Vec3d pos = context.get(this.position.parameter());
        if (pos == null) {
            return false;
        }

        ServerWorld world = context.world();
        BlockPos blockPos = BlockPos.ofFloored(pos);
        TntBlock.primeTnt(world, blockPos);
        world.removeBlock(blockPos, false);
        return true;
    }
}
