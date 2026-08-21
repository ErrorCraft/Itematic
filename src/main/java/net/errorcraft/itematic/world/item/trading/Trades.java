package net.errorcraft.itematic.world.item.trading;

import net.errorcraft.itematic.advancements.criterion.VillagerPredicate;
import net.errorcraft.itematic.core.registries.ItematicRegistries;
import net.errorcraft.itematic.mixin.world.entity.npc.villager.VillagerTradesAccessor;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.references.MobEffectIds;
import net.errorcraft.itematic.references.PotionIds;
import net.errorcraft.itematic.tags.PotionTags;
import net.errorcraft.itematic.world.item.trading.modifier.modifiers.EnchantWithLevelsTradeModifier;
import net.errorcraft.itematic.world.item.trading.modifier.modifiers.ItemFromTypeTradeModifier;
import net.errorcraft.itematic.world.item.trading.modifier.modifiers.SingleEnchantmentTradeModifier;
import net.errorcraft.itematic.world.level.storage.loot.functions.DyeItemModifier;
import net.errorcraft.itematic.world.level.storage.loot.functions.SetRandomPotionItemModifier;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.StructureTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.npc.villager.VillagerType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.ExplorationMapFunction;
import net.minecraft.world.level.storage.loot.functions.SequenceFunction;
import net.minecraft.world.level.storage.loot.functions.SetNameFunction;
import net.minecraft.world.level.storage.loot.functions.SetPotionFunction;
import net.minecraft.world.level.storage.loot.functions.SetStewEffectFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.List;
import java.util.Map;

public class Trades {
    public static final ResourceKey<Trade> BUY_WHEAT = of("buy_wheat");
    public static final ResourceKey<Trade> BUY_POTATO = of("buy_potato");
    public static final ResourceKey<Trade> BUY_CARROT = of("buy_carrot");
    public static final ResourceKey<Trade> BUY_BEETROOT = of("buy_beetroot");
    public static final ResourceKey<Trade> SELL_BREAD = of("sell_bread");
    public static final ResourceKey<Trade> BUY_PUMPKIN = of("buy_pumpkin");
    public static final ResourceKey<Trade> SELL_PUMPKIN_PIE = of("sell_pumpkin_pie");
    public static final ResourceKey<Trade> SELL_APPLE = of("sell_apple");
    public static final ResourceKey<Trade> SELL_COOKIE = of("sell_cookie");
    public static final ResourceKey<Trade> BUY_MELON = of("buy_melon");
    public static final ResourceKey<Trade> SELL_CAKE = of("sell_cake");
    public static final ResourceKey<Trade> SELL_NIGHT_VISION_SUSPICIOUS_STEW = of("sell_night_vision_suspicious_stew");
    public static final ResourceKey<Trade> SELL_JUMP_BOOST_SUSPICIOUS_STEW = of("sell_jump_boost_suspicious_stew");
    public static final ResourceKey<Trade> SELL_WEAKNESS_SUSPICIOUS_STEW = of("sell_weakness_suspicious_stew");
    public static final ResourceKey<Trade> SELL_BLINDNESS_SUSPICIOUS_STEW = of("sell_blindness_suspicious_stew");
    public static final ResourceKey<Trade> SELL_POISON_SUSPICIOUS_STEW = of("sell_poison_suspicious_stew");
    public static final ResourceKey<Trade> SELL_SATURATION_SUSPICIOUS_STEW = of("sell_saturation_suspicious_stew");
    public static final ResourceKey<Trade> SELL_GOLDEN_CARROT = of("sell_golden_carrot");
    public static final ResourceKey<Trade> SELL_GLISTERING_MELON_SLICE = of("sell_glistering_melon_slice");
    public static final ResourceKey<Trade> BUY_STRING_NOVICE = of("buy_string_novice");
    public static final ResourceKey<Trade> BUY_COAL = of("buy_coal");
    public static final ResourceKey<Trade> SELL_COOKED_COD_FROM_COD = of("sell_cooked_cod_from_cod");
    public static final ResourceKey<Trade> SELL_COD_BUCKET = of("sell_cod_bucket");
    public static final ResourceKey<Trade> BUY_COD = of("buy_cod");
    public static final ResourceKey<Trade> SELL_COOKED_SALMON_FROM_SALMON = of("sell_cooked_salmon_from_salmon");
    public static final ResourceKey<Trade> SELL_CAMPFIRE = of("sell_campfire");
    public static final ResourceKey<Trade> BUY_SALMON = of("buy_salmon");
    public static final ResourceKey<Trade> SELL_ENCHANTED_FISHING_ROD = of("sell_enchanted_fishing_rod");
    public static final ResourceKey<Trade> BUY_TROPICAL_FISH = of("buy_tropical_fish");
    public static final ResourceKey<Trade> BUY_PUFFERFISH = of("buy_pufferfish");
    public static final ResourceKey<Trade> BUY_BOAT = of("buy_boat");
    public static final ResourceKey<Trade> BUY_WHITE_WOOL = of("buy_white_wool");
    public static final ResourceKey<Trade> BUY_BROWN_WOOL = of("buy_brown_wool");
    public static final ResourceKey<Trade> BUY_BLACK_WOOL = of("buy_black_wool");
    public static final ResourceKey<Trade> BUY_GRAY_WOOL = of("buy_gray_wool");
    public static final ResourceKey<Trade> SELL_SHEARS = of("sell_shears");
    public static final ResourceKey<Trade> BUY_WHITE_DYE = of("buy_white_dye");
    public static final ResourceKey<Trade> BUY_GRAY_DYE = of("buy_gray_dye");
    public static final ResourceKey<Trade> BUY_BLACK_DYE = of("buy_black_dye");
    public static final ResourceKey<Trade> BUY_LIGHT_BLUE_DYE = of("buy_light_blue_dye");
    public static final ResourceKey<Trade> BUY_LIME_DYE = of("buy_lime_dye");
    public static final ResourceKey<Trade> SELL_WHITE_WOOL_SHEPHERD = of("sell_white_wool_shepherd");
    public static final ResourceKey<Trade> SELL_ORANGE_WOOL_SHEPHERD = of("sell_orange_wool_shepherd");
    public static final ResourceKey<Trade> SELL_MAGENTA_WOOL_SHEPHERD = of("sell_magenta_wool_shepherd");
    public static final ResourceKey<Trade> SELL_LIGHT_BLUE_WOOL_SHEPHERD = of("sell_light_blue_wool_shepherd");
    public static final ResourceKey<Trade> SELL_YELLOW_WOOL_SHEPHERD = of("sell_yellow_wool_shepherd");
    public static final ResourceKey<Trade> SELL_LIME_WOOL_SHEPHERD = of("sell_lime_wool_shepherd");
    public static final ResourceKey<Trade> SELL_PINK_WOOL_SHEPHERD = of("sell_pink_wool_shepherd");
    public static final ResourceKey<Trade> SELL_GRAY_WOOL_SHEPHERD = of("sell_gray_wool_shepherd");
    public static final ResourceKey<Trade> SELL_LIGHT_GRAY_WOOL_SHEPHERD = of("sell_light_gray_wool_shepherd");
    public static final ResourceKey<Trade> SELL_CYAN_WOOL_SHEPHERD = of("sell_cyan_wool_shepherd");
    public static final ResourceKey<Trade> SELL_PURPLE_WOOL_SHEPHERD = of("sell_purple_wool_shepherd");
    public static final ResourceKey<Trade> SELL_BLUE_WOOL_SHEPHERD = of("sell_blue_wool_shepherd");
    public static final ResourceKey<Trade> SELL_BROWN_WOOL_SHEPHERD = of("sell_brown_wool_shepherd");
    public static final ResourceKey<Trade> SELL_GREEN_WOOL_SHEPHERD = of("sell_green_wool_shepherd");
    public static final ResourceKey<Trade> SELL_RED_WOOL_SHEPHERD = of("sell_red_wool_shepherd");
    public static final ResourceKey<Trade> SELL_BLACK_WOOL_SHEPHERD = of("sell_black_wool_shepherd");
    public static final ResourceKey<Trade> SELL_WHITE_CARPET = of("sell_white_carpet");
    public static final ResourceKey<Trade> SELL_ORANGE_CARPET = of("sell_orange_carpet");
    public static final ResourceKey<Trade> SELL_MAGENTA_CARPET = of("sell_magenta_carpet");
    public static final ResourceKey<Trade> SELL_LIGHT_BLUE_CARPET = of("sell_light_blue_carpet");
    public static final ResourceKey<Trade> SELL_YELLOW_CARPET = of("sell_yellow_carpet");
    public static final ResourceKey<Trade> SELL_LIME_CARPET = of("sell_lime_carpet");
    public static final ResourceKey<Trade> SELL_PINK_CARPET = of("sell_pink_carpet");
    public static final ResourceKey<Trade> SELL_GRAY_CARPET = of("sell_gray_carpet");
    public static final ResourceKey<Trade> SELL_LIGHT_GRAY_CARPET = of("sell_light_gray_carpet");
    public static final ResourceKey<Trade> SELL_CYAN_CARPET = of("sell_cyan_carpet");
    public static final ResourceKey<Trade> SELL_PURPLE_CARPET = of("sell_purple_carpet");
    public static final ResourceKey<Trade> SELL_BLUE_CARPET = of("sell_blue_carpet");
    public static final ResourceKey<Trade> SELL_BROWN_CARPET = of("sell_brown_carpet");
    public static final ResourceKey<Trade> SELL_GREEN_CARPET = of("sell_green_carpet");
    public static final ResourceKey<Trade> SELL_RED_CARPET = of("sell_red_carpet");
    public static final ResourceKey<Trade> SELL_BLACK_CARPET = of("sell_black_carpet");
    public static final ResourceKey<Trade> BUY_YELLOW_DYE = of("buy_yellow_dye");
    public static final ResourceKey<Trade> BUY_LIGHT_GRAY_DYE = of("buy_light_gray_dye");
    public static final ResourceKey<Trade> BUY_ORANGE_DYE = of("buy_orange_dye");
    public static final ResourceKey<Trade> BUY_RED_DYE = of("buy_red_dye");
    public static final ResourceKey<Trade> BUY_PINK_DYE = of("buy_pink_dye");
    public static final ResourceKey<Trade> SELL_WHITE_BED = of("sell_white_bed");
    public static final ResourceKey<Trade> SELL_YELLOW_BED = of("sell_yellow_bed");
    public static final ResourceKey<Trade> SELL_RED_BED = of("sell_red_bed");
    public static final ResourceKey<Trade> SELL_BLACK_BED = of("sell_black_bed");
    public static final ResourceKey<Trade> SELL_BLUE_BED = of("sell_blue_bed");
    public static final ResourceKey<Trade> SELL_BROWN_BED = of("sell_brown_bed");
    public static final ResourceKey<Trade> SELL_CYAN_BED = of("sell_cyan_bed");
    public static final ResourceKey<Trade> SELL_GRAY_BED = of("sell_gray_bed");
    public static final ResourceKey<Trade> SELL_GREEN_BED = of("sell_green_bed");
    public static final ResourceKey<Trade> SELL_LIGHT_BLUE_BED = of("sell_light_blue_bed");
    public static final ResourceKey<Trade> SELL_LIGHT_GRAY_BED = of("sell_light_gray_bed");
    public static final ResourceKey<Trade> SELL_LIME_BED = of("sell_lime_bed");
    public static final ResourceKey<Trade> SELL_MAGENTA_BED = of("sell_magenta_bed");
    public static final ResourceKey<Trade> SELL_ORANGE_BED = of("sell_orange_bed");
    public static final ResourceKey<Trade> SELL_PINK_BED = of("sell_pink_bed");
    public static final ResourceKey<Trade> SELL_PURPLE_BED = of("sell_purple_bed");
    public static final ResourceKey<Trade> BUY_BROWN_DYE = of("buy_brown_dye");
    public static final ResourceKey<Trade> BUY_PURPLE_DYE = of("buy_purple_dye");
    public static final ResourceKey<Trade> BUY_BLUE_DYE = of("buy_blue_dye");
    public static final ResourceKey<Trade> BUY_GREEN_DYE = of("buy_green_dye");
    public static final ResourceKey<Trade> BUY_MAGENTA_DYE = of("buy_magenta_dye");
    public static final ResourceKey<Trade> BUY_CYAN_DYE = of("buy_cyan_dye");
    public static final ResourceKey<Trade> SELL_WHITE_BANNER = of("sell_white_banner");
    public static final ResourceKey<Trade> SELL_BLUE_BANNER = of("sell_blue_banner");
    public static final ResourceKey<Trade> SELL_LIGHT_BLUE_BANNER = of("sell_light_blue_banner");
    public static final ResourceKey<Trade> SELL_RED_BANNER = of("sell_red_banner");
    public static final ResourceKey<Trade> SELL_PINK_BANNER = of("sell_pink_banner");
    public static final ResourceKey<Trade> SELL_GREEN_BANNER = of("sell_green_banner");
    public static final ResourceKey<Trade> SELL_LIME_BANNER = of("sell_lime_banner");
    public static final ResourceKey<Trade> SELL_GRAY_BANNER = of("sell_gray_banner");
    public static final ResourceKey<Trade> SELL_BLACK_BANNER = of("sell_black_banner");
    public static final ResourceKey<Trade> SELL_PURPLE_BANNER = of("sell_purple_banner");
    public static final ResourceKey<Trade> SELL_MAGENTA_BANNER = of("sell_magenta_banner");
    public static final ResourceKey<Trade> SELL_CYAN_BANNER = of("sell_cyan_banner");
    public static final ResourceKey<Trade> SELL_BROWN_BANNER = of("sell_brown_banner");
    public static final ResourceKey<Trade> SELL_YELLOW_BANNER = of("sell_yellow_banner");
    public static final ResourceKey<Trade> SELL_ORANGE_BANNER = of("sell_orange_banner");
    public static final ResourceKey<Trade> SELL_LIGHT_GRAY_BANNER = of("sell_light_gray_banner");
    public static final ResourceKey<Trade> SELL_PAINTING = of("sell_painting");
    public static final ResourceKey<Trade> BUY_STICK = of("buy_stick");
    public static final ResourceKey<Trade> SELL_ARROW = of("sell_arrow");
    public static final ResourceKey<Trade> SELL_FLINT_FROM_GRAVEL = of("sell_flint_from_gravel");
    public static final ResourceKey<Trade> BUY_FLINT_APPRENTICE = of("buy_flint_apprentice");
    public static final ResourceKey<Trade> SELL_BOW = of("sell_bow");
    public static final ResourceKey<Trade> BUY_STRING_JOURNEYMAN = of("buy_string_journeyman");
    public static final ResourceKey<Trade> SELL_CROSSBOW = of("sell_crossbow");
    public static final ResourceKey<Trade> BUY_FEATHER = of("buy_feather");
    public static final ResourceKey<Trade> SELL_ENCHANTED_BOW = of("sell_enchanted_bow");
    public static final ResourceKey<Trade> BUY_TRIPWIRE_HOOK = of("buy_tripwire_hook");
    public static final ResourceKey<Trade> SELL_ENCHANTED_CROSSBOW = of("sell_enchanted_crossbow");
    public static final ResourceKey<Trade> SELL_TIPPED_ARROW = of("sell_tipped_arrow");
    public static final ResourceKey<Trade> BUY_PAPER_LIBRARIAN = of("buy_paper_librarian");
    public static final ResourceKey<Trade> SELL_ENCHANTED_BOOK_NOVICE = of("sell_enchanted_book_novice");
    public static final ResourceKey<Trade> SELL_BOOKSHELF = of("sell_bookshelf");
    public static final ResourceKey<Trade> BUY_BOOK = of("buy_book");
    public static final ResourceKey<Trade> SELL_ENCHANTED_BOOK_APPRENTICE = of("sell_enchanted_book_apprentice");
    public static final ResourceKey<Trade> SELL_LANTERN = of("sell_lantern");
    public static final ResourceKey<Trade> BUY_INK_SAC = of("buy_ink_sac");
    public static final ResourceKey<Trade> SELL_ENCHANTED_BOOK_JOURNEYMAN = of("sell_enchanted_book_journeyman");
    public static final ResourceKey<Trade> SELL_GLASS = of("sell_glass");
    public static final ResourceKey<Trade> BUY_WRITABLE_BOOK = of("buy_writable_book");
    public static final ResourceKey<Trade> SELL_ENCHANTED_BOOK_EXPERT = of("sell_enchanted_book_expert");
    public static final ResourceKey<Trade> SELL_CLOCK = of("sell_clock");
    public static final ResourceKey<Trade> SELL_COMPASS = of("sell_compass");
    public static final ResourceKey<Trade> SELL_NAME_TAG = of("sell_name_tag");
    public static final ResourceKey<Trade> BUY_PAPER_CARTOGRAPHER = of("buy_paper_cartographer");
    public static final ResourceKey<Trade> SELL_MAP = of("sell_map");
    public static final ResourceKey<Trade> BUY_GLASS_PANE = of("buy_glass_pane");
    public static final ResourceKey<Trade> SELL_TAIGA_VILLAGE_MAP = of("sell_taiga_village_map");
    public static final ResourceKey<Trade> SELL_SWAMP_HUT_MAP = of("sell_swamp_hut_map");
    public static final ResourceKey<Trade> SELL_SNOWY_VILLAGE_MAP = of("sell_snowy_village_map");
    public static final ResourceKey<Trade> SELL_SAVANNA_VILLAGE_MAP = of("sell_savanna_village_map");
    public static final ResourceKey<Trade> SELL_PLAINS_VILLAGE_MAP = of("sell_plains_village_map");
    public static final ResourceKey<Trade> SELL_JUNGLE_TEMPLE_MAP = of("sell_jungle_temple_map");
    public static final ResourceKey<Trade> SELL_DESERT_VILLAGE_MAP = of("sell_desert_village_map");
    public static final ResourceKey<Trade> BUY_COMPASS = of("buy_compass");
    public static final ResourceKey<Trade> SELL_MONUMENT_MAP = of("sell_monument_map");
    public static final ResourceKey<Trade> SELL_TRIAL_CHAMBER_MAP = of("sell_trial_chamber_map");
    public static final ResourceKey<Trade> SELL_ITEM_FRAME = of("sell_item_frame");
    public static final ResourceKey<Trade> SELL_BLUE_BANNER_CARTOGRAPHER = of("sell_blue_banner_cartographer");
    public static final ResourceKey<Trade> SELL_WHITE_BANNER_CARTOGRAPHER = of("sell_white_banner_cartographer");
    public static final ResourceKey<Trade> SELL_RED_BANNER_CARTOGRAPHER = of("sell_red_banner_cartographer");
    public static final ResourceKey<Trade> SELL_GREEN_BANNER_CARTOGRAPHER = of("sell_green_banner_cartographer");
    public static final ResourceKey<Trade> SELL_LIME_BANNER_CARTOGRAPHER = of("sell_lime_banner_cartographer");
    public static final ResourceKey<Trade> SELL_PURPLE_BANNER_CARTOGRAPHER = of("sell_purple_banner_cartographer");
    public static final ResourceKey<Trade> SELL_CYAN_BANNER_CARTOGRAPHER = of("sell_cyan_banner_cartographer");
    public static final ResourceKey<Trade> SELL_YELLOW_BANNER_CARTOGRAPHER = of("sell_yellow_banner_cartographer");
    public static final ResourceKey<Trade> SELL_ORANGE_BANNER_CARTOGRAPHER = of("sell_orange_banner_cartographer");
    public static final ResourceKey<Trade> SELL_BROWN_BANNER_CARTOGRAPHER = of("sell_brown_banner_cartographer");
    public static final ResourceKey<Trade> SELL_MAGENTA_BANNER_CARTOGRAPHER = of("sell_magenta_banner_cartographer");
    public static final ResourceKey<Trade> SELL_LIGHT_BLUE_BANNER_CARTOGRAPHER = of("sell_light_blue_banner_cartographer");
    public static final ResourceKey<Trade> SELL_PINK_BANNER_CARTOGRAPHER = of("sell_pink_banner_cartographer");
    public static final ResourceKey<Trade> SELL_GRAY_BANNER_CARTOGRAPHER = of("sell_gray_banner_cartographer");
    public static final ResourceKey<Trade> SELL_BLACK_BANNER_CARTOGRAPHER = of("sell_black_banner_cartographer");
    public static final ResourceKey<Trade> SELL_GLOBE_BANNER_PATTERN = of("sell_globe_banner_pattern");
    public static final ResourceKey<Trade> SELL_MANSION_MAP = of("sell_mansion_map");
    public static final ResourceKey<Trade> BUY_ROTTEN_FLESH = of("buy_rotten_flesh");
    public static final ResourceKey<Trade> SELL_REDSTONE = of("sell_redstone");
    public static final ResourceKey<Trade> BUY_GOLD_INGOT = of("buy_gold_ingot");
    public static final ResourceKey<Trade> SELL_LAPIS_LAZULI = of("sell_lapis_lazuli");
    public static final ResourceKey<Trade> BUY_RABBIT_FOOT = of("buy_rabbit_foot");
    public static final ResourceKey<Trade> SELL_GLOWSTONE = of("sell_glowstone");
    public static final ResourceKey<Trade> BUY_TURTLE_SCUTE = of("buy_turtle_scute");
    public static final ResourceKey<Trade> BUY_GLASS_BOTTLE = of("buy_glass_bottle");
    public static final ResourceKey<Trade> SELL_ENDER_PEARL = of("sell_ender_pearl");
    public static final ResourceKey<Trade> BUY_NETHER_WART = of("buy_nether_wart");
    public static final ResourceKey<Trade> SELL_EXPERIENCE_BOTTLE = of("sell_experience_bottle");
    public static final ResourceKey<Trade> BUY_COAL_NOVICE_MORE_ITEMS = of("buy_coal_novice_more_items");
    public static final ResourceKey<Trade> SELL_IRON_LEGGINGS = of("sell_iron_leggings");
    public static final ResourceKey<Trade> SELL_IRON_BOOTS = of("sell_iron_boots");
    public static final ResourceKey<Trade> SELL_IRON_HELMET = of("sell_iron_helmet");
    public static final ResourceKey<Trade> SELL_IRON_CHESTPLATE = of("sell_iron_chestplate");
    public static final ResourceKey<Trade> BUY_IRON_INGOT = of("buy_iron_ingot");
    public static final ResourceKey<Trade> SELL_BELL = of("sell_bell");
    public static final ResourceKey<Trade> SELL_CHAINMAIL_BOOTS = of("sell_chainmail_boots");
    public static final ResourceKey<Trade> SELL_CHAINMAIL_LEGGINGS = of("sell_chainmail_leggings");
    public static final ResourceKey<Trade> BUY_LAVA_BUCKET = of("buy_lava_bucket");
    public static final ResourceKey<Trade> BUY_DIAMOND_JOURNEYMAN = of("buy_diamond_journeyman");
    public static final ResourceKey<Trade> SELL_CHAINMAIL_HELMET = of("sell_chainmail_helmet");
    public static final ResourceKey<Trade> SELL_CHAINMAIL_CHESTPLATE = of("sell_chainmail_chestplate");
    public static final ResourceKey<Trade> SELL_SHIELD = of("sell_shield");
    public static final ResourceKey<Trade> SELL_ENCHANTED_DIAMOND_LEGGINGS = of("sell_enchanted_diamond_leggings");
    public static final ResourceKey<Trade> SELL_ENCHANTED_DIAMOND_BOOTS = of("sell_enchanted_diamond_boots");
    public static final ResourceKey<Trade> SELL_ENCHANTED_DIAMOND_HELMET = of("sell_enchanted_diamond_helmet");
    public static final ResourceKey<Trade> SELL_ENCHANTED_DIAMOND_CHESTPLATE = of("sell_enchanted_diamond_chestplate");
    public static final ResourceKey<Trade> SELL_IRON_AXE = of("sell_iron_axe");
    public static final ResourceKey<Trade> SELL_ENCHANTED_IRON_SWORD = of("sell_enchanted_iron_sword");
    public static final ResourceKey<Trade> BUY_FLINT_WEAPONSMITH_JOURNEYMAN = of("buy_flint_weaponsmith_journeyman");
    public static final ResourceKey<Trade> BUY_DIAMOND_EXPERT = of("buy_diamond_expert");
    public static final ResourceKey<Trade> SELL_ENCHANTED_DIAMOND_AXE = of("sell_enchanted_diamond_axe");
    public static final ResourceKey<Trade> SELL_ENCHANTED_DIAMOND_SWORD = of("sell_enchanted_diamond_sword");
    public static final ResourceKey<Trade> SELL_STONE_AXE = of("sell_stone_axe");
    public static final ResourceKey<Trade> SELL_STONE_SHOVEL = of("sell_stone_shovel");
    public static final ResourceKey<Trade> SELL_STONE_PICKAXE = of("sell_stone_pickaxe");
    public static final ResourceKey<Trade> SELL_STONE_HOE = of("sell_stone_hoe");
    public static final ResourceKey<Trade> BUY_FLINT_TOOLSMITH_JOURNEYMAN = of("buy_flint_toolsmith_journeyman");
    public static final ResourceKey<Trade> SELL_ENCHANTED_IRON_AXE = of("sell_enchanted_iron_axe");
    public static final ResourceKey<Trade> SELL_ENCHANTED_IRON_SHOVEL = of("sell_enchanted_iron_shovel");
    public static final ResourceKey<Trade> SELL_ENCHANTED_IRON_PICKAXE = of("sell_enchanted_iron_pickaxe");
    public static final ResourceKey<Trade> SELL_DIAMOND_HOE = of("sell_diamond_hoe");
    public static final ResourceKey<Trade> SELL_ENCHANTED_DIAMOND_SHOVEL = of("sell_enchanted_diamond_shovel");
    public static final ResourceKey<Trade> SELL_ENCHANTED_DIAMOND_PICKAXE = of("sell_enchanted_diamond_pickaxe");
    public static final ResourceKey<Trade> BUY_CHICKEN = of("buy_chicken");
    public static final ResourceKey<Trade> BUY_PORKCHOP = of("buy_porkchop");
    public static final ResourceKey<Trade> BUY_RABBIT = of("buy_rabbit");
    public static final ResourceKey<Trade> SELL_RABBIT_STEW = of("sell_rabbit_stew");
    public static final ResourceKey<Trade> SELL_COOKED_PORKCHOP = of("sell_cooked_porkchop");
    public static final ResourceKey<Trade> SELL_COOKED_CHICKEN = of("sell_cooked_chicken");
    public static final ResourceKey<Trade> BUY_MUTTON = of("buy_mutton");
    public static final ResourceKey<Trade> BUY_BEEF = of("buy_beef");
    public static final ResourceKey<Trade> BUY_DRIED_KELP_BLOCK = of("buy_dried_kelp_block");
    public static final ResourceKey<Trade> BUY_SWEET_BERRIES = of("buy_sweet_berries");
    public static final ResourceKey<Trade> BUY_LEATHER = of("buy_leather");
    public static final ResourceKey<Trade> SELL_LEATHER_LEGGINGS = of("sell_leather_leggings");
    public static final ResourceKey<Trade> SELL_LEATHER_CHESTPLATE = of("sell_leather_chestplate");
    public static final ResourceKey<Trade> SELL_LEATHER_HELMET_APPRENTICE = of("sell_leather_helmet_apprentice");
    public static final ResourceKey<Trade> SELL_LEATHER_BOOTS = of("sell_leather_boots");
    public static final ResourceKey<Trade> BUY_RABBIT_HIDE = of("buy_rabbit_hide");
    public static final ResourceKey<Trade> SELL_LEATHER_HORSE_ARMOR = of("sell_leather_horse_armor");
    public static final ResourceKey<Trade> SELL_SADDLE = of("sell_saddle");
    public static final ResourceKey<Trade> SELL_LEATHER_HELMET_MASTER = of("sell_leather_helmet_master");
    public static final ResourceKey<Trade> BUY_CLAY_BALL = of("buy_clay_ball");
    public static final ResourceKey<Trade> SELL_BRICK = of("sell_brick");
    public static final ResourceKey<Trade> BUY_STONE = of("buy_stone");
    public static final ResourceKey<Trade> SELL_CHISELED_STONE_BRICKS = of("sell_chiseled_stone_bricks");
    public static final ResourceKey<Trade> BUY_GRANITE = of("buy_granite");
    public static final ResourceKey<Trade> BUY_ANDESITE = of("buy_andesite");
    public static final ResourceKey<Trade> BUY_DIORITE = of("buy_diorite");
    public static final ResourceKey<Trade> SELL_DRIPSTONE_BLOCK = of("sell_dripstone_block");
    public static final ResourceKey<Trade> SELL_POLISHED_ANDESITE = of("sell_polished_andesite");
    public static final ResourceKey<Trade> SELL_POLISHED_DIORITE = of("sell_polished_diorite");
    public static final ResourceKey<Trade> SELL_POLISHED_GRANITE = of("sell_polished_granite");
    public static final ResourceKey<Trade> BUY_QUARTZ = of("buy_quartz");
    public static final ResourceKey<Trade> SELL_ORANGE_TERRACOTTA = of("sell_orange_terracotta");
    public static final ResourceKey<Trade> SELL_WHITE_TERRACOTTA = of("sell_white_terracotta");
    public static final ResourceKey<Trade> SELL_BLUE_TERRACOTTA = of("sell_blue_terracotta");
    public static final ResourceKey<Trade> SELL_LIGHT_BLUE_TERRACOTTA = of("sell_light_blue_terracotta");
    public static final ResourceKey<Trade> SELL_GRAY_TERRACOTTA = of("sell_gray_terracotta");
    public static final ResourceKey<Trade> SELL_LIGHT_GRAY_TERRACOTTA = of("sell_light_gray_terracotta");
    public static final ResourceKey<Trade> SELL_BLACK_TERRACOTTA = of("sell_black_terracotta");
    public static final ResourceKey<Trade> SELL_RED_TERRACOTTA = of("sell_red_terracotta");
    public static final ResourceKey<Trade> SELL_PINK_TERRACOTTA = of("sell_pink_terracotta");
    public static final ResourceKey<Trade> SELL_MAGENTA_TERRACOTTA = of("sell_magenta_terracotta");
    public static final ResourceKey<Trade> SELL_LIME_TERRACOTTA = of("sell_lime_terracotta");
    public static final ResourceKey<Trade> SELL_GREEN_TERRACOTTA = of("sell_green_terracotta");
    public static final ResourceKey<Trade> SELL_CYAN_TERRACOTTA = of("sell_cyan_terracotta");
    public static final ResourceKey<Trade> SELL_PURPLE_TERRACOTTA = of("sell_purple_terracotta");
    public static final ResourceKey<Trade> SELL_YELLOW_TERRACOTTA = of("sell_yellow_terracotta");
    public static final ResourceKey<Trade> SELL_BROWN_TERRACOTTA = of("sell_brown_terracotta");
    public static final ResourceKey<Trade> SELL_ORANGE_GLAZED_TERRACOTTA = of("sell_orange_glazed_terracotta");
    public static final ResourceKey<Trade> SELL_WHITE_GLAZED_TERRACOTTA = of("sell_white_glazed_terracotta");
    public static final ResourceKey<Trade> SELL_BLUE_GLAZED_TERRACOTTA = of("sell_blue_glazed_terracotta");
    public static final ResourceKey<Trade> SELL_LIGHT_BLUE_GLAZED_TERRACOTTA = of("sell_light_blue_glazed_terracotta");
    public static final ResourceKey<Trade> SELL_GRAY_GLAZED_TERRACOTTA = of("sell_gray_glazed_terracotta");
    public static final ResourceKey<Trade> SELL_LIGHT_GRAY_GLAZED_TERRACOTTA = of("sell_light_gray_glazed_terracotta");
    public static final ResourceKey<Trade> SELL_BLACK_GLAZED_TERRACOTTA = of("sell_black_glazed_terracotta");
    public static final ResourceKey<Trade> SELL_RED_GLAZED_TERRACOTTA = of("sell_red_glazed_terracotta");
    public static final ResourceKey<Trade> SELL_PINK_GLAZED_TERRACOTTA = of("sell_pink_glazed_terracotta");
    public static final ResourceKey<Trade> SELL_MAGENTA_GLAZED_TERRACOTTA = of("sell_magenta_glazed_terracotta");
    public static final ResourceKey<Trade> SELL_LIME_GLAZED_TERRACOTTA = of("sell_lime_glazed_terracotta");
    public static final ResourceKey<Trade> SELL_GREEN_GLAZED_TERRACOTTA = of("sell_green_glazed_terracotta");
    public static final ResourceKey<Trade> SELL_CYAN_GLAZED_TERRACOTTA = of("sell_cyan_glazed_terracotta");
    public static final ResourceKey<Trade> SELL_PURPLE_GLAZED_TERRACOTTA = of("sell_purple_glazed_terracotta");
    public static final ResourceKey<Trade> SELL_YELLOW_GLAZED_TERRACOTTA = of("sell_yellow_glazed_terracotta");
    public static final ResourceKey<Trade> SELL_BROWN_GLAZED_TERRACOTTA = of("sell_brown_glazed_terracotta");
    public static final ResourceKey<Trade> SELL_QUARTZ_PILLAR = of("sell_quartz_pillar");
    public static final ResourceKey<Trade> SELL_QUARTZ_BLOCK = of("sell_quartz_block");
    public static final ResourceKey<Trade> BUY_WATER_BOTTLE = of("buy_water_bottle");
    public static final ResourceKey<Trade> BUY_WATER_BUCKET = of("buy_water_bucket");
    public static final ResourceKey<Trade> BUY_MILK_BUCKET = of("buy_milk_bucket");
    public static final ResourceKey<Trade> BUY_FERMENTED_SPIDER_EYE = of("buy_fermented_spider_eye");
    public static final ResourceKey<Trade> BUY_BAKED_POTATO = of("buy_baked_potato");
    public static final ResourceKey<Trade> BUY_HAY_BLOCK = of("buy_hay_block");
    public static final ResourceKey<Trade> SELL_PACKED_ICE = of("sell_packed_ice");
    public static final ResourceKey<Trade> SELL_BLUE_ICE = of("sell_blue_ice");
    public static final ResourceKey<Trade> SELL_GUNPOWDER = of("sell_gunpowder");
    public static final ResourceKey<Trade> SELL_PODZOL = of("sell_podzol");
    public static final ResourceKey<Trade> SELL_ACACIA_LOG = of("sell_acacia_log");
    public static final ResourceKey<Trade> SELL_BIRCH_LOG = of("sell_birch_log");
    public static final ResourceKey<Trade> SELL_DARK_OAK_LOG = of("sell_dark_oak_log");
    public static final ResourceKey<Trade> SELL_JUNGLE_LOG = of("sell_jungle_log");
    public static final ResourceKey<Trade> SELL_OAK_LOG = of("sell_oak_log");
    public static final ResourceKey<Trade> SELL_SPRUCE_LOG = of("sell_spruce_log");
    public static final ResourceKey<Trade> SELL_CHERRY_LOG = of("sell_cherry_log");
    public static final ResourceKey<Trade> SELL_MANGROVE_LOG = of("sell_mangrove_log");
    public static final ResourceKey<Trade> SELL_PALE_OAK_LOG = of("sell_pale_oak_log");
    public static final ResourceKey<Trade> SELL_ENCHANTED_IRON_PICKAXE_WANDERING_TRADER = of("sell_enchanted_iron_pickaxe_wandering_trader");
    public static final ResourceKey<Trade> SELL_LONG_INVISIBILITY_POTION = of("sell_long_invisibility_potion");
    public static final ResourceKey<Trade> SELL_TROPICAL_FISH_BUCKET = of("sell_tropical_fish_bucket");
    public static final ResourceKey<Trade> SELL_PUFFERFISH_BUCKET = of("sell_pufferfish_bucket");
    public static final ResourceKey<Trade> SELL_SEA_PICKLE = of("sell_sea_pickle");
    public static final ResourceKey<Trade> SELL_SLIME_BALL = of("sell_slime_ball");
    public static final ResourceKey<Trade> SELL_GLOWSTONE_WANDERING_TRADER = of("sell_glowstone_wandering_trader");
    public static final ResourceKey<Trade> SELL_NAUTILUS_SHELL = of("sell_nautilus_shell");
    public static final ResourceKey<Trade> SELL_FERN = of("sell_fern");
    public static final ResourceKey<Trade> SELL_SUGAR_CANE = of("sell_sugar_cane");
    public static final ResourceKey<Trade> SELL_PUMPKIN = of("sell_pumpkin");
    public static final ResourceKey<Trade> SELL_KELP = of("sell_kelp");
    public static final ResourceKey<Trade> SELL_CACTUS = of("sell_cactus");
    public static final ResourceKey<Trade> SELL_DANDELION = of("sell_dandelion");
    public static final ResourceKey<Trade> SELL_POPPY = of("sell_poppy");
    public static final ResourceKey<Trade> SELL_BLUE_ORCHID = of("sell_blue_orchid");
    public static final ResourceKey<Trade> SELL_ALLIUM = of("sell_allium");
    public static final ResourceKey<Trade> SELL_AZURE_BLUET = of("sell_azure_bluet");
    public static final ResourceKey<Trade> SELL_RED_TULIP = of("sell_red_tulip");
    public static final ResourceKey<Trade> SELL_ORANGE_TULIP = of("sell_orange_tulip");
    public static final ResourceKey<Trade> SELL_WHITE_TULIP = of("sell_white_tulip");
    public static final ResourceKey<Trade> SELL_PINK_TULIP = of("sell_pink_tulip");
    public static final ResourceKey<Trade> SELL_OXEYE_DAISY = of("sell_oxeye_daisy");
    public static final ResourceKey<Trade> SELL_CORNFLOWER = of("sell_cornflower");
    public static final ResourceKey<Trade> SELL_LILY_OF_THE_VALLEY = of("sell_lily_of_the_valley");
    public static final ResourceKey<Trade> SELL_OPEN_EYEBLOSSOM = of("sell_open_eyeblossom");
    public static final ResourceKey<Trade> SELL_WHEAT_SEEDS = of("sell_wheat_seeds");
    public static final ResourceKey<Trade> SELL_BEETROOT_SEEDS = of("sell_beetroot_seeds");
    public static final ResourceKey<Trade> SELL_PUMPKIN_SEEDS = of("sell_pumpkin_seeds");
    public static final ResourceKey<Trade> SELL_MELON_SEEDS = of("sell_melon_seeds");
    public static final ResourceKey<Trade> SELL_ACACIA_SAPLING = of("sell_acacia_sapling");
    public static final ResourceKey<Trade> SELL_BIRCH_SAPLING = of("sell_birch_sapling");
    public static final ResourceKey<Trade> SELL_DARK_OAK_SAPLING = of("sell_dark_oak_sapling");
    public static final ResourceKey<Trade> SELL_JUNGLE_SAPLING = of("sell_jungle_sapling");
    public static final ResourceKey<Trade> SELL_OAK_SAPLING = of("sell_oak_sapling");
    public static final ResourceKey<Trade> SELL_SPRUCE_SAPLING = of("sell_spruce_sapling");
    public static final ResourceKey<Trade> SELL_CHERRY_SAPLING = of("sell_cherry_sapling");
    public static final ResourceKey<Trade> SELL_PALE_OAK_SAPLING = of("sell_pale_oak_sapling");
    public static final ResourceKey<Trade> SELL_MANGROVE_PROPAGULE = of("sell_mangrove_propagule");
    public static final ResourceKey<Trade> SELL_RED_DYE = of("sell_red_dye");
    public static final ResourceKey<Trade> SELL_WHITE_DYE = of("sell_white_dye");
    public static final ResourceKey<Trade> SELL_BLUE_DYE = of("sell_blue_dye");
    public static final ResourceKey<Trade> SELL_PINK_DYE = of("sell_pink_dye");
    public static final ResourceKey<Trade> SELL_BLACK_DYE = of("sell_black_dye");
    public static final ResourceKey<Trade> SELL_GREEN_DYE = of("sell_green_dye");
    public static final ResourceKey<Trade> SELL_LIGHT_GRAY_DYE = of("sell_light_gray_dye");
    public static final ResourceKey<Trade> SELL_MAGENTA_DYE = of("sell_magenta_dye");
    public static final ResourceKey<Trade> SELL_YELLOW_DYE = of("sell_yellow_dye");
    public static final ResourceKey<Trade> SELL_GRAY_DYE = of("sell_gray_dye");
    public static final ResourceKey<Trade> SELL_PURPLE_DYE = of("sell_purple_dye");
    public static final ResourceKey<Trade> SELL_LIGHT_BLUE_DYE = of("sell_light_blue_dye");
    public static final ResourceKey<Trade> SELL_LIME_DYE = of("sell_lime_dye");
    public static final ResourceKey<Trade> SELL_ORANGE_DYE = of("sell_orange_dye");
    public static final ResourceKey<Trade> SELL_BROWN_DYE = of("sell_brown_dye");
    public static final ResourceKey<Trade> SELL_CYAN_DYE = of("sell_cyan_dye");
    public static final ResourceKey<Trade> SELL_BRAIN_CORAL_BLOCK = of("sell_brain_coral_block");
    public static final ResourceKey<Trade> SELL_BUBBLE_CORAL_BLOCK = of("sell_bubble_coral_block");
    public static final ResourceKey<Trade> SELL_FIRE_CORAL_BLOCK = of("sell_fire_coral_block");
    public static final ResourceKey<Trade> SELL_HORN_CORAL_BLOCK = of("sell_horn_coral_block");
    public static final ResourceKey<Trade> SELL_TUBE_CORAL_BLOCK = of("sell_tube_coral_block");
    public static final ResourceKey<Trade> SELL_VINE = of("sell_vine");
    public static final ResourceKey<Trade> SELL_PALE_HANGING_MOSS = of("sell_pale_hanging_moss");
    public static final ResourceKey<Trade> SELL_BROWN_MUSHROOM = of("sell_brown_mushroom");
    public static final ResourceKey<Trade> SELL_RED_MUSHROOM = of("sell_red_mushroom");
    public static final ResourceKey<Trade> SELL_LILY_PAD = of("sell_lily_pad");
    public static final ResourceKey<Trade> SELL_SMALL_DRIPLEAF = of("sell_small_dripleaf");
    public static final ResourceKey<Trade> SELL_SAND = of("sell_sand");
    public static final ResourceKey<Trade> SELL_RED_SAND = of("sell_red_sand");
    public static final ResourceKey<Trade> SELL_POINTED_DRIPSTONE = of("sell_pointed_dripstone");
    public static final ResourceKey<Trade> SELL_ROOTED_DIRT = of("sell_rooted_dirt");
    public static final ResourceKey<Trade> SELL_MOSS_BLOCK = of("sell_moss_block");
    public static final ResourceKey<Trade> SELL_PALE_MOSS_BLOCK = of("sell_pale_moss_block");
    public static final ResourceKey<Trade> SELL_WILDFLOWERS = of("sell_wildflowers");
    public static final ResourceKey<Trade> SELL_TALL_DRY_GRASS = of("sell_tall_dry_grass");
    public static final ResourceKey<Trade> SELL_FIREFLY_BUSH = of("sell_firefly_bush");

    private Trades() {}

    public static void bootstrap(BootstrapContext<Trade> registerable) {
        HolderGetter<Item> items = registerable.lookup(Registries.ITEM);
        HolderGetter<Enchantment> enchantments = registerable.lookup(Registries.ENCHANTMENT);
        HolderGetter<MobEffect> statusEffects = registerable.lookup(Registries.MOB_EFFECT);
        HolderGetter<Potion> potions = registerable.lookup(Registries.POTION);
        HolderGetter<VillagerType> villagerTypes = registerable.lookup(Registries.VILLAGER_TYPE);

        registerable.register(BUY_WHEAT, buy(items, items.getOrThrow(ItemIds.WHEAT), 20, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.noviceBuyTradeExperience()));
        registerable.register(BUY_POTATO, buy(items, items.getOrThrow(ItemIds.POTATO), 26, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.noviceBuyTradeExperience()));
        registerable.register(BUY_CARROT, buy(items, items.getOrThrow(ItemIds.CARROT), 22, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.noviceBuyTradeExperience()));
        registerable.register(BUY_BEETROOT, buy(items, items.getOrThrow(ItemIds.BEETROOT), 15, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.noviceBuyTradeExperience()));
        registerable.register(SELL_BREAD, sell(items, items.getOrThrow(ItemIds.BREAD), 6, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.noviceSellTradeExperience(), 1));
        registerable.register(BUY_PUMPKIN, buy(items, items.getOrThrow(ItemIds.PUMPKIN), 6, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.apprenticeBuyTradeExperience()));
        registerable.register(SELL_PUMPKIN_PIE, sell(items, items.getOrThrow(ItemIds.PUMPKIN_PIE), 4, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_APPLE, sell(items, items.getOrThrow(ItemIds.APPLE), 4, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_COOKIE, sell(items, items.getOrThrow(ItemIds.COOKIE), 18, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.journeymanSellTradeExperience(), 3));
        registerable.register(BUY_MELON, buy(items, items.getOrThrow(ItemIds.MELON), 4, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.journeymanBuyTradeExperience()));
        registerable.register(SELL_CAKE, sell(items, items.getOrThrow(ItemIds.CAKE), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertSellTradeExperience(), 1));
        registerable.register(SELL_NIGHT_VISION_SUSPICIOUS_STEW, sellSuspiciousStew(items, statusEffects.getOrThrow(MobEffectIds.NIGHT_VISION), 100));
        registerable.register(SELL_JUMP_BOOST_SUSPICIOUS_STEW, sellSuspiciousStew(items, statusEffects.getOrThrow(MobEffectIds.JUMP_BOOST), 160));
        registerable.register(SELL_WEAKNESS_SUSPICIOUS_STEW, sellSuspiciousStew(items, statusEffects.getOrThrow(MobEffectIds.WEAKNESS), 140));
        registerable.register(SELL_BLINDNESS_SUSPICIOUS_STEW, sellSuspiciousStew(items, statusEffects.getOrThrow(MobEffectIds.BLINDNESS), 120));
        registerable.register(SELL_POISON_SUSPICIOUS_STEW, sellSuspiciousStew(items, statusEffects.getOrThrow(MobEffectIds.POISON), 280));
        registerable.register(SELL_SATURATION_SUSPICIOUS_STEW, sellSuspiciousStew(items, statusEffects.getOrThrow(MobEffectIds.SATURATION), 7));
        registerable.register(SELL_GOLDEN_CARROT, sell(items, items.getOrThrow(ItemIds.GOLDEN_CARROT), 3, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.masterTradeExperience(), 3));
        registerable.register(SELL_GLISTERING_MELON_SLICE, sell(items, items.getOrThrow(ItemIds.GLISTERING_MELON_SLICE), 3, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.masterTradeExperience(), 4));
        registerable.register(BUY_STRING_NOVICE, buy(items, items.getOrThrow(ItemIds.STRING), 20, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.noviceBuyTradeExperience()));
        registerable.register(BUY_COAL, buy(items, items.getOrThrow(ItemIds.COAL), 10, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.noviceBuyTradeExperience()));
        registerable.register(SELL_COOKED_COD_FROM_COD, sell(items, items.getOrThrow(ItemIds.COD), 6, items.getOrThrow(ItemIds.COOKED_COD), 6, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.noviceSellTradeExperience(), VillagerTradesAccessor.lowPriceMultiplier()));
        registerable.register(SELL_COD_BUCKET, sell(items, items.getOrThrow(ItemIds.COD_BUCKET), 1, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.noviceSellTradeExperience(), 3));
        registerable.register(BUY_COD, buy(items, items.getOrThrow(ItemIds.COD), 15, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.apprenticeBuyTradeExperience()));
        registerable.register(SELL_COOKED_SALMON_FROM_SALMON, sell(items, items.getOrThrow(ItemIds.SALMON), 6, items.getOrThrow(ItemIds.COOKED_SALMON), 6, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.apprenticeSellTradeExperience(), VillagerTradesAccessor.lowPriceMultiplier()));
        registerable.register(SELL_CAMPFIRE, sell(items, items.getOrThrow(ItemIds.CAMPFIRE), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.apprenticeSellTradeExperience(), 2));
        registerable.register(BUY_SALMON, buy(items, items.getOrThrow(ItemIds.SALMON), 13, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.journeymanBuyTradeExperience()));
        registerable.register(SELL_ENCHANTED_FISHING_ROD, sellEnchantedItem(items, items.getOrThrow(ItemIds.FISHING_ROD), VillagerTradesAccessor.journeymanSellTradeExperience(), 3, VillagerTradesAccessor.highPriceMultiplier()));
        registerable.register(BUY_TROPICAL_FISH, buy(items, items.getOrThrow(ItemIds.TROPICAL_FISH), 6, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertBuyTradeExperience()));
        registerable.register(BUY_PUFFERFISH, buy(items, items.getOrThrow(ItemIds.PUFFERFISH), 4, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.masterTradeExperience()));
        registerable.register(BUY_BOAT, buyFromType(items, items.getOrThrow(ItemIds.OAK_BOAT), Map.of(
            villagerTypes.getOrThrow(VillagerType.PLAINS), items.getOrThrow(ItemIds.OAK_BOAT),
            villagerTypes.getOrThrow(VillagerType.TAIGA), items.getOrThrow(ItemIds.SPRUCE_BOAT),
            villagerTypes.getOrThrow(VillagerType.SNOW), items.getOrThrow(ItemIds.SPRUCE_BOAT),
            villagerTypes.getOrThrow(VillagerType.DESERT), items.getOrThrow(ItemIds.JUNGLE_BOAT),
            villagerTypes.getOrThrow(VillagerType.JUNGLE), items.getOrThrow(ItemIds.JUNGLE_BOAT),
            villagerTypes.getOrThrow(VillagerType.SAVANNA), items.getOrThrow(ItemIds.ACACIA_BOAT),
            villagerTypes.getOrThrow(VillagerType.SWAMP), items.getOrThrow(ItemIds.DARK_OAK_BOAT)
        )));
        registerable.register(BUY_WHITE_WOOL, buy(items, items.getOrThrow(ItemIds.WHITE_WOOL), 18, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.noviceBuyTradeExperience()));
        registerable.register(BUY_BROWN_WOOL, buy(items, items.getOrThrow(ItemIds.BROWN_WOOL), 18, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.noviceBuyTradeExperience()));
        registerable.register(BUY_BLACK_WOOL, buy(items, items.getOrThrow(ItemIds.BLACK_WOOL), 18, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.noviceBuyTradeExperience()));
        registerable.register(BUY_GRAY_WOOL, buy(items, items.getOrThrow(ItemIds.GRAY_WOOL), 18, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.noviceBuyTradeExperience()));
        registerable.register(SELL_SHEARS, sell(items, items.getOrThrow(ItemIds.SHEARS), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.noviceSellTradeExperience(), 2));
        registerable.register(BUY_WHITE_DYE, buy(items, items.getOrThrow(ItemIds.WHITE_DYE), 12, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.apprenticeBuyTradeExperience()));
        registerable.register(BUY_GRAY_DYE, buy(items, items.getOrThrow(ItemIds.GRAY_DYE), 12, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.apprenticeBuyTradeExperience()));
        registerable.register(BUY_BLACK_DYE, buy(items, items.getOrThrow(ItemIds.BLACK_DYE), 12, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.apprenticeBuyTradeExperience()));
        registerable.register(BUY_LIGHT_BLUE_DYE, buy(items, items.getOrThrow(ItemIds.LIGHT_BLUE_DYE), 12, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.apprenticeBuyTradeExperience()));
        registerable.register(BUY_LIME_DYE, buy(items, items.getOrThrow(ItemIds.LIME_DYE), 12, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.apprenticeBuyTradeExperience()));
        registerable.register(SELL_WHITE_WOOL_SHEPHERD, sell(items, items.getOrThrow(ItemIds.WHITE_WOOL), 1, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_ORANGE_WOOL_SHEPHERD, sell(items, items.getOrThrow(ItemIds.ORANGE_WOOL), 1, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_MAGENTA_WOOL_SHEPHERD, sell(items, items.getOrThrow(ItemIds.MAGENTA_WOOL), 1, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_LIGHT_BLUE_WOOL_SHEPHERD, sell(items, items.getOrThrow(ItemIds.LIGHT_BLUE_WOOL), 1, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_YELLOW_WOOL_SHEPHERD, sell(items, items.getOrThrow(ItemIds.YELLOW_WOOL), 1, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_LIME_WOOL_SHEPHERD, sell(items, items.getOrThrow(ItemIds.LIME_WOOL), 1, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_PINK_WOOL_SHEPHERD, sell(items, items.getOrThrow(ItemIds.PINK_WOOL), 1, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_GRAY_WOOL_SHEPHERD, sell(items, items.getOrThrow(ItemIds.GRAY_WOOL), 1, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_LIGHT_GRAY_WOOL_SHEPHERD, sell(items, items.getOrThrow(ItemIds.LIGHT_GRAY_WOOL), 1, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_CYAN_WOOL_SHEPHERD, sell(items, items.getOrThrow(ItemIds.CYAN_WOOL), 1, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_PURPLE_WOOL_SHEPHERD, sell(items, items.getOrThrow(ItemIds.PURPLE_WOOL), 1, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_BLUE_WOOL_SHEPHERD, sell(items, items.getOrThrow(ItemIds.BLUE_WOOL), 1, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_BROWN_WOOL_SHEPHERD, sell(items, items.getOrThrow(ItemIds.BROWN_WOOL), 1, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_GREEN_WOOL_SHEPHERD, sell(items, items.getOrThrow(ItemIds.GREEN_WOOL), 1, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_RED_WOOL_SHEPHERD, sell(items, items.getOrThrow(ItemIds.RED_WOOL), 1, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_BLACK_WOOL_SHEPHERD, sell(items, items.getOrThrow(ItemIds.BLACK_WOOL), 1, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_WHITE_CARPET, sell(items, items.getOrThrow(ItemIds.WHITE_CARPET), 4, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_ORANGE_CARPET, sell(items, items.getOrThrow(ItemIds.ORANGE_CARPET), 4, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_MAGENTA_CARPET, sell(items, items.getOrThrow(ItemIds.MAGENTA_CARPET), 4, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_LIGHT_BLUE_CARPET, sell(items, items.getOrThrow(ItemIds.LIGHT_BLUE_CARPET), 4, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_YELLOW_CARPET, sell(items, items.getOrThrow(ItemIds.YELLOW_CARPET), 4, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_LIME_CARPET, sell(items, items.getOrThrow(ItemIds.LIME_CARPET), 4, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_PINK_CARPET, sell(items, items.getOrThrow(ItemIds.PINK_CARPET), 4, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_GRAY_CARPET, sell(items, items.getOrThrow(ItemIds.GRAY_CARPET), 4, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_LIGHT_GRAY_CARPET, sell(items, items.getOrThrow(ItemIds.LIGHT_GRAY_CARPET), 4, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_CYAN_CARPET, sell(items, items.getOrThrow(ItemIds.CYAN_CARPET), 4, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_PURPLE_CARPET, sell(items, items.getOrThrow(ItemIds.PURPLE_CARPET), 4, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_BLUE_CARPET, sell(items, items.getOrThrow(ItemIds.BLUE_CARPET), 4, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_BROWN_CARPET, sell(items, items.getOrThrow(ItemIds.BROWN_CARPET), 4, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_GREEN_CARPET, sell(items, items.getOrThrow(ItemIds.GREEN_CARPET), 4, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_RED_CARPET, sell(items, items.getOrThrow(ItemIds.RED_CARPET), 4, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_BLACK_CARPET, sell(items, items.getOrThrow(ItemIds.BLACK_CARPET), 4, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(BUY_YELLOW_DYE, buy(items, items.getOrThrow(ItemIds.YELLOW_DYE), 12, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.journeymanBuyTradeExperience()));
        registerable.register(BUY_LIGHT_GRAY_DYE, buy(items, items.getOrThrow(ItemIds.LIGHT_GRAY_DYE), 12, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.journeymanBuyTradeExperience()));
        registerable.register(BUY_ORANGE_DYE, buy(items, items.getOrThrow(ItemIds.ORANGE_DYE), 12, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.journeymanBuyTradeExperience()));
        registerable.register(BUY_RED_DYE, buy(items, items.getOrThrow(ItemIds.RED_DYE), 12, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.journeymanBuyTradeExperience()));
        registerable.register(BUY_PINK_DYE, buy(items, items.getOrThrow(ItemIds.PINK_DYE), 12, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.journeymanBuyTradeExperience()));
        registerable.register(SELL_WHITE_BED, sell(items, items.getOrThrow(ItemIds.WHITE_BED), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.journeymanSellTradeExperience(), 3));
        registerable.register(SELL_YELLOW_BED, sell(items, items.getOrThrow(ItemIds.YELLOW_BED), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.journeymanSellTradeExperience(), 3));
        registerable.register(SELL_RED_BED, sell(items, items.getOrThrow(ItemIds.RED_BED), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.journeymanSellTradeExperience(), 3));
        registerable.register(SELL_BLACK_BED, sell(items, items.getOrThrow(ItemIds.BLACK_BED), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.journeymanSellTradeExperience(), 3));
        registerable.register(SELL_BLUE_BED, sell(items, items.getOrThrow(ItemIds.BLUE_BED), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.journeymanSellTradeExperience(), 3));
        registerable.register(SELL_BROWN_BED, sell(items, items.getOrThrow(ItemIds.BROWN_BED), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.journeymanSellTradeExperience(), 3));
        registerable.register(SELL_CYAN_BED, sell(items, items.getOrThrow(ItemIds.CYAN_BED), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.journeymanSellTradeExperience(), 3));
        registerable.register(SELL_GRAY_BED, sell(items, items.getOrThrow(ItemIds.GRAY_BED), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.journeymanSellTradeExperience(), 3));
        registerable.register(SELL_GREEN_BED, sell(items, items.getOrThrow(ItemIds.GREEN_BED), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.journeymanSellTradeExperience(), 3));
        registerable.register(SELL_LIGHT_BLUE_BED, sell(items, items.getOrThrow(ItemIds.LIGHT_BLUE_BED), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.journeymanSellTradeExperience(), 3));
        registerable.register(SELL_LIGHT_GRAY_BED, sell(items, items.getOrThrow(ItemIds.LIGHT_GRAY_BED), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.journeymanSellTradeExperience(), 3));
        registerable.register(SELL_LIME_BED, sell(items, items.getOrThrow(ItemIds.LIME_BED), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.journeymanSellTradeExperience(), 3));
        registerable.register(SELL_MAGENTA_BED, sell(items, items.getOrThrow(ItemIds.MAGENTA_BED), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.journeymanSellTradeExperience(), 3));
        registerable.register(SELL_ORANGE_BED, sell(items, items.getOrThrow(ItemIds.ORANGE_BED), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.journeymanSellTradeExperience(), 3));
        registerable.register(SELL_PINK_BED, sell(items, items.getOrThrow(ItemIds.PINK_BED), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.journeymanSellTradeExperience(), 3));
        registerable.register(SELL_PURPLE_BED, sell(items, items.getOrThrow(ItemIds.PURPLE_BED), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.journeymanSellTradeExperience(), 3));
        registerable.register(BUY_BROWN_DYE, buy(items, items.getOrThrow(ItemIds.BROWN_DYE), 12, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.expertBuyTradeExperience()));
        registerable.register(BUY_PURPLE_DYE, buy(items, items.getOrThrow(ItemIds.PURPLE_DYE), 12, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.expertBuyTradeExperience()));
        registerable.register(BUY_BLUE_DYE, buy(items, items.getOrThrow(ItemIds.BLUE_DYE), 12, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.expertBuyTradeExperience()));
        registerable.register(BUY_GREEN_DYE, buy(items, items.getOrThrow(ItemIds.GREEN_DYE), 12, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.expertBuyTradeExperience()));
        registerable.register(BUY_MAGENTA_DYE, buy(items, items.getOrThrow(ItemIds.MAGENTA_DYE), 12, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.expertBuyTradeExperience()));
        registerable.register(BUY_CYAN_DYE, buy(items, items.getOrThrow(ItemIds.CYAN_DYE), 12, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.expertBuyTradeExperience()));
        registerable.register(SELL_WHITE_BANNER, sell(items, items.getOrThrow(ItemIds.WHITE_BANNER), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertSellTradeExperience(), 3));
        registerable.register(SELL_BLUE_BANNER, sell(items, items.getOrThrow(ItemIds.BLUE_BANNER), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertSellTradeExperience(), 3));
        registerable.register(SELL_LIGHT_BLUE_BANNER, sell(items, items.getOrThrow(ItemIds.LIGHT_BLUE_BANNER), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertSellTradeExperience(), 3));
        registerable.register(SELL_RED_BANNER, sell(items, items.getOrThrow(ItemIds.RED_BANNER), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertSellTradeExperience(), 3));
        registerable.register(SELL_PINK_BANNER, sell(items, items.getOrThrow(ItemIds.PINK_BANNER), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertSellTradeExperience(), 3));
        registerable.register(SELL_GREEN_BANNER, sell(items, items.getOrThrow(ItemIds.GREEN_BANNER), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertSellTradeExperience(), 3));
        registerable.register(SELL_LIME_BANNER, sell(items, items.getOrThrow(ItemIds.LIME_BANNER), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertSellTradeExperience(), 3));
        registerable.register(SELL_GRAY_BANNER, sell(items, items.getOrThrow(ItemIds.GRAY_BANNER), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertSellTradeExperience(), 3));
        registerable.register(SELL_BLACK_BANNER, sell(items, items.getOrThrow(ItemIds.BLACK_BANNER), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertSellTradeExperience(), 3));
        registerable.register(SELL_PURPLE_BANNER, sell(items, items.getOrThrow(ItemIds.PURPLE_BANNER), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertSellTradeExperience(), 3));
        registerable.register(SELL_MAGENTA_BANNER, sell(items, items.getOrThrow(ItemIds.MAGENTA_BANNER), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertSellTradeExperience(), 3));
        registerable.register(SELL_CYAN_BANNER, sell(items, items.getOrThrow(ItemIds.CYAN_BANNER), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertSellTradeExperience(), 3));
        registerable.register(SELL_BROWN_BANNER, sell(items, items.getOrThrow(ItemIds.BROWN_BANNER), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertSellTradeExperience(), 3));
        registerable.register(SELL_YELLOW_BANNER, sell(items, items.getOrThrow(ItemIds.YELLOW_BANNER), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertSellTradeExperience(), 3));
        registerable.register(SELL_ORANGE_BANNER, sell(items, items.getOrThrow(ItemIds.ORANGE_BANNER), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertSellTradeExperience(), 3));
        registerable.register(SELL_LIGHT_GRAY_BANNER, sell(items, items.getOrThrow(ItemIds.LIGHT_GRAY_BANNER), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertSellTradeExperience(), 3));
        registerable.register(SELL_PAINTING, sell(items, items.getOrThrow(ItemIds.PAINTING), 3, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.masterTradeExperience(), 2));
        registerable.register(BUY_STICK, buy(items, items.getOrThrow(ItemIds.STICK), 32, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.noviceBuyTradeExperience()));
        registerable.register(SELL_ARROW, sell(items, items.getOrThrow(ItemIds.ARROW), 16, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_FLINT_FROM_GRAVEL, sell(items, items.getOrThrow(ItemIds.GRAVEL), 10, items.getOrThrow(ItemIds.FLINT), 10, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.noviceSellTradeExperience(), VillagerTradesAccessor.lowPriceMultiplier()));
        registerable.register(BUY_FLINT_APPRENTICE, buy(items, items.getOrThrow(ItemIds.FLINT), 26, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.apprenticeBuyTradeExperience()));
        registerable.register(SELL_BOW, sell(items, items.getOrThrow(ItemIds.BOW), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.apprenticeSellTradeExperience(), 2));
        registerable.register(BUY_STRING_JOURNEYMAN, buy(items, items.getOrThrow(ItemIds.STRING), 14, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.journeymanBuyTradeExperience()));
        registerable.register(SELL_CROSSBOW, sell(items, items.getOrThrow(ItemIds.CROSSBOW), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.journeymanSellTradeExperience(), 3));
        registerable.register(BUY_FEATHER, buy(items, items.getOrThrow(ItemIds.FEATHER), 24, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.expertBuyTradeExperience()));
        registerable.register(SELL_ENCHANTED_BOW, sellEnchantedItem(items, items.getOrThrow(ItemIds.BOW), VillagerTradesAccessor.expertSellTradeExperience(), 2));
        registerable.register(BUY_TRIPWIRE_HOOK, buy(items, items.getOrThrow(ItemIds.TRIPWIRE_HOOK), 8, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.masterTradeExperience()));
        registerable.register(SELL_ENCHANTED_CROSSBOW, sellEnchantedItem(items, items.getOrThrow(ItemIds.CROSSBOW), 15, 3));
        registerable.register(SELL_TIPPED_ARROW, sellWithPotion(items, potions.getOrThrow(PotionTags.TRADEABLE), items.getOrThrow(ItemIds.ARROW), items.getOrThrow(ItemIds.TIPPED_ARROW), VillagerTradesAccessor.masterTradeExperience()));
        registerable.register(BUY_PAPER_LIBRARIAN, buy(items, items.getOrThrow(ItemIds.PAPER), 24, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.noviceBuyTradeExperience()));
        registerable.register(SELL_ENCHANTED_BOOK_NOVICE, sellEnchantedBook(items, VillagerTradesAccessor.noviceSellTradeExperience(), enchantments.getOrThrow(EnchantmentTags.TRADEABLE)));
        registerable.register(SELL_BOOKSHELF, sell(items, items.getOrThrow(ItemIds.BOOKSHELF), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.noviceSellTradeExperience(), 9));
        registerable.register(BUY_BOOK, buy(items, items.getOrThrow(ItemIds.BOOK), 4, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.apprenticeBuyTradeExperience()));
        registerable.register(SELL_ENCHANTED_BOOK_APPRENTICE, sellEnchantedBook(items, VillagerTradesAccessor.apprenticeSellTradeExperience(), enchantments.getOrThrow(EnchantmentTags.TRADEABLE)));
        registerable.register(SELL_LANTERN, sell(items, items.getOrThrow(ItemIds.LANTERN), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(BUY_INK_SAC, buy(items, items.getOrThrow(ItemIds.INK_SAC), 5, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.journeymanBuyTradeExperience()));
        registerable.register(SELL_ENCHANTED_BOOK_JOURNEYMAN, sellEnchantedBook(items, VillagerTradesAccessor.journeymanSellTradeExperience(), enchantments.getOrThrow(EnchantmentTags.TRADEABLE)));
        registerable.register(SELL_GLASS, sell(items, items.getOrThrow(ItemIds.GLASS), 4, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.journeymanSellTradeExperience(), 1));
        registerable.register(BUY_WRITABLE_BOOK, buy(items, items.getOrThrow(ItemIds.WRITABLE_BOOK), 2, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertBuyTradeExperience()));
        registerable.register(SELL_ENCHANTED_BOOK_EXPERT, sellEnchantedBook(items, VillagerTradesAccessor.expertSellTradeExperience(), enchantments.getOrThrow(EnchantmentTags.TRADEABLE)));
        registerable.register(SELL_CLOCK, sell(items, items.getOrThrow(ItemIds.CLOCK), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertSellTradeExperience(), 5));
        registerable.register(SELL_COMPASS, sell(items, items.getOrThrow(ItemIds.COMPASS), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertSellTradeExperience(), 4));
        registerable.register(SELL_NAME_TAG, sell(items, items.getOrThrow(ItemIds.NAME_TAG), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.masterTradeExperience(), 20));
        registerable.register(BUY_PAPER_CARTOGRAPHER, buy(items, items.getOrThrow(ItemIds.PAPER), 24, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.noviceBuyTradeExperience()));
        registerable.register(SELL_MAP, sell(items, items.getOrThrow(ItemIds.MAP), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.noviceSellTradeExperience(), 7));
        registerable.register(BUY_GLASS_PANE, buy(items, items.getOrThrow(ItemIds.GLASS_PANE), 11, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.apprenticeBuyTradeExperience()));
        registerable.register(SELL_TAIGA_VILLAGE_MAP, sellMap(items, 8, StructureTags.ON_TAIGA_VILLAGE_MAPS, "filled_map.village_taiga", MapDecorationTypes.TAIGA_VILLAGE, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.apprenticeSellTradeExperience(), VillagerType.SWAMP, VillagerType.SNOW, VillagerType.PLAINS));
        registerable.register(SELL_SWAMP_HUT_MAP, sellMap(items, 8, StructureTags.ON_SWAMP_EXPLORER_MAPS, "filled_map.explorer_swamp", MapDecorationTypes.SWAMP_HUT, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.apprenticeSellTradeExperience(), VillagerType.TAIGA, VillagerType.SNOW, VillagerType.JUNGLE));
        registerable.register(SELL_SNOWY_VILLAGE_MAP, sellMap(items, 8, StructureTags.ON_SNOWY_VILLAGE_MAPS, "filled_map.village_snowy", MapDecorationTypes.SNOWY_VILLAGE, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.apprenticeSellTradeExperience(), VillagerType.TAIGA, VillagerType.SWAMP));
        registerable.register(SELL_SAVANNA_VILLAGE_MAP, sellMap(items, 8, StructureTags.ON_SAVANNA_VILLAGE_MAPS, "filled_map.village_savanna", MapDecorationTypes.SAVANNA_VILLAGE, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.apprenticeSellTradeExperience(), VillagerType.PLAINS, VillagerType.JUNGLE, VillagerType.DESERT));
        registerable.register(SELL_PLAINS_VILLAGE_MAP, sellMap(items, 8, StructureTags.ON_PLAINS_VILLAGE_MAPS, "filled_map.village_plains", MapDecorationTypes.PLAINS_VILLAGE, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.apprenticeSellTradeExperience(), VillagerType.TAIGA, VillagerType.SNOW, VillagerType.SAVANNA, VillagerType.DESERT));
        registerable.register(SELL_JUNGLE_TEMPLE_MAP, sellMap(items, 8, StructureTags.ON_JUNGLE_EXPLORER_MAPS, "filled_map.explorer_jungle", MapDecorationTypes.JUNGLE_TEMPLE, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.apprenticeSellTradeExperience(), VillagerType.SWAMP, VillagerType.SAVANNA, VillagerType.DESERT));
        registerable.register(SELL_DESERT_VILLAGE_MAP, sellMap(items, 8, StructureTags.ON_DESERT_VILLAGE_MAPS, "filled_map.village_desert", MapDecorationTypes.DESERT_VILLAGE, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.apprenticeSellTradeExperience(), VillagerType.SAVANNA, VillagerType.JUNGLE));
        registerable.register(BUY_COMPASS, buy(items, items.getOrThrow(ItemIds.COMPASS), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.journeymanBuyTradeExperience()));
        registerable.register(SELL_MONUMENT_MAP, sellMap(items, 13, StructureTags.ON_OCEAN_EXPLORER_MAPS, "filled_map.monument", MapDecorationTypes.OCEAN_MONUMENT, VillagerTradesAccessor.journeymanSellTradeExperience()));
        registerable.register(SELL_TRIAL_CHAMBER_MAP, sellMap(items, 12, StructureTags.ON_TRIAL_CHAMBERS_MAPS, "filled_map.trial_chambers", MapDecorationTypes.TRIAL_CHAMBERS, VillagerTradesAccessor.journeymanSellTradeExperience()));
        registerable.register(SELL_ITEM_FRAME, sell(items, items.getOrThrow(ItemIds.ITEM_FRAME), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertSellTradeExperience(), 7));
        registerable.register(SELL_BLUE_BANNER_CARTOGRAPHER, sell(items, items.getOrThrow(ItemIds.BLUE_BANNER), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertSellTradeExperience(), 2, VillagerType.SNOW, VillagerType.TAIGA));
        registerable.register(SELL_WHITE_BANNER_CARTOGRAPHER, sell(items, items.getOrThrow(ItemIds.WHITE_BANNER), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertSellTradeExperience(), 2, VillagerType.SNOW, VillagerType.PLAINS));
        registerable.register(SELL_RED_BANNER_CARTOGRAPHER, sell(items, items.getOrThrow(ItemIds.RED_BANNER), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertSellTradeExperience(), 2, VillagerType.SNOW, VillagerType.SAVANNA));
        registerable.register(SELL_GREEN_BANNER_CARTOGRAPHER, sell(items, items.getOrThrow(ItemIds.GREEN_BANNER), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertSellTradeExperience(), 2, VillagerType.DESERT, VillagerType.SAVANNA, VillagerType.JUNGLE));
        registerable.register(SELL_LIME_BANNER_CARTOGRAPHER, sell(items, items.getOrThrow(ItemIds.LIME_BANNER), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertSellTradeExperience(), 2, VillagerType.DESERT, VillagerType.TAIGA));
        registerable.register(SELL_PURPLE_BANNER_CARTOGRAPHER, sell(items, items.getOrThrow(ItemIds.PURPLE_BANNER), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertSellTradeExperience(), 2, VillagerType.TAIGA, VillagerType.SWAMP));
        registerable.register(SELL_CYAN_BANNER_CARTOGRAPHER, sell(items, items.getOrThrow(ItemIds.CYAN_BANNER), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertSellTradeExperience(), 2, VillagerType.DESERT, VillagerType.SNOW));
        registerable.register(SELL_YELLOW_BANNER_CARTOGRAPHER, sell(items, items.getOrThrow(ItemIds.YELLOW_BANNER), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertSellTradeExperience(), 2, VillagerType.PLAINS, VillagerType.JUNGLE));
        registerable.register(SELL_ORANGE_BANNER_CARTOGRAPHER, sell(items, items.getOrThrow(ItemIds.ORANGE_BANNER), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertSellTradeExperience(), 2, VillagerType.SAVANNA, VillagerType.DESERT));
        registerable.register(SELL_BROWN_BANNER_CARTOGRAPHER, sell(items, items.getOrThrow(ItemIds.BROWN_BANNER), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertSellTradeExperience(), 2, VillagerType.PLAINS, VillagerType.JUNGLE));
        registerable.register(SELL_MAGENTA_BANNER_CARTOGRAPHER, sell(items, items.getOrThrow(ItemIds.MAGENTA_BANNER), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertSellTradeExperience(), 2, VillagerType.SAVANNA));
        registerable.register(SELL_LIGHT_BLUE_BANNER_CARTOGRAPHER, sell(items, items.getOrThrow(ItemIds.LIGHT_BLUE_BANNER), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertSellTradeExperience(), 2, VillagerType.SNOW, VillagerType.SWAMP));
        registerable.register(SELL_PINK_BANNER_CARTOGRAPHER, sell(items, items.getOrThrow(ItemIds.PINK_BANNER), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertSellTradeExperience(), 2, VillagerType.TAIGA, VillagerType.PLAINS));
        registerable.register(SELL_GRAY_BANNER_CARTOGRAPHER, sell(items, items.getOrThrow(ItemIds.GRAY_BANNER), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertSellTradeExperience(), 2, VillagerType.DESERT));
        registerable.register(SELL_BLACK_BANNER_CARTOGRAPHER, sell(items, items.getOrThrow(ItemIds.BLACK_BANNER), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertSellTradeExperience(), 2, VillagerType.SWAMP));
        registerable.register(SELL_GLOBE_BANNER_PATTERN, sell(items, items.getOrThrow(ItemIds.GLOBE_BANNER_PATTERN), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.masterTradeExperience(), 8));
        registerable.register(SELL_MANSION_MAP, sellMap(items, 14, StructureTags.ON_WOODLAND_EXPLORER_MAPS, "filled_map.mansion", MapDecorationTypes.WOODLAND_MANSION, VillagerTradesAccessor.journeymanSellTradeExperience()));
        registerable.register(BUY_ROTTEN_FLESH, buy(items, items.getOrThrow(ItemIds.ROTTEN_FLESH), 32, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.noviceBuyTradeExperience()));
        registerable.register(SELL_REDSTONE, sell(items, items.getOrThrow(ItemIds.REDSTONE), 2, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.noviceSellTradeExperience(), 1));
        registerable.register(BUY_GOLD_INGOT, buy(items, items.getOrThrow(ItemIds.GOLD_INGOT), 3, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.apprenticeBuyTradeExperience()));
        registerable.register(SELL_LAPIS_LAZULI, sell(items, items.getOrThrow(ItemIds.LAPIS_LAZULI), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(BUY_RABBIT_FOOT, buy(items, items.getOrThrow(ItemIds.RABBIT_FOOT), 2, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.journeymanBuyTradeExperience()));
        registerable.register(SELL_GLOWSTONE, sell(items, items.getOrThrow(ItemIds.GLOWSTONE), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.journeymanSellTradeExperience(), 4));
        registerable.register(BUY_TURTLE_SCUTE, buy(items, items.getOrThrow(ItemIds.TURTLE_SCUTE), 4, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertBuyTradeExperience()));
        registerable.register(BUY_GLASS_BOTTLE, buy(items, items.getOrThrow(ItemIds.GLASS_BOTTLE), 9, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertBuyTradeExperience()));
        registerable.register(SELL_ENDER_PEARL, sell(items, items.getOrThrow(ItemIds.ENDER_PEARL), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertSellTradeExperience(), 5));
        registerable.register(BUY_NETHER_WART, buy(items, items.getOrThrow(ItemIds.NETHER_WART), 22, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.masterTradeExperience()));
        registerable.register(SELL_EXPERIENCE_BOTTLE, sell(items, items.getOrThrow(ItemIds.EXPERIENCE_BOTTLE), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.masterTradeExperience(), 3));
        registerable.register(BUY_COAL_NOVICE_MORE_ITEMS, buy(items, items.getOrThrow(ItemIds.COAL), 15, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.noviceBuyTradeExperience()));
        registerable.register(SELL_IRON_LEGGINGS, sell(items, items.getOrThrow(ItemIds.IRON_LEGGINGS), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.noviceSellTradeExperience(), 7));
        registerable.register(SELL_IRON_BOOTS, sell(items, items.getOrThrow(ItemIds.IRON_BOOTS), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.noviceSellTradeExperience(), 4));
        registerable.register(SELL_IRON_HELMET, sell(items, items.getOrThrow(ItemIds.IRON_HELMET), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.noviceSellTradeExperience(), 5));
        registerable.register(SELL_IRON_CHESTPLATE, sell(items, items.getOrThrow(ItemIds.IRON_CHESTPLATE), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.noviceSellTradeExperience(), 9));
        registerable.register(BUY_IRON_INGOT, buy(items, items.getOrThrow(ItemIds.IRON_INGOT), 4, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.apprenticeBuyTradeExperience()));
        registerable.register(SELL_BELL, sell(items, items.getOrThrow(ItemIds.BELL), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.apprenticeSellTradeExperience(), 36));
        registerable.register(SELL_CHAINMAIL_BOOTS, sell(items, items.getOrThrow(ItemIds.CHAINMAIL_BOOTS), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_CHAINMAIL_LEGGINGS, sell(items, items.getOrThrow(ItemIds.CHAINMAIL_LEGGINGS), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.apprenticeSellTradeExperience(), 3));
        registerable.register(BUY_LAVA_BUCKET, buy(items, items.getOrThrow(ItemIds.LAVA_BUCKET), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.journeymanBuyTradeExperience()));
        registerable.register(BUY_DIAMOND_JOURNEYMAN, buy(items, items.getOrThrow(ItemIds.DIAMOND), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.journeymanBuyTradeExperience()));
        registerable.register(SELL_CHAINMAIL_HELMET, sell(items, items.getOrThrow(ItemIds.CHAINMAIL_HELMET), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.journeymanSellTradeExperience(), 1));
        registerable.register(SELL_CHAINMAIL_CHESTPLATE, sell(items, items.getOrThrow(ItemIds.CHAINMAIL_CHESTPLATE), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.journeymanSellTradeExperience(), 4));
        registerable.register(SELL_SHIELD, sell(items, items.getOrThrow(ItemIds.SHIELD), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.journeymanSellTradeExperience(), 5));
        registerable.register(SELL_ENCHANTED_DIAMOND_LEGGINGS, sellEnchantedItem(items, items.getOrThrow(ItemIds.DIAMOND_LEGGINGS), VillagerTradesAccessor.expertSellTradeExperience(), 14, VillagerTradesAccessor.highPriceMultiplier()));
        registerable.register(SELL_ENCHANTED_DIAMOND_BOOTS, sellEnchantedItem(items, items.getOrThrow(ItemIds.DIAMOND_BOOTS), VillagerTradesAccessor.expertSellTradeExperience(), 8, VillagerTradesAccessor.highPriceMultiplier()));
        registerable.register(SELL_ENCHANTED_DIAMOND_HELMET, sellEnchantedItem(items, items.getOrThrow(ItemIds.DIAMOND_HELMET), VillagerTradesAccessor.masterTradeExperience(), 8, VillagerTradesAccessor.highPriceMultiplier()));
        registerable.register(SELL_ENCHANTED_DIAMOND_CHESTPLATE, sellEnchantedItem(items, items.getOrThrow(ItemIds.DIAMOND_CHESTPLATE), VillagerTradesAccessor.masterTradeExperience(), 16, VillagerTradesAccessor.highPriceMultiplier()));
        registerable.register(SELL_IRON_AXE, sell(items, items.getOrThrow(ItemIds.IRON_AXE), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.noviceSellTradeExperience(), 3));
        registerable.register(SELL_ENCHANTED_IRON_SWORD, sellEnchantedItem(items, items.getOrThrow(ItemIds.IRON_SWORD), VillagerTradesAccessor.noviceSellTradeExperience(), 2));
        registerable.register(BUY_FLINT_WEAPONSMITH_JOURNEYMAN, buy(items, items.getOrThrow(ItemIds.FLINT), 24, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.journeymanBuyTradeExperience()));
        registerable.register(BUY_DIAMOND_EXPERT, buy(items, items.getOrThrow(ItemIds.DIAMOND), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertBuyTradeExperience()));
        registerable.register(SELL_ENCHANTED_DIAMOND_AXE, sellEnchantedItem(items, items.getOrThrow(ItemIds.DIAMOND_AXE), VillagerTradesAccessor.expertSellTradeExperience(), 12, VillagerTradesAccessor.highPriceMultiplier()));
        registerable.register(SELL_ENCHANTED_DIAMOND_SWORD, sellEnchantedItem(items, items.getOrThrow(ItemIds.DIAMOND_SWORD), VillagerTradesAccessor.masterTradeExperience(), 8, VillagerTradesAccessor.highPriceMultiplier()));
        registerable.register(SELL_STONE_AXE, sell(items, items.getOrThrow(ItemIds.STONE_AXE), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_STONE_SHOVEL, sell(items, items.getOrThrow(ItemIds.STONE_SHOVEL), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_STONE_PICKAXE, sell(items, items.getOrThrow(ItemIds.STONE_PICKAXE), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_STONE_HOE, sell(items, items.getOrThrow(ItemIds.STONE_HOE), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.noviceSellTradeExperience(), 1));
        registerable.register(BUY_FLINT_TOOLSMITH_JOURNEYMAN, buy(items, items.getOrThrow(ItemIds.FLINT), 30, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.journeymanBuyTradeExperience()));
        registerable.register(SELL_ENCHANTED_IRON_AXE, sellEnchantedItem(items, items.getOrThrow(ItemIds.IRON_AXE), VillagerTradesAccessor.journeymanSellTradeExperience(), 1, VillagerTradesAccessor.highPriceMultiplier()));
        registerable.register(SELL_ENCHANTED_IRON_SHOVEL, sellEnchantedItem(items, items.getOrThrow(ItemIds.IRON_SHOVEL), VillagerTradesAccessor.journeymanSellTradeExperience(), 2, VillagerTradesAccessor.highPriceMultiplier()));
        registerable.register(SELL_ENCHANTED_IRON_PICKAXE, sellEnchantedItem(items, items.getOrThrow(ItemIds.IRON_PICKAXE), VillagerTradesAccessor.journeymanSellTradeExperience(), 3, VillagerTradesAccessor.highPriceMultiplier()));
        registerable.register(SELL_DIAMOND_HOE, sell(items, items.getOrThrow(ItemIds.DIAMOND_HOE), 1, VillagerTradesAccessor.rareMaxUses(), VillagerTradesAccessor.journeymanSellTradeExperience(), 4));
        registerable.register(SELL_ENCHANTED_DIAMOND_SHOVEL, sellEnchantedItem(items, items.getOrThrow(ItemIds.DIAMOND_SHOVEL), VillagerTradesAccessor.expertSellTradeExperience(), 5, VillagerTradesAccessor.highPriceMultiplier()));
        registerable.register(SELL_ENCHANTED_DIAMOND_PICKAXE, sellEnchantedItem(items, items.getOrThrow(ItemIds.DIAMOND_PICKAXE), VillagerTradesAccessor.masterTradeExperience(), 13, VillagerTradesAccessor.highPriceMultiplier()));
        registerable.register(BUY_CHICKEN, buy(items, items.getOrThrow(ItemIds.CHICKEN), 14, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.noviceBuyTradeExperience()));
        registerable.register(BUY_PORKCHOP, buy(items, items.getOrThrow(ItemIds.PORKCHOP), 7, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.noviceBuyTradeExperience()));
        registerable.register(BUY_RABBIT, buy(items, items.getOrThrow(ItemIds.RABBIT), 4, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.noviceBuyTradeExperience()));
        registerable.register(SELL_RABBIT_STEW, sell(items, items.getOrThrow(ItemIds.RABBIT_STEW), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_COOKED_PORKCHOP, sell(items, items.getOrThrow(ItemIds.COOKED_PORKCHOP), 5, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_COOKED_CHICKEN, sell(items, items.getOrThrow(ItemIds.COOKED_CHICKEN), 8, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(BUY_MUTTON, buy(items, items.getOrThrow(ItemIds.MUTTON), 7, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.journeymanBuyTradeExperience()));
        registerable.register(BUY_BEEF, buy(items, items.getOrThrow(ItemIds.BEEF), 10, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.journeymanBuyTradeExperience()));
        registerable.register(BUY_DRIED_KELP_BLOCK, buy(items, items.getOrThrow(ItemIds.DRIED_KELP_BLOCK), 10, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertBuyTradeExperience()));
        registerable.register(BUY_SWEET_BERRIES, buy(items, items.getOrThrow(ItemIds.SWEET_BERRIES), 10, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.masterTradeExperience()));
        registerable.register(BUY_LEATHER, buy(items, items.getOrThrow(ItemIds.LEATHER), 6, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.noviceBuyTradeExperience()));
        registerable.register(SELL_LEATHER_LEGGINGS, sellDyedItem(items, items.getOrThrow(ItemIds.LEATHER_LEGGINGS), 3));
        registerable.register(SELL_LEATHER_CHESTPLATE, sellDyedItem(items, items.getOrThrow(ItemIds.LEATHER_CHESTPLATE), 7));
        registerable.register(SELL_LEATHER_HELMET_APPRENTICE, sellDyedItem(items, items.getOrThrow(ItemIds.LEATHER_HELMET), 5, VillagerTradesAccessor.apprenticeSellTradeExperience()));
        registerable.register(SELL_LEATHER_BOOTS, sellDyedItem(items, items.getOrThrow(ItemIds.LEATHER_BOOTS), 4, VillagerTradesAccessor.apprenticeSellTradeExperience()));
        registerable.register(BUY_RABBIT_HIDE, buy(items, items.getOrThrow(ItemIds.RABBIT_HIDE), 9, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.journeymanBuyTradeExperience()));
        registerable.register(SELL_LEATHER_HORSE_ARMOR, sellDyedItem(items, items.getOrThrow(ItemIds.LEATHER_HORSE_ARMOR), 6, VillagerTradesAccessor.expertSellTradeExperience()));
        registerable.register(SELL_SADDLE, sell(items, items.getOrThrow(ItemIds.SADDLE), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.masterTradeExperience(), 6));
        registerable.register(SELL_LEATHER_HELMET_MASTER, sellDyedItem(items, items.getOrThrow(ItemIds.LEATHER_HELMET), 5, VillagerTradesAccessor.masterTradeExperience()));
        registerable.register(BUY_CLAY_BALL, buy(items, items.getOrThrow(ItemIds.CLAY_BALL), 10, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.noviceBuyTradeExperience()));
        registerable.register(SELL_BRICK, sell(items, items.getOrThrow(ItemIds.BRICK), 10, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.noviceSellTradeExperience(), 1));
        registerable.register(BUY_STONE, buy(items, items.getOrThrow(ItemIds.STONE), 20, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.apprenticeBuyTradeExperience()));
        registerable.register(SELL_CHISELED_STONE_BRICKS, sell(items, items.getOrThrow(ItemIds.CHISELED_STONE_BRICKS), 4, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(BUY_GRANITE, buy(items, items.getOrThrow(ItemIds.GRANITE), 16, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.journeymanBuyTradeExperience()));
        registerable.register(BUY_ANDESITE, buy(items, items.getOrThrow(ItemIds.ANDESITE), 16, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.journeymanBuyTradeExperience()));
        registerable.register(BUY_DIORITE, buy(items, items.getOrThrow(ItemIds.DIORITE), 16, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.journeymanBuyTradeExperience()));
        registerable.register(SELL_DRIPSTONE_BLOCK, sell(items, items.getOrThrow(ItemIds.DRIPSTONE_BLOCK), 4, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.journeymanSellTradeExperience(), 1));
        registerable.register(SELL_POLISHED_ANDESITE, sell(items, items.getOrThrow(ItemIds.POLISHED_ANDESITE), 4, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.journeymanSellTradeExperience(), 1));
        registerable.register(SELL_POLISHED_DIORITE, sell(items, items.getOrThrow(ItemIds.POLISHED_DIORITE), 4, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.journeymanSellTradeExperience(), 1));
        registerable.register(SELL_POLISHED_GRANITE, sell(items, items.getOrThrow(ItemIds.POLISHED_GRANITE), 4, VillagerTradesAccessor.commonMaxUses(), VillagerTradesAccessor.journeymanSellTradeExperience(), 1));
        registerable.register(BUY_QUARTZ, buy(items, items.getOrThrow(ItemIds.QUARTZ), 12, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertBuyTradeExperience()));
        registerable.register(SELL_ORANGE_TERRACOTTA, sell(items, items.getOrThrow(ItemIds.ORANGE_TERRACOTTA), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertSellTradeExperience(), 1));
        registerable.register(SELL_WHITE_TERRACOTTA, sell(items, items.getOrThrow(ItemIds.WHITE_TERRACOTTA), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertSellTradeExperience(), 1));
        registerable.register(SELL_BLUE_TERRACOTTA, sell(items, items.getOrThrow(ItemIds.BLUE_TERRACOTTA), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertSellTradeExperience(), 1));
        registerable.register(SELL_LIGHT_BLUE_TERRACOTTA, sell(items, items.getOrThrow(ItemIds.LIGHT_BLUE_TERRACOTTA), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertSellTradeExperience(), 1));
        registerable.register(SELL_GRAY_TERRACOTTA, sell(items, items.getOrThrow(ItemIds.GRAY_TERRACOTTA), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertSellTradeExperience(), 1));
        registerable.register(SELL_LIGHT_GRAY_TERRACOTTA, sell(items, items.getOrThrow(ItemIds.LIGHT_GRAY_TERRACOTTA), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertSellTradeExperience(), 1));
        registerable.register(SELL_BLACK_TERRACOTTA, sell(items, items.getOrThrow(ItemIds.BLACK_TERRACOTTA), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertSellTradeExperience(), 1));
        registerable.register(SELL_RED_TERRACOTTA, sell(items, items.getOrThrow(ItemIds.RED_TERRACOTTA), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertSellTradeExperience(), 1));
        registerable.register(SELL_PINK_TERRACOTTA, sell(items, items.getOrThrow(ItemIds.PINK_TERRACOTTA), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertSellTradeExperience(), 1));
        registerable.register(SELL_MAGENTA_TERRACOTTA, sell(items, items.getOrThrow(ItemIds.MAGENTA_TERRACOTTA), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertSellTradeExperience(), 1));
        registerable.register(SELL_LIME_TERRACOTTA, sell(items, items.getOrThrow(ItemIds.LIME_TERRACOTTA), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertSellTradeExperience(), 1));
        registerable.register(SELL_GREEN_TERRACOTTA, sell(items, items.getOrThrow(ItemIds.GREEN_TERRACOTTA), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertSellTradeExperience(), 1));
        registerable.register(SELL_CYAN_TERRACOTTA, sell(items, items.getOrThrow(ItemIds.CYAN_TERRACOTTA), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertSellTradeExperience(), 1));
        registerable.register(SELL_PURPLE_TERRACOTTA, sell(items, items.getOrThrow(ItemIds.PURPLE_TERRACOTTA), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertSellTradeExperience(), 1));
        registerable.register(SELL_YELLOW_TERRACOTTA, sell(items, items.getOrThrow(ItemIds.YELLOW_TERRACOTTA), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertSellTradeExperience(), 1));
        registerable.register(SELL_BROWN_TERRACOTTA, sell(items, items.getOrThrow(ItemIds.BROWN_TERRACOTTA), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertSellTradeExperience(), 1));
        registerable.register(SELL_ORANGE_GLAZED_TERRACOTTA, sell(items, items.getOrThrow(ItemIds.ORANGE_GLAZED_TERRACOTTA), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertSellTradeExperience(), 1));
        registerable.register(SELL_WHITE_GLAZED_TERRACOTTA, sell(items, items.getOrThrow(ItemIds.WHITE_GLAZED_TERRACOTTA), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertSellTradeExperience(), 1));
        registerable.register(SELL_BLUE_GLAZED_TERRACOTTA, sell(items, items.getOrThrow(ItemIds.BLUE_GLAZED_TERRACOTTA), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertSellTradeExperience(), 1));
        registerable.register(SELL_LIGHT_BLUE_GLAZED_TERRACOTTA, sell(items, items.getOrThrow(ItemIds.LIGHT_BLUE_GLAZED_TERRACOTTA), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertSellTradeExperience(), 1));
        registerable.register(SELL_GRAY_GLAZED_TERRACOTTA, sell(items, items.getOrThrow(ItemIds.GRAY_GLAZED_TERRACOTTA), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertSellTradeExperience(), 1));
        registerable.register(SELL_LIGHT_GRAY_GLAZED_TERRACOTTA, sell(items, items.getOrThrow(ItemIds.LIGHT_GRAY_GLAZED_TERRACOTTA), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertSellTradeExperience(), 1));
        registerable.register(SELL_BLACK_GLAZED_TERRACOTTA, sell(items, items.getOrThrow(ItemIds.BLACK_GLAZED_TERRACOTTA), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertSellTradeExperience(), 1));
        registerable.register(SELL_RED_GLAZED_TERRACOTTA, sell(items, items.getOrThrow(ItemIds.RED_GLAZED_TERRACOTTA), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertSellTradeExperience(), 1));
        registerable.register(SELL_PINK_GLAZED_TERRACOTTA, sell(items, items.getOrThrow(ItemIds.PINK_GLAZED_TERRACOTTA), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertSellTradeExperience(), 1));
        registerable.register(SELL_MAGENTA_GLAZED_TERRACOTTA, sell(items, items.getOrThrow(ItemIds.MAGENTA_GLAZED_TERRACOTTA), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertSellTradeExperience(), 1));
        registerable.register(SELL_LIME_GLAZED_TERRACOTTA, sell(items, items.getOrThrow(ItemIds.LIME_GLAZED_TERRACOTTA), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertSellTradeExperience(), 1));
        registerable.register(SELL_GREEN_GLAZED_TERRACOTTA, sell(items, items.getOrThrow(ItemIds.GREEN_GLAZED_TERRACOTTA), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertSellTradeExperience(), 1));
        registerable.register(SELL_CYAN_GLAZED_TERRACOTTA, sell(items, items.getOrThrow(ItemIds.CYAN_GLAZED_TERRACOTTA), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertSellTradeExperience(), 1));
        registerable.register(SELL_PURPLE_GLAZED_TERRACOTTA, sell(items, items.getOrThrow(ItemIds.PURPLE_GLAZED_TERRACOTTA), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertSellTradeExperience(), 1));
        registerable.register(SELL_YELLOW_GLAZED_TERRACOTTA, sell(items, items.getOrThrow(ItemIds.YELLOW_GLAZED_TERRACOTTA), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertSellTradeExperience(), 1));
        registerable.register(SELL_BROWN_GLAZED_TERRACOTTA, sell(items, items.getOrThrow(ItemIds.BROWN_GLAZED_TERRACOTTA), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.expertSellTradeExperience(), 1));
        registerable.register(SELL_QUARTZ_PILLAR, sell(items, items.getOrThrow(ItemIds.QUARTZ_PILLAR), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.masterTradeExperience(), 1));
        registerable.register(SELL_QUARTZ_BLOCK, sell(items, items.getOrThrow(ItemIds.QUARTZ_BLOCK), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.masterTradeExperience(), 1));
        registerable.register(SELL_SEA_PICKLE, sell(items, items.getOrThrow(ItemIds.SEA_PICKLE), 1, 5, VillagerTradesAccessor.noviceSellTradeExperience(), 2));
        registerable.register(SELL_SLIME_BALL, sell(items, items.getOrThrow(ItemIds.SLIME_BALL), 1, 5, VillagerTradesAccessor.noviceSellTradeExperience(), 4));
        registerable.register(SELL_GLOWSTONE_WANDERING_TRADER, sell(items, items.getOrThrow(ItemIds.GLOWSTONE), 1, 5, VillagerTradesAccessor.noviceSellTradeExperience(), 2));
        registerable.register(SELL_NAUTILUS_SHELL, sell(items, items.getOrThrow(ItemIds.NAUTILUS_SHELL), 1, 5, VillagerTradesAccessor.noviceSellTradeExperience(), 5));
        registerable.register(SELL_FERN, sell(items, items.getOrThrow(ItemIds.FERN), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_SUGAR_CANE, sell(items, items.getOrThrow(ItemIds.SUGAR_CANE), 1, 8, VillagerTradesAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_PUMPKIN, sell(items, items.getOrThrow(ItemIds.PUMPKIN), 1, 4, VillagerTradesAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_KELP, sell(items, items.getOrThrow(ItemIds.KELP), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.noviceSellTradeExperience(), 3));
        registerable.register(SELL_CACTUS, sell(items, items.getOrThrow(ItemIds.CACTUS), 1, 8, VillagerTradesAccessor.noviceSellTradeExperience(), 3));
        registerable.register(SELL_DANDELION, sell(items, items.getOrThrow(ItemIds.DANDELION), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_POPPY, sell(items, items.getOrThrow(ItemIds.POPPY), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_BLUE_ORCHID, sell(items, items.getOrThrow(ItemIds.BLUE_ORCHID), 1, 8, VillagerTradesAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_ALLIUM, sell(items, items.getOrThrow(ItemIds.ALLIUM), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_AZURE_BLUET, sell(items, items.getOrThrow(ItemIds.AZURE_BLUET), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_RED_TULIP, sell(items, items.getOrThrow(ItemIds.RED_TULIP), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_ORANGE_TULIP, sell(items, items.getOrThrow(ItemIds.ORANGE_TULIP), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_WHITE_TULIP, sell(items, items.getOrThrow(ItemIds.WHITE_TULIP), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_PINK_TULIP, sell(items, items.getOrThrow(ItemIds.PINK_TULIP), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_OXEYE_DAISY, sell(items, items.getOrThrow(ItemIds.OXEYE_DAISY), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_CORNFLOWER, sell(items, items.getOrThrow(ItemIds.CORNFLOWER), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_LILY_OF_THE_VALLEY, sell(items, items.getOrThrow(ItemIds.LILY_OF_THE_VALLEY), 1, 7, VillagerTradesAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_OPEN_EYEBLOSSOM, sell(items, items.getOrThrow(ItemIds.OPEN_EYEBLOSSOM), 1, 7, VillagerTradesAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_WHEAT_SEEDS, sell(items, items.getOrThrow(ItemIds.WHEAT_SEEDS), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_BEETROOT_SEEDS, sell(items, items.getOrThrow(ItemIds.BEETROOT_SEEDS), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_PUMPKIN_SEEDS, sell(items, items.getOrThrow(ItemIds.PUMPKIN_SEEDS), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_MELON_SEEDS, sell(items, items.getOrThrow(ItemIds.MELON_SEEDS), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_ACACIA_SAPLING, sell(items, items.getOrThrow(ItemIds.ACACIA_SAPLING), 1, 8, VillagerTradesAccessor.noviceSellTradeExperience(), 5));
        registerable.register(SELL_BIRCH_SAPLING, sell(items, items.getOrThrow(ItemIds.BIRCH_SAPLING), 1, 8, VillagerTradesAccessor.noviceSellTradeExperience(), 5));
        registerable.register(SELL_DARK_OAK_SAPLING, sell(items, items.getOrThrow(ItemIds.DARK_OAK_SAPLING), 1, 8, VillagerTradesAccessor.noviceSellTradeExperience(), 5));
        registerable.register(SELL_JUNGLE_SAPLING, sell(items, items.getOrThrow(ItemIds.JUNGLE_SAPLING), 1, 8, VillagerTradesAccessor.noviceSellTradeExperience(), 5));
        registerable.register(SELL_OAK_SAPLING, sell(items, items.getOrThrow(ItemIds.OAK_SAPLING), 1, 8, VillagerTradesAccessor.noviceSellTradeExperience(), 5));
        registerable.register(SELL_SPRUCE_SAPLING, sell(items, items.getOrThrow(ItemIds.SPRUCE_SAPLING), 1, 8, VillagerTradesAccessor.noviceSellTradeExperience(), 5));
        registerable.register(SELL_CHERRY_SAPLING, sell(items, items.getOrThrow(ItemIds.CHERRY_SAPLING), 1, 8, VillagerTradesAccessor.noviceSellTradeExperience(), 5));
        registerable.register(SELL_PALE_OAK_SAPLING, sell(items, items.getOrThrow(ItemIds.PALE_OAK_SAPLING), 1, 8, VillagerTradesAccessor.noviceSellTradeExperience(), 5));
        registerable.register(SELL_MANGROVE_PROPAGULE, sell(items, items.getOrThrow(ItemIds.MANGROVE_PROPAGULE), 1, 8, VillagerTradesAccessor.noviceSellTradeExperience(), 5));
        registerable.register(SELL_RED_DYE, sell(items, items.getOrThrow(ItemIds.RED_DYE), 3, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_WHITE_DYE, sell(items, items.getOrThrow(ItemIds.WHITE_DYE), 3, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_BLUE_DYE, sell(items, items.getOrThrow(ItemIds.BLUE_DYE), 3, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_PINK_DYE, sell(items, items.getOrThrow(ItemIds.PINK_DYE), 3, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_BLACK_DYE, sell(items, items.getOrThrow(ItemIds.BLACK_DYE), 3, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_GREEN_DYE, sell(items, items.getOrThrow(ItemIds.GREEN_DYE), 3, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_LIGHT_GRAY_DYE, sell(items, items.getOrThrow(ItemIds.LIGHT_GRAY_DYE), 3, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_MAGENTA_DYE, sell(items, items.getOrThrow(ItemIds.MAGENTA_DYE), 3, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_YELLOW_DYE, sell(items, items.getOrThrow(ItemIds.YELLOW_DYE), 3, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_GRAY_DYE, sell(items, items.getOrThrow(ItemIds.GRAY_DYE), 3, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_PURPLE_DYE, sell(items, items.getOrThrow(ItemIds.PURPLE_DYE), 3, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_LIGHT_BLUE_DYE, sell(items, items.getOrThrow(ItemIds.LIGHT_BLUE_DYE), 3, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_LIME_DYE, sell(items, items.getOrThrow(ItemIds.LIME_DYE), 3, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_ORANGE_DYE, sell(items, items.getOrThrow(ItemIds.ORANGE_DYE), 3, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_BROWN_DYE, sell(items, items.getOrThrow(ItemIds.BROWN_DYE), 3, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_CYAN_DYE, sell(items, items.getOrThrow(ItemIds.CYAN_DYE), 3, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_BRAIN_CORAL_BLOCK, sell(items, items.getOrThrow(ItemIds.BRAIN_CORAL_BLOCK), 1, 8, VillagerTradesAccessor.noviceSellTradeExperience(), 3));
        registerable.register(SELL_BUBBLE_CORAL_BLOCK, sell(items, items.getOrThrow(ItemIds.BUBBLE_CORAL_BLOCK), 1, 8, VillagerTradesAccessor.noviceSellTradeExperience(), 3));
        registerable.register(SELL_FIRE_CORAL_BLOCK, sell(items, items.getOrThrow(ItemIds.FIRE_CORAL_BLOCK), 1, 8, VillagerTradesAccessor.noviceSellTradeExperience(), 3));
        registerable.register(SELL_HORN_CORAL_BLOCK, sell(items, items.getOrThrow(ItemIds.HORN_CORAL_BLOCK), 1, 8, VillagerTradesAccessor.noviceSellTradeExperience(), 3));
        registerable.register(SELL_TUBE_CORAL_BLOCK, sell(items, items.getOrThrow(ItemIds.TUBE_CORAL_BLOCK), 1, 8, VillagerTradesAccessor.noviceSellTradeExperience(), 3));
        registerable.register(SELL_VINE, sell(items, items.getOrThrow(ItemIds.VINE), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_PALE_HANGING_MOSS, sell(items, items.getOrThrow(ItemIds.PALE_HANGING_MOSS), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_BROWN_MUSHROOM, sell(items, items.getOrThrow(ItemIds.BROWN_MUSHROOM), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_RED_MUSHROOM, sell(items, items.getOrThrow(ItemIds.RED_MUSHROOM), 1, VillagerTradesAccessor.defaultMaxUses(), VillagerTradesAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_LILY_PAD, sell(items, items.getOrThrow(ItemIds.LILY_PAD), 2, 5, VillagerTradesAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_SMALL_DRIPLEAF, sell(items, items.getOrThrow(ItemIds.SMALL_DRIPLEAF), 2, 5, VillagerTradesAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_SAND, sell(items, items.getOrThrow(ItemIds.SAND), 8, 8, VillagerTradesAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_RED_SAND, sell(items, items.getOrThrow(ItemIds.RED_SAND), 4, 6, VillagerTradesAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_POINTED_DRIPSTONE, sell(items, items.getOrThrow(ItemIds.POINTED_DRIPSTONE), 2, 5, VillagerTradesAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_ROOTED_DIRT, sell(items, items.getOrThrow(ItemIds.ROOTED_DIRT), 2, 5, VillagerTradesAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_MOSS_BLOCK, sell(items, items.getOrThrow(ItemIds.MOSS_BLOCK), 2, 5, VillagerTradesAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_PALE_MOSS_BLOCK, sell(items, items.getOrThrow(ItemIds.PALE_MOSS_BLOCK), 2, 5, VillagerTradesAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_WILDFLOWERS, sell(items, items.getOrThrow(ItemIds.WILDFLOWERS), 1, 5, VillagerTradesAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_TALL_DRY_GRASS, sell(items, items.getOrThrow(ItemIds.TALL_DRY_GRASS), 1, 5, VillagerTradesAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_FIREFLY_BUSH, sell(items, items.getOrThrow(ItemIds.FIREFLY_BUSH), 1, 5, VillagerTradesAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_TROPICAL_FISH_BUCKET, sell(items, items.getOrThrow(ItemIds.TROPICAL_FISH_BUCKET), 1, 4, 1, 5));
        registerable.register(SELL_PUFFERFISH_BUCKET, sell(items, items.getOrThrow(ItemIds.PUFFERFISH_BUCKET), 1, 4, 1, 5));
        registerable.register(BUY_WATER_BOTTLE, buyWithPotion(items, potions.getOrThrow(PotionIds.WATER), items.getOrThrow(ItemIds.POTION)));
        registerable.register(BUY_WATER_BUCKET, buy(items, items.getOrThrow(ItemIds.WATER_BUCKET), 1, 2, 2, 1));
        registerable.register(BUY_MILK_BUCKET, buy(items, items.getOrThrow(ItemIds.MILK_BUCKET), 1, 2, 2, 1));
        registerable.register(BUY_FERMENTED_SPIDER_EYE, buy(items, items.getOrThrow(ItemIds.FERMENTED_SPIDER_EYE), 1, 3, 2, 1));
        registerable.register(BUY_BAKED_POTATO, buy(items, items.getOrThrow(ItemIds.BAKED_POTATO), 4, 1, 2, 1));
        registerable.register(BUY_HAY_BLOCK, buy(items, items.getOrThrow(ItemIds.HAY_BLOCK), 1, 1, 2, 1));
        registerable.register(SELL_PACKED_ICE, sell(items, items.getOrThrow(ItemIds.PACKED_ICE), 1, 6, 1, 3));
        registerable.register(SELL_BLUE_ICE, sell(items, items.getOrThrow(ItemIds.BLUE_ICE), 1, 6, 1, 6));
        registerable.register(SELL_GUNPOWDER, sell(items, items.getOrThrow(ItemIds.GUNPOWDER), 1, 8, 1, 1));
        registerable.register(SELL_PODZOL, sell(items, items.getOrThrow(ItemIds.PODZOL), 3, 6, 1, 3));
        registerable.register(SELL_ACACIA_LOG, sell(items, items.getOrThrow(ItemIds.ACACIA_LOG), 8, 4, 1, 1));
        registerable.register(SELL_BIRCH_LOG, sell(items, items.getOrThrow(ItemIds.BIRCH_LOG), 8, 4, 1, 1));
        registerable.register(SELL_CHERRY_LOG, sell(items, items.getOrThrow(ItemIds.CHERRY_LOG), 8, 4, 1, 1));
        registerable.register(SELL_MANGROVE_LOG, sell(items, items.getOrThrow(ItemIds.MANGROVE_LOG), 8, 4, 1, 1));
        registerable.register(SELL_DARK_OAK_LOG, sell(items, items.getOrThrow(ItemIds.DARK_OAK_LOG), 8, 4, 1, 1));
        registerable.register(SELL_JUNGLE_LOG, sell(items, items.getOrThrow(ItemIds.JUNGLE_LOG), 8, 4, 1, 1));
        registerable.register(SELL_OAK_LOG, sell(items, items.getOrThrow(ItemIds.OAK_LOG), 8, 4, 1, 1));
        registerable.register(SELL_SPRUCE_LOG, sell(items, items.getOrThrow(ItemIds.SPRUCE_LOG), 8, 4, 1, 1));
        registerable.register(SELL_PALE_OAK_LOG, sell(items, items.getOrThrow(ItemIds.PALE_OAK_LOG), 8, 4, 1, 1));
        registerable.register(SELL_ENCHANTED_IRON_PICKAXE_WANDERING_TRADER, sellEnchantedItem(items, items.getOrThrow(ItemIds.IRON_PICKAXE), 1, 3, VillagerTradesAccessor.highPriceMultiplier(), 1));
        registerable.register(SELL_LONG_INVISIBILITY_POTION, sellWithPotion(items, potions.getOrThrow(PotionIds.LONG_INVISIBILITY), items.getOrThrow(ItemIds.POTION), 1));
    }

    private static Trade buy(HolderGetter<Item> items, Holder<Item> item, int count, int maxUses, int tradeExperience) {
        return Trade.builder(Trade.Entry.ofEmerald(items))
            .wants(Trade.Entry.of(item, count))
            .maxUses(maxUses)
            .tradeExperience(tradeExperience)
            .build();
    }

    private static Trade buy(HolderGetter<Item> items, Holder<Item> item, int count, int givenAmount, int maxUses, int tradeExperience) {
        return Trade.builder(Trade.Entry.ofEmerald(items, givenAmount))
            .wants(Trade.Entry.of(item, count))
            .maxUses(maxUses)
            .tradeExperience(tradeExperience)
            .build();
    }

    private static Trade buyFromType(HolderGetter<Item> items, Holder<Item> item, Map<Holder<VillagerType>, Holder<Item>> types) {
        return Trade.builder(Trade.Entry.ofEmerald(items))
            .wants(Trade.Entry.of(item))
            .tradeExperience(VillagerTradesAccessor.masterTradeExperience())
            .tradeModifier(ItemFromTypeTradeModifier.of(types))
            .build();
    }

    private static Trade buyWithPotion(HolderGetter<Item> items, Holder<Potion> potion, Holder<Item> item) {
        return Trade.builder(Trade.Entry.ofEmerald(items))
            .wants(Trade.Entry.of(item, 1, SetPotionFunction.setPotion(potion).build()))
            .maxUses(2)
            .tradeExperience(1)
            .build();
    }

    private static Trade sell(HolderGetter<Item> items, Holder<Item> item, int count, int maxUses, int tradeExperience, int price) {
        return Trade.builder(Trade.Entry.of(item, count))
            .wants(Trade.Entry.ofEmerald(items, price))
            .maxUses(maxUses)
            .tradeExperience(tradeExperience)
            .build();
    }

    @SafeVarargs
    private static Trade sell(HolderGetter<Item> items, Holder<Item> item, int count, int maxUses, int tradeExperience, int price, ResourceKey<VillagerType>... types) {
        return Trade.builder(Trade.Entry.of(item, count))
            .wants(Trade.Entry.ofEmerald(items, price))
            .maxUses(maxUses)
            .tradeExperience(tradeExperience)
            .merchantPredicate(
                LootItemEntityPropertyCondition.hasProperties(
                    LootContext.EntityTarget.THIS,
                    EntityPredicate.Builder.entity()
                        .subPredicate(VillagerPredicate.of(
                            HolderSet.direct(BuiltInRegistries.VILLAGER_TYPE::getOrThrow, types)
                        ))
                )
            )
            .build();
    }

    private static Trade sell(HolderGetter<Item> items, Holder<Item> item, int count, Holder<Item> processedItem, int processedCount, int maxUses, int tradeExperience, float priceMultiplier) {
        return Trade.builder(Trade.Entry.of(processedItem, processedCount))
            .wants(Trade.Entry.ofEmerald(items))
            .wants(Trade.Entry.of(item, count))
            .maxUses(maxUses)
            .tradeExperience(tradeExperience)
            .priceMultiplier(priceMultiplier)
            .build();
    }

    private static Trade sellSuspiciousStew(HolderGetter<Item> items, Holder<MobEffect> statusEffect, int duration) {
        return Trade.builder(
            Trade.Entry.of(items.getOrThrow(ItemIds.SUSPICIOUS_STEW), 1, SetStewEffectFunction.stewEffect()
                .withEffect(statusEffect, ConstantValue.exactly(duration))
                .build()))
            .wants(Trade.Entry.ofEmerald(items))
            .tradeExperience(15)
            .build();
    }

    private static Trade sellEnchantedItem(HolderGetter<Item> items, Holder<Item> item, int tradeExperience, int basePrice) {
        return sellEnchantedItem(items, item, tradeExperience, basePrice, 0.05f);
    }

    private static Trade sellEnchantedItem(HolderGetter<Item> items, Holder<Item> item, int tradeExperience, int basePrice, float priceMultiplier) {
        return sellEnchantedItem(items, item, tradeExperience, basePrice, priceMultiplier, 3);
    }

    private static Trade sellEnchantedItem(HolderGetter<Item> items, Holder<Item> item, int tradeExperience, int basePrice, float priceMultiplier, int maxUses) {
        return Trade.builder(Trade.Entry.of(item))
            .wants(Trade.Entry.ofEmerald(items, basePrice))
            .maxUses(maxUses)
            .tradeExperience(tradeExperience)
            .tradeModifier(EnchantWithLevelsTradeModifier.of(0, 5, 19))
            .priceMultiplier(priceMultiplier)
            .build();
    }

    private static Trade sellWithPotion(HolderGetter<Item> items, HolderSet<Potion> potions, Holder<Item> item, Holder<Item> resultItem, int tradeExperience) {
        return Trade.builder(Trade.Entry.of(resultItem, 5, SetRandomPotionItemModifier.of(potions)))
            .wants(Trade.Entry.ofEmerald(items, 2))
            .wants(Trade.Entry.of(item, 5))
            .tradeExperience(tradeExperience)
            .build();
    }

    private static Trade sellWithPotion(HolderGetter<Item> items, Holder<Potion> potion, Holder<Item> resultItem, int tradeExperience) {
        return Trade.builder(Trade.Entry.of(resultItem, 1, SetPotionFunction.setPotion(potion).build()))
            .wants(Trade.Entry.ofEmerald(items, 5))
            .tradeExperience(tradeExperience)
            .maxUses(1)
            .build();
    }

    private static Trade sellDyedItem(HolderGetter<Item> items, Holder<Item> item, int price) {
        return sellDyedItem(items, item, price, 1);
    }

    private static Trade sellDyedItem(HolderGetter<Item> items, Holder<Item> item, int price, int tradeExperience) {
        return Trade.builder(Trade.Entry.of(item, 1, DyeItemModifier.of(1.0f, 0.3f, 0.2f)))
            .wants(Trade.Entry.ofEmerald(items, price))
            .tradeExperience(tradeExperience)
            .build();
    }

    private static Trade sellMap(HolderGetter<Item> items, int price, TagKey<Structure> structure, String name, Holder<MapDecorationType> mapDecorationType, int tradeExperience) {
        SequenceFunction itemModifier = SequenceFunction.of(List.of(
            ExplorationMapFunction.makeExplorationMap()
                .setDestination(structure)
                .setMapDecoration(mapDecorationType)
                .setSearchRadius(100)
                .build(),
            SetNameFunction.setName(Component.translatable(name), SetNameFunction.Target.ITEM_NAME)
                .build()
        ));
        return Trade.builder(Trade.Entry.of(items.getOrThrow(ItemIds.MAP), 1, itemModifier))
            .wants(Trade.Entry.ofEmerald(items, price))
            .tradeExperience(tradeExperience)
            .build();
    }

    @SafeVarargs
    private static Trade sellMap(HolderGetter<Item> items, int price, TagKey<Structure> structure, String name, Holder<MapDecorationType> decoration, int maxUses, int experience, ResourceKey<VillagerType>... types) {
        SequenceFunction itemModifier = SequenceFunction.of(List.of(
            ExplorationMapFunction.makeExplorationMap()
                .setDestination(structure)
                .setMapDecoration(decoration)
                .setSearchRadius(100)
                .build(),
            SetNameFunction.setName(Component.translatable(name), SetNameFunction.Target.ITEM_NAME)
                .build()
        ));
        return Trade.builder(Trade.Entry.of(items.getOrThrow(ItemIds.MAP), 1, itemModifier))
            .wants(Trade.Entry.ofEmerald(items, price))
            .maxUses(maxUses)
            .tradeExperience(experience)
            .merchantPredicate(
                LootItemEntityPropertyCondition.hasProperties(
                    LootContext.EntityTarget.THIS,
                    EntityPredicate.Builder.entity()
                        .subPredicate(VillagerPredicate.of(
                            HolderSet.direct(BuiltInRegistries.VILLAGER_TYPE::getOrThrow, types)
                        ))
                )
            )
            .build();
    }

    private static Trade sellEnchantedBook(HolderGetter<Item> items, int tradeExperience, HolderSet<Enchantment> enchantments) {
        return Trade.builder(Trade.Entry.of(items.getOrThrow(ItemIds.BOOK)))
            .wants(Trade.Entry.ofEmerald(items, 2))
            .wants(Trade.Entry.of(items.getOrThrow(ItemIds.BOOK)))
            .tradeExperience(tradeExperience)
            .tradeModifier(SingleEnchantmentTradeModifier.of(0, 5, 10, 3, enchantments))
            .priceMultiplier(VillagerTradesAccessor.highPriceMultiplier())
            .build();
    }

    private static ResourceKey<Trade> of(String id) {
        return ResourceKey.create(ItematicRegistries.TRADE, Identifier.withDefaultNamespace(id));
    }
}
