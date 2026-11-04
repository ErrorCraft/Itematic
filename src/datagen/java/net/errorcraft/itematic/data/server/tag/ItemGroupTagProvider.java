package net.errorcraft.itematic.data.server.tag;

import net.errorcraft.itematic.core.registries.ItematicRegistries;
import net.errorcraft.itematic.tags.ItemGroupTags;
import net.errorcraft.itematic.world.item.group.ItemGroup;
import net.errorcraft.itematic.world.item.group.ItemGroups;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class ItemGroupTagProvider extends FabricTagsProvider<ItemGroup> {
    public ItemGroupTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, ItematicRegistries.ITEM_GROUP, registryLookupFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        this.builder(ItemGroupTags.CREATIVE_MODE_GROUPS)
            .add(ItemGroups.BUILDING_BLOCKS)
            .add(ItemGroups.COLORED_BLOCKS)
            .add(ItemGroups.NATURAL_BLOCKS)
            .add(ItemGroups.FUNCTIONAL_BLOCKS)
            .add(ItemGroups.REDSTONE_BLOCKS)
            .add(ItemGroups.TOOLS_AND_UTILITIES)
            .add(ItemGroups.COMBAT)
            .add(ItemGroups.FOOD_AND_DRINKS)
            .add(ItemGroups.INGREDIENTS)
            .add(ItemGroups.SPAWN_EGGS);
    }
}
