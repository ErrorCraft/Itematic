package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionEntry;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.sequence.handler.SequenceHandler;
import net.minecraft.core.Holder;

import java.util.stream.Stream;

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
        return ActionType.SEQUENCE;
    }

    @Override
    public boolean execute(ActionContext context) {
        try {
            return this.handler.handle(context);
        } catch (StackOverflowError e) {
            return false;
        }
    }

    public Stream<Holder.Reference<ActionEntry>> streamReferences() {
        return this.handler.streamEntries()
            .filter(entry -> entry instanceof Holder.Reference<ActionEntry>)
            .map(entry -> (Holder.Reference<ActionEntry>) entry);
    }
}
