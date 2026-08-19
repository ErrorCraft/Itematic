package net.errorcraft.itematic.world.item.group.entry;

import net.errorcraft.itematic.core.registries.ItematicRegistries;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.tags.ItemGroupItemTags;
import net.errorcraft.itematic.world.entity.raid.ItematicRaids;
import net.errorcraft.itematic.world.item.group.entry.entries.*;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.InstrumentTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.PaintingVariantTags;
import net.minecraft.world.item.FireworkRocketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Fireworks;
import net.minecraft.world.item.component.OminousBottleAmplifier;
import net.minecraft.world.level.block.LightBlock;
import net.minecraft.world.level.block.TestBlock;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.state.properties.TestBlockMode;

import java.util.ArrayList;
import java.util.List;

public class ItemGroupEntryProviders {
    public static final ResourceKey<ItemGroupEntryProvider> BUILDING_BLOCKS = of("building_blocks");
    public static final ResourceKey<ItemGroupEntryProvider> COLORED_BLOCKS = of("colored_blocks");
    public static final ResourceKey<ItemGroupEntryProvider> NATURAL_BLOCKS = of("natural_blocks");
    public static final ResourceKey<ItemGroupEntryProvider> FUNCTIONAL_BLOCKS = of("functional_blocks");
    public static final ResourceKey<ItemGroupEntryProvider> REDSTONE_BLOCKS = of("redstone_blocks");
    public static final ResourceKey<ItemGroupEntryProvider> TOOLS_AND_UTILITIES = of("tools_and_utilities");
    public static final ResourceKey<ItemGroupEntryProvider> COMBAT = of("combat");
    public static final ResourceKey<ItemGroupEntryProvider> FOOD_AND_DRINKS = of("food_and_drinks");
    public static final ResourceKey<ItemGroupEntryProvider> INGREDIENTS = of("ingredients");
    public static final ResourceKey<ItemGroupEntryProvider> SPAWN_EGGS = of("spawn_eggs");
    public static final ResourceKey<ItemGroupEntryProvider> OP_BLOCKS = of("op_blocks");

    private ItemGroupEntryProviders() {}

    public static void bootstrap(BootstrapContext<ItemGroupEntryProvider> registerable) {
        HolderGetter<Item> items = registerable.lookup(Registries.ITEM);
        HolderGetter<BannerPattern> bannerPatterns = registerable.lookup(Registries.BANNER_PATTERN);

        registerable.register(BUILDING_BLOCKS, ItemGroupEntryProvider.builder()
            .add(ItemGroupItemTags.WOODEN_BUILDING_BLOCKS)
            .add(ItemGroupItemTags.STONE_LIKE_BUILDING_BLOCKS)
            .add(items.getOrThrow(ItemIds.COAL_BLOCK))
            .add(ItemGroupItemTags.IRON_BUILDING_BLOCKS)
            .add(ItemGroupItemTags.GOLD_BUILDING_BLOCKS)
            .add(items.getOrThrow(ItemIds.REDSTONE_BLOCK))
            .add(items.getOrThrow(ItemIds.EMERALD_BLOCK))
            .add(items.getOrThrow(ItemIds.LAPIS_BLOCK))
            .add(items.getOrThrow(ItemIds.DIAMOND_BLOCK))
            .add(items.getOrThrow(ItemIds.NETHERITE_BLOCK))
            .add(ItemGroupItemTags.QUARTZ_BUILDING_BLOCKS)
            .add(ItemGroupItemTags.SMOOTH_QUARTZ_BUILDING_BLOCKS)
            .add(items.getOrThrow(ItemIds.AMETHYST_BLOCK))
            .add(ItemGroupItemTags.COPPER_LIKE_BUILDING_BLOCKS)
            .build()
        );
        registerable.register(COLORED_BLOCKS, ItemGroupEntryProvider.builder()
            .add(ItemGroupItemTags.WOOL)
            .add(ItemGroupItemTags.WOOL_CARPETS)
            .add(ItemGroupItemTags.TERRACOTTA)
            .add(ItemGroupItemTags.CONCRETE)
            .add(ItemGroupItemTags.CONCRETE_POWDER)
            .add(ItemGroupItemTags.GLAZED_TERRACOTTA)
            .add(ItemGroupItemTags.GLASS)
            .add(ItemGroupItemTags.GLASS_PANES)
            .add(ItemGroupItemTags.SHULKER_BOXES)
            .add(ItemGroupItemTags.BEDS)
            .add(ItemGroupItemTags.CANDLES)
            .add(ItemGroupItemTags.BANNERS)
            .build()
        );
        registerable.register(NATURAL_BLOCKS, ItemGroupEntryProvider.builder()
            .add(ItemGroupItemTags.GRASS_LIKE_BLOCKS)
            .add(ItemGroupItemTags.DIRT_LIKE_BLOCKS)
            .add(items.getOrThrow(ItemIds.GRAVEL))
            .add(ItemGroupItemTags.SAND_BLOCKS)
            .add(ItemGroupItemTags.ICE_BLOCKS)
            .add(ItemGroupItemTags.SNOW_BLOCKS)
            .add(ItemGroupItemTags.MOSS_BLOCKS)
            .add(ItemGroupItemTags.STONE_LIKE_BLOCKS)
            .add(ItemGroupItemTags.NETHER_BLOCKS)
            .add(items.getOrThrow(ItemIds.END_STONE))
            .add(ItemGroupItemTags.ORE_LIKE_BLOCKS)
            .add(items.getOrThrow(ItemIds.GLOWSTONE))
            .add(ItemGroupItemTags.AMETHYST_BLOCKS)
            .add(ItemGroupItemTags.LOG_LIKE_BLOCKS)
            .add(ItemGroupItemTags.LEAVES)
            .add(ItemGroupItemTags.MUSHROOM_LIKE_BLOCKS)
            .add(ItemGroupItemTags.SAPLINGS)
            .add(ItemGroupItemTags.PLANTS)
            .add(ItemGroupItemTags.EGG_LIKE_BLOCKS)
            .add(items.getOrThrow(ItemIds.DRIED_GHAST))
            .add(ItemGroupItemTags.SEEDS)
            .add(ItemGroupItemTags.WATER_BLOCKS)
            .add(items.getOrThrow(ItemIds.SPONGE))
            .add(items.getOrThrow(ItemIds.WET_SPONGE))
            .add(items.getOrThrow(ItemIds.MELON))
            .add(ItemGroupItemTags.PUMPKINS)
            .add(items.getOrThrow(ItemIds.HAY_BLOCK))
            .add(items.getOrThrow(ItemIds.BEE_NEST))
            .add(items.getOrThrow(ItemIds.HONEYCOMB_BLOCK))
            .add(items.getOrThrow(ItemIds.SLIME_BLOCK))
            .add(items.getOrThrow(ItemIds.HONEY_BLOCK))
            .add(items.getOrThrow(ItemIds.RESIN_BLOCK))
            .add(ItemGroupItemTags.FROGLIGHTS)
            .add(ItemGroupItemTags.SCULK)
            .add(items.getOrThrow(ItemIds.COBWEB))
            .add(items.getOrThrow(ItemIds.BEDROCK))
            .build()
        );
        registerable.register(FUNCTIONAL_BLOCKS, ItemGroupEntryProvider.builder()
            .add(ItemGroupItemTags.TORCHES)
            .add(ItemGroupItemTags.LANTERNS)
            .add(ItemGroupItemTags.CHAINS)
            .add(ItemGroupItemTags.LIGHT_EMITTING_BLOCKS)
            .add(items.getOrThrow(ItemIds.CRAFTING_TABLE))
            .add(items.getOrThrow(ItemIds.STONECUTTER))
            .add(items.getOrThrow(ItemIds.CARTOGRAPHY_TABLE))
            .add(items.getOrThrow(ItemIds.FLETCHING_TABLE))
            .add(items.getOrThrow(ItemIds.SMITHING_TABLE))
            .add(items.getOrThrow(ItemIds.GRINDSTONE))
            .add(items.getOrThrow(ItemIds.LOOM))
            .add(items.getOrThrow(ItemIds.FURNACE))
            .add(items.getOrThrow(ItemIds.SMOKER))
            .add(items.getOrThrow(ItemIds.BLAST_FURNACE))
            .add(items.getOrThrow(ItemIds.CAMPFIRE))
            .add(items.getOrThrow(ItemIds.SOUL_CAMPFIRE))
            .add(items.getOrThrow(ItemIds.ANVIL))
            .add(items.getOrThrow(ItemIds.CHIPPED_ANVIL))
            .add(items.getOrThrow(ItemIds.DAMAGED_ANVIL))
            .add(items.getOrThrow(ItemIds.COMPOSTER))
            .add(items.getOrThrow(ItemIds.NOTE_BLOCK))
            .add(items.getOrThrow(ItemIds.JUKEBOX))
            .add(items.getOrThrow(ItemIds.ENCHANTING_TABLE))
            .add(items.getOrThrow(ItemIds.END_CRYSTAL))
            .add(items.getOrThrow(ItemIds.BREWING_STAND))
            .add(items.getOrThrow(ItemIds.CAULDRON))
            .add(items.getOrThrow(ItemIds.BELL))
            .add(items.getOrThrow(ItemIds.BEACON))
            .add(items.getOrThrow(ItemIds.CONDUIT))
            .add(items.getOrThrow(ItemIds.LODESTONE))
            .add(items.getOrThrow(ItemIds.LADDER))
            .add(items.getOrThrow(ItemIds.SCAFFOLDING))
            .add(items.getOrThrow(ItemIds.BEE_NEST))
            .add(items.getOrThrow(ItemIds.BEEHIVE))
            .add(items.getOrThrow(ItemIds.SUSPICIOUS_SAND))
            .add(items.getOrThrow(ItemIds.SUSPICIOUS_GRAVEL))
            .add(ItemGroupItemTags.LIGHTNING_RODS)
            .add(items.getOrThrow(ItemIds.FLOWER_POT))
            .add(items.getOrThrow(ItemIds.DECORATED_POT))
            .add(items.getOrThrow(ItemIds.ARMOR_STAND))
            .add(items.getOrThrow(ItemIds.ITEM_FRAME))
            .add(items.getOrThrow(ItemIds.GLOW_ITEM_FRAME))
            .add(items.getOrThrow(ItemIds.PAINTING))
            .add(PaintingVariantItemGroupEntry.expected(
                items.getOrThrow(ItemIds.PAINTING),
                PaintingVariantTags.PLACEABLE
            ))
            .add(items.getOrThrow(ItemIds.BOOKSHELF))
            .add(items.getOrThrow(ItemIds.CHISELED_BOOKSHELF))
            .add(ItemGroupItemTags.SHELVES)
            .add(items.getOrThrow(ItemIds.LECTERN))
            .add(items.getOrThrow(ItemIds.TINTED_GLASS))
            .add(ItemGroupItemTags.SIGNS)
            .add(items.getOrThrow(ItemIds.CHEST))
            .add(items.getOrThrow(ItemIds.BARREL))
            .add(items.getOrThrow(ItemIds.ENDER_CHEST))
            .add(ItemGroupItemTags.SHULKER_BOXES)
            .add(items.getOrThrow(ItemIds.RESPAWN_ANCHOR))
            .add(ItemGroupItemTags.BEDS)
            .add(ItemGroupItemTags.CANDLES)
            .add(ItemGroupItemTags.BANNERS)
            .add(StackItemGroupEntry.fromStack(ItematicRaids.getOminousBanner(items, bannerPatterns)))
            .add(ItemGroupItemTags.HEADS)
            .add(items.getOrThrow(ItemIds.DRAGON_EGG))
            .add(items.getOrThrow(ItemIds.END_PORTAL_FRAME))
            .add(items.getOrThrow(ItemIds.ENDER_EYE))
            .add(items.getOrThrow(ItemIds.VAULT))
            .add(ItemGroupItemTags.COPPER_GOLEMS)
            .add(ItemGroupItemTags.INFESTED_BLOCKS)
            .build()
        );
        registerable.register(REDSTONE_BLOCKS, ItemGroupEntryProvider.builder()
            .add(items.getOrThrow(ItemIds.REDSTONE))
            .add(items.getOrThrow(ItemIds.REDSTONE_TORCH))
            .add(items.getOrThrow(ItemIds.REDSTONE_BLOCK))
            .add(items.getOrThrow(ItemIds.REPEATER))
            .add(items.getOrThrow(ItemIds.COMPARATOR))
            .add(items.getOrThrow(ItemIds.TARGET))
            .add(items.getOrThrow(ItemIds.COPPER_BULB))
            .add(items.getOrThrow(ItemIds.EXPOSED_COPPER_BULB))
            .add(items.getOrThrow(ItemIds.WEATHERED_COPPER_BULB))
            .add(items.getOrThrow(ItemIds.OXIDIZED_COPPER_BULB))
            .add(items.getOrThrow(ItemIds.LEVER))
            .add(items.getOrThrow(ItemIds.OAK_BUTTON))
            .add(items.getOrThrow(ItemIds.STONE_BUTTON))
            .add(items.getOrThrow(ItemIds.OAK_PRESSURE_PLATE))
            .add(items.getOrThrow(ItemIds.STONE_PRESSURE_PLATE))
            .add(items.getOrThrow(ItemIds.LIGHT_WEIGHTED_PRESSURE_PLATE))
            .add(items.getOrThrow(ItemIds.HEAVY_WEIGHTED_PRESSURE_PLATE))
            .add(items.getOrThrow(ItemIds.SCULK_SENSOR))
            .add(items.getOrThrow(ItemIds.CALIBRATED_SCULK_SENSOR))
            .add(items.getOrThrow(ItemIds.SCULK_SHRIEKER))
            .add(items.getOrThrow(ItemIds.AMETHYST_BLOCK))
            .add(items.getOrThrow(ItemIds.WHITE_WOOL))
            .add(items.getOrThrow(ItemIds.TRIPWIRE_HOOK))
            .add(items.getOrThrow(ItemIds.STRING))
            .add(items.getOrThrow(ItemIds.LECTERN))
            .add(items.getOrThrow(ItemIds.DAYLIGHT_DETECTOR))
            .add(ItemGroupItemTags.LIGHTNING_RODS)
            .add(items.getOrThrow(ItemIds.PISTON))
            .add(items.getOrThrow(ItemIds.STICKY_PISTON))
            .add(items.getOrThrow(ItemIds.SLIME_BLOCK))
            .add(items.getOrThrow(ItemIds.HONEY_BLOCK))
            .add(items.getOrThrow(ItemIds.DISPENSER))
            .add(items.getOrThrow(ItemIds.DROPPER))
            .add(items.getOrThrow(ItemIds.CRAFTER))
            .add(items.getOrThrow(ItemIds.HOPPER))
            .add(ItemGroupItemTags.CHESTS)
            .add(items.getOrThrow(ItemIds.BARREL))
            .add(items.getOrThrow(ItemIds.CHISELED_BOOKSHELF))
            .add(ItemGroupItemTags.SHELVES)
            .add(items.getOrThrow(ItemIds.FURNACE))
            .add(items.getOrThrow(ItemIds.TRAPPED_CHEST))
            .add(items.getOrThrow(ItemIds.JUKEBOX))
            .add(items.getOrThrow(ItemIds.DECORATED_POT))
            .add(items.getOrThrow(ItemIds.OBSERVER))
            .add(items.getOrThrow(ItemIds.NOTE_BLOCK))
            .add(items.getOrThrow(ItemIds.COMPOSTER))
            .add(items.getOrThrow(ItemIds.CAULDRON))
            .add(ItemGroupItemTags.RAILS)
            .add(ItemGroupItemTags.MINECARTS)
            .add(items.getOrThrow(ItemIds.OAK_CHEST_BOAT))
            .add(items.getOrThrow(ItemIds.BAMBOO_CHEST_RAFT))
            .add(items.getOrThrow(ItemIds.OAK_DOOR))
            .add(items.getOrThrow(ItemIds.IRON_DOOR))
            .add(items.getOrThrow(ItemIds.OAK_FENCE_GATE))
            .add(items.getOrThrow(ItemIds.OAK_TRAPDOOR))
            .add(items.getOrThrow(ItemIds.IRON_TRAPDOOR))
            .add(items.getOrThrow(ItemIds.TNT))
            .add(items.getOrThrow(ItemIds.REDSTONE_LAMP))
            .add(items.getOrThrow(ItemIds.BELL))
            .add(items.getOrThrow(ItemIds.BIG_DRIPLEAF))
            .add(items.getOrThrow(ItemIds.ARMOR_STAND))
            .add(items.getOrThrow(ItemIds.REDSTONE_ORE))
            .build()
        );
        registerable.register(TOOLS_AND_UTILITIES, ItemGroupEntryProvider.builder()
            .add(ItemGroupItemTags.TOOLS)
            .add(ItemGroupItemTags.BUCKETS)
            .add(items.getOrThrow(ItemIds.FISHING_ROD))
            .add(items.getOrThrow(ItemIds.FLINT_AND_STEEL))
            .add(items.getOrThrow(ItemIds.FIRE_CHARGE))
            .add(items.getOrThrow(ItemIds.BONE_MEAL))
            .add(items.getOrThrow(ItemIds.SHEARS))
            .add(items.getOrThrow(ItemIds.BRUSH))
            .add(items.getOrThrow(ItemIds.NAME_TAG))
            .add(items.getOrThrow(ItemIds.LEAD))
            .add(ItemGroupItemTags.BUNDLES)
            .add(items.getOrThrow(ItemIds.COMPASS))
            .add(items.getOrThrow(ItemIds.RECOVERY_COMPASS))
            .add(items.getOrThrow(ItemIds.CLOCK))
            .add(items.getOrThrow(ItemIds.SPYGLASS))
            .add(items.getOrThrow(ItemIds.MAP))
            .add(items.getOrThrow(ItemIds.WRITABLE_BOOK))
            .add(items.getOrThrow(ItemIds.WIND_CHARGE))
            .add(items.getOrThrow(ItemIds.ENDER_PEARL))
            .add(items.getOrThrow(ItemIds.ENDER_EYE))
            .add(items.getOrThrow(ItemIds.ELYTRA))
            .add(flightDuration(items.getOrThrow(ItemIds.FIREWORK_ROCKET)))
            .add(items.getOrThrow(ItemIds.SADDLE))
            .add(ItemGroupItemTags.HARNESSES)
            .add(items.getOrThrow(ItemIds.CARROT_ON_A_STICK))
            .add(items.getOrThrow(ItemIds.WARPED_FUNGUS_ON_A_STICK))
            .add(ItemGroupItemTags.BOATS)
            .add(ItemGroupItemTags.RAILS)
            .add(ItemGroupItemTags.MINECARTS)
            .add(InstrumentItemGroupEntry.of(items.getOrThrow(ItemIds.GOAT_HORN), InstrumentTags.GOAT_HORNS))
            .add(ItemGroupItemTags.MUSIC_DISCS)
            .build()
        );
        registerable.register(COMBAT, ItemGroupEntryProvider.builder()
            .add(ItemGroupItemTags.SWORDS)
            .add(ItemGroupItemTags.SPEARS)
            .add(ItemGroupItemTags.AXES)
            .add(items.getOrThrow(ItemIds.TRIDENT))
            .add(items.getOrThrow(ItemIds.MACE))
            .add(items.getOrThrow(ItemIds.SHIELD))
            .add(ItemGroupItemTags.ARMOR)
            .add(ItemGroupItemTags.HORSE_ARMOR)
            .add(items.getOrThrow(ItemIds.WOLF_ARMOR))
            .add(ItemGroupItemTags.NAUTILUS_ARMOR)
            .add(items.getOrThrow(ItemIds.TOTEM_OF_UNDYING))
            .add(items.getOrThrow(ItemIds.TNT))
            .add(items.getOrThrow(ItemIds.END_CRYSTAL))
            .add(items.getOrThrow(ItemIds.SNOWBALL))
            .add(ItemGroupItemTags.EGGS)
            .add(items.getOrThrow(ItemIds.WIND_CHARGE))
            .add(items.getOrThrow(ItemIds.BOW))
            .add(items.getOrThrow(ItemIds.CROSSBOW))
            .add(flightDuration(items.getOrThrow(ItemIds.FIREWORK_ROCKET)))
            .add(items.getOrThrow(ItemIds.ARROW))
            .add(items.getOrThrow(ItemIds.SPECTRAL_ARROW))
            .add(PotionItemGroupEntry.of(items.getOrThrow(ItemIds.TIPPED_ARROW)))
            .build()
        );
        registerable.register(FOOD_AND_DRINKS, ItemGroupEntryProvider.builder()
            .add(ItemGroupItemTags.FOOD)
            .add(SuspiciousEffectIngredientItemGroupEntry.of(items.getOrThrow(ItemIds.SUSPICIOUS_STEW)))
            .add(items.getOrThrow(ItemIds.MILK_BUCKET))
            .add(items.getOrThrow(ItemIds.HONEY_BOTTLE))
            .add(ominousBottles(items.getOrThrow(ItemIds.OMINOUS_BOTTLE)))
            .add(PotionItemGroupEntry.of(items.getOrThrow(ItemIds.POTION)))
            .add(PotionItemGroupEntry.of(items.getOrThrow(ItemIds.SPLASH_POTION)))
            .add(PotionItemGroupEntry.of(items.getOrThrow(ItemIds.LINGERING_POTION)))
            .build()
        );
        registerable.register(INGREDIENTS, ItemGroupEntryProvider.builder()
            .add(items.getOrThrow(ItemIds.COAL))
            .add(items.getOrThrow(ItemIds.CHARCOAL))
            .add(items.getOrThrow(ItemIds.RAW_IRON))
            .add(items.getOrThrow(ItemIds.RAW_COPPER))
            .add(items.getOrThrow(ItemIds.RAW_GOLD))
            .add(items.getOrThrow(ItemIds.EMERALD))
            .add(items.getOrThrow(ItemIds.LAPIS_LAZULI))
            .add(items.getOrThrow(ItemIds.DIAMOND))
            .add(items.getOrThrow(ItemIds.ANCIENT_DEBRIS))
            .add(items.getOrThrow(ItemIds.QUARTZ))
            .add(items.getOrThrow(ItemIds.AMETHYST_SHARD))
            .add(items.getOrThrow(ItemIds.RESIN_CLUMP))
            .add(ItemGroupItemTags.NUGGETS)
            .add(items.getOrThrow(ItemIds.IRON_INGOT))
            .add(items.getOrThrow(ItemIds.COPPER_INGOT))
            .add(items.getOrThrow(ItemIds.GOLD_INGOT))
            .add(items.getOrThrow(ItemIds.NETHERITE_SCRAP))
            .add(items.getOrThrow(ItemIds.NETHERITE_INGOT))
            .add(items.getOrThrow(ItemIds.STICK))
            .add(items.getOrThrow(ItemIds.FLINT))
            .add(items.getOrThrow(ItemIds.WHEAT))
            .add(items.getOrThrow(ItemIds.BONE))
            .add(items.getOrThrow(ItemIds.BONE_MEAL))
            .add(items.getOrThrow(ItemIds.STRING))
            .add(items.getOrThrow(ItemIds.FEATHER))
            .add(items.getOrThrow(ItemIds.SNOWBALL))
            .add(ItemGroupItemTags.EGGS)
            .add(items.getOrThrow(ItemIds.LEATHER))
            .add(items.getOrThrow(ItemIds.RABBIT_HIDE))
            .add(items.getOrThrow(ItemIds.HONEYCOMB))
            .add(items.getOrThrow(ItemIds.INK_SAC))
            .add(items.getOrThrow(ItemIds.GLOW_INK_SAC))
            .add(items.getOrThrow(ItemIds.TURTLE_SCUTE))
            .add(items.getOrThrow(ItemIds.ARMADILLO_SCUTE))
            .add(items.getOrThrow(ItemIds.SLIME_BALL))
            .add(items.getOrThrow(ItemIds.CLAY_BALL))
            .add(items.getOrThrow(ItemIds.PRISMARINE_SHARD))
            .add(items.getOrThrow(ItemIds.PRISMARINE_CRYSTALS))
            .add(items.getOrThrow(ItemIds.NAUTILUS_SHELL))
            .add(items.getOrThrow(ItemIds.HEART_OF_THE_SEA))
            .add(items.getOrThrow(ItemIds.FIRE_CHARGE))
            .add(items.getOrThrow(ItemIds.BLAZE_ROD))
            .add(items.getOrThrow(ItemIds.BREEZE_ROD))
            .add(items.getOrThrow(ItemIds.HEAVY_CORE))
            .add(items.getOrThrow(ItemIds.NETHER_STAR))
            .add(items.getOrThrow(ItemIds.ENDER_PEARL))
            .add(items.getOrThrow(ItemIds.ENDER_EYE))
            .add(items.getOrThrow(ItemIds.SHULKER_SHELL))
            .add(items.getOrThrow(ItemIds.POPPED_CHORUS_FRUIT))
            .add(items.getOrThrow(ItemIds.ECHO_SHARD))
            .add(items.getOrThrow(ItemIds.DISC_FRAGMENT_5))
            .add(ItemGroupItemTags.DYES)
            .add(items.getOrThrow(ItemIds.BOWL))
            .add(items.getOrThrow(ItemIds.BRICK))
            .add(items.getOrThrow(ItemIds.NETHER_BRICK))
            .add(items.getOrThrow(ItemIds.RESIN_BRICK))
            .add(items.getOrThrow(ItemIds.PAPER))
            .add(items.getOrThrow(ItemIds.BOOK))
            .add(items.getOrThrow(ItemIds.FIREWORK_STAR))
            .add(ItemGroupItemTags.BREWING_INGREDIENTS)
            .add(ItemGroupItemTags.BANNER_PATTERNS)
            .add(ItemTags.DECORATED_POT_SHERDS)
            .add(ItemGroupItemTags.SMITHING_TEMPLATES)
            .add(items.getOrThrow(ItemIds.EXPERIENCE_BOTTLE))
            .add(items.getOrThrow(ItemIds.TRIAL_KEY))
            .add(items.getOrThrow(ItemIds.OMINOUS_TRIAL_KEY))
            .add(EnchantmentItemGroupEntry.of(items.getOrThrow(ItemIds.ENCHANTED_BOOK)))
            .build()
        );
        registerable.register(SPAWN_EGGS, ItemGroupEntryProvider.builder()
            .add(ItemGroupItemTags.SPAWNERS)
            .add(items.getOrThrow(ItemIds.CREAKING_HEART))
            .add(items.getOrThrow(ItemIds.ALLAY_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.ARMADILLO_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.AXOLOTL_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.BAT_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.BEE_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.BLAZE_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.BOGGED_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.BREEZE_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.CAMEL_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.CAMEL_HUSK_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.CAT_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.CAVE_SPIDER_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.CHICKEN_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.COD_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.COPPER_GOLEM_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.COW_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.CREAKING_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.CREEPER_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.DOLPHIN_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.DONKEY_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.DROWNED_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.ELDER_GUARDIAN_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.ENDERMAN_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.ENDERMITE_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.EVOKER_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.FOX_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.FROG_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.GHAST_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.GLOW_SQUID_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.GOAT_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.GUARDIAN_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.HAPPY_GHAST_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.HOGLIN_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.HORSE_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.HUSK_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.IRON_GOLEM_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.LLAMA_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.MAGMA_CUBE_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.MOOSHROOM_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.MULE_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.NAUTILUS_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.OCELOT_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.PANDA_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.PARCHED_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.PARROT_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.PHANTOM_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.PIG_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.PIGLIN_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.PIGLIN_BRUTE_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.PILLAGER_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.POLAR_BEAR_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.PUFFERFISH_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.RABBIT_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.RAVAGER_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.SALMON_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.SHEEP_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.SHULKER_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.SILVERFISH_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.SKELETON_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.SKELETON_HORSE_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.SLIME_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.SNIFFER_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.SNOW_GOLEM_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.SPIDER_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.SQUID_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.STRAY_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.STRIDER_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.TADPOLE_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.TRADER_LLAMA_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.TROPICAL_FISH_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.TURTLE_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.VEX_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.VILLAGER_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.VINDICATOR_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.WANDERING_TRADER_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.WARDEN_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.WITCH_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.WITHER_SKELETON_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.WOLF_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.ZOGLIN_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.ZOMBIE_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.ZOMBIE_HORSE_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.ZOMBIE_NAUTILUS_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.ZOMBIE_VILLAGER_SPAWN_EGG))
            .add(items.getOrThrow(ItemIds.ZOMBIFIED_PIGLIN_SPAWN_EGG))
            .build()
        );
        registerable.register(OP_BLOCKS, ItemGroupEntryProvider.builder()
            .add(ItemGroupEntry.requiresPermissions(items.getOrThrow(ItemIds.COMMAND_BLOCK)))
            .add(ItemGroupEntry.requiresPermissions(items.getOrThrow(ItemIds.CHAIN_COMMAND_BLOCK)))
            .add(ItemGroupEntry.requiresPermissions(items.getOrThrow(ItemIds.REPEATING_COMMAND_BLOCK)))
            .add(ItemGroupEntry.requiresPermissions(items.getOrThrow(ItemIds.COMMAND_BLOCK_MINECART)))
            .add(ItemGroupEntry.requiresPermissions(items.getOrThrow(ItemIds.JIGSAW)))
            .add(ItemGroupEntry.requiresPermissions(items.getOrThrow(ItemIds.STRUCTURE_BLOCK)))
            .add(ItemGroupEntry.requiresPermissions(items.getOrThrow(ItemIds.STRUCTURE_VOID)))
            .add(ItemGroupEntry.requiresPermissions(items.getOrThrow(ItemIds.BARRIER)))
            .add(ItemGroupEntry.requiresPermissions(items.getOrThrow(ItemIds.DEBUG_STICK)))
            .add(ItemGroupEntry.requiresPermissions(items.getOrThrow(ItemIds.TEST_INSTANCE_BLOCK)))
            .add(testBlocks(items.getOrThrow(ItemIds.TEST_BLOCK)))
            .add(lightBlocks(items.getOrThrow(ItemIds.LIGHT)))
            .add(PaintingVariantItemGroupEntry.unexpected(items.getOrThrow(ItemIds.PAINTING), PaintingVariantTags.PLACEABLE))
            .build()
        );
    }

    private static ResourceKey<ItemGroupEntryProvider> of(String id) {
        return ResourceKey.create(ItematicRegistries.ITEM_GROUP_ENTRY_PROVIDER, Identifier.withDefaultNamespace(id));
    }

    private static ItemGroupEntry<?>[] flightDuration(Holder<Item> item) {
        List<ItemGroupEntry<?>> entries = new ArrayList<>();
        for (byte flight : FireworkRocketItem.CRAFTABLE_DURATIONS) {
            entries.add(ItemGroupEntry.simple(
                item,
                DataComponentPatch.builder()
                    .set(DataComponents.FIREWORKS, new Fireworks(flight, List.of()))
                    .build()
            ));
        }

        return entries.toArray(ItemGroupEntry[]::new);
    }

    private static ItemGroupEntry<?>[] testBlocks(Holder<Item> item) {
        List<ItemGroupEntry<?>> entries = new ArrayList<>(TestBlockMode.values().length);
        for (TestBlockMode mode : TestBlockMode.values()) {
            entries.add(StackItemGroupEntry.fromStack(
                TestBlock.setModeOnStack(
                    new ItemStack(item),
                    mode
                ),
                true
            ));
        }

        return entries.toArray(ItemGroupEntry[]::new);
    }

    private static ItemGroupEntry<?>[] lightBlocks(Holder<Item> item) {
        List<ItemGroupEntry<?>> entries = new ArrayList<>(LightBlock.MAX_LEVEL);
        for (int level = LightBlock.MAX_LEVEL; level >= 0; --level) {
            entries.add(StackItemGroupEntry.fromStack(
                LightBlock.setLightOnStack(
                    new ItemStack(item),
                    level
                ),
                true
            ));
        }

        return entries.toArray(ItemGroupEntry[]::new);
    }

    private static ItemGroupEntry<?>[] ominousBottles(Holder<Item> item) {
        List<ItemGroupEntry<?>> entries = new ArrayList<>(OminousBottleAmplifier.MAX_AMPLIFIER - OminousBottleAmplifier.MIN_AMPLIFIER + 1);
        for (int amplifier = OminousBottleAmplifier.MIN_AMPLIFIER; amplifier <= OminousBottleAmplifier.MAX_AMPLIFIER; amplifier++) {
            ItemStack stack = new ItemStack(item);
            stack.set(DataComponents.OMINOUS_BOTTLE_AMPLIFIER, new OminousBottleAmplifier(amplifier));
            entries.add(StackItemGroupEntry.fromStack(stack));
        }

        return entries.toArray(ItemGroupEntry[]::new);
    }
}
