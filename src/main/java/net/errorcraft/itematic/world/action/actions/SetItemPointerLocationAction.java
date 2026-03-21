package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.NewActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LodestoneTrackerComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.util.math.GlobalPos;

import java.util.Optional;

public record SetItemPointerLocationAction(PositionTarget position) implements Action<SetItemPointerLocationAction> {
    public static final MapCodec<SetItemPointerLocationAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        PositionTarget.CODEC.fieldOf("position").forGetter(SetItemPointerLocationAction::position)
    ).apply(instance, SetItemPointerLocationAction::new));

    public static SetItemPointerLocationAction of(PositionTarget position) {
        return new SetItemPointerLocationAction(position);
    }

    @Override
    public ActionType<SetItemPointerLocationAction> type() {
        return ActionTypes.SET_ITEM_POINTER_LOCATION;
    }

    @Override
    public boolean execute(NewActionContext context) {
        ItemStack stack = context.get(LootContextParameters.TOOL);
        if (stack == null || stack.isEmpty()) {
            return false;
        }

        ItemStack resultStack = stack.split(1);
        resultStack.set(
            DataComponentTypes.LODESTONE_TRACKER,
            new LodestoneTrackerComponent(
                Optional.of(GlobalPos.create(
                    context.world().getRegistryKey(),
                    context.getBlockPos(this.position.parameter())
                )),
                true
            )
        );
        context.exchangeStack(resultStack);
        return true;
    }
}
