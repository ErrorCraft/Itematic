package net.errorcraft.itematic.item.event;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.mojang.serialization.Codec;
import net.errorcraft.itematic.registry.ItematicRegistries;
import net.errorcraft.itematic.serialisation.MultiMapCodec;
import net.errorcraft.itematic.world.action.ActionSet;
import net.errorcraft.itematic.world.action.context.ActionContext;

import java.util.Comparator;

public class ItemEventMap {
    public static final ItemEventMap EMPTY = new ItemEventMap();
    public static final Codec<ItemEventMap> CODEC = MultiMapCodec.ofRegistry(ItematicRegistries.ITEM_EVENT, "event", ActionSet.CODEC).xmap(ItemEventMap::new, v -> v.events);

    private final Multimap<ItemEvent, ActionSet> events;

    private ItemEventMap() {
        this(ImmutableMultimap.of());
    }

    private ItemEventMap(Multimap<ItemEvent, ActionSet> events) {
        this.events = events;
    }

    public static Builder builder() {
        return new Builder();
    }

    public void invokeEvent(ItemEvent event, ActionContext context) {
        for (ActionSet set : this.events.get(event)) {
            set.execute(context);
        }
    }

    public static class Builder {
        private final Multimap<ItemEvent, ActionSet> events = MultimapBuilder.treeKeys(Comparator.comparingInt(ItematicRegistries.ITEM_EVENT::getRawId)).arrayListValues().build();

        public Builder add(ItemEvent event, ActionSet actions) {
            this.events.put(event, actions);
            return this;
        }

        public ItemEventMap build() {
            return new ItemEventMap(this.events);
        }
    }
}
