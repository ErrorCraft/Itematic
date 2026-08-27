package net.errorcraft.itematic.world.item.group.entry.entries;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.item.group.entry.ItemGroupEntryType;
import net.errorcraft.itematic.world.item.group.entry.PossiblyHiddenItemGroupEntry;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Instrument;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.InstrumentComponent;

import java.util.Collection;

public class InstrumentItemGroupEntry extends PossiblyHiddenItemGroupEntry<InstrumentItemGroupEntry> {
    public static final MapCodec<InstrumentItemGroupEntry> CODEC = RecordCodecBuilder.mapCodec(instance -> codec(instance).and(instance.group(
        Item.CODEC.fieldOf("item").forGetter(entry -> entry.item),
        TagKey.codec(Registries.INSTRUMENT).fieldOf("tag").forGetter(entry -> entry.tag)
    )).apply(instance, InstrumentItemGroupEntry::new));

    private final Holder<Item> item;
    private final TagKey<Instrument> tag;

    public InstrumentItemGroupEntry(Holder<Item> item, TagKey<Instrument> tag) {
        this(CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS, false, item, tag);
    }

    public InstrumentItemGroupEntry(CreativeModeTab.TabVisibility visibility, boolean requiresPermissions, Holder<Item> item, TagKey<Instrument> tag) {
        super(visibility, requiresPermissions);
        this.item = item;
        this.tag = tag;
    }

    public static InstrumentItemGroupEntry of(Holder<Item> item, TagKey<Instrument> tag) {
        return new InstrumentItemGroupEntry(item, tag);
    }

    @Override
    public ItemGroupEntryType<InstrumentItemGroupEntry> type() {
        return ItemGroupEntryType.INSTRUMENT;
    }

    @Override
    protected Collection<ItemStack> createStacks(CreativeModeTab.ItemDisplayParameters context) {
        return context.holders()
            .lookupOrThrow(Registries.INSTRUMENT)
            .getOrThrow(this.tag)
            .stream()
            .map(instrument -> {
                ItemStack stack = new ItemStack(this.item);
                stack.set(DataComponents.INSTRUMENT, new InstrumentComponent(instrument));
                return stack;
            })
            .toList();
    }
}
