package net.errorcraft.itematic.item.group.entry.entries;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.group.entry.ItemGroupEntryType;
import net.errorcraft.itematic.item.group.entry.PossiblyHiddenItemGroupEntry;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.InstrumentComponent;
import net.minecraft.item.Instrument;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.registry.tag.TagKey;

import java.util.Collection;

public class InstrumentItemGroupEntry extends PossiblyHiddenItemGroupEntry {
    public static final MapCodec<InstrumentItemGroupEntry> CODEC = RecordCodecBuilder.mapCodec(instance -> createCodec(instance).and(instance.group(
        RegistryFixedCodec.of(RegistryKeys.ITEM).fieldOf("item").forGetter(entry -> entry.item),
        TagKey.unprefixedCodec(RegistryKeys.INSTRUMENT).fieldOf("tag").forGetter(entry -> entry.tag)
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

    public static InstrumentItemGroupEntry of(RegistryEntry<Item> item, TagKey<Instrument> tag) {
        return new InstrumentItemGroupEntry(item, tag);
    }

    @Override
    public ItemGroupEntryType type() {
        return ItemGroupEntryType.INSTRUMENT;
    }

    @Override
    protected Collection<ItemStack> createStacks(ItemGroup.DisplayContext context) {
        return context.lookup()
            .getOrThrow(RegistryKeys.INSTRUMENT)
            .getOrThrow(this.tag)
            .stream()
            .map(instrument -> {
                ItemStack stack = new ItemStack(this.item);
                stack.set(DataComponentTypes.INSTRUMENT, new InstrumentComponent(instrument));
                return stack;
            })
            .toList();
    }
}
