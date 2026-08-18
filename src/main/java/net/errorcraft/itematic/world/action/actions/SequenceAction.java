package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.sequence.handler.SequenceHandler;

public record SequenceAction(SequenceHandler<?> handler) implements Action<SequenceAction> {
    public static final MapCodec<SequenceAction> CODEC = SequenceHandler.CODEC.xmap(SequenceAction::new, SequenceAction::handler);

    public static SequenceAction of(SequenceHandler.Builder<?, ?> builder) {
        return new SequenceAction(builder.build());
    }

    public static SequenceAction of(SequenceHandler<?> handler) {
        return new SequenceAction(handler);
    }

    @Override
    public ActionType<SequenceAction> type() {
        return ActionTypes.SEQUENCE;
    }

    @Override
    public boolean execute(ActionContext context) {
        try {
            return this.handler.handle(context);
        } catch (StackOverflowError e) {
            return false;
        }
    }
}
