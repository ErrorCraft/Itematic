package net.errorcraft.itematic.item.group.entry.entries;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.group.entry.ItemGroupEntry;
import net.errorcraft.itematic.item.group.entry.ItemGroupEntryType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.TagKey;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TagItemGroupEntry extends ItemGroupEntry {
    public static final Codec<TagItemGroupEntry> CODEC = RecordCodecBuilder.create(instance -> createCodec(instance).and(
        TagKey.unprefixedCodec(RegistryKeys.ITEM).fieldOf("tag").forGetter(TagItemGroupEntry::tag)
    ).apply(instance, TagItemGroupEntry::new));

    private final TagKey<Item> tag;

    public TagItemGroupEntry(TagKey<Item> tag) {
        this(ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS, false, tag);
    }

    public TagItemGroupEntry(ItemGroup.StackVisibility visibility, boolean requiresPermissions, TagKey<Item> tag) {
        super(visibility, requiresPermissions);
        this.tag = tag;
    }

    public TagKey<Item> tag() {
        return this.tag;
    }

    @Override
    protected Collection<ItemStack> createStacks(ItemGroup.DisplayContext context) {
        return context.lookup().getWrapperOrThrow(RegistryKeys.ITEM).getOptional(this.tag)
            .map(TagItemGroupEntry::createStacks)
            .orElse(List.of());
    }

    @Override
    protected ItemGroupEntryType type() {
        return ItemGroupEntryType.TAG;
    }

    private static Collection<ItemStack> createStacks(RegistryEntryList.Named<Item> entries) {
        List<ItemStack> stacks = new ArrayList<>(entries.size());
        for (RegistryEntry<Item> entry : entries) {
            stacks.add(new ItemStack(entry));
        }
        return stacks;
    }
}
