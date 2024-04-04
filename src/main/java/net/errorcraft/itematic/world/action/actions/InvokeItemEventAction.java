package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.event.ItemEvent;
import net.errorcraft.itematic.registry.ItematicRegistries;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;

public record InvokeItemEventAction(ItemEvent event) implements Action<InvokeItemEventAction> {
    public static final MapCodec<InvokeItemEventAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        ItematicRegistries.ITEM_EVENT.getCodec().fieldOf("event").forGetter(InvokeItemEventAction::event)
    ).apply(instance, InvokeItemEventAction::new));

    public static InvokeItemEventAction of(ItemEvent event) {
        return new InvokeItemEventAction(event);
    }

    @Override
    public ActionType<InvokeItemEventAction> type() {
        return ActionTypes.INVOKE_ITEM_EVENT;
    }

    @Override
    public boolean execute(ActionContext context) {
        return context.stack().itematic$invokeEvent(this.event, context);
    }
}
