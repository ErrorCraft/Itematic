package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.ItemStackUtil;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.ItemStackTarget;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LodestoneTrackerComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;

import java.util.Optional;

public record SetItemPointerLocationAction(ItemStackTarget stack, PositionTarget position) implements Action<SetItemPointerLocationAction> {
    public static final MapCodec<SetItemPointerLocationAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        ItemStackTarget.CODEC.optionalFieldOf("stack", ItemStackTarget.TOOL).forGetter(SetItemPointerLocationAction::stack),
        PositionTarget.CODEC.fieldOf("position").forGetter(SetItemPointerLocationAction::position)
    ).apply(instance, SetItemPointerLocationAction::new));

    public static SetItemPointerLocationAction of(ItemStackTarget stack, PositionTarget position) {
        return new SetItemPointerLocationAction(stack, position);
    }

    @Override
    public ActionType<SetItemPointerLocationAction> type() {
        return ActionTypes.SET_ITEM_POINTER_LOCATION;
    }

    @Override
    public boolean execute(ActionContext context) {
        ItemStack stack = this.stack.get(context);
        if (ItemStackUtil.isNullOrEmpty(stack)) {
            return false;
        }

        BlockPos pos = context.get(this.position.parameter(), BlockPos::ofFloored);
        if (pos == null) {
            return false;
        }

        stack.set(
            DataComponentTypes.LODESTONE_TRACKER,
            new LodestoneTrackerComponent(
                Optional.of(GlobalPos.create(context.world().getRegistryKey(), pos)),
                true
            )
        );
        return true;
    }
}
