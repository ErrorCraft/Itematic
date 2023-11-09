package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.block.TntBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public record PrimeTntAction(ActionContextParameter position) implements Action {
    public static final Codec<PrimeTntAction> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ActionContextParameter.CODEC.fieldOf("position").forGetter(PrimeTntAction::position)
    ).apply(instance, PrimeTntAction::new));

    @Override
    public ActionType<?> type() {
        return ActionTypes.PRIME_TNT;
    }

    @Override
    public boolean execute(ActionContext context) {
        BlockPos pos = context.blockPos(this.position);
        ServerWorld world = context.world();
        if (world.getBlockState(pos).getBlock() instanceof TntBlock) {
            TntBlock.primeTnt(world, pos);
            world.removeBlock(pos, false);
            return true;
        }
        return false;
    }
}
