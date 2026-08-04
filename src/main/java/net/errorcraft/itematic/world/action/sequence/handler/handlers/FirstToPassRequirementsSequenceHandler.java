package net.errorcraft.itematic.world.action.sequence.handler.handlers;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.world.action.ActionEntry;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.sequence.handler.SequenceHandler;
import net.errorcraft.itematic.world.action.sequence.handler.SequenceHandlerType;
import net.errorcraft.itematic.world.action.sequence.handler.SequenceHandlerTypes;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record FirstToPassRequirementsSequenceHandler(HolderSet<ActionEntry> entries) implements SequenceHandler<FirstToPassRequirementsSequenceHandler> {
    public static final Codec<FirstToPassRequirementsSequenceHandler> CODEC = ActionEntry.REGISTRY_ENTRY_LIST_CODEC.xmap(FirstToPassRequirementsSequenceHandler::new, FirstToPassRequirementsSequenceHandler::entries);

    public static FirstToPassRequirementsSequenceHandler of(HolderSet<ActionEntry> entries) {
        return new FirstToPassRequirementsSequenceHandler(entries);
    }

    public static net.errorcraft.itematic.world.action.sequence.handler.handlers.FirstToPassRequirementsSequenceHandler.Builder builder() {
        return new net.errorcraft.itematic.world.action.sequence.handler.handlers.FirstToPassRequirementsSequenceHandler.Builder();
    }

    @Override
    public SequenceHandlerType<FirstToPassRequirementsSequenceHandler> type() {
        return SequenceHandlerTypes.FIRST_TO_PASS_REQUIREMENTS;
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

    @Override
    public Iterable<Holder<ActionEntry>> iterateEntries() {
        return this.entries;
    }

    public static class Builder implements SequenceHandler.Builder<FirstToPassRequirementsSequenceHandler, net.errorcraft.itematic.world.action.sequence.handler.handlers.FirstToPassRequirementsSequenceHandler.Builder> {
        private final List<Holder<ActionEntry>> entries = new ArrayList<>();

        private Builder() {}

        @Override
        public FirstToPassRequirementsSequenceHandler build() {
            return new FirstToPassRequirementsSequenceHandler(HolderSet.direct(this.entries));
        }

        @Override
        public net.errorcraft.itematic.world.action.sequence.handler.handlers.FirstToPassRequirementsSequenceHandler.Builder add(Holder<ActionEntry> entry) {
            this.entries.add(entry);
            return this;
        }
    }
}
