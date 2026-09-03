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

public record UncheckedSequenceHandler(HolderSet<ActionEntry> entries) implements SequenceHandler<UncheckedSequenceHandler> {
    public static final Codec<UncheckedSequenceHandler> CODEC = ActionEntry.LIST_CODEC.xmap(
        UncheckedSequenceHandler::new,
        UncheckedSequenceHandler::entries
    );

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public SequenceHandlerType<UncheckedSequenceHandler> type() {
        return SequenceHandlerType.UNCHECKED;
    }

    @Override
    public boolean handle(ActionContext context) {
        boolean result = false;
        for (Holder<ActionEntry> entry : this.entries) {
            result |= entry.value().execute(context).orElse(false);
        }

        return result;
    }

    public static class Builder implements SequenceHandler.Builder<UncheckedSequenceHandler, Builder> {
        private final List<Holder<ActionEntry>> entries = new ArrayList<>();

        @Override
        public UncheckedSequenceHandler build() {
            return new UncheckedSequenceHandler(HolderSet.direct(this.entries));
        }

        @Override
        public Builder add(Holder<ActionEntry> entry) {
            this.entries.add(entry);
            return this;
        }
    }
}
