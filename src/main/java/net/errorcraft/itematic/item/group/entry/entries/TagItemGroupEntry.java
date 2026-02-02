package net.errorcraft.itematic.item.group.entry.entries;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.group.entry.ItemGroupEntryType;
import net.errorcraft.itematic.item.group.entry.PossiblyHiddenItemGroupEntry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.TagKey;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class TagItemGroupEntry extends PossiblyHiddenItemGroupEntry {
    public static final MapCodec<TagItemGroupEntry> CODEC = RecordCodecBuilder.mapCodec(instance -> createCodec(instance).and(
        TagKey.unprefixedCodec(RegistryKeys.ITEM).fieldOf("tag").forGetter(entry -> entry.tag)
    ).apply(instance, TagItemGroupEntry::new));

    private final TagKey<Item> tag;

    public TagItemGroupEntry(TagKey<Item> tag) {
        this(ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS, false, tag);
    }

    public TagItemGroupEntry(ItemGroup.StackVisibility visibility, boolean requiresPermissions, TagKey<Item> tag) {
        super(visibility, requiresPermissions);
        this.tag = tag;
    }

    @Override
    public ItemGroupEntryType type() {
        return ItemGroupEntryType.TAG;
    }

    @Override
    protected Collection<ItemStack> createStacks(ItemGroup.DisplayContext context) {
        return context.lookup().getOrThrow(RegistryKeys.ITEM).getOptional(this.tag)
            .map(RegistryEntryList.ListBacked::stream)
            .map(stream -> stream.map(ItemStack::new))
            .map(Stream::toList)
            .orElseGet(List::of);
    }
}
