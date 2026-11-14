package net.errorcraft.itematic.world.item.group.entry.entries;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.item.group.entry.PossiblyHiddenItemGroupEntry;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.codec.RegistryCodecs;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Instrument;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.InstrumentComponent;

import java.util.Collection;

public class InstrumentEntry extends PossiblyHiddenItemGroupEntry<InstrumentEntry> {
    public static final MapCodec<InstrumentEntry> CODEC = RecordCodecBuilder.mapCodec(instance -> codec(instance).and(instance.group(
        Item.CODEC.fieldOf("item").forGetter(entry -> entry.item),
        RegistryCodecs.holderSet(Registries.INSTRUMENT).fieldOf("instruments").forGetter(entry -> entry.instruments)
    )).apply(instance, InstrumentEntry::new));

    private final Holder<Item> item;
    private final HolderSet<Instrument> instruments;

    public InstrumentEntry(Holder<Item> item, HolderSet<Instrument> instruments) {
        this(CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS, false, item, instruments);
    }

    public InstrumentEntry(CreativeModeTab.TabVisibility visibility, boolean requiresPermissions, Holder<Item> item, HolderSet<Instrument> instruments) {
        super(visibility, requiresPermissions);
        this.item = item;
        this.instruments = instruments;
    }

    public static InstrumentEntry of(Holder<Item> item, HolderSet<Instrument> instruments) {
        return new InstrumentEntry(item, instruments);
    }

    @Override
    public MapCodec<InstrumentEntry> codec() {
        return CODEC;
    }

    @Override
    protected Collection<ItemStack> createStacks(CreativeModeTab.ItemDisplayParameters context) {
        return this.instruments.stream()
            .map(instrument -> {
                ItemStack stack = new ItemStack(this.item);
                stack.set(DataComponents.INSTRUMENT, new InstrumentComponent(instrument));
                return stack;
            })
            .toList();
    }
}
