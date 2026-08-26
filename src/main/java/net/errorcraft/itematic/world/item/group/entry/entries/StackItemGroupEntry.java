package net.errorcraft.itematic.world.item.group.entry.entries;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.item.ItemStackTemplates;
import net.errorcraft.itematic.world.item.group.entry.ItemGroupEntry;
import net.errorcraft.itematic.world.item.group.entry.ItemGroupEntryType;
import net.errorcraft.itematic.world.item.group.entry.PossiblyHiddenItemGroupEntry;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;

import java.util.Collection;
import java.util.List;

public final class StackItemGroupEntry extends PossiblyHiddenItemGroupEntry<StackItemGroupEntry> {
    public static final MapCodec<StackItemGroupEntry> CODEC = RecordCodecBuilder.mapCodec(instance -> codec(instance).and(
        ItemStackTemplates.SINGLE_ITEM_MAP_CODEC.forGetter(entry -> entry.item)
    ).apply(instance, StackItemGroupEntry::new));

    private final ItemStackTemplate item;

    public StackItemGroupEntry(Holder<Item> item) {
        this(ItemStackTemplates.of(item));
    }

    public StackItemGroupEntry(Holder<Item> item, DataComponentPatch components) {
        this(ItemStackTemplates.of(item, components));
    }

    public StackItemGroupEntry(ItemStackTemplate item) {
        this.item = item;
    }

    public StackItemGroupEntry(CreativeModeTab.TabVisibility visibility, boolean requiresPermissions, ItemStackTemplate item) {
        super(visibility, requiresPermissions);
        this.item = item;
    }

    public static StackItemGroupEntry requiresPermissions(Holder<Item> item) {
        return requiresPermissions(ItemStackTemplates.of(item));
    }

    public static StackItemGroupEntry requiresPermissions(ItemStackTemplate item) {
        return new StackItemGroupEntry(
            CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS,
            true,
            item
        );
    }

    @Override
    public ItemGroupEntryType<StackItemGroupEntry> type() {
        return ItemGroupEntryType.STACK;
    }

    @Override
    public Either<ItemGroupEntry<?>, Holder<Item>> createEither() {
        if (this.isSimple()) {
            return Either.right(this.item.item());
        }

        return super.createEither();
    }

    @Override
    protected Collection<ItemStack> createStacks(CreativeModeTab.ItemDisplayParameters context) {
        return List.of(this.item.create());
    }

    private boolean isSimple() {
        return this.visibility() == CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
            && !this.requiresPermissions()
            && this.item.components().isEmpty();
    }
}
