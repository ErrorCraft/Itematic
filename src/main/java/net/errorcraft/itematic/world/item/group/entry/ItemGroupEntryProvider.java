package net.errorcraft.itematic.world.item.group.entry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.item.group.entry.entries.ItemItemGroupEntry;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public record ItemGroupEntryProvider(List<ItemGroupEntry<?>> entries) {
    public static final Codec<ItemGroupEntryProvider> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ItemGroupEntry.CODEC.listOf().fieldOf("entries").forGetter(ItemGroupEntryProvider::entries)
    ).apply(instance, ItemGroupEntryProvider::new));

    public static Builder builder(HolderGetter<Item> items) {
        return new Builder(items);
    }

    public void collectEntries(CreativeModeTab.ItemDisplayParameters context, CreativeModeTab.Output entries) {
        for (ItemGroupEntry<?> entry : this.entries) {
            entry.addStacks(context, entries);
        }
    }

    public static class Builder {
        private final List<ItemGroupEntry<?>> entries = new ArrayList<>();
        private final HolderGetter<Item> items;

        private Builder(HolderGetter<Item> items) {
            this.items = items;
        }

        public ItemGroupEntryProvider build() {
            return new ItemGroupEntryProvider(this.entries);
        }

        public Builder add(ItemGroupEntry<?> entry) {
            this.entries.add(entry);
            return this;
        }

        public Builder add(Consumer<Builder> builder) {
            builder.accept(this);
            return this;
        }

        public Builder add(ResourceKey<Item> item) {
            return this.add(
                new ItemItemGroupEntry(
                    HolderSet.direct(this.items.getOrThrow(item))
                )
            );
        }

        public Builder add(ResourceKey<Item> item, DataComponentPatch components) {
            return this.add(
                new ItemItemGroupEntry(
                    HolderSet.direct(this.items.getOrThrow(item)),
                    components
                )
            );
        }

        public Builder addRequiresPermissions(ResourceKey<Item> item) {
            return this.addRequiresPermissions(item, DataComponentPatch.EMPTY);
        }

        public Builder addRequiresPermissions(ResourceKey<Item> item, DataComponentPatch components) {
            return this.add(
                new ItemItemGroupEntry(
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS,
                    true,
                    HolderSet.direct(this.items.getOrThrow(item)),
                    components
                )
            );
        }

        public Builder add(TagKey<Item> items) {
            return this.add(
                new ItemItemGroupEntry(
                    this.items.getOrThrow(items)
                )
            );
        }
    }
}
