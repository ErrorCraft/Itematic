package net.errorcraft.itematic.world.action;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Keyable;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.item.ItemEvent;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;

import java.util.HashMap;
import java.util.Map;

public class ActionEventMap<T> {
    private static final ActionEventMap<?> EMPTY = new ActionEventMap<>(Map.of());

    private final Map<T, Holder<ActionEntry>> events;

    private ActionEventMap(Map<T, Holder<ActionEntry>> events) {
        this.events = events;
    }

    @SuppressWarnings("unchecked")
    public static <T> ActionEventMap<T> empty() {
        return (ActionEventMap<T>) EMPTY;
    }

    public static <T> Codec<ActionEventMap<T>> codec(Codec<T> keyCodec, Keyable keys) {
        return Codec.simpleMap(keyCodec, ActionEntry.REGISTRY_CODEC, keys)
            .xmap(ActionEventMap::new, map -> map.events)
            .codec();
    }

    public static <T> Codec<ActionEventMap<T>> codec(Registry<T> registry) {
        return codec(registry.byNameCodec(), registry);
    }

    public boolean invokeEvent(T event, ActionContext context) {
        Holder<ActionEntry> entry = this.events.get(event);
        if (entry == null) {
            return false;
        }

        return entry.value()
            .execute(context)
            .orElse(false);
    }

    public boolean hasListener(T event) {
        return this.events.containsKey(event);
    }

    public static class Builder<T> {
        private final Map<T, Holder<ActionEntry>> events = new HashMap<>();

        private Builder() {}

        public static Builder<ItemEvent> item() {
            return new Builder<>();
        }

        public ActionEventMap<T> build() {
            return new ActionEventMap<>(this.events);
        }

        public Builder<T> add(T event, ActionEntry action) {
            return this.add(event, Holder.direct(action));
        }

        public Builder<T> add(T event, Holder<ActionEntry> entry) {
            if (this.events.containsKey(event)) {
                throw new IllegalArgumentException("Duplicate entry for item event " + event);
            }

            this.events.put(event, entry);
            return this;
        }
    }
}
