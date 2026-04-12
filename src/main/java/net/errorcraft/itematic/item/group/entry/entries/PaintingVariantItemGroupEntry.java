package net.errorcraft.itematic.item.group.entry.entries;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.group.entry.ItemGroupEntryType;
import net.errorcraft.itematic.item.group.entry.PossiblyHiddenItemGroupEntry;
import net.errorcraft.itematic.mixin.item.ItemGroupsAccessor;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.TagPredicate;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.registry.tag.TagKey;

import java.util.Collection;
import java.util.Comparator;

public class PaintingVariantItemGroupEntry extends PossiblyHiddenItemGroupEntry {
    public static final MapCodec<PaintingVariantItemGroupEntry> CODEC = RecordCodecBuilder.mapCodec(instance -> createCodec(instance).and(instance.group(
        RegistryFixedCodec.of(RegistryKeys.ITEM).fieldOf("item").forGetter(entry -> entry.item),
        TagPredicate.createCodec(RegistryKeys.PAINTING_VARIANT).fieldOf("tag").forGetter(entry -> entry.tag)
    )).apply(instance, PaintingVariantItemGroupEntry::new));
    private static final Comparator<RegistryEntry<PaintingVariant>> PAINTING_VARIANT_COMPARATOR = ItemGroupsAccessor.paintingVariantComparator();

    private final RegistryEntry<Item> item;
    private final TagPredicate<PaintingVariant> tag;

    public PaintingVariantItemGroupEntry(boolean requiresPermissions, RegistryEntry<Item> item, TagPredicate<PaintingVariant> tag) {
        this(ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS, requiresPermissions, item, tag);
    }

    public PaintingVariantItemGroupEntry(ItemGroup.StackVisibility visibility, boolean requiresPermissions, RegistryEntry<Item> item, TagPredicate<PaintingVariant> tag) {
        super(visibility, requiresPermissions);
        this.item = item;
        this.tag = tag;
    }

    public static PaintingVariantItemGroupEntry expected(RegistryEntry<Item> item, TagKey<PaintingVariant> tag) {
        return new PaintingVariantItemGroupEntry(false, item, TagPredicate.expected(tag));
    }

    public static PaintingVariantItemGroupEntry unexpected(RegistryEntry<Item> item, TagKey<PaintingVariant> tag) {
        return new PaintingVariantItemGroupEntry(true, item, TagPredicate.unexpected(tag));
    }

    @Override
    public ItemGroupEntryType type() {
        return ItemGroupEntryType.PAINTING_VARIANT;
    }

    @Override
    protected Collection<ItemStack> createStacks(ItemGroup.DisplayContext context) {
        return context.lookup()
            .getOrThrow(RegistryKeys.PAINTING_VARIANT)
            .streamEntries()
            .filter(this.tag::test)
            .sorted(PAINTING_VARIANT_COMPARATOR)
            .map(paintingVariant -> {
                ItemStack stack = new ItemStack(this.item);
                stack.set(DataComponentTypes.PAINTING_VARIANT, paintingVariant);
                return stack;
            })
            .toList();
    }
}
