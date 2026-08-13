package net.errorcraft.itematic.world.item.group.entry.entries;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.item.group.entry.ItemGroupEntryType;
import net.errorcraft.itematic.world.item.group.entry.PossiblyHiddenItemGroupEntry;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class TagItemGroupEntry extends PossiblyHiddenItemGroupEntry<TagItemGroupEntry> {
    public static final MapCodec<TagItemGroupEntry> CODEC = RecordCodecBuilder.mapCodec(instance -> codec(instance).and(
        TagKey.codec(Registries.ITEM).fieldOf("tag").forGetter(entry -> entry.tag)
    ).apply(instance, TagItemGroupEntry::new));

    private final TagKey<Item> tag;

    public TagItemGroupEntry(TagKey<Item> tag) {
        this(CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS, false, tag);
    }

    public TagItemGroupEntry(CreativeModeTab.TabVisibility visibility, boolean requiresPermissions, TagKey<Item> tag) {
        super(visibility, requiresPermissions);
        this.tag = tag;
    }

    @Override
    public ItemGroupEntryType<TagItemGroupEntry> type() {
        return ItemGroupEntryType.TAG;
    }

    @Override
    protected Collection<ItemStack> createStacks(CreativeModeTab.ItemDisplayParameters context) {
        return context.holders().lookupOrThrow(Registries.ITEM).get(this.tag)
            .map(HolderSet.ListBacked::stream)
            .map(stream -> stream.map(ItemStack::new))
            .map(Stream::toList)
            .orElseGet(List::of);
    }
}
