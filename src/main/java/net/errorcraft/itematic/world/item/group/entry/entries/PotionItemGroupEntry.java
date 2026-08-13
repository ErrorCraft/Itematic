package net.errorcraft.itematic.world.item.group.entry.entries;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.item.alchemy.PotionContentsUtil;
import net.errorcraft.itematic.world.item.group.entry.ItemGroupEntryType;
import net.errorcraft.itematic.world.item.group.entry.PossiblyHiddenItemGroupEntry;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;

public class PotionItemGroupEntry extends PossiblyHiddenItemGroupEntry<PotionItemGroupEntry> {
    public static final MapCodec<PotionItemGroupEntry> CODEC = RecordCodecBuilder.mapCodec(instance -> codec(instance).and(
        RegistryFixedCodec.create(Registries.ITEM).fieldOf("item").forGetter(entry -> entry.item)
    ).apply(instance, PotionItemGroupEntry::new));

    private final Holder<Item> item;

    public PotionItemGroupEntry(Holder<Item> item) {
        this(CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS, false, item);
    }

    public PotionItemGroupEntry(CreativeModeTab.TabVisibility visibility, boolean requiresPermissions, Holder<Item> item) {
        super(visibility, requiresPermissions);
        this.item = item;
    }

    public static PotionItemGroupEntry of(Holder<Item> item) {
        return new PotionItemGroupEntry(item);
    }

    @Override
    public ItemGroupEntryType<PotionItemGroupEntry> type() {
        return ItemGroupEntryType.POTION;
    }

    @Override
    protected Collection<ItemStack> createStacks(CreativeModeTab.ItemDisplayParameters context) {
        return context.holders()
            .lookupOrThrow(Registries.POTION)
            .listElements()
            .map(entry -> PotionContentsUtil.setPotion(new ItemStack(this.item), entry))
            .toList();
    }
}
