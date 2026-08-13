package net.errorcraft.itematic.world.item.group.entry.entries;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.mixin.item.ItemGroupsAccessor;
import net.errorcraft.itematic.world.item.group.entry.ItemGroupEntryType;
import net.errorcraft.itematic.world.item.group.entry.PossiblyHiddenItemGroupEntry;
import net.minecraft.advancements.criterion.TagPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.decoration.painting.PaintingVariant;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;
import java.util.Comparator;

public class PaintingVariantItemGroupEntry extends PossiblyHiddenItemGroupEntry<PaintingVariantItemGroupEntry> {
    public static final MapCodec<PaintingVariantItemGroupEntry> CODEC = RecordCodecBuilder.mapCodec(instance -> codec(instance).and(instance.group(
        RegistryFixedCodec.create(Registries.ITEM).fieldOf("item").forGetter(entry -> entry.item),
        TagPredicate.codec(Registries.PAINTING_VARIANT).fieldOf("tag").forGetter(entry -> entry.tag)
    )).apply(instance, PaintingVariantItemGroupEntry::new));
    private static final Comparator<Holder<PaintingVariant>> PAINTING_VARIANT_COMPARATOR = ItemGroupsAccessor.paintingVariantComparator();

    private final Holder<Item> item;
    private final TagPredicate<PaintingVariant> tag;

    public PaintingVariantItemGroupEntry(boolean requiresPermissions, Holder<Item> item, TagPredicate<PaintingVariant> tag) {
        this(CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS, requiresPermissions, item, tag);
    }

    public PaintingVariantItemGroupEntry(CreativeModeTab.TabVisibility visibility, boolean requiresPermissions, Holder<Item> item, TagPredicate<PaintingVariant> tag) {
        super(visibility, requiresPermissions);
        this.item = item;
        this.tag = tag;
    }

    public static PaintingVariantItemGroupEntry expected(Holder<Item> item, TagKey<PaintingVariant> tag) {
        return new PaintingVariantItemGroupEntry(false, item, TagPredicate.is(tag));
    }

    public static PaintingVariantItemGroupEntry unexpected(Holder<Item> item, TagKey<PaintingVariant> tag) {
        return new PaintingVariantItemGroupEntry(true, item, TagPredicate.isNot(tag));
    }

    @Override
    public ItemGroupEntryType<PaintingVariantItemGroupEntry> type() {
        return ItemGroupEntryType.PAINTING_VARIANT;
    }

    @Override
    protected Collection<ItemStack> createStacks(CreativeModeTab.ItemDisplayParameters context) {
        return context.holders()
            .lookupOrThrow(Registries.PAINTING_VARIANT)
            .listElements()
            .filter(this.tag::matches)
            .sorted(PAINTING_VARIANT_COMPARATOR)
            .map(paintingVariant -> {
                ItemStack stack = new ItemStack(this.item);
                stack.set(DataComponents.PAINTING_VARIANT, paintingVariant);
                return stack;
            })
            .toList();
    }
}
