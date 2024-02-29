package net.errorcraft.itematic.item.group.entry.entries;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.group.entry.ItemGroupEntry;
import net.errorcraft.itematic.item.group.entry.ItemGroupEntryType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;

import java.util.Collection;

public class PotionItemGroupEntry extends ItemGroupEntry {
    public static final Codec<PotionItemGroupEntry> CODEC = RecordCodecBuilder.create(instance -> createCodec(instance).and(
        RegistryFixedCodec.of(RegistryKeys.ITEM).fieldOf("item").forGetter(PotionItemGroupEntry::item)
    ).apply(instance, PotionItemGroupEntry::new));

    private final RegistryEntry<Item> item;

    public PotionItemGroupEntry(RegistryEntry<Item> item) {
        this(ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS, false, item);
    }

    public PotionItemGroupEntry(ItemGroup.StackVisibility visibility, boolean requiresPermissions, RegistryEntry<Item> item) {
        super(visibility, requiresPermissions);
        this.item = item;
    }

    public RegistryEntry<Item> item() {
        return this.item;
    }

    public static PotionItemGroupEntry of(RegistryEntry<Item> item) {
        return new PotionItemGroupEntry(item);
    }

    @Override
    protected Collection<ItemStack> createStacks(ItemGroup.DisplayContext context) {
        return context.lookup()
            .getWrapperOrThrow(RegistryKeys.POTION)
            .streamEntries()
            .filter(entry -> !entry.matchesKey(Potions.EMPTY_KEY))
            .map(entry -> PotionUtil.setPotion(new ItemStack(this.item), entry))
            .toList();
    }

    @Override
    protected ItemGroupEntryType type() {
        return ItemGroupEntryType.POTION;
    }
}
