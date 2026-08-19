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
import java.util.Optional;

public record FirstToPassRequirementsSequenceHandler(HolderSet<ActionEntry> entries) implements SequenceHandler<FirstToPassRequirementsSequenceHandler> {
    public static final Codec<FirstToPassRequirementsSequenceHandler> CODEC = ActionEntry.REGISTRY_ENTRY_LIST_CODEC.xmap(
        FirstToPassRequirementsSequenceHandler::new,
        FirstToPassRequirementsSequenceHandler::entries
    );

    public static Builder builder() {
        return new Builder();
    }

    public static FirstToPassRequirementsSequenceHandler of(HolderSet<ActionEntry> entries) {
        return new FirstToPassRequirementsSequenceHandler(entries);
    }

    @Override
    public SequenceHandlerType<FirstToPassRequirementsSequenceHandler> type() {
        return SequenceHandlerType.FIRST_TO_PASS_REQUIREMENTS;
    }

    @Override
    public boolean handle(ActionContext context) {
        for (Holder<ActionEntry> entry : this.entries) {
            Optional<Boolean> result = entry.value().execute(context);
            if (result.isPresent()) {
                return result.get();
            }
        }

        return false;
    }

    public static class Builder implements SequenceHandler.Builder<FirstToPassRequirementsSequenceHandler, Builder> {
        private final List<Holder<ActionEntry>> entries = new ArrayList<>();

        private Builder() {}

        @Override
        public FirstToPassRequirementsSequenceHandler build() {
            return new FirstToPassRequirementsSequenceHandler(HolderSet.direct(this.entries));
        }

        @Override
        public Builder add(Holder<ActionEntry> entry) {
            this.entries.add(entry);
            return this;
        }
    }
}
