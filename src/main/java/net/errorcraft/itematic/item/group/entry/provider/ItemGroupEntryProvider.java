package net.errorcraft.itematic.item.group.entry.provider;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.group.entry.ItemGroupEntry;
import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import java.util.ArrayList;
import java.util.List;

public record ItemGroupEntryProvider(List<ItemGroupEntry> entries) {
    public static final Codec<ItemGroupEntryProvider> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ItemGroupEntry.CODEC.listOf().fieldOf("entries").forGetter(ItemGroupEntryProvider::entries)
    ).apply(instance, ItemGroupEntryProvider::new));

    public static Builder builder() {
        return new Builder();
    }

    public void collectEntries(CreativeModeTab.ItemDisplayParameters context, CreativeModeTab.Output entries) {
        for (ItemGroupEntry entry : this.entries) {
            entry.addStacks(context, entries);
        }
    }

    public static class Builder {
        private final List<ItemGroupEntry> entries = new ArrayList<>();

        private Builder() {}

        public ItemGroupEntryProvider build() {
            return new ItemGroupEntryProvider(this.entries);
        }

        public Builder add(Holder<Item> entry) {
            return this.add(ItemGroupEntry.simple(entry));
        }

        public Builder add(TagKey<Item> tag) {
            return this.add(ItemGroupEntry.tag(tag));
        }

        public Builder add(ItemGroupEntry entry) {
            this.entries.add(entry);
            return this;
        }

        public Builder add(ItemGroupEntry... entries) {
            this.entries.addAll(List.of(entries));
            return this;
        }
    }
}
