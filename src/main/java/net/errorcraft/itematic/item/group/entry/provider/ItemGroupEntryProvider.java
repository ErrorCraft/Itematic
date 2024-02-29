package net.errorcraft.itematic.item.group.entry.provider;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.group.entry.ItemGroupEntry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;

import java.util.ArrayList;
import java.util.List;

public record ItemGroupEntryProvider(List<ItemGroupEntry> entries) {
    public static final Codec<ItemGroupEntryProvider> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ItemGroupEntry.CODEC.listOf().fieldOf("entries").forGetter(ItemGroupEntryProvider::entries)
    ).apply(instance, ItemGroupEntryProvider::new));

    public void collectEntries(ItemGroup.DisplayContext context, ItemGroup.Entries entries) {
        for (ItemGroupEntry entry : this.entries) {
            entry.addStacks(context, entries);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static ItemGroupEntryProvider of(ItemGroupEntry... entries) {
        return new ItemGroupEntryProvider(List.of(entries));
    }

    public static class Builder {
        private final List<ItemGroupEntry> entries = new ArrayList<>();

        public ItemGroupEntryProvider build() {
            return new ItemGroupEntryProvider(this.entries);
        }

        public Builder add(RegistryEntry<Item> entry) {
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
