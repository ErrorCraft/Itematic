package net.errorcraft.itematic.world.item.group.entry.entries;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.mixin.world.item.CreativeModeTabsAccessor;
import net.errorcraft.itematic.world.item.group.entry.PossiblyHiddenItemGroupEntry;
import net.minecraft.advancements.predicates.TagPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.decoration.painting.PaintingVariant;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;
import java.util.Comparator;

public class PaintingVariantEntry extends PossiblyHiddenItemGroupEntry<PaintingVariantEntry> {
    public static final MapCodec<PaintingVariantEntry> CODEC = RecordCodecBuilder.mapCodec(instance -> codec(instance).and(instance.group(
        Item.CODEC.fieldOf("item").forGetter(entry -> entry.item),
        TagPredicate.codec(Registries.PAINTING_VARIANT).fieldOf("painting_variants").forGetter(entry -> entry.tag)
    )).apply(instance, PaintingVariantEntry::new));
    private static final Comparator<Holder<PaintingVariant>> PAINTING_VARIANT_COMPARATOR = CreativeModeTabsAccessor.paintingVariantComparator();

    private final Holder<Item> item;
    private final TagPredicate<PaintingVariant> tag;

    public PaintingVariantEntry(boolean requiresPermissions, Holder<Item> item, TagPredicate<PaintingVariant> tag) {
        this(CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS, requiresPermissions, item, tag);
    }

    public PaintingVariantEntry(CreativeModeTab.TabVisibility visibility, boolean requiresPermissions, Holder<Item> item, TagPredicate<PaintingVariant> tag) {
        super(visibility, requiresPermissions);
        this.item = item;
        this.tag = tag;
    }

    public static PaintingVariantEntry expected(Holder<Item> item, HolderSet<PaintingVariant> paintingVariants) {
        return new PaintingVariantEntry(false, item, TagPredicate.is(paintingVariants));
    }

    public static PaintingVariantEntry unexpected(Holder<Item> item, HolderSet<PaintingVariant> paintingVariants) {
        return new PaintingVariantEntry(true, item, TagPredicate.isNot(paintingVariants));
    }

    @Override
    public MapCodec<PaintingVariantEntry> codec() {
        return CODEC;
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
