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
import java.util.Optional;

public record FirstToPassRequirementsSequenceHandler(RegistryEntryList<ActionEntry> entries) implements SequenceHandler {
    public static final Codec<FirstToPassRequirementsSequenceHandler> CODEC = ActionEntry.REGISTRY_ENTRY_LIST_CODEC.xmap(FirstToPassRequirementsSequenceHandler::new, FirstToPassRequirementsSequenceHandler::entries);

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public SequenceHandlerType<?> type() {
        return SequenceHandlerTypes.FIRST_TO_PASS_REQUIREMENTS;
    }

    @Override
    public boolean handle(ActionContext context) {
        for (RegistryEntry<ActionEntry> entry : this.entries) {
            Optional<Boolean> result = entry.value().execute(context);
            if (result.isPresent()) {
                return result.get();
            }
        }
        return false;
    }

    @Override
    public Iterable<RegistryEntry<ActionEntry>> iterateEntries() {
        return this.entries;
    }

    public static FirstToPassRequirementsSequenceHandler tag(RegistryEntryList.Named<ActionEntry> tag) {
        return new FirstToPassRequirementsSequenceHandler(tag);
    }

    public static class Builder implements SequenceHandler.Builder<FirstToPassRequirementsSequenceHandler, Builder> {
        private final List<RegistryEntry<ActionEntry>> entries = new ArrayList<>();

        @Override
        public FirstToPassRequirementsSequenceHandler build() {
            return new FirstToPassRequirementsSequenceHandler(RegistryEntryList.of(this.entries));
        }

        @Override
        public Builder add(RegistryEntry<ActionEntry> entry) {
            this.entries.add(entry);
            return this;
        }
    }
}
