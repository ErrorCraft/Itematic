package net.errorcraft.itematic.item.event;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.registry.ItematicRegistries;
import net.errorcraft.itematic.world.action.ActionEntry;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.HashMap;
import java.util.Map;

public class ItemEventMap {
    public static final ItemEventMap EMPTY = new ItemEventMap();
    public static final Codec<ItemEventMap> CODEC = Codec.simpleMap(ItematicRegistries.ITEM_EVENT.getCodec(), ActionEntry.REGISTRY_CODEC, ItematicRegistries.ITEM_EVENT)
        .xmap(ItemEventMap::new, v -> v.events)
        .codec();

    private final Map<ItemEvent, RegistryEntry<ActionEntry>> events;

    private ItemEventMap() {
        this(Map.of());
    }

    private ItemEventMap(Map<ItemEvent, RegistryEntry<ActionEntry>> events) {
        this.events = events;
    }

    public static Builder builder() {
        return new Builder();
    }

    public boolean invokeEvent(ItemEvent event, ActionContext context) {
        RegistryEntry<ActionEntry> entry = this.events.get(event);
        if (entry == null) {
            return false;
        }
        return entry.value()
            .execute(context)
            .orElse(false);
    }

    public boolean hasListener(ItemEvent event) {
        return this.events.containsKey(event);
    }

    public static class Builder {
        private final Map<ItemEvent, RegistryEntry<ActionEntry>> events = new HashMap<>();

        public ItemEventMap build() {
            return new ItemEventMap(this.events);
        }

        public Builder add(ItemEvent event, ActionEntry action) {
            return this.add(event, RegistryEntry.of(action));
        }

        public Builder add(ItemEvent event, RegistryEntry<ActionEntry> entry) {
            if (this.events.containsKey(event)) {
                throw new IllegalArgumentException("Duplicate entry for item event " + event);
            }
            this.events.put(event, entry);
            return this;
        }
    }
}
