package net.errorcraft.itematic.data.server.tag;

import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.errorcraft.itematic.tags.ItemGroupEntryProviderTags;
import net.errorcraft.itematic.world.item.group.entry.ItemGroupEntryProvider;
import net.errorcraft.itematic.world.item.group.entry.ItemGroupEntryProviders;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class ItemGroupEntryProviderTagProvider extends FabricTagProvider<ItemGroupEntryProvider> {
    public ItemGroupEntryProviderTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, ItematicRegistryKeys.ITEM_GROUP_ENTRY_PROVIDER, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        this.builder(ItemGroupEntryProviderTags.BUILDING_BLOCKS)
            .add(ItemGroupEntryProviders.BUILDING_BLOCKS);
        this.builder(ItemGroupEntryProviderTags.COLORED_BLOCKS)
            .add(ItemGroupEntryProviders.COLORED_BLOCKS);
        this.builder(ItemGroupEntryProviderTags.NATURAL_BLOCKS)
            .add(ItemGroupEntryProviders.NATURAL_BLOCKS);
        this.builder(ItemGroupEntryProviderTags.FUNCTIONAL_BLOCKS)
            .add(ItemGroupEntryProviders.FUNCTIONAL_BLOCKS);
        this.builder(ItemGroupEntryProviderTags.REDSTONE_BLOCKS)
            .add(ItemGroupEntryProviders.REDSTONE_BLOCKS);
        this.builder(ItemGroupEntryProviderTags.TOOLS_AND_UTILITIES)
            .add(ItemGroupEntryProviders.TOOLS_AND_UTILITIES);
        this.builder(ItemGroupEntryProviderTags.COMBAT)
            .add(ItemGroupEntryProviders.COMBAT);
        this.builder(ItemGroupEntryProviderTags.FOOD_AND_DRINKS)
            .add(ItemGroupEntryProviders.FOOD_AND_DRINKS);
        this.builder(ItemGroupEntryProviderTags.INGREDIENTS)
            .add(ItemGroupEntryProviders.INGREDIENTS);
        this.builder(ItemGroupEntryProviderTags.SPAWN_EGGS)
            .add(ItemGroupEntryProviders.SPAWN_EGGS);
        this.builder(ItemGroupEntryProviderTags.OP_BLOCKS)
            .add(ItemGroupEntryProviders.OP_BLOCKS);
    }
}
