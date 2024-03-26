package net.errorcraft.itematic.world.action.sequence.handler.handlers;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionEntry;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.sequence.handler.SequenceHandler;
import net.errorcraft.itematic.world.action.sequence.handler.SequenceHandlerType;
import net.errorcraft.itematic.world.action.sequence.handler.SequenceHandlerTypes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.dynamic.Codecs;

import java.util.ArrayList;
import java.util.List;

public record PassingSequenceHandler(List<Entry> entries) implements SequenceHandler {
    public static final Codec<PassingSequenceHandler> CODEC = Entry.CODEC.listOf().xmap(PassingSequenceHandler::new, PassingSequenceHandler::entries);

    @Override
    public SequenceHandlerType<?> type() {
        return SequenceHandlerTypes.PASSING;
    }

    @Override
    public boolean handle(ActionContext context) {
        for (Entry entry : this.entries) {
            if (!entry.execute(context)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Iterable<RegistryEntry<ActionEntry>> iterateEntries() {
        return () -> this.entries.stream().map(Entry::action).iterator();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements SequenceHandler.Builder<PassingSequenceHandler, Builder> {
        private final List<Entry> entries = new ArrayList<>();

        @Override
        public PassingSequenceHandler build() {
            return new PassingSequenceHandler(this.entries);
        }

        @Override
        public Builder add(RegistryEntry<ActionEntry> entry) {
            this.entries.add(Entry.required(entry));
            return this;
        }

        public Builder addOptional(Action<?> action) {
            return this.addOptional(RegistryEntry.of(ActionEntry.of(action)));
        }

        public Builder addOptional(RegistryEntry<ActionEntry> entry) {
            this.entries.add(Entry.optional(entry));
            return this;
        }
    }

    public record Entry(RegistryEntry<ActionEntry> action, boolean optional) {
        public static final Codec<Entry> ELEMENT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ActionEntry.REGISTRY_CODEC.fieldOf("entry").forGetter(Entry::action),
            Codecs.createStrictOptionalFieldCodec(Codec.BOOL, "optional", false).forGetter(Entry::optional)
        ).apply(instance, Entry::new));
        public static final Codec<Entry> CODEC = Codecs.either(ELEMENT_CODEC, ActionEntry.REGISTRY_CODEC)
            .xmap(either -> either.map(entry -> entry, Entry::required), entry -> entry.optional ? Either.left(entry) : Either.right(entry.action));

        private boolean execute(ActionContext context) {
            if (this.action.value().execute(context).orElse(false)) {
                return true;
            }
            return this.optional;
        }

        public static Entry required(RegistryEntry<ActionEntry> action) {
            return new Entry(action, false);
        }

        public static Entry optional(RegistryEntry<ActionEntry> action) {
            return new Entry(action, true);
        }
    }
}
