package net.errorcraft.itematic.data.server.tag;

import net.errorcraft.itematic.item.group.entry.provider.ItemGroupEntryProvider;
import net.errorcraft.itematic.item.group.entry.provider.ItemGroupEntryProviderKeys;
import net.errorcraft.itematic.item.group.entry.provider.ItemGroupEntryProviderTags;
import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ItemGroupEntryProviderTagProvider extends FabricTagProvider<ItemGroupEntryProvider> {
    public ItemGroupEntryProviderTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, ItematicRegistryKeys.ITEM_GROUP_ENTRY_PROVIDER, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup lookup) {
        this.builder(ItemGroupEntryProviderTags.BUILDING_BLOCKS)
            .add(ItemGroupEntryProviderKeys.BUILDING_BLOCKS);
        this.builder(ItemGroupEntryProviderTags.COLORED_BLOCKS)
            .add(ItemGroupEntryProviderKeys.COLORED_BLOCKS);
        this.builder(ItemGroupEntryProviderTags.NATURAL_BLOCKS)
            .add(ItemGroupEntryProviderKeys.NATURAL_BLOCKS);
        this.builder(ItemGroupEntryProviderTags.FUNCTIONAL_BLOCKS)
            .add(ItemGroupEntryProviderKeys.FUNCTIONAL_BLOCKS);
        this.builder(ItemGroupEntryProviderTags.REDSTONE_BLOCKS)
            .add(ItemGroupEntryProviderKeys.REDSTONE_BLOCKS);
        this.builder(ItemGroupEntryProviderTags.TOOLS_AND_UTILITIES)
            .add(ItemGroupEntryProviderKeys.TOOLS_AND_UTILITIES);
        this.builder(ItemGroupEntryProviderTags.COMBAT)
            .add(ItemGroupEntryProviderKeys.COMBAT);
        this.builder(ItemGroupEntryProviderTags.FOOD_AND_DRINKS)
            .add(ItemGroupEntryProviderKeys.FOOD_AND_DRINKS);
        this.builder(ItemGroupEntryProviderTags.INGREDIENTS)
            .add(ItemGroupEntryProviderKeys.INGREDIENTS);
        this.builder(ItemGroupEntryProviderTags.SPAWN_EGGS)
            .add(ItemGroupEntryProviderKeys.SPAWN_EGGS);
        this.builder(ItemGroupEntryProviderTags.OP_BLOCKS)
            .add(ItemGroupEntryProviderKeys.OP_BLOCKS);
    }
}
