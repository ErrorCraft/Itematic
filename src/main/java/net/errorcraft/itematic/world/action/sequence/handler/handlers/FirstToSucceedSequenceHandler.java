package net.errorcraft.itematic.world.action.sequence.handler.handlers;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.world.action.ActionEntry;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.sequence.handler.SequenceHandler;
import net.errorcraft.itematic.world.action.sequence.handler.SequenceHandlerType;
import net.errorcraft.itematic.world.action.sequence.handler.SequenceHandlerTypes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;

import java.util.ArrayList;
import java.util.List;

public record FirstToSucceedSequenceHandler(RegistryEntryList<ActionEntry> entries) implements SequenceHandler {
    public static final Codec<FirstToSucceedSequenceHandler> CODEC = ActionEntry.REGISTRY_ENTRY_LIST_CODEC.xmap(FirstToSucceedSequenceHandler::new, FirstToSucceedSequenceHandler::entries);

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public SequenceHandlerType<?> type() {
        return SequenceHandlerTypes.FIRST_TO_SUCCEED;
    }

    @Override
    public boolean handle(ActionContext context) {
        for (RegistryEntry<ActionEntry> entry : this.entries) {
            if (entry.value().execute(context).orElse(false)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterable<RegistryEntry<ActionEntry>> iterateEntries() {
        return this.entries;
    }

    public static class Builder implements SequenceHandler.Builder<FirstToSucceedSequenceHandler, FirstToSucceedSequenceHandler.Builder> {
        private final List<RegistryEntry<ActionEntry>> entries = new ArrayList<>();

        @Override
        public FirstToSucceedSequenceHandler build() {
            return new FirstToSucceedSequenceHandler(RegistryEntryList.of(this.entries));
        }

        @Override
        public FirstToSucceedSequenceHandler.Builder add(RegistryEntry<ActionEntry> entry) {
            this.entries.add(entry);
            return this;
        }
    }
}
