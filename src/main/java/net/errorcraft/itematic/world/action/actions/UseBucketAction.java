package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;

public record UseBucketAction(ActionContextParameter position) implements Action<UseBucketAction> {
    public static final MapCodec<UseBucketAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        ActionContextParameter.CODEC.fieldOf("position").forGetter(UseBucketAction::position)
    ).apply(instance, UseBucketAction::new));

    public static UseBucketAction of(ActionContextParameter position) {
        return new UseBucketAction(position);
    }

    @Override
    public ActionType<UseBucketAction> type() {
        return ActionTypes.USE_BUCKET;
    }

    @Override
    public boolean execute(ActionContext context) {
        ItemStack stack = context.stack();
        return stack.itematic$getComponent(ItemComponentTypes.BUCKET)
            .map(c -> {
                BlockPos pos = context.blockPos(this.position);
                BlockHitResult hitResult = new BlockHitResult(pos.toCenterPos(), context.side(), pos, true);
                return c.place(context.world(), context.player(ActionContextParameter.THIS).orElse(null), context.hand(), stack, context.resultStackConsumer(), hitResult)
                    .isAccepted();
            })
            .orElse(false);
    }
}
