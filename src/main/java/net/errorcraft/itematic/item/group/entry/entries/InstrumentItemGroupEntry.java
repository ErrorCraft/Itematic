package net.errorcraft.itematic.item.group.entry.entries;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.group.entry.ItemGroupEntry;
import net.errorcraft.itematic.item.group.entry.ItemGroupEntryType;
import net.errorcraft.itematic.mixin.item.GoatHornItemAccessor;
import net.minecraft.item.Instrument;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.registry.tag.TagKey;

import java.util.Collection;

public class InstrumentItemGroupEntry extends ItemGroupEntry {
    public static final Codec<InstrumentItemGroupEntry> CODEC = RecordCodecBuilder.create(instance -> createCodec(instance).and(instance.group(
        RegistryFixedCodec.of(RegistryKeys.ITEM).fieldOf("item").forGetter(InstrumentItemGroupEntry::item),
        TagKey.unprefixedCodec(RegistryKeys.INSTRUMENT).fieldOf("tag").forGetter(InstrumentItemGroupEntry::tag)
    )).apply(instance, InstrumentItemGroupEntry::new));

    private final RegistryEntry<Item> item;
    private final TagKey<Instrument> tag;

    public InstrumentItemGroupEntry(RegistryEntry<Item> item, TagKey<Instrument> tag) {
        this(ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS, false, item, tag);
    }

    public InstrumentItemGroupEntry(ItemGroup.StackVisibility visibility, boolean requiresPermissions, RegistryEntry<Item> item, TagKey<Instrument> tag) {
        super(visibility, requiresPermissions);
        this.item = item;
        this.tag = tag;
    }

    public RegistryEntry<Item> item() {
        return this.item;
    }

    public TagKey<Instrument> tag() {
        return this.tag;
    }

    public static InstrumentItemGroupEntry of(RegistryEntry<Item> item, TagKey<Instrument> tag) {
        return new InstrumentItemGroupEntry(item, tag);
    }

    @Override
    protected Collection<ItemStack> createStacks(ItemGroup.DisplayContext context) {
        return context.lookup()
            .getWrapperOrThrow(RegistryKeys.INSTRUMENT)
            .getOrThrow(this.tag)
            .stream()
            .map(instrument -> {
                ItemStack stack = new ItemStack(this.item);
                GoatHornItemAccessor.setInstrument(stack, instrument);
                return stack;
            })
            .toList();
    }

    @Override
    protected ItemGroupEntryType type() {
        return ItemGroupEntryType.INSTRUMENT;
    }
}
