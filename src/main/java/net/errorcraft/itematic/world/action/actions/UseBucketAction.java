package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.BucketItemComponent;
import net.errorcraft.itematic.util.context.ItematicContextParameters;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.NewActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public record UseBucketAction(PositionTarget position) implements Action<UseBucketAction> {
    public static final MapCodec<UseBucketAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        PositionTarget.CODEC.fieldOf("position").forGetter(UseBucketAction::position)
    ).apply(instance, UseBucketAction::new));

    public static UseBucketAction of(PositionTarget position) {
        return new UseBucketAction(position);
    }

    @Override
    public ActionType<UseBucketAction> type() {
        return ActionTypes.USE_BUCKET;
    }

    @Override
    public boolean execute(NewActionContext context) {
        ItemStack stack = context.getOrDefault(LootContextParameters.TOOL, ItemStack.EMPTY);
        return stack.itematic$getBehavior(ItemComponentTypes.BUCKET)
            .map(bucket -> this.place(bucket, stack, context))
            .orElse(false);
    }

    private boolean place(BucketItemComponent bucket, ItemStack stack, NewActionContext context) {
        Vec3d pos = context.get(this.position.parameter());
        if (pos == null) {
            return false;
        }

        Direction side = context.get(ItematicContextParameters.SIDE);
        if (side == null) {
            return false;
        }

        Hand hand = context.getOrDefault(ItematicContextParameters.HAND, Hand.MAIN_HAND);
        BlockHitResult hitResult = new BlockHitResult(pos, side, BlockPos.ofFloored(pos), true);
        return bucket.place(
            context.world(),
            context.get(LootContextParameters.THIS_ENTITY) instanceof PlayerEntity player ? player : null,
            hand,
            stack,
            context.stackExchanger(),
            hitResult
        ).succeeds();
    }
}
