package net.errorcraft.itematic.item.event;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.mojang.serialization.Codec;
import net.errorcraft.itematic.registry.ItematicRegistries;
import net.errorcraft.itematic.serialization.MultiMapCodec;
import net.errorcraft.itematic.world.action.ActionEntry;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.Comparator;

public class ItemEventMap {
    public static final ItemEventMap EMPTY = new ItemEventMap();
    public static final Codec<ItemEventMap> CODEC = MultiMapCodec.ofRegistry(ItematicRegistries.ITEM_EVENT, "event", ActionEntry.REGISTRY_CODEC, "entry").xmap(ItemEventMap::new, v -> v.events);

    private final Multimap<ItemEvent, RegistryEntry<ActionEntry>> events;

    private ItemEventMap() {
        this(ImmutableMultimap.of());
    }

    private ItemEventMap(Multimap<ItemEvent, RegistryEntry<ActionEntry>> events) {
        this.events = events;
    }

    public static Builder builder() {
        return new Builder();
    }

    public boolean invokeEvent(ItemEvent event, ActionContext context) {
        boolean result = false;
        for (RegistryEntry<ActionEntry> entry : this.events.get(event)) {
            result |= entry.value().execute(context).orElse(false);
        }
        return result;
    }

    public static class Builder {
        private final Multimap<ItemEvent, RegistryEntry<ActionEntry>> events = MultimapBuilder.treeKeys(Comparator.comparingInt(ItematicRegistries.ITEM_EVENT::getRawId)).arrayListValues().build();

        public Builder add(ItemEvent event, ActionEntry action) {
            this.events.put(event, RegistryEntry.of(action));
            return this;
        }

        public Builder add(ItemEvent event, RegistryEntry.Reference<ActionEntry> action) {
            this.events.put(event, action);
            return this;
        }

        public ItemEventMap build() {
            return new ItemEventMap(this.events);
        }
    }
}
