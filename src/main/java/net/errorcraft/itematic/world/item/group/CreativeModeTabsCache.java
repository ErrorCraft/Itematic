package net.errorcraft.itematic.world.item.group;

import net.errorcraft.itematic.core.registries.ItematicRegistries;
import net.errorcraft.itematic.mixin.world.item.CreativeModeTabsAccessor;
import net.errorcraft.itematic.tags.ItemGroupTags;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.world.item.CreativeModeTab;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class CreativeModeTabsCache {
    private static final int TABS_PER_ROW = 5;
    private static final int TABS_PER_PAGE = 10;
    private static final CreativeModeTab EMPTY = CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0).build();
    private static final List<CreativeModeTab> TABS = new ArrayList<>();
    @Nullable
    private static CreativeModeTab SEARCH;

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
        HolderLookup.RegistryLookup<ItemGroup> itemGroups = lookup.lookupOrThrow(ItematicRegistries.ITEM_GROUP);
        itemGroups.get(ItemGroupTags.CREATIVE_MODE_GROUPS)
            .ifPresent(creativeModeGroups -> {
                for (int i = 0; i < creativeModeGroups.size(); i++) {
                    Holder<ItemGroup> itemGroup = creativeModeGroups.get(i);
                    CreativeModeTab creativeModeTab = createTabWithContents(itemGroup, i);
                    TABS.add(creativeModeTab);
                }

                itemGroups.get(ItemGroups.SEARCH)
                    .map(itemGroup -> createSearchTab(
                        itemGroup,
                        creativeModeGroups
                    ))
                    .ifPresent(searchTab -> {
                        TABS.add(searchTab);
                        SEARCH = searchTab;
                        SEARCH.buildContents(parameters);
                    });
            });
        itemGroups.get(ItemGroups.OP_BLOCKS)
            .map(CreativeModeTabsCache::createSpecialItemsTab)
            .ifPresent(TABS::add);
        TABS.forEach(tab -> tab.buildContents(parameters));
        itemGroups.get(ItemGroups.HOTBAR)
            .map(CreativeModeTabsCache::createHotbarTab)
            .ifPresent(TABS::add);
        itemGroups.get(ItemGroups.INVENTORY)
            .map(CreativeModeTabsCache::createInventoryTab)
            .ifPresent(TABS::add);
    }

    @Unique
    private static CreativeModeTab createTabWithContents(Holder<ItemGroup> itemGroup, int index) {
        return builder(itemGroup, index)
            .itematic$displayItems(itemGroup)
            .build();
    }

    @Unique
    private static CreativeModeTab createHotbarTab(Holder.Reference<ItemGroup> itemGroup) {
        return builderOnEveryPage(itemGroup, CreativeModeTab.Row.TOP, 5)
            .type(CreativeModeTab.Type.HOTBAR)
            .build();
    }

    @Unique
    private static CreativeModeTab createSearchTab(Holder.Reference<ItemGroup> itemGroup, HolderSet<ItemGroup> itemGroups) {
        return builderOnEveryPage(itemGroup, CreativeModeTab.Row.TOP, 6)
            .itematic$searchDisplayItems(itemGroups)
            .type(CreativeModeTab.Type.SEARCH)
            .backgroundTexture(CreativeModeTabsAccessor.searchBackground())
            .build();
    }

    @Unique
    private static CreativeModeTab createSpecialItemsTab(Holder.Reference<ItemGroup> itemGroup) {
        return builderOnEveryPage(itemGroup, CreativeModeTab.Row.BOTTOM, 5)
            .itematic$displayItems(itemGroup)
            .build();
    }

    private static CreativeModeTab createInventoryTab(Holder.Reference<ItemGroup> itemGroup) {
        return builderOnEveryPage(itemGroup, CreativeModeTab.Row.BOTTOM, 6)
            .type(CreativeModeTab.Type.INVENTORY)
            .backgroundTexture(CreativeModeTabsAccessor.inventoryBackground())
            .hideTitle()
            .noScrollBar()
            .build();
    }

    @SuppressWarnings("DataFlowIssue")
    private static CreativeModeTab.Builder builder(Holder<ItemGroup> itemGroup, int index) {
        return CreativeModeTab.builder(row(index), column(index))
            .itematic$page(page(index))
            .title(itemGroup.value().name())
            .icon(() -> itemGroup.value().icon().create());
    }

    private static CreativeModeTab.Builder builderOnEveryPage(Holder<ItemGroup> itemGroup, CreativeModeTab.Row row, int column) {
        return CreativeModeTab.builder(row, column)
            .itematic$page(ItemGroup.DISPLAY_ON_EVERY_PAGE)
            .title(itemGroup.value().name())
            .icon(() -> itemGroup.value().icon().create())
            .alignedRight();
    }

    private static CreativeModeTab.@Nullable Row row(int index) {
        if (index == -1) {
            return null;
        }

        if ((index / TABS_PER_ROW) % 2 == 0) {
            return CreativeModeTab.Row.TOP;
        }

        return CreativeModeTab.Row.BOTTOM;
    }

    private static int column(int index) {
        if (index == -1) {
            return -1;
        }

        return index % TABS_PER_ROW;
    }

    private static int page(int index) {
        if (index == ItemGroup.DISPLAY_ON_EVERY_PAGE) {
            return ItemGroup.DISPLAY_ON_EVERY_PAGE;
        }

        return index / TABS_PER_PAGE;
    }
}
