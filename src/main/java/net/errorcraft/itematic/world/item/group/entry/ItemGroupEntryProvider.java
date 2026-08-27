package net.errorcraft.itematic.world.item.group.entry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.item.group.entry.entries.StackItemGroupEntry;
import net.errorcraft.itematic.world.item.group.entry.entries.TagItemGroupEntry;
import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;

import java.util.ArrayList;
import java.util.List;

public record ItemGroupEntryProvider(List<ItemGroupEntry<?>> entries) {
    public static final Codec<ItemGroupEntryProvider> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ItemGroupEntry.CODEC.listOf().fieldOf("entries").forGetter(ItemGroupEntryProvider::entries)
    ).apply(instance, ItemGroupEntryProvider::new));

    public static Builder builder() {
        return new Builder();
    }

    public void collectEntries(CreativeModeTab.ItemDisplayParameters context, CreativeModeTab.Output entries) {
        for (ItemGroupEntry<?> entry : this.entries) {
            entry.addStacks(context, entries);
        }
    }

    public static class Builder {
        private final List<ItemGroupEntry<?>> entries = new ArrayList<>();

        private Builder() {}

        public ItemGroupEntryProvider build() {
            return new ItemGroupEntryProvider(this.entries);
        }

        public Builder add(Holder<Item> item) {
            return this.add(new StackItemGroupEntry(item));
        }

        public Builder add(ItemStackTemplate item) {
            return this.add(new StackItemGroupEntry(item));
        }

        public Builder add(TagKey<Item> tag) {
            return this.add(new TagItemGroupEntry(tag));
        }

        public Builder add(ItemGroupEntry<?> entry) {
            this.entries.add(entry);
            return this;
        }

        public Builder add(ItemGroupEntry<?>... entries) {
            this.entries.addAll(List.of(entries));
            return this;
        }
    }
}
