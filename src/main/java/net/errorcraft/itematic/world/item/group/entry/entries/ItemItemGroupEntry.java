package net.errorcraft.itematic.world.item.group.entry.entries;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.item.Items;
import net.errorcraft.itematic.world.item.group.entry.ItemGroupEntry;
import net.errorcraft.itematic.world.item.group.entry.ItemGroupEntryType;
import net.errorcraft.itematic.world.item.group.entry.PossiblyHiddenItemGroupEntry;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;

public class ItemItemGroupEntry extends PossiblyHiddenItemGroupEntry<ItemItemGroupEntry> {
    public static final MapCodec<ItemItemGroupEntry> CODEC = RecordCodecBuilder.mapCodec(instance -> codec(instance).and(instance.group(
        Items.LIST_CODEC.fieldOf("items").forGetter(entry -> entry.items),
        DataComponentPatch.CODEC.optionalFieldOf("components", DataComponentPatch.EMPTY).forGetter(entry -> entry.components)
    )).apply(instance, ItemItemGroupEntry::new));

    private final HolderSet<Item> items;
    private final DataComponentPatch components;

    public ItemItemGroupEntry(HolderSet<Item> items) {
        this(items, DataComponentPatch.EMPTY);
    }

    public ItemItemGroupEntry(HolderSet<Item> items, DataComponentPatch components) {
        this.items = items;
        this.components = components;
    }

    public ItemItemGroupEntry(CreativeModeTab.TabVisibility visibility, boolean requiresPermissions, HolderSet<Item> items, DataComponentPatch components) {
        super(visibility, requiresPermissions);
        this.items = items;
        this.components = components;
    }

    @Override
    public ItemGroupEntryType<ItemItemGroupEntry> type() {
        return ItemGroupEntryType.ITEM;
    }

    public Either<ItemGroupEntry<?>, HolderSet<Item>> createEither() {
        if (this.isSimple()) {
            return Either.right(this.items);
        }

        return Either.left(this);
    }

    @Override
    protected Collection<ItemStack> createStacks(CreativeModeTab.ItemDisplayParameters context) {
        return this.items.stream()
            .map(item -> new ItemStack(item, 1, this.components))
            .toList();
    }

    private boolean isSimple() {
        return this.visibility() == CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
            && !this.requiresPermissions()
            && this.components.isEmpty();
    }
}
