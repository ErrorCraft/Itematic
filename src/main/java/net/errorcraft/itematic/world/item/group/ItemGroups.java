package net.errorcraft.itematic.world.item.group;

import net.errorcraft.itematic.core.registries.ItematicRegistries;
import net.errorcraft.itematic.tags.ItemGroupEntryProviderTags;
import net.errorcraft.itematic.world.item.ItemStackTemplates;
import net.errorcraft.itematic.world.item.group.entry.ItemGroupEntryProvider;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.references.BlockItemIds;
import net.minecraft.references.ItemIds;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public class ItemGroups {
    public static final ResourceKey<ItemGroup> INVENTORY = of("inventory");
    public static final ResourceKey<ItemGroup> SEARCH = of("search");
    public static final ResourceKey<ItemGroup> HOTBAR = of("hotbar");
    public static final ResourceKey<ItemGroup> BUILDING_BLOCKS = of("building_blocks");
    public static final ResourceKey<ItemGroup> COLORED_BLOCKS = of("colored_blocks");
    public static final ResourceKey<ItemGroup> NATURAL_BLOCKS = of("natural_blocks");
    public static final ResourceKey<ItemGroup> FUNCTIONAL_BLOCKS = of("functional_blocks");
    public static final ResourceKey<ItemGroup> REDSTONE_BLOCKS = of("redstone_blocks");
    public static final ResourceKey<ItemGroup> TOOLS_AND_UTILITIES = of("tools_and_utilities");
    public static final ResourceKey<ItemGroup> COMBAT = of("combat");
    public static final ResourceKey<ItemGroup> FOOD_AND_DRINKS = of("food_and_drinks");
    public static final ResourceKey<ItemGroup> INGREDIENTS = of("ingredients");
    public static final ResourceKey<ItemGroup> SPAWN_EGGS = of("spawn_eggs");
    public static final ResourceKey<ItemGroup> OP_BLOCKS = of("op_blocks");

    public static void bootstrap(BootstrapContext<ItemGroup> context) {
        HolderGetter<Item> items = context.lookup(Registries.ITEM);
        HolderGetter<ItemGroupEntryProvider> itemGroupEntryProviders = context.lookup(ItematicRegistries.ITEM_GROUP_ENTRY_PROVIDER);

        context.register(
            INVENTORY,
            new ItemGroup(
                Component.translatable("itemGroup.inventory"),
                ItemStackTemplates.of(items.getOrThrow(BlockItemIds.CHEST.item())),
                HolderSet.empty()
            )
        );
        context.register(
            SEARCH,
            new ItemGroup(
                Component.translatable("itemGroup.search"),
                ItemStackTemplates.of(items.getOrThrow(ItemIds.COMPASS)),
                HolderSet.empty()
            )
        );
        context.register(
            HOTBAR,
            new ItemGroup(
                Component.translatable("itemGroup.hotbar"),
                ItemStackTemplates.of(items.getOrThrow(BlockItemIds.BOOKSHELF.item())),
                HolderSet.empty()
            )
        );
        context.register(
            BUILDING_BLOCKS,
            new ItemGroup(
                Component.translatable("itemGroup.buildingBlocks"),
                ItemStackTemplates.of(items.getOrThrow(BlockItemIds.BRICKS.item())),
                itemGroupEntryProviders.getOrThrow(ItemGroupEntryProviderTags.BUILDING_BLOCKS)
            )
        );
        context.register(
            COLORED_BLOCKS,
            new ItemGroup(
                Component.translatable("itemGroup.coloredBlocks"),
                ItemStackTemplates.of(items.getOrThrow(BlockItemIds.WOOL.cyan().item())),
                itemGroupEntryProviders.getOrThrow(ItemGroupEntryProviderTags.COLORED_BLOCKS)
            )
        );
        context.register(
            NATURAL_BLOCKS,
            new ItemGroup(
                Component.translatable("itemGroup.natural"),
                ItemStackTemplates.of(items.getOrThrow(BlockItemIds.GRASS_BLOCK.item())),
                itemGroupEntryProviders.getOrThrow(ItemGroupEntryProviderTags.NATURAL_BLOCKS)
            )
        );
        context.register(
            FUNCTIONAL_BLOCKS,
            new ItemGroup(
                Component.translatable("itemGroup.functional"),
                ItemStackTemplates.of(items.getOrThrow(BlockItemIds.OAK_SIGN.item())),
                itemGroupEntryProviders.getOrThrow(ItemGroupEntryProviderTags.FUNCTIONAL_BLOCKS)
            )
        );
        context.register(
            REDSTONE_BLOCKS,
            new ItemGroup(
                Component.translatable("itemGroup.redstone"),
                ItemStackTemplates.of(items.getOrThrow(BlockItemIds.REDSTONE_DUST.item())),
                itemGroupEntryProviders.getOrThrow(ItemGroupEntryProviderTags.REDSTONE_BLOCKS)
            )
        );
        context.register(
            TOOLS_AND_UTILITIES,
            new ItemGroup(
                Component.translatable("itemGroup.tools"),
                ItemStackTemplates.of(items.getOrThrow(ItemIds.DIAMOND_PICKAXE)),
                itemGroupEntryProviders.getOrThrow(ItemGroupEntryProviderTags.TOOLS_AND_UTILITIES)
            )
        );
        context.register(
            COMBAT,
            new ItemGroup(
                Component.translatable("itemGroup.combat"),
                ItemStackTemplates.of(items.getOrThrow(ItemIds.NETHERITE_SWORD)),
                itemGroupEntryProviders.getOrThrow(ItemGroupEntryProviderTags.COMBAT)
            )
        );
        context.register(
            FOOD_AND_DRINKS,
            new ItemGroup(
                Component.translatable("itemGroup.foodAndDrink"),
                ItemStackTemplates.of(items.getOrThrow(ItemIds.GOLDEN_APPLE)),
                itemGroupEntryProviders.getOrThrow(ItemGroupEntryProviderTags.FOOD_AND_DRINKS)
            )
        );
        context.register(
            INGREDIENTS,
            new ItemGroup(
                Component.translatable("itemGroup.ingredients"),
                ItemStackTemplates.of(items.getOrThrow(ItemIds.IRON_INGOT)),
                itemGroupEntryProviders.getOrThrow(ItemGroupEntryProviderTags.INGREDIENTS)
            )
        );
        context.register(
            SPAWN_EGGS,
            new ItemGroup(
                Component.translatable("itemGroup.spawnEggs"),
                ItemStackTemplates.of(items.getOrThrow(ItemIds.CREEPER_SPAWN_EGG)),
                itemGroupEntryProviders.getOrThrow(ItemGroupEntryProviderTags.SPAWN_EGGS)
            )
        );
        context.register(
            OP_BLOCKS,
            new ItemGroup(
                Component.translatable("itemGroup.op"),
                ItemStackTemplates.of(items.getOrThrow(BlockItemIds.COMMAND_BLOCK.item())),
                itemGroupEntryProviders.getOrThrow(ItemGroupEntryProviderTags.OP_BLOCKS)
            )
        );
    }

    private static ResourceKey<ItemGroup> of(String id) {
        return ResourceKey.create(
            ItematicRegistries.ITEM_GROUP,
            Identifier.withDefaultNamespace(id)
        );
    }
}
