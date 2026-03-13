package net.errorcraft.itematic.world.action.sequence.handler.handlers;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.world.action.ActionEntry;
import net.errorcraft.itematic.world.action.context.NewActionContext;
import net.errorcraft.itematic.world.action.sequence.handler.SequenceHandler;
import net.errorcraft.itematic.world.action.sequence.handler.SequenceHandlerType;
import net.errorcraft.itematic.world.action.sequence.handler.SequenceHandlerTypes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;

import java.util.ArrayList;
import java.util.List;

public record UncheckedSequenceHandler(RegistryEntryList<ActionEntry> entries) implements SequenceHandler<UncheckedSequenceHandler> {
    public static final Codec<UncheckedSequenceHandler> CODEC = ActionEntry.REGISTRY_ENTRY_LIST_CODEC.xmap(UncheckedSequenceHandler::new, UncheckedSequenceHandler::entries);

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public SequenceHandlerType<UncheckedSequenceHandler> type() {
        return SequenceHandlerTypes.UNCHECKED;
    }

    @Override
    public boolean handle(NewActionContext context) {
        boolean result = false;
        for (RegistryEntry<ActionEntry> entry : this.entries) {
            result |= entry.value().execute(context).orElse(false);
        }

        return result;
    }

    @Override
    public Iterable<RegistryEntry<ActionEntry>> iterateEntries() {
        return this.entries;
    }

    public static class Builder implements SequenceHandler.Builder<UncheckedSequenceHandler, Builder> {
        private final List<RegistryEntry<ActionEntry>> entries = new ArrayList<>();

        @Override
        public UncheckedSequenceHandler build() {
            return new UncheckedSequenceHandler(RegistryEntryList.of(this.entries));
        }

        @Override
        public Builder add(RegistryEntry<ActionEntry> entry) {
            this.entries.add(entry);
            return this;
        }
    }
}
