package net.errorcraft.itematic.world.action.sequence.handler.handlers;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.world.action.ActionEntry;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.sequence.handler.SequenceHandler;
import net.errorcraft.itematic.world.action.sequence.handler.SequenceHandlerType;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import java.util.ArrayList;
import java.util.List;

public record FirstToSucceedSequenceHandler(HolderSet<ActionEntry> entries) implements SequenceHandler<FirstToSucceedSequenceHandler> {
    public static final Codec<FirstToSucceedSequenceHandler> CODEC = ActionEntry.REGISTRY_ENTRY_LIST_CODEC.xmap(
        FirstToSucceedSequenceHandler::new,
        FirstToSucceedSequenceHandler::entries
    );

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public SequenceHandlerType<FirstToSucceedSequenceHandler> type() {
        return SequenceHandlerType.FIRST_TO_SUCCEED;
    }

    @Override
    public boolean handle(ActionContext context) {
        for (Holder<ActionEntry> entry : this.entries) {
            if (entry.value().execute(context).orElse(false)) {
                return true;
            }
        }

        return false;
    }

    public static class Builder implements SequenceHandler.Builder<FirstToSucceedSequenceHandler, FirstToSucceedSequenceHandler.Builder> {
        private final List<Holder<ActionEntry>> entries = new ArrayList<>();

        @Override
        public FirstToSucceedSequenceHandler build() {
            return new FirstToSucceedSequenceHandler(HolderSet.direct(this.entries));
        }

        @Override
        public FirstToSucceedSequenceHandler.Builder add(Holder<ActionEntry> entry) {
            this.entries.add(entry);
            return this;
        }
    }
}
