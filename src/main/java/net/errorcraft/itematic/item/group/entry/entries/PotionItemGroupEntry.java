package net.errorcraft.itematic.item.group.entry.entries;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.component.PotionContentsComponentUtil;
import net.errorcraft.itematic.item.group.entry.ItemGroupEntryType;
import net.errorcraft.itematic.item.group.entry.PossiblyHiddenItemGroupEntry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;

import java.util.Collection;

public class PotionItemGroupEntry extends PossiblyHiddenItemGroupEntry {
    public static final MapCodec<PotionItemGroupEntry> CODEC = RecordCodecBuilder.mapCodec(instance -> createCodec(instance).and(
        RegistryFixedCodec.of(RegistryKeys.ITEM).fieldOf("item").forGetter(entry -> entry.item)
    ).apply(instance, PotionItemGroupEntry::new));

    private final RegistryEntry<Item> item;

    public PotionItemGroupEntry(RegistryEntry<Item> item) {
        this(ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS, false, item);
    }

    public PotionItemGroupEntry(ItemGroup.StackVisibility visibility, boolean requiresPermissions, RegistryEntry<Item> item) {
        super(visibility, requiresPermissions);
        this.item = item;
    }

    public static PotionItemGroupEntry of(RegistryEntry<Item> item) {
        return new PotionItemGroupEntry(item);
    }

    @Override
    public ItemGroupEntryType type() {
        return ItemGroupEntryType.POTION;
    }

    @Override
    protected Collection<ItemStack> createStacks(ItemGroup.DisplayContext context) {
        return context.lookup()
            .getOrThrow(RegistryKeys.POTION)
            .streamEntries()
            .map(entry -> PotionContentsComponentUtil.setPotion(new ItemStack(this.item), entry))
            .toList();
    }
}
