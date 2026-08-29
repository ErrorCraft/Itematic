package net.errorcraft.itematic.world.action.sequence.handler.handlers;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionEntry;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.sequence.handler.SequenceHandler;
import net.errorcraft.itematic.world.action.sequence.handler.SequenceHandlerType;
import net.minecraft.core.Holder;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public record PassingSequenceHandler(List<Entry> entries) implements SequenceHandler<PassingSequenceHandler> {
    public static final Codec<PassingSequenceHandler> CODEC = Entry.CODEC.listOf().xmap(
        PassingSequenceHandler::new,
        PassingSequenceHandler::entries
    );

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public SequenceHandlerType<PassingSequenceHandler> type() {
        return SequenceHandlerType.PASSING;
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

    public static class Builder implements SequenceHandler.Builder<PassingSequenceHandler, Builder> {
        private final List<Entry> entries = new ArrayList<>();

        @Override
        public PassingSequenceHandler build() {
            return new PassingSequenceHandler(this.entries);
        }

        @Override
        public Builder add(Holder<ActionEntry> entry) {
            this.entries.add(Entry.required(entry));
            return this;
        }

        public Builder addOptional(Action<?> action) {
            return this.addOptional(Holder.direct(ActionEntry.of(action)));
        }

        public Builder addOptional(Holder<ActionEntry> entry) {
            this.entries.add(Entry.optional(entry));
            return this;
        }
    }

    public record Entry(Holder<ActionEntry> entry, boolean optional) {
        public static final Codec<Entry> ELEMENT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ActionEntry.CODEC.fieldOf("entry").forGetter(Entry::entry),
            Codec.BOOL.optionalFieldOf("optional", false).forGetter(Entry::optional)
        ).apply(instance, Entry::new));
        public static final Codec<Entry> CODEC = Codec.either(ELEMENT_CODEC, ActionEntry.CODEC)
            .xmap(
                either -> either.map(Function.identity(), Entry::required),
                entry -> entry.optional ? Either.left(entry) : Either.right(entry.entry)
            );

        private static Entry required(Holder<ActionEntry> action) {
            return new Entry(action, false);
        }

        private static Entry optional(Holder<ActionEntry> action) {
            return new Entry(action, true);
        }

        private boolean execute(ActionContext context) {
            if (this.entry.value().execute(context).orElse(false)) {
                return true;
            }

            return this.optional;
        }
    }
}
