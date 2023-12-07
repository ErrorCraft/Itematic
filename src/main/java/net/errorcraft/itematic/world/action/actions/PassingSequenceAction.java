package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.serialization.ItematicCodecs;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.minecraft.util.dynamic.Codecs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public record PassingSequenceAction(List<Entry> entries) implements Action {
    public static final Codec<PassingSequenceAction> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Entry.CODEC.listOf().fieldOf("entries").forGetter(PassingSequenceAction::entries)
    ).apply(instance, PassingSequenceAction::new));

    @Override
    public ActionType<?> type() {
        return ActionTypes.PASSING_SEQUENCE;
    }

    @Override
    public boolean execute(ActionContext context) {
        for (Entry entry : this.entries) {
            if (!entry.execute(context)) {
                return false;
            }
        }
        return true;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static PassingSequenceAction of(Action... actions) {
        return new PassingSequenceAction(Arrays.stream(actions).map(Entry::required).toList());
    }

    public static class Builder {
        private final List<Entry> entries = new ArrayList<>();

        private Builder() {}

        public PassingSequenceAction build() {
            return new PassingSequenceAction(entries);
        }

        public Builder add(Action action) {
            this.entries.add(Entry.required(action));
            return this;
        }

        public Builder addOptional(Action action) {
            this.entries.add(Entry.optional(action));
            return this;
        }
    }

    public record Entry(Action action, boolean optional) {
        public static final Codec<Entry> INLINE_CODEC = Action.CODEC.xmap(Entry::required, Entry::action);
        public static final Codec<Entry> FULL_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Action.CODEC.fieldOf("action").forGetter(Entry::action),
            Codecs.createStrictOptionalFieldCodec(Codec.BOOL, "optional", false).forGetter(Entry::optional)
        ).apply(instance, Entry::new));
        public static final Codec<Entry> CODEC = ItematicCodecs.alternatively(INLINE_CODEC, FULL_CODEC, Entry::isRequired);

        public boolean execute(ActionContext context) {
            if (this.action.execute(context)) {
                return true;
            }
            return this.optional;
        }

        public boolean isRequired() {
            return !this.optional;
        }

        public static Entry required(Action action) {
            return new Entry(action, false);
        }

        public static Entry optional(Action action) {
            return new Entry(action, true);
        }
    }
}
