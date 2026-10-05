package net.errorcraft.itematic.world.item.group;

import net.errorcraft.itematic.core.registries.ItematicRegistries;
import net.errorcraft.itematic.mixin.world.item.CreativeModeTabsAccessor;
import net.errorcraft.itematic.tags.ItemGroupTags;
import net.errorcraft.itematic.world.item.group.entry.ItemGroupEntryProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackLinkedSet;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class CreativeModeTabsCache {
    private static final CreativeModeTab EMPTY = CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0).build();
    private static final List<CreativeModeTab> TABS = new ArrayList<>();
    @Nullable
    private static CreativeModeTab SEARCH;
    public static final int DISPLAY_TAB_ON_EVERY_PAGE = -1;

    public static Stream<CreativeModeTab> streamTabs() {
        return TABS.stream();
    }

    public static CreativeModeTab firstTab() {
        if (TABS.isEmpty()) {
            return EMPTY;
        }

        return TABS.getFirst();
    }

    public static CreativeModeTab search() {
        if (SEARCH == null) {
            return EMPTY;
        }

        return SEARCH;
    }

    public static void refresh(HolderLookup.Provider lookup, CreativeModeTab.ItemDisplayParameters parameters) {
        TABS.clear();
        SEARCH = null;
        HolderLookup.RegistryLookup<ItemGroup> itemGroups = lookup.lookupOrThrow(ItematicRegistries.ITEM_GROUP);
        itemGroups.get(ItemGroupTags.CREATIVE_MODE_GROUPS)
            .stream()
            .flatMap(HolderSet.ListBacked::stream)
            .map(CreativeModeTabsCache::createTabWithContents)
            .forEach(TABS::add);
        itemGroups.get(ItemGroups.OP_BLOCKS)
            .map(CreativeModeTabsCache::createSpecialItemsTab)
            .ifPresent(TABS::add);
        itemGroups.get(ItemGroups.SEARCH)
            .map(CreativeModeTabsCache::createSearchTab)
            .ifPresent(searchTab -> {
                TABS.add(searchTab);
                SEARCH = searchTab;
            });
        itemGroups.get(ItemGroups.HOTBAR)
            .map(CreativeModeTabsCache::createHotbarTab)
            .ifPresent(TABS::add);
        itemGroups.get(ItemGroups.INVENTORY)
            .map(CreativeModeTabsCache::createInventoryTab)
            .ifPresent(TABS::add);
        TABS.forEach(tab -> tab.buildContents(parameters));
        int index = 0;
        for (CreativeModeTab tab : TABS) {
            if (tab.itematic$place(index)) {
                index++;
            }
        }
    }

    @SuppressWarnings("DataFlowIssue")
    private static CreativeModeTab createTabWithContents(Holder<ItemGroup> itemGroup) {
        return CreativeModeTab.builder(null, 0)
            .title(itemGroup.value().name())
            .icon(() -> itemGroup.value().icon().create())
            .displayItems(contentDisplayItemsGenerator(itemGroup))
            .build();
    }

    private static CreativeModeTab createHotbarTab(Holder<ItemGroup> itemGroup) {
        return builderOnEveryPage(itemGroup, CreativeModeTab.Row.TOP, 5)
            .type(CreativeModeTab.Type.HOTBAR)
            .build();
    }

    private static CreativeModeTab createSearchTab(Holder<ItemGroup> itemGroup) {
        return builderOnEveryPage(itemGroup, CreativeModeTab.Row.TOP, 6)
            .displayItems(CreativeModeTabsCache::searchDisplayItemsGenerator)
            .type(CreativeModeTab.Type.SEARCH)
            .backgroundTexture(CreativeModeTabsAccessor.searchBackground())
            .build();
    }

    private static CreativeModeTab createSpecialItemsTab(Holder<ItemGroup> itemGroup) {
        return builderOnEveryPage(itemGroup, CreativeModeTab.Row.BOTTOM, 5)
            .displayItems(contentDisplayItemsGenerator(itemGroup))
            .build();
    }

    private static CreativeModeTab createInventoryTab(Holder<ItemGroup> itemGroup) {
        return builderOnEveryPage(itemGroup, CreativeModeTab.Row.BOTTOM, 6)
            .type(CreativeModeTab.Type.INVENTORY)
            .backgroundTexture(CreativeModeTabsAccessor.inventoryBackground())
            .hideTitle()
            .noScrollBar()
            .build();
    }

    private static CreativeModeTab.Builder builderOnEveryPage(Holder<ItemGroup> itemGroup, CreativeModeTab.Row row, int column) {
        return CreativeModeTab.builder(row, column)
            .title(itemGroup.value().name())
            .icon(() -> itemGroup.value().icon().create())
            .alignedRight();
    }

    private static CreativeModeTab.DisplayItemsGenerator contentDisplayItemsGenerator(Holder<ItemGroup> itemGroup) {
        return (parameters, output) -> {
            for (Holder<ItemGroupEntryProvider> entry : itemGroup.value().entries()) {
                entry.value().collectEntries(parameters, output);
            }
        };
    }

    private static void searchDisplayItemsGenerator(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
        Set<ItemStack> stacks = ItemStackLinkedSet.createTypeAndComponentsSet();
        for (CreativeModeTab tab : TABS) {
            if (tab.getType() != CreativeModeTab.Type.CATEGORY) {
                continue;
            }

            stacks.addAll(tab.getSearchTabDisplayItems());
        }

        output.acceptAll(stacks);
    }
}
