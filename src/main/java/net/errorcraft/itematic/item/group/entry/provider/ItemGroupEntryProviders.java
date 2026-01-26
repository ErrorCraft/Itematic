package net.errorcraft.itematic.item.group.entry.provider;

import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.ItematicItemTags;
import net.errorcraft.itematic.item.group.entry.ItemGroupEntry;
import net.errorcraft.itematic.item.group.entry.entries.*;
import net.errorcraft.itematic.village.raid.RaidUtil;
import net.minecraft.block.LightBlock;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.component.ComponentChanges;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FireworksComponent;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.OminousBottleItem;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.InstrumentTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.PaintingVariantTags;

import java.util.ArrayList;
import java.util.List;

public class ItemGroupEntryProviders {
    private ItemGroupEntryProviders() {}

    public static void bootstrap(Registerable<ItemGroupEntryProvider> registerable) {
        RegistryEntryLookup<Item> items = registerable.getRegistryLookup(RegistryKeys.ITEM);
        RegistryEntryLookup<BannerPattern> bannerPatterns = registerable.getRegistryLookup(RegistryKeys.BANNER_PATTERN);

        registerable.register(ItemGroupEntryProviderKeys.BUILDING_BLOCKS, ItemGroupEntryProvider.of(
            ItemGroupEntry.tag(ItematicItemTags.WOODEN_BUILDING_BLOCKS),
            ItemGroupEntry.tag(ItematicItemTags.STONE_LIKE_BUILDING_BLOCKS),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.COAL_BLOCK)),
            ItemGroupEntry.tag(ItematicItemTags.IRON_BUILDING_BLOCKS),
            ItemGroupEntry.tag(ItematicItemTags.GOLD_BUILDING_BLOCKS),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.REDSTONE_BLOCK)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.EMERALD_BLOCK)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.LAPIS_BLOCK)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.DIAMOND_BLOCK)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.NETHERITE_BLOCK)),
            ItemGroupEntry.tag(ItematicItemTags.QUARTZ_BUILDING_BLOCKS),
            ItemGroupEntry.tag(ItematicItemTags.SMOOTH_QUARTZ_BUILDING_BLOCKS),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.AMETHYST_BLOCK)),
            ItemGroupEntry.tag(ItematicItemTags.COPPER_LIKE_BUILDING_BLOCKS)
        ));
        registerable.register(ItemGroupEntryProviderKeys.COLORED_BLOCKS, ItemGroupEntryProvider.of(
            ItemGroupEntry.tag(ItematicItemTags.WOOL),
            ItemGroupEntry.tag(ItematicItemTags.WOOL_CARPETS),
            ItemGroupEntry.tag(ItematicItemTags.TERRACOTTA),
            ItemGroupEntry.tag(ItematicItemTags.CONCRETE),
            ItemGroupEntry.tag(ItematicItemTags.CONCRETE_POWDER),
            ItemGroupEntry.tag(ItematicItemTags.GLAZED_TERRACOTTA),
            ItemGroupEntry.tag(ItematicItemTags.GLASS),
            ItemGroupEntry.tag(ItematicItemTags.GLASS_PANES),
            ItemGroupEntry.tag(ItematicItemTags.ITEM_GROUP_SHULKER_BOXES),
            ItemGroupEntry.tag(ItematicItemTags.BEDS),
            ItemGroupEntry.tag(ItematicItemTags.CANDLES),
            ItemGroupEntry.tag(ItematicItemTags.BANNERS)
        ));
        registerable.register(ItemGroupEntryProviderKeys.NATURAL_BLOCKS, ItemGroupEntryProvider.of(
            ItemGroupEntry.tag(ItematicItemTags.GRASS_LIKE_BLOCKS),
            ItemGroupEntry.tag(ItematicItemTags.DIRT_LIKE_BLOCKS),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.GRAVEL)),
            ItemGroupEntry.tag(ItematicItemTags.SAND_BLOCKS),
            ItemGroupEntry.tag(ItematicItemTags.ICE_BLOCKS),
            ItemGroupEntry.tag(ItematicItemTags.SNOW_BLOCKS),
            ItemGroupEntry.tag(ItematicItemTags.MOSS_BLOCKS),
            ItemGroupEntry.tag(ItematicItemTags.STONE_LIKE_BLOCKS),
            ItemGroupEntry.tag(ItematicItemTags.NETHER_BLOCKS),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.END_STONE)),
            ItemGroupEntry.tag(ItematicItemTags.ORE_LIKE_BLOCKS),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.GLOWSTONE)),
            ItemGroupEntry.tag(ItematicItemTags.AMETHYST_BLOCKS),
            ItemGroupEntry.tag(ItematicItemTags.LOG_LIKE_BLOCKS),
            ItemGroupEntry.tag(ItematicItemTags.LEAVES),
            ItemGroupEntry.tag(ItematicItemTags.MUSHROOM_LIKE_BLOCKS),
            ItemGroupEntry.tag(ItematicItemTags.SAPLINGS),
            ItemGroupEntry.tag(ItematicItemTags.PLANTS),
            ItemGroupEntry.tag(ItematicItemTags.EGG_LIKE_BLOCKS),
            ItemGroupEntry.tag(ItematicItemTags.SEEDS),
            ItemGroupEntry.tag(ItematicItemTags.WATER_BLOCKS),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.SPONGE)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.WET_SPONGE)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.MELON)),
            ItemGroupEntry.tag(ItematicItemTags.PUMPKINS),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.HAY_BLOCK)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.BEE_NEST)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.HONEYCOMB_BLOCK)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.SLIME_BLOCK)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.HONEY_BLOCK)),
            ItemGroupEntry.tag(ItematicItemTags.FROGLIGHTS),
            ItemGroupEntry.tag(ItematicItemTags.SCULK),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.COBWEB)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.BEDROCK))
        ));
        registerable.register(ItemGroupEntryProviderKeys.FUNCTIONAL_BLOCKS, ItemGroupEntryProvider.of(
            ItemGroupEntry.tag(ItematicItemTags.TORCHES),
            ItemGroupEntry.tag(ItematicItemTags.LANTERNS),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.CHAIN)),
            ItemGroupEntry.tag(ItematicItemTags.LIGHT_EMITTING_BLOCKS),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.CRAFTING_TABLE)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.STONECUTTER)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.CARTOGRAPHY_TABLE)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.FLETCHING_TABLE)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.SMITHING_TABLE)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.GRINDSTONE)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.LOOM)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.FURNACE)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.SMOKER)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.BLAST_FURNACE)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.CAMPFIRE)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.SOUL_CAMPFIRE)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.ANVIL)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.CHIPPED_ANVIL)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.DAMAGED_ANVIL)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.COMPOSTER)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.NOTE_BLOCK)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.JUKEBOX)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.ENCHANTING_TABLE)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.END_CRYSTAL)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.BREWING_STAND)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.CAULDRON)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.BELL)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.BEACON)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.CONDUIT)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.LODESTONE)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.LADDER)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.SCAFFOLDING)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.BEE_NEST)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.BEEHIVE)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.SUSPICIOUS_SAND)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.SUSPICIOUS_GRAVEL)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.LIGHTNING_ROD)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.FLOWER_POT)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.DECORATED_POT)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.ARMOR_STAND)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.ITEM_FRAME)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.GLOW_ITEM_FRAME)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.PAINTING)),
            PaintingVariantItemGroupEntry.expected(items.getOrThrow(ItemKeys.PAINTING), PaintingVariantTags.PLACEABLE),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.BOOKSHELF)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.CHISELED_BOOKSHELF)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.LECTERN)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.TINTED_GLASS)),
            ItemGroupEntry.tag(ItematicItemTags.SIGNS),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.CHEST)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.BARREL)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.ENDER_CHEST)),
            ItemGroupEntry.tag(ItematicItemTags.ITEM_GROUP_SHULKER_BOXES),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.RESPAWN_ANCHOR)),
            ItemGroupEntry.tag(ItematicItemTags.BEDS),
            ItemGroupEntry.tag(ItematicItemTags.CANDLES),
            ItemGroupEntry.tag(ItematicItemTags.BANNERS),
            StackItemGroupEntry.fromStack(RaidUtil.getOminousBanner(items, bannerPatterns)),
            ItemGroupEntry.tag(ItematicItemTags.HEADS),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.DRAGON_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.END_PORTAL_FRAME)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.ENDER_EYE)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.VAULT)),
            ItemGroupEntry.tag(ItematicItemTags.INFESTED_BLOCKS)
        ));
        registerable.register(ItemGroupEntryProviderKeys.REDSTONE_BLOCKS, ItemGroupEntryProvider.of(
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.REDSTONE)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.REDSTONE_TORCH)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.REDSTONE_BLOCK)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.REPEATER)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.COMPARATOR)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.TARGET)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.COPPER_BULB)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.EXPOSED_COPPER_BULB)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.WEATHERED_COPPER_BULB)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.OXIDIZED_COPPER_BULB)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.LEVER)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.OAK_BUTTON)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.STONE_BUTTON)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.OAK_PRESSURE_PLATE)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.STONE_PRESSURE_PLATE)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.LIGHT_WEIGHTED_PRESSURE_PLATE)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.HEAVY_WEIGHTED_PRESSURE_PLATE)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.SCULK_SENSOR)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.CALIBRATED_SCULK_SENSOR)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.SCULK_SHRIEKER)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.AMETHYST_BLOCK)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.WHITE_WOOL)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.TRIPWIRE_HOOK)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.STRING)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.LECTERN)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.DAYLIGHT_DETECTOR)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.LIGHTNING_ROD)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.PISTON)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.STICKY_PISTON)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.SLIME_BLOCK)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.HONEY_BLOCK)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.DISPENSER)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.DROPPER)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.CRAFTER)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.HOPPER)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.CHEST)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.BARREL)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.CHISELED_BOOKSHELF)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.FURNACE)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.TRAPPED_CHEST)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.JUKEBOX)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.DECORATED_POT)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.OBSERVER)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.NOTE_BLOCK)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.COMPOSTER)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.CAULDRON)),
            ItemGroupEntry.tag(ItematicItemTags.RAILS),
            ItemGroupEntry.tag(ItematicItemTags.MINECARTS),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.OAK_CHEST_BOAT)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.BAMBOO_CHEST_RAFT)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.OAK_DOOR)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.IRON_DOOR)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.OAK_FENCE_GATE)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.OAK_TRAPDOOR)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.IRON_TRAPDOOR)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.TNT)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.REDSTONE_LAMP)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.BELL)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.BIG_DRIPLEAF)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.ARMOR_STAND)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.REDSTONE_ORE))
        ));
        registerable.register(ItemGroupEntryProviderKeys.TOOLS_AND_UTILITIES, ItemGroupEntryProvider.builder()
            .add(ItemGroupEntry.tag(ItematicItemTags.TOOLS))
            .add(ItemGroupEntry.tag(ItematicItemTags.BUCKETS))
            .add(items.getOrThrow(ItemKeys.FISHING_ROD))
            .add(items.getOrThrow(ItemKeys.FLINT_AND_STEEL))
            .add(items.getOrThrow(ItemKeys.FIRE_CHARGE))
            .add(items.getOrThrow(ItemKeys.BONE_MEAL))
            .add(items.getOrThrow(ItemKeys.SHEARS))
            .add(items.getOrThrow(ItemKeys.BRUSH))
            .add(items.getOrThrow(ItemKeys.NAME_TAG))
            .add(items.getOrThrow(ItemKeys.LEAD))
            .add(items.getOrThrow(ItemKeys.BUNDLE))
            .add(items.getOrThrow(ItemKeys.COMPASS))
            .add(items.getOrThrow(ItemKeys.RECOVERY_COMPASS))
            .add(items.getOrThrow(ItemKeys.CLOCK))
            .add(items.getOrThrow(ItemKeys.SPYGLASS))
            .add(items.getOrThrow(ItemKeys.MAP))
            .add(items.getOrThrow(ItemKeys.WRITABLE_BOOK))
            .add(items.getOrThrow(ItemKeys.WIND_CHARGE))
            .add(items.getOrThrow(ItemKeys.ENDER_PEARL))
            .add(items.getOrThrow(ItemKeys.ENDER_EYE))
            .add(items.getOrThrow(ItemKeys.ELYTRA))
            .add(flightDuration(items.getOrThrow(ItemKeys.FIREWORK_ROCKET)))
            .add(items.getOrThrow(ItemKeys.SADDLE))
            .add(items.getOrThrow(ItemKeys.CARROT_ON_A_STICK))
            .add(items.getOrThrow(ItemKeys.WARPED_FUNGUS_ON_A_STICK))
            .add(ItemGroupEntry.tag(ItematicItemTags.BOATS))
            .add(ItemGroupEntry.tag(ItematicItemTags.RAILS))
            .add(ItemGroupEntry.tag(ItematicItemTags.MINECARTS))
            .add(InstrumentItemGroupEntry.of(items.getOrThrow(ItemKeys.GOAT_HORN), InstrumentTags.GOAT_HORNS))
            .add(ItemGroupEntry.tag(ItematicItemTags.MUSIC_DISCS))
            .build());
        registerable.register(ItemGroupEntryProviderKeys.COMBAT, ItemGroupEntryProvider.builder()
            .add(ItematicItemTags.SWORDS)
            .add(ItematicItemTags.AXES)
            .add(items.getOrThrow(ItemKeys.TRIDENT))
            .add(items.getOrThrow(ItemKeys.MACE))
            .add(items.getOrThrow(ItemKeys.SHIELD))
            .add(ItematicItemTags.ARMOR)
            .add(ItematicItemTags.HORSE_ARMOR)
            .add(items.getOrThrow(ItemKeys.WOLF_ARMOR))
            .add(items.getOrThrow(ItemKeys.TOTEM_OF_UNDYING))
            .add(items.getOrThrow(ItemKeys.TNT))
            .add(items.getOrThrow(ItemKeys.END_CRYSTAL))
            .add(items.getOrThrow(ItemKeys.SNOWBALL))
            .add(items.getOrThrow(ItemKeys.EGG))
            .add(items.getOrThrow(ItemKeys.WIND_CHARGE))
            .add(items.getOrThrow(ItemKeys.BOW))
            .add(items.getOrThrow(ItemKeys.CROSSBOW))
            .add(flightDuration(items.getOrThrow(ItemKeys.FIREWORK_ROCKET)))
            .add(items.getOrThrow(ItemKeys.ARROW))
            .add(items.getOrThrow(ItemKeys.SPECTRAL_ARROW))
            .add(PotionItemGroupEntry.of(items.getOrThrow(ItemKeys.TIPPED_ARROW)))
            .build()
        );
        registerable.register(ItemGroupEntryProviderKeys.FOOD_AND_DRINKS, ItemGroupEntryProvider.builder()
            .add(ItematicItemTags.FOOD)
            .add(SuspiciousEffectIngredientItemGroupEntry.of(items.getOrThrow(ItemKeys.SUSPICIOUS_STEW)))
            .add(items.getOrThrow(ItemKeys.MILK_BUCKET))
            .add(items.getOrThrow(ItemKeys.HONEY_BOTTLE))
            .add(ominousBottles(items.getOrThrow(ItemKeys.OMINOUS_BOTTLE)))
            .add(PotionItemGroupEntry.of(items.getOrThrow(ItemKeys.POTION)))
            .add(PotionItemGroupEntry.of(items.getOrThrow(ItemKeys.SPLASH_POTION)))
            .add(PotionItemGroupEntry.of(items.getOrThrow(ItemKeys.LINGERING_POTION)))
            .build()
        );
        registerable.register(ItemGroupEntryProviderKeys.INGREDIENTS, ItemGroupEntryProvider.builder()
            .add(items.getOrThrow(ItemKeys.COAL))
            .add(items.getOrThrow(ItemKeys.CHARCOAL))
            .add(items.getOrThrow(ItemKeys.RAW_IRON))
            .add(items.getOrThrow(ItemKeys.RAW_COPPER))
            .add(items.getOrThrow(ItemKeys.RAW_GOLD))
            .add(items.getOrThrow(ItemKeys.EMERALD))
            .add(items.getOrThrow(ItemKeys.LAPIS_LAZULI))
            .add(items.getOrThrow(ItemKeys.DIAMOND))
            .add(items.getOrThrow(ItemKeys.ANCIENT_DEBRIS))
            .add(items.getOrThrow(ItemKeys.QUARTZ))
            .add(items.getOrThrow(ItemKeys.AMETHYST_SHARD))
            .add(items.getOrThrow(ItemKeys.IRON_NUGGET))
            .add(items.getOrThrow(ItemKeys.GOLD_NUGGET))
            .add(items.getOrThrow(ItemKeys.IRON_INGOT))
            .add(items.getOrThrow(ItemKeys.COPPER_INGOT))
            .add(items.getOrThrow(ItemKeys.GOLD_INGOT))
            .add(items.getOrThrow(ItemKeys.NETHERITE_SCRAP))
            .add(items.getOrThrow(ItemKeys.NETHERITE_INGOT))
            .add(items.getOrThrow(ItemKeys.STICK))
            .add(items.getOrThrow(ItemKeys.FLINT))
            .add(items.getOrThrow(ItemKeys.WHEAT))
            .add(items.getOrThrow(ItemKeys.BONE))
            .add(items.getOrThrow(ItemKeys.BONE_MEAL))
            .add(items.getOrThrow(ItemKeys.STRING))
            .add(items.getOrThrow(ItemKeys.FEATHER))
            .add(items.getOrThrow(ItemKeys.SNOWBALL))
            .add(items.getOrThrow(ItemKeys.EGG))
            .add(items.getOrThrow(ItemKeys.LEATHER))
            .add(items.getOrThrow(ItemKeys.RABBIT_HIDE))
            .add(items.getOrThrow(ItemKeys.HONEYCOMB))
            .add(items.getOrThrow(ItemKeys.INK_SAC))
            .add(items.getOrThrow(ItemKeys.GLOW_INK_SAC))
            .add(items.getOrThrow(ItemKeys.TURTLE_SCUTE))
            .add(items.getOrThrow(ItemKeys.ARMADILLO_SCUTE))
            .add(items.getOrThrow(ItemKeys.SLIME_BALL))
            .add(items.getOrThrow(ItemKeys.CLAY_BALL))
            .add(items.getOrThrow(ItemKeys.PRISMARINE_SHARD))
            .add(items.getOrThrow(ItemKeys.PRISMARINE_CRYSTALS))
            .add(items.getOrThrow(ItemKeys.NAUTILUS_SHELL))
            .add(items.getOrThrow(ItemKeys.HEART_OF_THE_SEA))
            .add(items.getOrThrow(ItemKeys.FIRE_CHARGE))
            .add(items.getOrThrow(ItemKeys.BLAZE_ROD))
            .add(items.getOrThrow(ItemKeys.BREEZE_ROD))
            .add(items.getOrThrow(ItemKeys.HEAVY_CORE))
            .add(items.getOrThrow(ItemKeys.NETHER_STAR))
            .add(items.getOrThrow(ItemKeys.ENDER_PEARL))
            .add(items.getOrThrow(ItemKeys.ENDER_EYE))
            .add(items.getOrThrow(ItemKeys.SHULKER_SHELL))
            .add(items.getOrThrow(ItemKeys.POPPED_CHORUS_FRUIT))
            .add(items.getOrThrow(ItemKeys.ECHO_SHARD))
            .add(items.getOrThrow(ItemKeys.DISC_FRAGMENT_5))
            .add(ItematicItemTags.DYES)
            .add(items.getOrThrow(ItemKeys.BOWL))
            .add(items.getOrThrow(ItemKeys.BRICK))
            .add(items.getOrThrow(ItemKeys.NETHER_BRICK))
            .add(items.getOrThrow(ItemKeys.PAPER))
            .add(items.getOrThrow(ItemKeys.BOOK))
            .add(items.getOrThrow(ItemKeys.FIREWORK_STAR))
            .add(ItematicItemTags.BREWING_INGREDIENTS)
            .add(ItematicItemTags.BANNER_PATTERNS)
            .add(ItemTags.DECORATED_POT_SHERDS)
            .add(ItematicItemTags.SMITHING_TEMPLATES)
            .add(items.getOrThrow(ItemKeys.EXPERIENCE_BOTTLE))
            .add(items.getOrThrow(ItemKeys.TRIAL_KEY))
            .add(items.getOrThrow(ItemKeys.OMINOUS_TRIAL_KEY))
            .add(EnchantmentItemGroupEntry.of(items.getOrThrow(ItemKeys.ENCHANTED_BOOK)))
            .build()
        );
        registerable.register(ItemGroupEntryProviderKeys.SPAWN_EGGS, ItemGroupEntryProvider.of(
            ItemGroupEntry.tag(ItematicItemTags.SPAWNERS),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.ALLAY_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.ARMADILLO_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.AXOLOTL_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.BAT_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.BEE_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.BLAZE_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.BOGGED_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.BREEZE_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.CAMEL_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.CAT_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.CAVE_SPIDER_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.CHICKEN_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.COD_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.COW_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.CREEPER_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.DOLPHIN_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.DONKEY_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.DROWNED_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.ELDER_GUARDIAN_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.ENDERMAN_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.ENDERMITE_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.EVOKER_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.FOX_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.FROG_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.GHAST_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.GLOW_SQUID_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.GOAT_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.GUARDIAN_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.HOGLIN_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.HORSE_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.HUSK_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.IRON_GOLEM_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.LLAMA_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.MAGMA_CUBE_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.MOOSHROOM_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.MULE_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.OCELOT_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.PANDA_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.PARROT_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.PHANTOM_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.PIG_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.PIGLIN_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.PIGLIN_BRUTE_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.PILLAGER_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.POLAR_BEAR_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.PUFFERFISH_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.RABBIT_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.RAVAGER_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.SALMON_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.SHEEP_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.SHULKER_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.SILVERFISH_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.SKELETON_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.SKELETON_HORSE_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.SLIME_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.SNIFFER_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.SNOW_GOLEM_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.SPIDER_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.SQUID_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.STRAY_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.STRIDER_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.TADPOLE_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.TRADER_LLAMA_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.TROPICAL_FISH_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.TURTLE_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.VEX_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.VILLAGER_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.VINDICATOR_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.WANDERING_TRADER_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.WARDEN_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.WITCH_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.WITHER_SKELETON_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.WOLF_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.ZOGLIN_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.ZOMBIE_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.ZOMBIE_HORSE_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.ZOMBIE_VILLAGER_SPAWN_EGG)),
            ItemGroupEntry.simple(items.getOrThrow(ItemKeys.ZOMBIFIED_PIGLIN_SPAWN_EGG))
        ));
        registerable.register(ItemGroupEntryProviderKeys.OP_BLOCKS, ItemGroupEntryProvider.builder()
            .add(ItemGroupEntry.requiresPermissions(items.getOrThrow(ItemKeys.COMMAND_BLOCK)))
            .add(ItemGroupEntry.requiresPermissions(items.getOrThrow(ItemKeys.CHAIN_COMMAND_BLOCK)))
            .add(ItemGroupEntry.requiresPermissions(items.getOrThrow(ItemKeys.REPEATING_COMMAND_BLOCK)))
            .add(ItemGroupEntry.requiresPermissions(items.getOrThrow(ItemKeys.COMMAND_BLOCK_MINECART)))
            .add(ItemGroupEntry.requiresPermissions(items.getOrThrow(ItemKeys.JIGSAW)))
            .add(ItemGroupEntry.requiresPermissions(items.getOrThrow(ItemKeys.STRUCTURE_BLOCK)))
            .add(ItemGroupEntry.requiresPermissions(items.getOrThrow(ItemKeys.STRUCTURE_VOID)))
            .add(ItemGroupEntry.requiresPermissions(items.getOrThrow(ItemKeys.BARRIER)))
            .add(ItemGroupEntry.requiresPermissions(items.getOrThrow(ItemKeys.DEBUG_STICK)))
            .add(lightBlocks(items.getOrThrow(ItemKeys.LIGHT)))
            .add(PaintingVariantItemGroupEntry.unexpected(items.getOrThrow(ItemKeys.PAINTING), PaintingVariantTags.PLACEABLE))
            .build()
        );
    }

    private static ItemGroupEntry[] flightDuration(RegistryEntry<Item> item) {
        List<ItemGroupEntry> entries = new ArrayList<>();
        for (byte flight : FireworkRocketItem.FLIGHT_VALUES) {
            entries.add(StackItemGroupEntry.builder(item)
                .components(ComponentChanges.builder()
                    .add(DataComponentTypes.FIREWORKS, new FireworksComponent(flight, List.of())))
                .build()
            );
        }

        return entries.toArray(ItemGroupEntry[]::new);
    }

    private static ItemGroupEntry[] lightBlocks(RegistryEntry<Item> item) {
        List<ItemGroupEntry> entries = new ArrayList<>(LightBlock.field_33722);
        for (int level = LightBlock.field_33722; level >= 0; --level) {
            entries.add(StackItemGroupEntry.fromStack(LightBlock.addNbtForLevel(new ItemStack(item), level), true));
        }

        return entries.toArray(ItemGroupEntry[]::new);
    }

    private static ItemGroupEntry[] ominousBottles(RegistryEntry<Item> item) {
        List<ItemGroupEntry> entries = new ArrayList<>(OminousBottleItem.field_50145 - OminousBottleItem.field_50144);
        for (int amplifier = OminousBottleItem.field_50144; amplifier <= OminousBottleItem.field_50145; amplifier++) {
            ItemStack stack = new ItemStack(item);
            stack.set(DataComponentTypes.OMINOUS_BOTTLE_AMPLIFIER, amplifier);
            entries.add(StackItemGroupEntry.fromStack(stack));
        }

        return entries.toArray(ItemGroupEntry[]::new);
    }
}
