package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.errorcraft.itematic.world.item.component.Bucket;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public record UseBucketAction(PositionTarget position) implements Action<UseBucketAction> {
    public static final MapCodec<UseBucketAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        PositionTarget.CODEC.fieldOf("position").forGetter(UseBucketAction::position)
    ).apply(instance, UseBucketAction::new));

    public static UseBucketAction of(PositionTarget position) {
        return new UseBucketAction(position);
    }

    @Override
    public ActionType<UseBucketAction> type() {
        return ActionType.USE_BUCKET;
    }

    @Override
    public boolean execute(ActionContext context) {
        ItemInstance item = context.get(LootContextParams.TOOL);
        if (item == null) {
            return false;
        }

        Bucket bucket = item.get(ItematicDataComponents.BUCKET);
        if (bucket == null) {
            return false;
        }

        return bucket.use(context, this.position, false, null);
    }
}
