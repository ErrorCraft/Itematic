package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameters;

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
    public boolean execute(ActionContext context) {
        ItemStack stack = context.getOrDefault(LootContextParameters.TOOL, ItemStack.EMPTY);
        return stack.itematic$getBehavior(ItemComponentTypes.BUCKET)
            .map(bucket -> bucket.use(context, this.position, false))
            .orElse(false);
    }
}
