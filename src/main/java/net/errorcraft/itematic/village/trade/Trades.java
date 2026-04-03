package net.errorcraft.itematic.village.trade;

import net.errorcraft.itematic.entity.effect.StatusEffectKeys;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.loot.function.DyeItemModifier;
import net.errorcraft.itematic.loot.function.SetRandomPotionItemModifier;
import net.errorcraft.itematic.mixin.village.TradeOffersAccessor;
import net.errorcraft.itematic.potion.PotionTags;
import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.errorcraft.itematic.village.trade.modifier.modifiers.EnchantWithLevelsTradeModifier;
import net.errorcraft.itematic.village.trade.modifier.modifiers.ItemFromTypeTradeModifier;
import net.errorcraft.itematic.village.trade.modifier.modifiers.SingleEnchantmentTradeModifier;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;
import net.minecraft.item.map.MapDecorationType;
import net.minecraft.item.map.MapDecorationTypes;
import net.minecraft.loot.function.AndLootFunction;
import net.minecraft.loot.function.ExplorationMapLootFunction;
import net.minecraft.loot.function.SetNameLootFunction;
import net.minecraft.loot.function.SetStewEffectLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.registry.tag.StructureTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.village.VillagerType;
import net.minecraft.world.gen.structure.Structure;

import java.util.List;
import java.util.Map;

public class Trades {
    public static final RegistryKey<Trade> BUY_WHEAT = of("buy_wheat");
    public static final RegistryKey<Trade> BUY_POTATO = of("buy_potato");
    public static final RegistryKey<Trade> BUY_CARROT = of("buy_carrot");
    public static final RegistryKey<Trade> BUY_BEETROOT = of("buy_beetroot");
    public static final RegistryKey<Trade> SELL_BREAD = of("sell_bread");
    public static final RegistryKey<Trade> BUY_PUMPKIN = of("buy_pumpkin");
    public static final RegistryKey<Trade> SELL_PUMPKIN_PIE = of("sell_pumpkin_pie");
    public static final RegistryKey<Trade> SELL_APPLE = of("sell_apple");
    public static final RegistryKey<Trade> SELL_COOKIE = of("sell_cookie");
    public static final RegistryKey<Trade> BUY_MELON = of("buy_melon");
    public static final RegistryKey<Trade> SELL_CAKE = of("sell_cake");
    public static final RegistryKey<Trade> SELL_NIGHT_VISION_SUSPICIOUS_STEW = of("sell_night_vision_suspicious_stew");
    public static final RegistryKey<Trade> SELL_JUMP_BOOST_SUSPICIOUS_STEW = of("sell_jump_boost_suspicious_stew");
    public static final RegistryKey<Trade> SELL_WEAKNESS_SUSPICIOUS_STEW = of("sell_weakness_suspicious_stew");
    public static final RegistryKey<Trade> SELL_BLINDNESS_SUSPICIOUS_STEW = of("sell_blindness_suspicious_stew");
    public static final RegistryKey<Trade> SELL_POISON_SUSPICIOUS_STEW = of("sell_poison_suspicious_stew");
    public static final RegistryKey<Trade> SELL_SATURATION_SUSPICIOUS_STEW = of("sell_saturation_suspicious_stew");
    public static final RegistryKey<Trade> SELL_GOLDEN_CARROT = of("sell_golden_carrot");
    public static final RegistryKey<Trade> SELL_GLISTERING_MELON_SLICE = of("sell_glistering_melon_slice");
    public static final RegistryKey<Trade> BUY_STRING_NOVICE = of("buy_string_novice");
    public static final RegistryKey<Trade> BUY_COAL = of("buy_coal");
    public static final RegistryKey<Trade> SELL_COOKED_COD_FROM_COD = of("sell_cooked_cod_from_cod");
    public static final RegistryKey<Trade> SELL_COD_BUCKET = of("sell_cod_bucket");
    public static final RegistryKey<Trade> BUY_COD = of("buy_cod");
    public static final RegistryKey<Trade> SELL_COOKED_SALMON_FROM_SALMON = of("sell_cooked_salmon_from_salmon");
    public static final RegistryKey<Trade> SELL_CAMPFIRE = of("sell_campfire");
    public static final RegistryKey<Trade> BUY_SALMON = of("buy_salmon");
    public static final RegistryKey<Trade> SELL_ENCHANTED_FISHING_ROD = of("sell_enchanted_fishing_rod");
    public static final RegistryKey<Trade> BUY_TROPICAL_FISH = of("buy_tropical_fish");
    public static final RegistryKey<Trade> BUY_PUFFERFISH = of("buy_pufferfish");
    public static final RegistryKey<Trade> BUY_BOAT = of("buy_boat");
    public static final RegistryKey<Trade> BUY_WHITE_WOOL = of("buy_white_wool");
    public static final RegistryKey<Trade> BUY_BROWN_WOOL = of("buy_brown_wool");
    public static final RegistryKey<Trade> BUY_BLACK_WOOL = of("buy_black_wool");
    public static final RegistryKey<Trade> BUY_GRAY_WOOL = of("buy_gray_wool");
    public static final RegistryKey<Trade> SELL_SHEARS = of("sell_shears");
    public static final RegistryKey<Trade> BUY_WHITE_DYE = of("buy_white_dye");
    public static final RegistryKey<Trade> BUY_GRAY_DYE = of("buy_gray_dye");
    public static final RegistryKey<Trade> BUY_BLACK_DYE = of("buy_black_dye");
    public static final RegistryKey<Trade> BUY_LIGHT_BLUE_DYE = of("buy_light_blue_dye");
    public static final RegistryKey<Trade> BUY_LIME_DYE = of("buy_lime_dye");
    public static final RegistryKey<Trade> SELL_WHITE_WOOL = of("sell_white_wool");
    public static final RegistryKey<Trade> SELL_ORANGE_WOOL = of("sell_orange_wool");
    public static final RegistryKey<Trade> SELL_MAGENTA_WOOL = of("sell_magenta_wool");
    public static final RegistryKey<Trade> SELL_LIGHT_BLUE_WOOL = of("sell_light_blue_wool");
    public static final RegistryKey<Trade> SELL_YELLOW_WOOL = of("sell_yellow_wool");
    public static final RegistryKey<Trade> SELL_LIME_WOOL = of("sell_lime_wool");
    public static final RegistryKey<Trade> SELL_PINK_WOOL = of("sell_pink_wool");
    public static final RegistryKey<Trade> SELL_GRAY_WOOL = of("sell_gray_wool");
    public static final RegistryKey<Trade> SELL_LIGHT_GRAY_WOOL = of("sell_light_gray_wool");
    public static final RegistryKey<Trade> SELL_CYAN_WOOL = of("sell_cyan_wool");
    public static final RegistryKey<Trade> SELL_PURPLE_WOOL = of("sell_purple_wool");
    public static final RegistryKey<Trade> SELL_BLUE_WOOL = of("sell_blue_wool");
    public static final RegistryKey<Trade> SELL_BROWN_WOOL = of("sell_brown_wool");
    public static final RegistryKey<Trade> SELL_GREEN_WOOL = of("sell_green_wool");
    public static final RegistryKey<Trade> SELL_RED_WOOL = of("sell_red_wool");
    public static final RegistryKey<Trade> SELL_BLACK_WOOL = of("sell_black_wool");
    public static final RegistryKey<Trade> SELL_WHITE_CARPET = of("sell_white_carpet");
    public static final RegistryKey<Trade> SELL_ORANGE_CARPET = of("sell_orange_carpet");
    public static final RegistryKey<Trade> SELL_MAGENTA_CARPET = of("sell_magenta_carpet");
    public static final RegistryKey<Trade> SELL_LIGHT_BLUE_CARPET = of("sell_light_blue_carpet");
    public static final RegistryKey<Trade> SELL_YELLOW_CARPET = of("sell_yellow_carpet");
    public static final RegistryKey<Trade> SELL_LIME_CARPET = of("sell_lime_carpet");
    public static final RegistryKey<Trade> SELL_PINK_CARPET = of("sell_pink_carpet");
    public static final RegistryKey<Trade> SELL_GRAY_CARPET = of("sell_gray_carpet");
    public static final RegistryKey<Trade> SELL_LIGHT_GRAY_CARPET = of("sell_light_gray_carpet");
    public static final RegistryKey<Trade> SELL_CYAN_CARPET = of("sell_cyan_carpet");
    public static final RegistryKey<Trade> SELL_PURPLE_CARPET = of("sell_purple_carpet");
    public static final RegistryKey<Trade> SELL_BLUE_CARPET = of("sell_blue_carpet");
    public static final RegistryKey<Trade> SELL_BROWN_CARPET = of("sell_brown_carpet");
    public static final RegistryKey<Trade> SELL_GREEN_CARPET = of("sell_green_carpet");
    public static final RegistryKey<Trade> SELL_RED_CARPET = of("sell_red_carpet");
    public static final RegistryKey<Trade> SELL_BLACK_CARPET = of("sell_black_carpet");
    public static final RegistryKey<Trade> BUY_YELLOW_DYE = of("buy_yellow_dye");
    public static final RegistryKey<Trade> BUY_LIGHT_GRAY_DYE = of("buy_light_gray_dye");
    public static final RegistryKey<Trade> BUY_ORANGE_DYE = of("buy_orange_dye");
    public static final RegistryKey<Trade> BUY_RED_DYE = of("buy_red_dye");
    public static final RegistryKey<Trade> BUY_PINK_DYE = of("buy_pink_dye");
    public static final RegistryKey<Trade> SELL_WHITE_BED = of("sell_white_bed");
    public static final RegistryKey<Trade> SELL_YELLOW_BED = of("sell_yellow_bed");
    public static final RegistryKey<Trade> SELL_RED_BED = of("sell_red_bed");
    public static final RegistryKey<Trade> SELL_BLACK_BED = of("sell_black_bed");
    public static final RegistryKey<Trade> SELL_BLUE_BED = of("sell_blue_bed");
    public static final RegistryKey<Trade> SELL_BROWN_BED = of("sell_brown_bed");
    public static final RegistryKey<Trade> SELL_CYAN_BED = of("sell_cyan_bed");
    public static final RegistryKey<Trade> SELL_GRAY_BED = of("sell_gray_bed");
    public static final RegistryKey<Trade> SELL_GREEN_BED = of("sell_green_bed");
    public static final RegistryKey<Trade> SELL_LIGHT_BLUE_BED = of("sell_light_blue_bed");
    public static final RegistryKey<Trade> SELL_LIGHT_GRAY_BED = of("sell_light_gray_bed");
    public static final RegistryKey<Trade> SELL_LIME_BED = of("sell_lime_bed");
    public static final RegistryKey<Trade> SELL_MAGENTA_BED = of("sell_magenta_bed");
    public static final RegistryKey<Trade> SELL_ORANGE_BED = of("sell_orange_bed");
    public static final RegistryKey<Trade> SELL_PINK_BED = of("sell_pink_bed");
    public static final RegistryKey<Trade> SELL_PURPLE_BED = of("sell_purple_bed");
    public static final RegistryKey<Trade> BUY_BROWN_DYE = of("buy_brown_dye");
    public static final RegistryKey<Trade> BUY_PURPLE_DYE = of("buy_purple_dye");
    public static final RegistryKey<Trade> BUY_BLUE_DYE = of("buy_blue_dye");
    public static final RegistryKey<Trade> BUY_GREEN_DYE = of("buy_green_dye");
    public static final RegistryKey<Trade> BUY_MAGENTA_DYE = of("buy_magenta_dye");
    public static final RegistryKey<Trade> BUY_CYAN_DYE = of("buy_cyan_dye");
    public static final RegistryKey<Trade> SELL_WHITE_BANNER = of("sell_white_banner");
    public static final RegistryKey<Trade> SELL_BLUE_BANNER = of("sell_blue_banner");
    public static final RegistryKey<Trade> SELL_LIGHT_BLUE_BANNER = of("sell_light_blue_banner");
    public static final RegistryKey<Trade> SELL_RED_BANNER = of("sell_red_banner");
    public static final RegistryKey<Trade> SELL_PINK_BANNER = of("sell_pink_banner");
    public static final RegistryKey<Trade> SELL_GREEN_BANNER = of("sell_green_banner");
    public static final RegistryKey<Trade> SELL_LIME_BANNER = of("sell_lime_banner");
    public static final RegistryKey<Trade> SELL_GRAY_BANNER = of("sell_gray_banner");
    public static final RegistryKey<Trade> SELL_BLACK_BANNER = of("sell_black_banner");
    public static final RegistryKey<Trade> SELL_PURPLE_BANNER = of("sell_purple_banner");
    public static final RegistryKey<Trade> SELL_MAGENTA_BANNER = of("sell_magenta_banner");
    public static final RegistryKey<Trade> SELL_CYAN_BANNER = of("sell_cyan_banner");
    public static final RegistryKey<Trade> SELL_BROWN_BANNER = of("sell_brown_banner");
    public static final RegistryKey<Trade> SELL_YELLOW_BANNER = of("sell_yellow_banner");
    public static final RegistryKey<Trade> SELL_ORANGE_BANNER = of("sell_orange_banner");
    public static final RegistryKey<Trade> SELL_LIGHT_GRAY_BANNER = of("sell_light_gray_banner");
    public static final RegistryKey<Trade> SELL_PAINTING = of("sell_painting");
    public static final RegistryKey<Trade> BUY_STICK = of("buy_stick");
    public static final RegistryKey<Trade> SELL_ARROW = of("sell_arrow");
    public static final RegistryKey<Trade> SELL_FLINT_FROM_GRAVEL = of("sell_flint_from_gravel");
    public static final RegistryKey<Trade> BUY_FLINT_APPRENTICE = of("buy_flint_apprentice");
    public static final RegistryKey<Trade> SELL_BOW = of("sell_bow");
    public static final RegistryKey<Trade> BUY_STRING_JOURNEYMAN = of("buy_string_journeyman");
    public static final RegistryKey<Trade> SELL_CROSSBOW = of("sell_crossbow");
    public static final RegistryKey<Trade> BUY_FEATHER = of("buy_feather");
    public static final RegistryKey<Trade> SELL_ENCHANTED_BOW = of("sell_enchanted_bow");
    public static final RegistryKey<Trade> BUY_TRIPWIRE_HOOK = of("buy_tripwire_hook");
    public static final RegistryKey<Trade> SELL_ENCHANTED_CROSSBOW = of("sell_enchanted_crossbow");
    public static final RegistryKey<Trade> SELL_TIPPED_ARROW = of("sell_tipped_arrow");
    public static final RegistryKey<Trade> BUY_PAPER = of("buy_paper");
    public static final RegistryKey<Trade> SELL_ENCHANTED_BOOK_NOVICE = of("sell_enchanted_book_novice");
    public static final RegistryKey<Trade> SELL_BOOKSHELF = of("sell_bookshelf");
    public static final RegistryKey<Trade> BUY_BOOK = of("buy_book");
    public static final RegistryKey<Trade> SELL_ENCHANTED_BOOK_APPRENTICE = of("sell_enchanted_book_apprentice");
    public static final RegistryKey<Trade> SELL_LANTERN = of("sell_lantern");
    public static final RegistryKey<Trade> BUY_INK_SAC = of("buy_ink_sac");
    public static final RegistryKey<Trade> SELL_ENCHANTED_BOOK_JOURNEYMAN = of("sell_enchanted_book_journeyman");
    public static final RegistryKey<Trade> SELL_GLASS = of("sell_glass");
    public static final RegistryKey<Trade> BUY_WRITABLE_BOOK = of("buy_writable_book");
    public static final RegistryKey<Trade> SELL_ENCHANTED_BOOK_EXPERT = of("sell_enchanted_book_expert");
    public static final RegistryKey<Trade> SELL_CLOCK = of("sell_clock");
    public static final RegistryKey<Trade> SELL_COMPASS = of("sell_compass");
    public static final RegistryKey<Trade> SELL_NAME_TAG = of("sell_name_tag");
    public static final RegistryKey<Trade> SELL_MAP = of("sell_map");
    public static final RegistryKey<Trade> BUY_GLASS_PANE = of("buy_glass_pane");
    public static final RegistryKey<Trade> SELL_MONUMENT_MAP = of("sell_monument_map");
    public static final RegistryKey<Trade> BUY_COMPASS = of("buy_compass");
    public static final RegistryKey<Trade> SELL_MANSION_MAP = of("sell_mansion_map");
    public static final RegistryKey<Trade> SELL_ITEM_FRAME = of("sell_item_frame");
    public static final RegistryKey<Trade> SELL_GLOBE_BANNER_PATTERN = of("sell_globe_banner_pattern");
    public static final RegistryKey<Trade> BUY_ROTTEN_FLESH = of("buy_rotten_flesh");
    public static final RegistryKey<Trade> SELL_REDSTONE = of("sell_redstone");
    public static final RegistryKey<Trade> BUY_GOLD_INGOT = of("buy_gold_ingot");
    public static final RegistryKey<Trade> SELL_LAPIS_LAZULI = of("sell_lapis_lazuli");
    public static final RegistryKey<Trade> BUY_RABBIT_FOOT = of("buy_rabbit_foot");
    public static final RegistryKey<Trade> SELL_GLOWSTONE = of("sell_glowstone");
    public static final RegistryKey<Trade> BUY_TURTLE_SCUTE = of("buy_turtle_scute");
    public static final RegistryKey<Trade> BUY_GLASS_BOTTLE = of("buy_glass_bottle");
    public static final RegistryKey<Trade> SELL_ENDER_PEARL = of("sell_ender_pearl");
    public static final RegistryKey<Trade> BUY_NETHER_WART = of("buy_nether_wart");
    public static final RegistryKey<Trade> SELL_EXPERIENCE_BOTTLE = of("sell_experience_bottle");
    public static final RegistryKey<Trade> BUY_COAL_NOVICE_MORE_ITEMS = of("buy_coal_novice_more_items");
    public static final RegistryKey<Trade> SELL_IRON_LEGGINGS = of("sell_iron_leggings");
    public static final RegistryKey<Trade> SELL_IRON_BOOTS = of("sell_iron_boots");
    public static final RegistryKey<Trade> SELL_IRON_HELMET = of("sell_iron_helmet");
    public static final RegistryKey<Trade> SELL_IRON_CHESTPLATE = of("sell_iron_chestplate");
    public static final RegistryKey<Trade> BUY_IRON_INGOT = of("buy_iron_ingot");
    public static final RegistryKey<Trade> SELL_BELL = of("sell_bell");
    public static final RegistryKey<Trade> SELL_CHAINMAIL_BOOTS = of("sell_chainmail_boots");
    public static final RegistryKey<Trade> SELL_CHAINMAIL_LEGGINGS = of("sell_chainmail_leggings");
    public static final RegistryKey<Trade> BUY_LAVA_BUCKET = of("buy_lava_bucket");
    public static final RegistryKey<Trade> BUY_DIAMOND_JOURNEYMAN = of("buy_diamond_journeyman");
    public static final RegistryKey<Trade> SELL_CHAINMAIL_HELMET = of("sell_chainmail_helmet");
    public static final RegistryKey<Trade> SELL_CHAINMAIL_CHESTPLATE = of("sell_chainmail_chestplate");
    public static final RegistryKey<Trade> SELL_SHIELD = of("sell_shield");
    public static final RegistryKey<Trade> SELL_ENCHANTED_DIAMOND_LEGGINGS = of("sell_enchanted_diamond_leggings");
    public static final RegistryKey<Trade> SELL_ENCHANTED_DIAMOND_BOOTS = of("sell_enchanted_diamond_boots");
    public static final RegistryKey<Trade> SELL_ENCHANTED_DIAMOND_HELMET = of("sell_enchanted_diamond_helmet");
    public static final RegistryKey<Trade> SELL_ENCHANTED_DIAMOND_CHESTPLATE = of("sell_enchanted_diamond_chestplate");
    public static final RegistryKey<Trade> SELL_IRON_AXE = of("sell_iron_axe");
    public static final RegistryKey<Trade> SELL_ENCHANTED_IRON_SWORD = of("sell_enchanted_iron_sword");
    public static final RegistryKey<Trade> BUY_FLINT_WEAPONSMITH_JOURNEYMAN = of("buy_flint_weaponsmith_journeyman");
    public static final RegistryKey<Trade> BUY_DIAMOND_EXPERT = of("buy_diamond_expert");
    public static final RegistryKey<Trade> SELL_ENCHANTED_DIAMOND_AXE = of("sell_enchanted_diamond_axe");
    public static final RegistryKey<Trade> SELL_ENCHANTED_DIAMOND_SWORD = of("sell_enchanted_diamond_sword");
    public static final RegistryKey<Trade> SELL_STONE_AXE = of("sell_stone_axe");
    public static final RegistryKey<Trade> SELL_STONE_SHOVEL = of("sell_stone_shovel");
    public static final RegistryKey<Trade> SELL_STONE_PICKAXE = of("sell_stone_pickaxe");
    public static final RegistryKey<Trade> SELL_STONE_HOE = of("sell_stone_hoe");
    public static final RegistryKey<Trade> BUY_FLINT_TOOLSMITH_JOURNEYMAN = of("buy_flint_toolsmith_journeyman");
    public static final RegistryKey<Trade> SELL_ENCHANTED_IRON_AXE = of("sell_enchanted_iron_axe");
    public static final RegistryKey<Trade> SELL_ENCHANTED_IRON_SHOVEL = of("sell_enchanted_iron_shovel");
    public static final RegistryKey<Trade> SELL_ENCHANTED_IRON_PICKAXE = of("sell_enchanted_iron_pickaxe");
    public static final RegistryKey<Trade> SELL_DIAMOND_HOE = of("sell_diamond_hoe");
    public static final RegistryKey<Trade> SELL_ENCHANTED_DIAMOND_SHOVEL = of("sell_enchanted_diamond_shovel");
    public static final RegistryKey<Trade> SELL_ENCHANTED_DIAMOND_PICKAXE = of("sell_enchanted_diamond_pickaxe");
    public static final RegistryKey<Trade> BUY_CHICKEN = of("buy_chicken");
    public static final RegistryKey<Trade> BUY_PORKCHOP = of("buy_porkchop");
    public static final RegistryKey<Trade> BUY_RABBIT = of("buy_rabbit");
    public static final RegistryKey<Trade> SELL_RABBIT_STEW = of("sell_rabbit_stew");
    public static final RegistryKey<Trade> SELL_COOKED_PORKCHOP = of("sell_cooked_porkchop");
    public static final RegistryKey<Trade> SELL_COOKED_CHICKEN = of("sell_cooked_chicken");
    public static final RegistryKey<Trade> BUY_MUTTON = of("buy_mutton");
    public static final RegistryKey<Trade> BUY_BEEF = of("buy_beef");
    public static final RegistryKey<Trade> BUY_DRIED_KELP_BLOCK = of("buy_dried_kelp_block");
    public static final RegistryKey<Trade> BUY_SWEET_BERRIES = of("buy_sweet_berries");
    public static final RegistryKey<Trade> BUY_LEATHER = of("buy_leather");
    public static final RegistryKey<Trade> SELL_LEATHER_LEGGINGS = of("sell_leather_leggings");
    public static final RegistryKey<Trade> SELL_LEATHER_CHESTPLATE = of("sell_leather_chestplate");
    public static final RegistryKey<Trade> SELL_LEATHER_HELMET_APPRENTICE = of("sell_leather_helmet_apprentice");
    public static final RegistryKey<Trade> SELL_LEATHER_BOOTS = of("sell_leather_boots");
    public static final RegistryKey<Trade> BUY_RABBIT_HIDE = of("buy_rabbit_hide");
    public static final RegistryKey<Trade> SELL_LEATHER_HORSE_ARMOR = of("sell_leather_horse_armor");
    public static final RegistryKey<Trade> SELL_SADDLE = of("sell_saddle");
    public static final RegistryKey<Trade> SELL_LEATHER_HELMET_MASTER = of("sell_leather_helmet_master");
    public static final RegistryKey<Trade> BUY_CLAY_BALL = of("buy_clay_ball");
    public static final RegistryKey<Trade> SELL_BRICK = of("sell_brick");
    public static final RegistryKey<Trade> BUY_STONE = of("buy_stone");
    public static final RegistryKey<Trade> SELL_CHISELED_STONE_BRICKS = of("sell_chiseled_stone_bricks");
    public static final RegistryKey<Trade> BUY_GRANITE = of("buy_granite");
    public static final RegistryKey<Trade> BUY_ANDESITE = of("buy_andesite");
    public static final RegistryKey<Trade> BUY_DIORITE = of("buy_diorite");
    public static final RegistryKey<Trade> SELL_DRIPSTONE_BLOCK = of("sell_dripstone_block");
    public static final RegistryKey<Trade> SELL_POLISHED_ANDESITE = of("sell_polished_andesite");
    public static final RegistryKey<Trade> SELL_POLISHED_DIORITE = of("sell_polished_diorite");
    public static final RegistryKey<Trade> SELL_POLISHED_GRANITE = of("sell_polished_granite");
    public static final RegistryKey<Trade> BUY_QUARTZ = of("buy_quartz");
    public static final RegistryKey<Trade> SELL_ORANGE_TERRACOTTA = of("sell_orange_terracotta");
    public static final RegistryKey<Trade> SELL_WHITE_TERRACOTTA = of("sell_white_terracotta");
    public static final RegistryKey<Trade> SELL_BLUE_TERRACOTTA = of("sell_blue_terracotta");
    public static final RegistryKey<Trade> SELL_LIGHT_BLUE_TERRACOTTA = of("sell_light_blue_terracotta");
    public static final RegistryKey<Trade> SELL_GRAY_TERRACOTTA = of("sell_gray_terracotta");
    public static final RegistryKey<Trade> SELL_LIGHT_GRAY_TERRACOTTA = of("sell_light_gray_terracotta");
    public static final RegistryKey<Trade> SELL_BLACK_TERRACOTTA = of("sell_black_terracotta");
    public static final RegistryKey<Trade> SELL_RED_TERRACOTTA = of("sell_red_terracotta");
    public static final RegistryKey<Trade> SELL_PINK_TERRACOTTA = of("sell_pink_terracotta");
    public static final RegistryKey<Trade> SELL_MAGENTA_TERRACOTTA = of("sell_magenta_terracotta");
    public static final RegistryKey<Trade> SELL_LIME_TERRACOTTA = of("sell_lime_terracotta");
    public static final RegistryKey<Trade> SELL_GREEN_TERRACOTTA = of("sell_green_terracotta");
    public static final RegistryKey<Trade> SELL_CYAN_TERRACOTTA = of("sell_cyan_terracotta");
    public static final RegistryKey<Trade> SELL_PURPLE_TERRACOTTA = of("sell_purple_terracotta");
    public static final RegistryKey<Trade> SELL_YELLOW_TERRACOTTA = of("sell_yellow_terracotta");
    public static final RegistryKey<Trade> SELL_BROWN_TERRACOTTA = of("sell_brown_terracotta");
    public static final RegistryKey<Trade> SELL_ORANGE_GLAZED_TERRACOTTA = of("sell_orange_glazed_terracotta");
    public static final RegistryKey<Trade> SELL_WHITE_GLAZED_TERRACOTTA = of("sell_white_glazed_terracotta");
    public static final RegistryKey<Trade> SELL_BLUE_GLAZED_TERRACOTTA = of("sell_blue_glazed_terracotta");
    public static final RegistryKey<Trade> SELL_LIGHT_BLUE_GLAZED_TERRACOTTA = of("sell_light_blue_glazed_terracotta");
    public static final RegistryKey<Trade> SELL_GRAY_GLAZED_TERRACOTTA = of("sell_gray_glazed_terracotta");
    public static final RegistryKey<Trade> SELL_LIGHT_GRAY_GLAZED_TERRACOTTA = of("sell_light_gray_glazed_terracotta");
    public static final RegistryKey<Trade> SELL_BLACK_GLAZED_TERRACOTTA = of("sell_black_glazed_terracotta");
    public static final RegistryKey<Trade> SELL_RED_GLAZED_TERRACOTTA = of("sell_red_glazed_terracotta");
    public static final RegistryKey<Trade> SELL_PINK_GLAZED_TERRACOTTA = of("sell_pink_glazed_terracotta");
    public static final RegistryKey<Trade> SELL_MAGENTA_GLAZED_TERRACOTTA = of("sell_magenta_glazed_terracotta");
    public static final RegistryKey<Trade> SELL_LIME_GLAZED_TERRACOTTA = of("sell_lime_glazed_terracotta");
    public static final RegistryKey<Trade> SELL_GREEN_GLAZED_TERRACOTTA = of("sell_green_glazed_terracotta");
    public static final RegistryKey<Trade> SELL_CYAN_GLAZED_TERRACOTTA = of("sell_cyan_glazed_terracotta");
    public static final RegistryKey<Trade> SELL_PURPLE_GLAZED_TERRACOTTA = of("sell_purple_glazed_terracotta");
    public static final RegistryKey<Trade> SELL_YELLOW_GLAZED_TERRACOTTA = of("sell_yellow_glazed_terracotta");
    public static final RegistryKey<Trade> SELL_BROWN_GLAZED_TERRACOTTA = of("sell_brown_glazed_terracotta");
    public static final RegistryKey<Trade> SELL_QUARTZ_PILLAR = of("sell_quartz_pillar");
    public static final RegistryKey<Trade> SELL_QUARTZ_BLOCK = of("sell_quartz_block");
    public static final RegistryKey<Trade> SELL_SEA_PICKLE = of("sell_sea_pickle");
    public static final RegistryKey<Trade> SELL_SLIME_BALL = of("sell_slime_ball");
    public static final RegistryKey<Trade> SELL_GLOWSTONE_WANDERING_TRADER = of("sell_glowstone_wandering_trader");
    public static final RegistryKey<Trade> SELL_NAUTILUS_SHELL = of("sell_nautilus_shell");
    public static final RegistryKey<Trade> SELL_FERN = of("sell_fern");
    public static final RegistryKey<Trade> SELL_SUGAR_CANE = of("sell_sugar_cane");
    public static final RegistryKey<Trade> SELL_PUMPKIN = of("sell_pumpkin");
    public static final RegistryKey<Trade> SELL_KELP = of("sell_kelp");
    public static final RegistryKey<Trade> SELL_CACTUS = of("sell_cactus");
    public static final RegistryKey<Trade> SELL_DANDELION = of("sell_dandelion");
    public static final RegistryKey<Trade> SELL_POPPY = of("sell_poppy");
    public static final RegistryKey<Trade> SELL_BLUE_ORCHID = of("sell_blue_orchid");
    public static final RegistryKey<Trade> SELL_ALLIUM = of("sell_allium");
    public static final RegistryKey<Trade> SELL_AZURE_BLUET = of("sell_azure_bluet");
    public static final RegistryKey<Trade> SELL_RED_TULIP = of("sell_red_tulip");
    public static final RegistryKey<Trade> SELL_ORANGE_TULIP = of("sell_orange_tulip");
    public static final RegistryKey<Trade> SELL_WHITE_TULIP = of("sell_white_tulip");
    public static final RegistryKey<Trade> SELL_PINK_TULIP = of("sell_pink_tulip");
    public static final RegistryKey<Trade> SELL_OXEYE_DAISY = of("sell_oxeye_daisy");
    public static final RegistryKey<Trade> SELL_CORNFLOWER = of("sell_cornflower");
    public static final RegistryKey<Trade> SELL_LILY_OF_THE_VALLEY = of("sell_lily_of_the_valley");
    public static final RegistryKey<Trade> SELL_WHEAT_SEEDS = of("sell_wheat_seeds");
    public static final RegistryKey<Trade> SELL_BEETROOT_SEEDS = of("sell_beetroot_seeds");
    public static final RegistryKey<Trade> SELL_PUMPKIN_SEEDS = of("sell_pumpkin_seeds");
    public static final RegistryKey<Trade> SELL_MELON_SEEDS = of("sell_melon_seeds");
    public static final RegistryKey<Trade> SELL_ACACIA_SAPLING = of("sell_acacia_sapling");
    public static final RegistryKey<Trade> SELL_BIRCH_SAPLING = of("sell_birch_sapling");
    public static final RegistryKey<Trade> SELL_DARK_OAK_SAPLING = of("sell_dark_oak_sapling");
    public static final RegistryKey<Trade> SELL_JUNGLE_SAPLING = of("sell_jungle_sapling");
    public static final RegistryKey<Trade> SELL_OAK_SAPLING = of("sell_oak_sapling");
    public static final RegistryKey<Trade> SELL_SPRUCE_SAPLING = of("sell_spruce_sapling");
    public static final RegistryKey<Trade> SELL_CHERRY_SAPLING = of("sell_cherry_sapling");
    public static final RegistryKey<Trade> SELL_MANGROVE_PROPAGULE = of("sell_mangrove_propagule");
    public static final RegistryKey<Trade> SELL_RED_DYE = of("sell_red_dye");
    public static final RegistryKey<Trade> SELL_WHITE_DYE = of("sell_white_dye");
    public static final RegistryKey<Trade> SELL_BLUE_DYE = of("sell_blue_dye");
    public static final RegistryKey<Trade> SELL_PINK_DYE = of("sell_pink_dye");
    public static final RegistryKey<Trade> SELL_BLACK_DYE = of("sell_black_dye");
    public static final RegistryKey<Trade> SELL_GREEN_DYE = of("sell_green_dye");
    public static final RegistryKey<Trade> SELL_LIGHT_GRAY_DYE = of("sell_light_gray_dye");
    public static final RegistryKey<Trade> SELL_MAGENTA_DYE = of("sell_magenta_dye");
    public static final RegistryKey<Trade> SELL_YELLOW_DYE = of("sell_yellow_dye");
    public static final RegistryKey<Trade> SELL_GRAY_DYE = of("sell_gray_dye");
    public static final RegistryKey<Trade> SELL_PURPLE_DYE = of("sell_purple_dye");
    public static final RegistryKey<Trade> SELL_LIGHT_BLUE_DYE = of("sell_light_blue_dye");
    public static final RegistryKey<Trade> SELL_LIME_DYE = of("sell_lime_dye");
    public static final RegistryKey<Trade> SELL_ORANGE_DYE = of("sell_orange_dye");
    public static final RegistryKey<Trade> SELL_BROWN_DYE = of("sell_brown_dye");
    public static final RegistryKey<Trade> SELL_CYAN_DYE = of("sell_cyan_dye");
    public static final RegistryKey<Trade> SELL_BRAIN_CORAL_BLOCK = of("sell_brain_coral_block");
    public static final RegistryKey<Trade> SELL_BUBBLE_CORAL_BLOCK = of("sell_bubble_coral_block");
    public static final RegistryKey<Trade> SELL_FIRE_CORAL_BLOCK = of("sell_fire_coral_block");
    public static final RegistryKey<Trade> SELL_HORN_CORAL_BLOCK = of("sell_horn_coral_block");
    public static final RegistryKey<Trade> SELL_TUBE_CORAL_BLOCK = of("sell_tube_coral_block");
    public static final RegistryKey<Trade> SELL_VINE = of("sell_vine");
    public static final RegistryKey<Trade> SELL_BROWN_MUSHROOM = of("sell_brown_mushroom");
    public static final RegistryKey<Trade> SELL_RED_MUSHROOM = of("sell_red_mushroom");
    public static final RegistryKey<Trade> SELL_LILY_PAD = of("sell_lily_pad");
    public static final RegistryKey<Trade> SELL_SMALL_DRIPLEAF = of("sell_small_dripleaf");
    public static final RegistryKey<Trade> SELL_SAND = of("sell_sand");
    public static final RegistryKey<Trade> SELL_RED_SAND = of("sell_red_sand");
    public static final RegistryKey<Trade> SELL_POINTED_DRIPSTONE = of("sell_pointed_dripstone");
    public static final RegistryKey<Trade> SELL_ROOTED_DIRT = of("sell_rooted_dirt");
    public static final RegistryKey<Trade> SELL_MOSS_BLOCK = of("sell_moss_block");
    public static final RegistryKey<Trade> SELL_TROPICAL_FISH_BUCKET = of("sell_tropical_fish_bucket");
    public static final RegistryKey<Trade> SELL_PUFFERFISH_BUCKET = of("sell_pufferfish_bucket");
    public static final RegistryKey<Trade> SELL_PACKED_ICE = of("sell_packed_ice");
    public static final RegistryKey<Trade> SELL_BLUE_ICE = of("sell_blue_ice");
    public static final RegistryKey<Trade> SELL_GUNPOWDER = of("sell_gunpowder");
    public static final RegistryKey<Trade> SELL_PODZOL = of("sell_podzol");

    private Trades() {}

    public static void bootstrap(Registerable<Trade> registerable) {
        RegistryEntryLookup<Item> items = registerable.getRegistryLookup(RegistryKeys.ITEM);
        RegistryEntryLookup<Enchantment> enchantments = registerable.getRegistryLookup(RegistryKeys.ENCHANTMENT);
        RegistryEntryLookup<StatusEffect> statusEffects = registerable.getRegistryLookup(RegistryKeys.STATUS_EFFECT);
        RegistryEntryLookup<Potion> potions = registerable.getRegistryLookup(RegistryKeys.POTION);
        RegistryEntryLookup<VillagerType> villagerTypes = registerable.getRegistryLookup(RegistryKeys.VILLAGER_TYPE);

        registerable.register(BUY_WHEAT, buy(items, items.getOrThrow(ItemKeys.WHEAT), 20, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.noviceBuyTradeExperience()));
        registerable.register(BUY_POTATO, buy(items, items.getOrThrow(ItemKeys.POTATO), 26, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.noviceBuyTradeExperience()));
        registerable.register(BUY_CARROT, buy(items, items.getOrThrow(ItemKeys.CARROT), 22, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.noviceBuyTradeExperience()));
        registerable.register(BUY_BEETROOT, buy(items, items.getOrThrow(ItemKeys.BEETROOT), 15, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.noviceBuyTradeExperience()));
        registerable.register(SELL_BREAD, sell(items, items.getOrThrow(ItemKeys.BREAD), 6, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.noviceSellTradeExperience(), 1));
        registerable.register(BUY_PUMPKIN, buy(items, items.getOrThrow(ItemKeys.PUMPKIN), 6, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.apprenticeBuyTradeExperience()));
        registerable.register(SELL_PUMPKIN_PIE, sell(items, items.getOrThrow(ItemKeys.PUMPKIN_PIE), 4, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_APPLE, sell(items, items.getOrThrow(ItemKeys.APPLE), 4, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_COOKIE, sell(items, items.getOrThrow(ItemKeys.COOKIE), 18, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.journeymanSellTradeExperience(), 3));
        registerable.register(BUY_MELON, buy(items, items.getOrThrow(ItemKeys.MELON), 4, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.journeymanBuyTradeExperience()));
        registerable.register(SELL_CAKE, sell(items, items.getOrThrow(ItemKeys.CAKE), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.expertSellTradeExperience(), 1));
        registerable.register(SELL_NIGHT_VISION_SUSPICIOUS_STEW, sellSuspiciousStew(items, statusEffects.getOrThrow(StatusEffectKeys.NIGHT_VISION), 100));
        registerable.register(SELL_JUMP_BOOST_SUSPICIOUS_STEW, sellSuspiciousStew(items, statusEffects.getOrThrow(StatusEffectKeys.JUMP_BOOST), 160));
        registerable.register(SELL_WEAKNESS_SUSPICIOUS_STEW, sellSuspiciousStew(items, statusEffects.getOrThrow(StatusEffectKeys.WEAKNESS), 140));
        registerable.register(SELL_BLINDNESS_SUSPICIOUS_STEW, sellSuspiciousStew(items, statusEffects.getOrThrow(StatusEffectKeys.BLINDNESS), 120));
        registerable.register(SELL_POISON_SUSPICIOUS_STEW, sellSuspiciousStew(items, statusEffects.getOrThrow(StatusEffectKeys.POISON), 280));
        registerable.register(SELL_SATURATION_SUSPICIOUS_STEW, sellSuspiciousStew(items, statusEffects.getOrThrow(StatusEffectKeys.SATURATION), 7));
        registerable.register(SELL_GOLDEN_CARROT, sell(items, items.getOrThrow(ItemKeys.GOLDEN_CARROT), 3, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.masterTradeExperience(), 3));
        registerable.register(SELL_GLISTERING_MELON_SLICE, sell(items, items.getOrThrow(ItemKeys.GLISTERING_MELON_SLICE), 3, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.masterTradeExperience(), 4));
        registerable.register(BUY_STRING_NOVICE, buy(items, items.getOrThrow(ItemKeys.STRING), 20, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.noviceBuyTradeExperience()));
        registerable.register(BUY_COAL, buy(items, items.getOrThrow(ItemKeys.COAL), 10, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.noviceBuyTradeExperience()));
        registerable.register(SELL_COOKED_COD_FROM_COD, sell(items, items.getOrThrow(ItemKeys.COD), 6, items.getOrThrow(ItemKeys.COOKED_COD), 6, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.noviceSellTradeExperience(), TradeOffersAccessor.lowPriceMultiplier()));
        registerable.register(SELL_COD_BUCKET, sell(items, items.getOrThrow(ItemKeys.COD_BUCKET), 1, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.noviceSellTradeExperience(), 3));
        registerable.register(BUY_COD, buy(items, items.getOrThrow(ItemKeys.COD), 15, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.apprenticeBuyTradeExperience()));
        registerable.register(SELL_COOKED_SALMON_FROM_SALMON, sell(items, items.getOrThrow(ItemKeys.SALMON), 6, items.getOrThrow(ItemKeys.COOKED_SALMON), 6, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.apprenticeSellTradeExperience(), TradeOffersAccessor.lowPriceMultiplier()));
        registerable.register(SELL_CAMPFIRE, sell(items, items.getOrThrow(ItemKeys.CAMPFIRE), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.apprenticeSellTradeExperience(), 2));
        registerable.register(BUY_SALMON, buy(items, items.getOrThrow(ItemKeys.SALMON), 13, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.journeymanBuyTradeExperience()));
        registerable.register(SELL_ENCHANTED_FISHING_ROD, sellEnchantedItem(items, items.getOrThrow(ItemKeys.FISHING_ROD), TradeOffersAccessor.journeymanSellTradeExperience(), 3, TradeOffersAccessor.highPriceMultiplier()));
        registerable.register(BUY_TROPICAL_FISH, buy(items, items.getOrThrow(ItemKeys.TROPICAL_FISH), 6, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.expertBuyTradeExperience()));
        registerable.register(BUY_PUFFERFISH, buy(items, items.getOrThrow(ItemKeys.PUFFERFISH), 4, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.masterTradeExperience()));
        registerable.register(BUY_BOAT, buyFromType(items, items.getOrThrow(ItemKeys.OAK_BOAT), Map.of(
            villagerTypes.getOrThrow(VillagerType.PLAINS), items.getOrThrow(ItemKeys.OAK_BOAT),
            villagerTypes.getOrThrow(VillagerType.TAIGA), items.getOrThrow(ItemKeys.SPRUCE_BOAT),
            villagerTypes.getOrThrow(VillagerType.SNOW), items.getOrThrow(ItemKeys.SPRUCE_BOAT),
            villagerTypes.getOrThrow(VillagerType.DESERT), items.getOrThrow(ItemKeys.JUNGLE_BOAT),
            villagerTypes.getOrThrow(VillagerType.JUNGLE), items.getOrThrow(ItemKeys.JUNGLE_BOAT),
            villagerTypes.getOrThrow(VillagerType.SAVANNA), items.getOrThrow(ItemKeys.ACACIA_BOAT),
            villagerTypes.getOrThrow(VillagerType.SWAMP), items.getOrThrow(ItemKeys.DARK_OAK_BOAT)
        )));
        registerable.register(BUY_WHITE_WOOL, buy(items, items.getOrThrow(ItemKeys.WHITE_WOOL), 18, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.noviceBuyTradeExperience()));
        registerable.register(BUY_BROWN_WOOL, buy(items, items.getOrThrow(ItemKeys.BROWN_WOOL), 18, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.noviceBuyTradeExperience()));
        registerable.register(BUY_BLACK_WOOL, buy(items, items.getOrThrow(ItemKeys.BLACK_WOOL), 18, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.noviceBuyTradeExperience()));
        registerable.register(BUY_GRAY_WOOL, buy(items, items.getOrThrow(ItemKeys.GRAY_WOOL), 18, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.noviceBuyTradeExperience()));
        registerable.register(SELL_SHEARS, sell(items, items.getOrThrow(ItemKeys.SHEARS), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.noviceSellTradeExperience(), 2));
        registerable.register(BUY_WHITE_DYE, buy(items, items.getOrThrow(ItemKeys.WHITE_DYE), 12, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.apprenticeBuyTradeExperience()));
        registerable.register(BUY_GRAY_DYE, buy(items, items.getOrThrow(ItemKeys.GRAY_DYE), 12, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.apprenticeBuyTradeExperience()));
        registerable.register(BUY_BLACK_DYE, buy(items, items.getOrThrow(ItemKeys.BLACK_DYE), 12, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.apprenticeBuyTradeExperience()));
        registerable.register(BUY_LIGHT_BLUE_DYE, buy(items, items.getOrThrow(ItemKeys.LIGHT_BLUE_DYE), 12, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.apprenticeBuyTradeExperience()));
        registerable.register(BUY_LIME_DYE, buy(items, items.getOrThrow(ItemKeys.LIME_DYE), 12, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.apprenticeBuyTradeExperience()));
        registerable.register(SELL_WHITE_WOOL, sell(items, items.getOrThrow(ItemKeys.WHITE_WOOL), 1, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_ORANGE_WOOL, sell(items, items.getOrThrow(ItemKeys.ORANGE_WOOL), 1, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_MAGENTA_WOOL, sell(items, items.getOrThrow(ItemKeys.MAGENTA_WOOL), 1, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_LIGHT_BLUE_WOOL, sell(items, items.getOrThrow(ItemKeys.LIGHT_BLUE_WOOL), 1, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_YELLOW_WOOL, sell(items, items.getOrThrow(ItemKeys.YELLOW_WOOL), 1, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_LIME_WOOL, sell(items, items.getOrThrow(ItemKeys.LIME_WOOL), 1, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_PINK_WOOL, sell(items, items.getOrThrow(ItemKeys.PINK_WOOL), 1, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_GRAY_WOOL, sell(items, items.getOrThrow(ItemKeys.GRAY_WOOL), 1, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_LIGHT_GRAY_WOOL, sell(items, items.getOrThrow(ItemKeys.LIGHT_GRAY_WOOL), 1, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_CYAN_WOOL, sell(items, items.getOrThrow(ItemKeys.CYAN_WOOL), 1, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_PURPLE_WOOL, sell(items, items.getOrThrow(ItemKeys.PURPLE_WOOL), 1, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_BLUE_WOOL, sell(items, items.getOrThrow(ItemKeys.BLUE_WOOL), 1, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_BROWN_WOOL, sell(items, items.getOrThrow(ItemKeys.BROWN_WOOL), 1, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_GREEN_WOOL, sell(items, items.getOrThrow(ItemKeys.GREEN_WOOL), 1, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_RED_WOOL, sell(items, items.getOrThrow(ItemKeys.RED_WOOL), 1, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_BLACK_WOOL, sell(items, items.getOrThrow(ItemKeys.BLACK_WOOL), 1, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_WHITE_CARPET, sell(items, items.getOrThrow(ItemKeys.WHITE_CARPET), 4, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_ORANGE_CARPET, sell(items, items.getOrThrow(ItemKeys.ORANGE_CARPET), 4, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_MAGENTA_CARPET, sell(items, items.getOrThrow(ItemKeys.MAGENTA_CARPET), 4, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_LIGHT_BLUE_CARPET, sell(items, items.getOrThrow(ItemKeys.LIGHT_BLUE_CARPET), 4, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_YELLOW_CARPET, sell(items, items.getOrThrow(ItemKeys.YELLOW_CARPET), 4, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_LIME_CARPET, sell(items, items.getOrThrow(ItemKeys.LIME_CARPET), 4, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_PINK_CARPET, sell(items, items.getOrThrow(ItemKeys.PINK_CARPET), 4, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_GRAY_CARPET, sell(items, items.getOrThrow(ItemKeys.GRAY_CARPET), 4, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_LIGHT_GRAY_CARPET, sell(items, items.getOrThrow(ItemKeys.LIGHT_GRAY_CARPET), 4, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_CYAN_CARPET, sell(items, items.getOrThrow(ItemKeys.CYAN_CARPET), 4, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_PURPLE_CARPET, sell(items, items.getOrThrow(ItemKeys.PURPLE_CARPET), 4, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_BLUE_CARPET, sell(items, items.getOrThrow(ItemKeys.BLUE_CARPET), 4, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_BROWN_CARPET, sell(items, items.getOrThrow(ItemKeys.BROWN_CARPET), 4, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_GREEN_CARPET, sell(items, items.getOrThrow(ItemKeys.GREEN_CARPET), 4, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_RED_CARPET, sell(items, items.getOrThrow(ItemKeys.RED_CARPET), 4, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_BLACK_CARPET, sell(items, items.getOrThrow(ItemKeys.BLACK_CARPET), 4, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(BUY_YELLOW_DYE, buy(items, items.getOrThrow(ItemKeys.YELLOW_DYE), 12, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.journeymanBuyTradeExperience()));
        registerable.register(BUY_LIGHT_GRAY_DYE, buy(items, items.getOrThrow(ItemKeys.LIGHT_GRAY_DYE), 12, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.journeymanBuyTradeExperience()));
        registerable.register(BUY_ORANGE_DYE, buy(items, items.getOrThrow(ItemKeys.ORANGE_DYE), 12, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.journeymanBuyTradeExperience()));
        registerable.register(BUY_RED_DYE, buy(items, items.getOrThrow(ItemKeys.RED_DYE), 12, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.journeymanBuyTradeExperience()));
        registerable.register(BUY_PINK_DYE, buy(items, items.getOrThrow(ItemKeys.PINK_DYE), 12, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.journeymanBuyTradeExperience()));
        registerable.register(SELL_WHITE_BED, sell(items, items.getOrThrow(ItemKeys.WHITE_BED), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.journeymanSellTradeExperience(), 3));
        registerable.register(SELL_YELLOW_BED, sell(items, items.getOrThrow(ItemKeys.YELLOW_BED), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.journeymanSellTradeExperience(), 3));
        registerable.register(SELL_RED_BED, sell(items, items.getOrThrow(ItemKeys.RED_BED), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.journeymanSellTradeExperience(), 3));
        registerable.register(SELL_BLACK_BED, sell(items, items.getOrThrow(ItemKeys.BLACK_BED), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.journeymanSellTradeExperience(), 3));
        registerable.register(SELL_BLUE_BED, sell(items, items.getOrThrow(ItemKeys.BLUE_BED), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.journeymanSellTradeExperience(), 3));
        registerable.register(SELL_BROWN_BED, sell(items, items.getOrThrow(ItemKeys.BROWN_BED), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.journeymanSellTradeExperience(), 3));
        registerable.register(SELL_CYAN_BED, sell(items, items.getOrThrow(ItemKeys.CYAN_BED), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.journeymanSellTradeExperience(), 3));
        registerable.register(SELL_GRAY_BED, sell(items, items.getOrThrow(ItemKeys.GRAY_BED), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.journeymanSellTradeExperience(), 3));
        registerable.register(SELL_GREEN_BED, sell(items, items.getOrThrow(ItemKeys.GREEN_BED), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.journeymanSellTradeExperience(), 3));
        registerable.register(SELL_LIGHT_BLUE_BED, sell(items, items.getOrThrow(ItemKeys.LIGHT_BLUE_BED), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.journeymanSellTradeExperience(), 3));
        registerable.register(SELL_LIGHT_GRAY_BED, sell(items, items.getOrThrow(ItemKeys.LIGHT_GRAY_BED), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.journeymanSellTradeExperience(), 3));
        registerable.register(SELL_LIME_BED, sell(items, items.getOrThrow(ItemKeys.LIME_BED), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.journeymanSellTradeExperience(), 3));
        registerable.register(SELL_MAGENTA_BED, sell(items, items.getOrThrow(ItemKeys.MAGENTA_BED), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.journeymanSellTradeExperience(), 3));
        registerable.register(SELL_ORANGE_BED, sell(items, items.getOrThrow(ItemKeys.ORANGE_BED), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.journeymanSellTradeExperience(), 3));
        registerable.register(SELL_PINK_BED, sell(items, items.getOrThrow(ItemKeys.PINK_BED), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.journeymanSellTradeExperience(), 3));
        registerable.register(SELL_PURPLE_BED, sell(items, items.getOrThrow(ItemKeys.PURPLE_BED), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.journeymanSellTradeExperience(), 3));
        registerable.register(BUY_BROWN_DYE, buy(items, items.getOrThrow(ItemKeys.BROWN_DYE), 12, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.expertBuyTradeExperience()));
        registerable.register(BUY_PURPLE_DYE, buy(items, items.getOrThrow(ItemKeys.PURPLE_DYE), 12, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.expertBuyTradeExperience()));
        registerable.register(BUY_BLUE_DYE, buy(items, items.getOrThrow(ItemKeys.BLUE_DYE), 12, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.expertBuyTradeExperience()));
        registerable.register(BUY_GREEN_DYE, buy(items, items.getOrThrow(ItemKeys.GREEN_DYE), 12, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.expertBuyTradeExperience()));
        registerable.register(BUY_MAGENTA_DYE, buy(items, items.getOrThrow(ItemKeys.MAGENTA_DYE), 12, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.expertBuyTradeExperience()));
        registerable.register(BUY_CYAN_DYE, buy(items, items.getOrThrow(ItemKeys.CYAN_DYE), 12, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.expertBuyTradeExperience()));
        registerable.register(SELL_WHITE_BANNER, sell(items, items.getOrThrow(ItemKeys.WHITE_BANNER), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.expertSellTradeExperience(), 3));
        registerable.register(SELL_BLUE_BANNER, sell(items, items.getOrThrow(ItemKeys.BLUE_BANNER), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.expertSellTradeExperience(), 3));
        registerable.register(SELL_LIGHT_BLUE_BANNER, sell(items, items.getOrThrow(ItemKeys.LIGHT_BLUE_BANNER), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.expertSellTradeExperience(), 3));
        registerable.register(SELL_RED_BANNER, sell(items, items.getOrThrow(ItemKeys.RED_BANNER), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.expertSellTradeExperience(), 3));
        registerable.register(SELL_PINK_BANNER, sell(items, items.getOrThrow(ItemKeys.PINK_BANNER), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.expertSellTradeExperience(), 3));
        registerable.register(SELL_GREEN_BANNER, sell(items, items.getOrThrow(ItemKeys.GREEN_BANNER), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.expertSellTradeExperience(), 3));
        registerable.register(SELL_LIME_BANNER, sell(items, items.getOrThrow(ItemKeys.LIME_BANNER), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.expertSellTradeExperience(), 3));
        registerable.register(SELL_GRAY_BANNER, sell(items, items.getOrThrow(ItemKeys.GRAY_BANNER), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.expertSellTradeExperience(), 3));
        registerable.register(SELL_BLACK_BANNER, sell(items, items.getOrThrow(ItemKeys.BLACK_BANNER), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.expertSellTradeExperience(), 3));
        registerable.register(SELL_PURPLE_BANNER, sell(items, items.getOrThrow(ItemKeys.PURPLE_BANNER), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.expertSellTradeExperience(), 3));
        registerable.register(SELL_MAGENTA_BANNER, sell(items, items.getOrThrow(ItemKeys.MAGENTA_BANNER), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.expertSellTradeExperience(), 3));
        registerable.register(SELL_CYAN_BANNER, sell(items, items.getOrThrow(ItemKeys.CYAN_BANNER), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.expertSellTradeExperience(), 3));
        registerable.register(SELL_BROWN_BANNER, sell(items, items.getOrThrow(ItemKeys.BROWN_BANNER), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.expertSellTradeExperience(), 3));
        registerable.register(SELL_YELLOW_BANNER, sell(items, items.getOrThrow(ItemKeys.YELLOW_BANNER), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.expertSellTradeExperience(), 3));
        registerable.register(SELL_ORANGE_BANNER, sell(items, items.getOrThrow(ItemKeys.ORANGE_BANNER), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.expertSellTradeExperience(), 3));
        registerable.register(SELL_LIGHT_GRAY_BANNER, sell(items, items.getOrThrow(ItemKeys.LIGHT_GRAY_BANNER), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.expertSellTradeExperience(), 3));
        registerable.register(SELL_PAINTING, sell(items, items.getOrThrow(ItemKeys.PAINTING), 3, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.masterTradeExperience(), 2));
        registerable.register(BUY_STICK, buy(items, items.getOrThrow(ItemKeys.STICK), 32, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.noviceBuyTradeExperience()));
        registerable.register(SELL_ARROW, sell(items, items.getOrThrow(ItemKeys.ARROW), 16, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_FLINT_FROM_GRAVEL, sell(items, items.getOrThrow(ItemKeys.GRAVEL), 10, items.getOrThrow(ItemKeys.FLINT), 10, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.noviceSellTradeExperience(), TradeOffersAccessor.lowPriceMultiplier()));
        registerable.register(BUY_FLINT_APPRENTICE, buy(items, items.getOrThrow(ItemKeys.FLINT), 26, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.apprenticeBuyTradeExperience()));
        registerable.register(SELL_BOW, sell(items, items.getOrThrow(ItemKeys.BOW), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.apprenticeSellTradeExperience(), 2));
        registerable.register(BUY_STRING_JOURNEYMAN, buy(items, items.getOrThrow(ItemKeys.STRING), 14, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.journeymanBuyTradeExperience()));
        registerable.register(SELL_CROSSBOW, sell(items, items.getOrThrow(ItemKeys.CROSSBOW), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.journeymanSellTradeExperience(), 3));
        registerable.register(BUY_FEATHER, buy(items, items.getOrThrow(ItemKeys.FEATHER), 24, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.expertBuyTradeExperience()));
        registerable.register(SELL_ENCHANTED_BOW, sellEnchantedItem(items, items.getOrThrow(ItemKeys.BOW), TradeOffersAccessor.expertSellTradeExperience(), 2));
        registerable.register(BUY_TRIPWIRE_HOOK, buy(items, items.getOrThrow(ItemKeys.TRIPWIRE_HOOK), 8, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.masterTradeExperience()));
        registerable.register(SELL_ENCHANTED_CROSSBOW, sellEnchantedItem(items, items.getOrThrow(ItemKeys.CROSSBOW), 15, 3));
        registerable.register(SELL_TIPPED_ARROW, sellWithPotion(items, potions.getOrThrow(PotionTags.TRADEABLE), items.getOrThrow(ItemKeys.ARROW), items.getOrThrow(ItemKeys.TIPPED_ARROW), TradeOffersAccessor.masterTradeExperience()));
        registerable.register(BUY_PAPER, buy(items, items.getOrThrow(ItemKeys.PAPER), 24, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.noviceBuyTradeExperience()));
        registerable.register(SELL_ENCHANTED_BOOK_NOVICE, sellEnchantedBook(items, TradeOffersAccessor.noviceSellTradeExperience(), enchantments.getOrThrow(EnchantmentTags.TRADEABLE)));
        registerable.register(SELL_BOOKSHELF, sell(items, items.getOrThrow(ItemKeys.BOOKSHELF), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.noviceSellTradeExperience(), 9));
        registerable.register(BUY_BOOK, buy(items, items.getOrThrow(ItemKeys.BOOK), 4, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.apprenticeBuyTradeExperience()));
        registerable.register(SELL_ENCHANTED_BOOK_APPRENTICE, sellEnchantedBook(items, TradeOffersAccessor.apprenticeSellTradeExperience(), enchantments.getOrThrow(EnchantmentTags.TRADEABLE)));
        registerable.register(SELL_LANTERN, sell(items, items.getOrThrow(ItemKeys.LANTERN), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(BUY_INK_SAC, buy(items, items.getOrThrow(ItemKeys.INK_SAC), 5, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.journeymanBuyTradeExperience()));
        registerable.register(SELL_ENCHANTED_BOOK_JOURNEYMAN, sellEnchantedBook(items, TradeOffersAccessor.journeymanSellTradeExperience(), enchantments.getOrThrow(EnchantmentTags.TRADEABLE)));
        registerable.register(SELL_GLASS, sell(items, items.getOrThrow(ItemKeys.GLASS), 4, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.journeymanSellTradeExperience(), 1));
        registerable.register(BUY_WRITABLE_BOOK, buy(items, items.getOrThrow(ItemKeys.WRITABLE_BOOK), 2, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.expertBuyTradeExperience()));
        registerable.register(SELL_ENCHANTED_BOOK_EXPERT, sellEnchantedBook(items, TradeOffersAccessor.expertSellTradeExperience(), enchantments.getOrThrow(EnchantmentTags.TRADEABLE)));
        registerable.register(SELL_CLOCK, sell(items, items.getOrThrow(ItemKeys.CLOCK), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.expertSellTradeExperience(), 5));
        registerable.register(SELL_COMPASS, sell(items, items.getOrThrow(ItemKeys.COMPASS), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.expertSellTradeExperience(), 4));
        registerable.register(SELL_NAME_TAG, sell(items, items.getOrThrow(ItemKeys.NAME_TAG), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.masterTradeExperience(), 20));
        registerable.register(SELL_MAP, sell(items, items.getOrThrow(ItemKeys.MAP), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.noviceSellTradeExperience(), 7));
        registerable.register(BUY_GLASS_PANE, buy(items, items.getOrThrow(ItemKeys.GLASS_PANE), 11, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.apprenticeBuyTradeExperience()));
        registerable.register(SELL_MONUMENT_MAP, sellMap(items, 13, StructureTags.ON_OCEAN_EXPLORER_MAPS, "filled_map.monument", MapDecorationTypes.MONUMENT, TradeOffersAccessor.apprenticeSellTradeExperience()));
        registerable.register(BUY_COMPASS, buy(items, items.getOrThrow(ItemKeys.COMPASS), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.journeymanBuyTradeExperience()));
        registerable.register(SELL_MANSION_MAP, sellMap(items, 14, StructureTags.ON_WOODLAND_EXPLORER_MAPS, "filled_map.mansion", MapDecorationTypes.MANSION, TradeOffersAccessor.journeymanSellTradeExperience()));
        registerable.register(SELL_ITEM_FRAME, sell(items, items.getOrThrow(ItemKeys.ITEM_FRAME), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.expertSellTradeExperience(), 7));
        registerable.register(SELL_GLOBE_BANNER_PATTERN, sell(items, items.getOrThrow(ItemKeys.GLOBE_BANNER_PATTERN), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.masterTradeExperience(), 8));
        registerable.register(BUY_ROTTEN_FLESH, buy(items, items.getOrThrow(ItemKeys.ROTTEN_FLESH), 32, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.noviceBuyTradeExperience()));
        registerable.register(SELL_REDSTONE, sell(items, items.getOrThrow(ItemKeys.REDSTONE), 2, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.noviceSellTradeExperience(), 1));
        registerable.register(BUY_GOLD_INGOT, buy(items, items.getOrThrow(ItemKeys.GOLD_INGOT), 3, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.apprenticeBuyTradeExperience()));
        registerable.register(SELL_LAPIS_LAZULI, sell(items, items.getOrThrow(ItemKeys.LAPIS_LAZULI), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(BUY_RABBIT_FOOT, buy(items, items.getOrThrow(ItemKeys.RABBIT_FOOT), 2, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.journeymanBuyTradeExperience()));
        registerable.register(SELL_GLOWSTONE, sell(items, items.getOrThrow(ItemKeys.GLOWSTONE), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.journeymanSellTradeExperience(), 4));
        registerable.register(BUY_TURTLE_SCUTE, buy(items, items.getOrThrow(ItemKeys.TURTLE_SCUTE), 4, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.expertBuyTradeExperience()));
        registerable.register(BUY_GLASS_BOTTLE, buy(items, items.getOrThrow(ItemKeys.GLASS_BOTTLE), 9, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.expertBuyTradeExperience()));
        registerable.register(SELL_ENDER_PEARL, sell(items, items.getOrThrow(ItemKeys.ENDER_PEARL), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.expertSellTradeExperience(), 5));
        registerable.register(BUY_NETHER_WART, buy(items, items.getOrThrow(ItemKeys.NETHER_WART), 22, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.masterTradeExperience()));
        registerable.register(SELL_EXPERIENCE_BOTTLE, sell(items, items.getOrThrow(ItemKeys.EXPERIENCE_BOTTLE), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.masterTradeExperience(), 3));
        registerable.register(BUY_COAL_NOVICE_MORE_ITEMS, buy(items, items.getOrThrow(ItemKeys.COAL), 15, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.noviceBuyTradeExperience()));
        registerable.register(SELL_IRON_LEGGINGS, sell(items, items.getOrThrow(ItemKeys.IRON_LEGGINGS), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.noviceSellTradeExperience(), 7));
        registerable.register(SELL_IRON_BOOTS, sell(items, items.getOrThrow(ItemKeys.IRON_BOOTS), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.noviceSellTradeExperience(), 4));
        registerable.register(SELL_IRON_HELMET, sell(items, items.getOrThrow(ItemKeys.IRON_HELMET), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.noviceSellTradeExperience(), 5));
        registerable.register(SELL_IRON_CHESTPLATE, sell(items, items.getOrThrow(ItemKeys.IRON_CHESTPLATE), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.noviceSellTradeExperience(), 9));
        registerable.register(BUY_IRON_INGOT, buy(items, items.getOrThrow(ItemKeys.IRON_INGOT), 4, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.apprenticeBuyTradeExperience()));
        registerable.register(SELL_BELL, sell(items, items.getOrThrow(ItemKeys.BELL), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.apprenticeSellTradeExperience(), 36));
        registerable.register(SELL_CHAINMAIL_BOOTS, sell(items, items.getOrThrow(ItemKeys.CHAINMAIL_BOOTS), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_CHAINMAIL_LEGGINGS, sell(items, items.getOrThrow(ItemKeys.CHAINMAIL_LEGGINGS), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.apprenticeSellTradeExperience(), 3));
        registerable.register(BUY_LAVA_BUCKET, buy(items, items.getOrThrow(ItemKeys.LAVA_BUCKET), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.journeymanBuyTradeExperience()));
        registerable.register(BUY_DIAMOND_JOURNEYMAN, buy(items, items.getOrThrow(ItemKeys.DIAMOND), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.journeymanBuyTradeExperience()));
        registerable.register(SELL_CHAINMAIL_HELMET, sell(items, items.getOrThrow(ItemKeys.CHAINMAIL_HELMET), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.journeymanSellTradeExperience(), 1));
        registerable.register(SELL_CHAINMAIL_CHESTPLATE, sell(items, items.getOrThrow(ItemKeys.CHAINMAIL_CHESTPLATE), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.journeymanSellTradeExperience(), 4));
        registerable.register(SELL_SHIELD, sell(items, items.getOrThrow(ItemKeys.SHIELD), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.journeymanSellTradeExperience(), 5));
        registerable.register(SELL_ENCHANTED_DIAMOND_LEGGINGS, sellEnchantedItem(items, items.getOrThrow(ItemKeys.DIAMOND_LEGGINGS), TradeOffersAccessor.expertSellTradeExperience(), 14, TradeOffersAccessor.highPriceMultiplier()));
        registerable.register(SELL_ENCHANTED_DIAMOND_BOOTS, sellEnchantedItem(items, items.getOrThrow(ItemKeys.DIAMOND_BOOTS), TradeOffersAccessor.expertSellTradeExperience(), 8, TradeOffersAccessor.highPriceMultiplier()));
        registerable.register(SELL_ENCHANTED_DIAMOND_HELMET, sellEnchantedItem(items, items.getOrThrow(ItemKeys.DIAMOND_HELMET), TradeOffersAccessor.masterTradeExperience(), 8, TradeOffersAccessor.highPriceMultiplier()));
        registerable.register(SELL_ENCHANTED_DIAMOND_CHESTPLATE, sellEnchantedItem(items, items.getOrThrow(ItemKeys.DIAMOND_CHESTPLATE), TradeOffersAccessor.masterTradeExperience(), 16, TradeOffersAccessor.highPriceMultiplier()));
        registerable.register(SELL_IRON_AXE, sell(items, items.getOrThrow(ItemKeys.IRON_AXE), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.noviceSellTradeExperience(), 3));
        registerable.register(SELL_ENCHANTED_IRON_SWORD, sellEnchantedItem(items, items.getOrThrow(ItemKeys.IRON_SWORD), TradeOffersAccessor.noviceSellTradeExperience(), 2));
        registerable.register(BUY_FLINT_WEAPONSMITH_JOURNEYMAN, buy(items, items.getOrThrow(ItemKeys.FLINT), 24, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.journeymanBuyTradeExperience()));
        registerable.register(BUY_DIAMOND_EXPERT, buy(items, items.getOrThrow(ItemKeys.DIAMOND), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.expertBuyTradeExperience()));
        registerable.register(SELL_ENCHANTED_DIAMOND_AXE, sellEnchantedItem(items, items.getOrThrow(ItemKeys.DIAMOND_AXE), TradeOffersAccessor.expertSellTradeExperience(), 12, TradeOffersAccessor.highPriceMultiplier()));
        registerable.register(SELL_ENCHANTED_DIAMOND_SWORD, sellEnchantedItem(items, items.getOrThrow(ItemKeys.DIAMOND_SWORD), TradeOffersAccessor.masterTradeExperience(), 8, TradeOffersAccessor.highPriceMultiplier()));
        registerable.register(SELL_STONE_AXE, sell(items, items.getOrThrow(ItemKeys.STONE_AXE), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_STONE_SHOVEL, sell(items, items.getOrThrow(ItemKeys.STONE_SHOVEL), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_STONE_PICKAXE, sell(items, items.getOrThrow(ItemKeys.STONE_PICKAXE), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_STONE_HOE, sell(items, items.getOrThrow(ItemKeys.STONE_HOE), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.noviceSellTradeExperience(), 1));
        registerable.register(BUY_FLINT_TOOLSMITH_JOURNEYMAN, buy(items, items.getOrThrow(ItemKeys.FLINT), 30, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.journeymanBuyTradeExperience()));
        registerable.register(SELL_ENCHANTED_IRON_AXE, sellEnchantedItem(items, items.getOrThrow(ItemKeys.IRON_AXE), TradeOffersAccessor.journeymanSellTradeExperience(), 1, TradeOffersAccessor.highPriceMultiplier()));
        registerable.register(SELL_ENCHANTED_IRON_SHOVEL, sellEnchantedItem(items, items.getOrThrow(ItemKeys.IRON_SHOVEL), TradeOffersAccessor.journeymanSellTradeExperience(), 2, TradeOffersAccessor.highPriceMultiplier()));
        registerable.register(SELL_ENCHANTED_IRON_PICKAXE, sellEnchantedItem(items, items.getOrThrow(ItemKeys.IRON_PICKAXE), TradeOffersAccessor.journeymanSellTradeExperience(), 3, TradeOffersAccessor.highPriceMultiplier()));
        registerable.register(SELL_DIAMOND_HOE, sell(items, items.getOrThrow(ItemKeys.DIAMOND_HOE), 1, TradeOffersAccessor.rareMaxUses(), TradeOffersAccessor.journeymanSellTradeExperience(), 4));
        registerable.register(SELL_ENCHANTED_DIAMOND_SHOVEL, sellEnchantedItem(items, items.getOrThrow(ItemKeys.DIAMOND_SHOVEL), TradeOffersAccessor.expertSellTradeExperience(), 5, TradeOffersAccessor.highPriceMultiplier()));
        registerable.register(SELL_ENCHANTED_DIAMOND_PICKAXE, sellEnchantedItem(items, items.getOrThrow(ItemKeys.DIAMOND_PICKAXE), TradeOffersAccessor.masterTradeExperience(), 13, TradeOffersAccessor.highPriceMultiplier()));
        registerable.register(BUY_CHICKEN, buy(items, items.getOrThrow(ItemKeys.CHICKEN), 14, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.noviceBuyTradeExperience()));
        registerable.register(BUY_PORKCHOP, buy(items, items.getOrThrow(ItemKeys.PORKCHOP), 7, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.noviceBuyTradeExperience()));
        registerable.register(BUY_RABBIT, buy(items, items.getOrThrow(ItemKeys.RABBIT), 4, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.noviceBuyTradeExperience()));
        registerable.register(SELL_RABBIT_STEW, sell(items, items.getOrThrow(ItemKeys.RABBIT_STEW), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_COOKED_PORKCHOP, sell(items, items.getOrThrow(ItemKeys.COOKED_PORKCHOP), 5, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(SELL_COOKED_CHICKEN, sell(items, items.getOrThrow(ItemKeys.COOKED_CHICKEN), 8, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(BUY_MUTTON, buy(items, items.getOrThrow(ItemKeys.MUTTON), 7, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.journeymanBuyTradeExperience()));
        registerable.register(BUY_BEEF, buy(items, items.getOrThrow(ItemKeys.BEEF), 10, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.journeymanBuyTradeExperience()));
        registerable.register(BUY_DRIED_KELP_BLOCK, buy(items, items.getOrThrow(ItemKeys.DRIED_KELP_BLOCK), 10, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.expertBuyTradeExperience()));
        registerable.register(BUY_SWEET_BERRIES, buy(items, items.getOrThrow(ItemKeys.SWEET_BERRIES), 10, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.masterTradeExperience()));
        registerable.register(BUY_LEATHER, buy(items, items.getOrThrow(ItemKeys.LEATHER), 6, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.noviceBuyTradeExperience()));
        registerable.register(SELL_LEATHER_LEGGINGS, sellDyedItem(items, items.getOrThrow(ItemKeys.LEATHER_LEGGINGS), 3));
        registerable.register(SELL_LEATHER_CHESTPLATE, sellDyedItem(items, items.getOrThrow(ItemKeys.LEATHER_CHESTPLATE), 7));
        registerable.register(SELL_LEATHER_HELMET_APPRENTICE, sellDyedItem(items, items.getOrThrow(ItemKeys.LEATHER_HELMET), 5, TradeOffersAccessor.apprenticeSellTradeExperience()));
        registerable.register(SELL_LEATHER_BOOTS, sellDyedItem(items, items.getOrThrow(ItemKeys.LEATHER_BOOTS), 4, TradeOffersAccessor.apprenticeSellTradeExperience()));
        registerable.register(BUY_RABBIT_HIDE, buy(items, items.getOrThrow(ItemKeys.RABBIT_HIDE), 9, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.journeymanBuyTradeExperience()));
        registerable.register(SELL_LEATHER_HORSE_ARMOR, sellDyedItem(items, items.getOrThrow(ItemKeys.LEATHER_HORSE_ARMOR), 6, TradeOffersAccessor.expertSellTradeExperience()));
        registerable.register(SELL_SADDLE, sell(items, items.getOrThrow(ItemKeys.SADDLE), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.masterTradeExperience(), 6));
        registerable.register(SELL_LEATHER_HELMET_MASTER, sellDyedItem(items, items.getOrThrow(ItemKeys.LEATHER_HELMET), 5, TradeOffersAccessor.masterTradeExperience()));
        registerable.register(BUY_CLAY_BALL, buy(items, items.getOrThrow(ItemKeys.CLAY_BALL), 10, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.noviceBuyTradeExperience()));
        registerable.register(SELL_BRICK, sell(items, items.getOrThrow(ItemKeys.BRICK), 10, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.noviceSellTradeExperience(), 1));
        registerable.register(BUY_STONE, buy(items, items.getOrThrow(ItemKeys.STONE), 20, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.apprenticeBuyTradeExperience()));
        registerable.register(SELL_CHISELED_STONE_BRICKS, sell(items, items.getOrThrow(ItemKeys.CHISELED_STONE_BRICKS), 4, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.apprenticeSellTradeExperience(), 1));
        registerable.register(BUY_GRANITE, buy(items, items.getOrThrow(ItemKeys.GRANITE), 16, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.journeymanBuyTradeExperience()));
        registerable.register(BUY_ANDESITE, buy(items, items.getOrThrow(ItemKeys.ANDESITE), 16, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.journeymanBuyTradeExperience()));
        registerable.register(BUY_DIORITE, buy(items, items.getOrThrow(ItemKeys.DIORITE), 16, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.journeymanBuyTradeExperience()));
        registerable.register(SELL_DRIPSTONE_BLOCK, sell(items, items.getOrThrow(ItemKeys.DRIPSTONE_BLOCK), 4, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.journeymanSellTradeExperience(), 1));
        registerable.register(SELL_POLISHED_ANDESITE, sell(items, items.getOrThrow(ItemKeys.POLISHED_ANDESITE), 4, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.journeymanSellTradeExperience(), 1));
        registerable.register(SELL_POLISHED_DIORITE, sell(items, items.getOrThrow(ItemKeys.POLISHED_DIORITE), 4, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.journeymanSellTradeExperience(), 1));
        registerable.register(SELL_POLISHED_GRANITE, sell(items, items.getOrThrow(ItemKeys.POLISHED_GRANITE), 4, TradeOffersAccessor.commonMaxUses(), TradeOffersAccessor.journeymanSellTradeExperience(), 1));
        registerable.register(BUY_QUARTZ, buy(items, items.getOrThrow(ItemKeys.QUARTZ), 12, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.expertBuyTradeExperience()));
        registerable.register(SELL_ORANGE_TERRACOTTA, sell(items, items.getOrThrow(ItemKeys.ORANGE_TERRACOTTA), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.expertSellTradeExperience(), 1));
        registerable.register(SELL_WHITE_TERRACOTTA, sell(items, items.getOrThrow(ItemKeys.WHITE_TERRACOTTA), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.expertSellTradeExperience(), 1));
        registerable.register(SELL_BLUE_TERRACOTTA, sell(items, items.getOrThrow(ItemKeys.BLUE_TERRACOTTA), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.expertSellTradeExperience(), 1));
        registerable.register(SELL_LIGHT_BLUE_TERRACOTTA, sell(items, items.getOrThrow(ItemKeys.LIGHT_BLUE_TERRACOTTA), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.expertSellTradeExperience(), 1));
        registerable.register(SELL_GRAY_TERRACOTTA, sell(items, items.getOrThrow(ItemKeys.GRAY_TERRACOTTA), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.expertSellTradeExperience(), 1));
        registerable.register(SELL_LIGHT_GRAY_TERRACOTTA, sell(items, items.getOrThrow(ItemKeys.LIGHT_GRAY_TERRACOTTA), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.expertSellTradeExperience(), 1));
        registerable.register(SELL_BLACK_TERRACOTTA, sell(items, items.getOrThrow(ItemKeys.BLACK_TERRACOTTA), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.expertSellTradeExperience(), 1));
        registerable.register(SELL_RED_TERRACOTTA, sell(items, items.getOrThrow(ItemKeys.RED_TERRACOTTA), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.expertSellTradeExperience(), 1));
        registerable.register(SELL_PINK_TERRACOTTA, sell(items, items.getOrThrow(ItemKeys.PINK_TERRACOTTA), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.expertSellTradeExperience(), 1));
        registerable.register(SELL_MAGENTA_TERRACOTTA, sell(items, items.getOrThrow(ItemKeys.MAGENTA_TERRACOTTA), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.expertSellTradeExperience(), 1));
        registerable.register(SELL_LIME_TERRACOTTA, sell(items, items.getOrThrow(ItemKeys.LIME_TERRACOTTA), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.expertSellTradeExperience(), 1));
        registerable.register(SELL_GREEN_TERRACOTTA, sell(items, items.getOrThrow(ItemKeys.GREEN_TERRACOTTA), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.expertSellTradeExperience(), 1));
        registerable.register(SELL_CYAN_TERRACOTTA, sell(items, items.getOrThrow(ItemKeys.CYAN_TERRACOTTA), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.expertSellTradeExperience(), 1));
        registerable.register(SELL_PURPLE_TERRACOTTA, sell(items, items.getOrThrow(ItemKeys.PURPLE_TERRACOTTA), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.expertSellTradeExperience(), 1));
        registerable.register(SELL_YELLOW_TERRACOTTA, sell(items, items.getOrThrow(ItemKeys.YELLOW_TERRACOTTA), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.expertSellTradeExperience(), 1));
        registerable.register(SELL_BROWN_TERRACOTTA, sell(items, items.getOrThrow(ItemKeys.BROWN_TERRACOTTA), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.expertSellTradeExperience(), 1));
        registerable.register(SELL_ORANGE_GLAZED_TERRACOTTA, sell(items, items.getOrThrow(ItemKeys.ORANGE_GLAZED_TERRACOTTA), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.expertSellTradeExperience(), 1));
        registerable.register(SELL_WHITE_GLAZED_TERRACOTTA, sell(items, items.getOrThrow(ItemKeys.WHITE_GLAZED_TERRACOTTA), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.expertSellTradeExperience(), 1));
        registerable.register(SELL_BLUE_GLAZED_TERRACOTTA, sell(items, items.getOrThrow(ItemKeys.BLUE_GLAZED_TERRACOTTA), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.expertSellTradeExperience(), 1));
        registerable.register(SELL_LIGHT_BLUE_GLAZED_TERRACOTTA, sell(items, items.getOrThrow(ItemKeys.LIGHT_BLUE_GLAZED_TERRACOTTA), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.expertSellTradeExperience(), 1));
        registerable.register(SELL_GRAY_GLAZED_TERRACOTTA, sell(items, items.getOrThrow(ItemKeys.GRAY_GLAZED_TERRACOTTA), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.expertSellTradeExperience(), 1));
        registerable.register(SELL_LIGHT_GRAY_GLAZED_TERRACOTTA, sell(items, items.getOrThrow(ItemKeys.LIGHT_GRAY_GLAZED_TERRACOTTA), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.expertSellTradeExperience(), 1));
        registerable.register(SELL_BLACK_GLAZED_TERRACOTTA, sell(items, items.getOrThrow(ItemKeys.BLACK_GLAZED_TERRACOTTA), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.expertSellTradeExperience(), 1));
        registerable.register(SELL_RED_GLAZED_TERRACOTTA, sell(items, items.getOrThrow(ItemKeys.RED_GLAZED_TERRACOTTA), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.expertSellTradeExperience(), 1));
        registerable.register(SELL_PINK_GLAZED_TERRACOTTA, sell(items, items.getOrThrow(ItemKeys.PINK_GLAZED_TERRACOTTA), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.expertSellTradeExperience(), 1));
        registerable.register(SELL_MAGENTA_GLAZED_TERRACOTTA, sell(items, items.getOrThrow(ItemKeys.MAGENTA_GLAZED_TERRACOTTA), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.expertSellTradeExperience(), 1));
        registerable.register(SELL_LIME_GLAZED_TERRACOTTA, sell(items, items.getOrThrow(ItemKeys.LIME_GLAZED_TERRACOTTA), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.expertSellTradeExperience(), 1));
        registerable.register(SELL_GREEN_GLAZED_TERRACOTTA, sell(items, items.getOrThrow(ItemKeys.GREEN_GLAZED_TERRACOTTA), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.expertSellTradeExperience(), 1));
        registerable.register(SELL_CYAN_GLAZED_TERRACOTTA, sell(items, items.getOrThrow(ItemKeys.CYAN_GLAZED_TERRACOTTA), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.expertSellTradeExperience(), 1));
        registerable.register(SELL_PURPLE_GLAZED_TERRACOTTA, sell(items, items.getOrThrow(ItemKeys.PURPLE_GLAZED_TERRACOTTA), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.expertSellTradeExperience(), 1));
        registerable.register(SELL_YELLOW_GLAZED_TERRACOTTA, sell(items, items.getOrThrow(ItemKeys.YELLOW_GLAZED_TERRACOTTA), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.expertSellTradeExperience(), 1));
        registerable.register(SELL_BROWN_GLAZED_TERRACOTTA, sell(items, items.getOrThrow(ItemKeys.BROWN_GLAZED_TERRACOTTA), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.expertSellTradeExperience(), 1));
        registerable.register(SELL_QUARTZ_PILLAR, sell(items, items.getOrThrow(ItemKeys.QUARTZ_PILLAR), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.masterTradeExperience(), 1));
        registerable.register(SELL_QUARTZ_BLOCK, sell(items, items.getOrThrow(ItemKeys.QUARTZ_BLOCK), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.masterTradeExperience(), 1));
        registerable.register(SELL_SEA_PICKLE, sell(items, items.getOrThrow(ItemKeys.SEA_PICKLE), 1, 5, TradeOffersAccessor.noviceSellTradeExperience(), 2));
        registerable.register(SELL_SLIME_BALL, sell(items, items.getOrThrow(ItemKeys.SLIME_BALL), 1, 5, TradeOffersAccessor.noviceSellTradeExperience(), 4));
        registerable.register(SELL_GLOWSTONE_WANDERING_TRADER, sell(items, items.getOrThrow(ItemKeys.GLOWSTONE), 1, 5, TradeOffersAccessor.noviceSellTradeExperience(), 2));
        registerable.register(SELL_NAUTILUS_SHELL, sell(items, items.getOrThrow(ItemKeys.NAUTILUS_SHELL), 1, 5, TradeOffersAccessor.noviceSellTradeExperience(), 5));
        registerable.register(SELL_FERN, sell(items, items.getOrThrow(ItemKeys.FERN), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_SUGAR_CANE, sell(items, items.getOrThrow(ItemKeys.SUGAR_CANE), 1, 8, TradeOffersAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_PUMPKIN, sell(items, items.getOrThrow(ItemKeys.PUMPKIN), 1, 4, TradeOffersAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_KELP, sell(items, items.getOrThrow(ItemKeys.KELP), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.noviceSellTradeExperience(), 3));
        registerable.register(SELL_CACTUS, sell(items, items.getOrThrow(ItemKeys.CACTUS), 1, 8, TradeOffersAccessor.noviceSellTradeExperience(), 3));
        registerable.register(SELL_DANDELION, sell(items, items.getOrThrow(ItemKeys.DANDELION), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_POPPY, sell(items, items.getOrThrow(ItemKeys.POPPY), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_BLUE_ORCHID, sell(items, items.getOrThrow(ItemKeys.BLUE_ORCHID), 1, 8, TradeOffersAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_ALLIUM, sell(items, items.getOrThrow(ItemKeys.ALLIUM), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_AZURE_BLUET, sell(items, items.getOrThrow(ItemKeys.AZURE_BLUET), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_RED_TULIP, sell(items, items.getOrThrow(ItemKeys.RED_TULIP), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_ORANGE_TULIP, sell(items, items.getOrThrow(ItemKeys.ORANGE_TULIP), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_WHITE_TULIP, sell(items, items.getOrThrow(ItemKeys.WHITE_TULIP), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_PINK_TULIP, sell(items, items.getOrThrow(ItemKeys.PINK_TULIP), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_OXEYE_DAISY, sell(items, items.getOrThrow(ItemKeys.OXEYE_DAISY), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_CORNFLOWER, sell(items, items.getOrThrow(ItemKeys.CORNFLOWER), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_LILY_OF_THE_VALLEY, sell(items, items.getOrThrow(ItemKeys.LILY_OF_THE_VALLEY), 1, 7, TradeOffersAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_WHEAT_SEEDS, sell(items, items.getOrThrow(ItemKeys.WHEAT_SEEDS), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_BEETROOT_SEEDS, sell(items, items.getOrThrow(ItemKeys.BEETROOT_SEEDS), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_PUMPKIN_SEEDS, sell(items, items.getOrThrow(ItemKeys.PUMPKIN_SEEDS), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_MELON_SEEDS, sell(items, items.getOrThrow(ItemKeys.MELON_SEEDS), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_ACACIA_SAPLING, sell(items, items.getOrThrow(ItemKeys.ACACIA_SAPLING), 1, 8, TradeOffersAccessor.noviceSellTradeExperience(), 5));
        registerable.register(SELL_BIRCH_SAPLING, sell(items, items.getOrThrow(ItemKeys.BIRCH_SAPLING), 1, 8, TradeOffersAccessor.noviceSellTradeExperience(), 5));
        registerable.register(SELL_DARK_OAK_SAPLING, sell(items, items.getOrThrow(ItemKeys.DARK_OAK_SAPLING), 1, 8, TradeOffersAccessor.noviceSellTradeExperience(), 5));
        registerable.register(SELL_JUNGLE_SAPLING, sell(items, items.getOrThrow(ItemKeys.JUNGLE_SAPLING), 1, 8, TradeOffersAccessor.noviceSellTradeExperience(), 5));
        registerable.register(SELL_OAK_SAPLING, sell(items, items.getOrThrow(ItemKeys.OAK_SAPLING), 1, 8, TradeOffersAccessor.noviceSellTradeExperience(), 5));
        registerable.register(SELL_SPRUCE_SAPLING, sell(items, items.getOrThrow(ItemKeys.SPRUCE_SAPLING), 1, 8, TradeOffersAccessor.noviceSellTradeExperience(), 5));
        registerable.register(SELL_CHERRY_SAPLING, sell(items, items.getOrThrow(ItemKeys.CHERRY_SAPLING), 1, 8, TradeOffersAccessor.noviceSellTradeExperience(), 5));
        registerable.register(SELL_MANGROVE_PROPAGULE, sell(items, items.getOrThrow(ItemKeys.MANGROVE_PROPAGULE), 1, 8, TradeOffersAccessor.noviceSellTradeExperience(), 5));
        registerable.register(SELL_RED_DYE, sell(items, items.getOrThrow(ItemKeys.RED_DYE), 3, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_WHITE_DYE, sell(items, items.getOrThrow(ItemKeys.WHITE_DYE), 3, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_BLUE_DYE, sell(items, items.getOrThrow(ItemKeys.BLUE_DYE), 3, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_PINK_DYE, sell(items, items.getOrThrow(ItemKeys.PINK_DYE), 3, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_BLACK_DYE, sell(items, items.getOrThrow(ItemKeys.BLACK_DYE), 3, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_GREEN_DYE, sell(items, items.getOrThrow(ItemKeys.GREEN_DYE), 3, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_LIGHT_GRAY_DYE, sell(items, items.getOrThrow(ItemKeys.LIGHT_GRAY_DYE), 3, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_MAGENTA_DYE, sell(items, items.getOrThrow(ItemKeys.MAGENTA_DYE), 3, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_YELLOW_DYE, sell(items, items.getOrThrow(ItemKeys.YELLOW_DYE), 3, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_GRAY_DYE, sell(items, items.getOrThrow(ItemKeys.GRAY_DYE), 3, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_PURPLE_DYE, sell(items, items.getOrThrow(ItemKeys.PURPLE_DYE), 3, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_LIGHT_BLUE_DYE, sell(items, items.getOrThrow(ItemKeys.LIGHT_BLUE_DYE), 3, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_LIME_DYE, sell(items, items.getOrThrow(ItemKeys.LIME_DYE), 3, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_ORANGE_DYE, sell(items, items.getOrThrow(ItemKeys.ORANGE_DYE), 3, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_BROWN_DYE, sell(items, items.getOrThrow(ItemKeys.BROWN_DYE), 3, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_CYAN_DYE, sell(items, items.getOrThrow(ItemKeys.CYAN_DYE), 3, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_BRAIN_CORAL_BLOCK, sell(items, items.getOrThrow(ItemKeys.BRAIN_CORAL_BLOCK), 1, 8, TradeOffersAccessor.noviceSellTradeExperience(), 3));
        registerable.register(SELL_BUBBLE_CORAL_BLOCK, sell(items, items.getOrThrow(ItemKeys.BUBBLE_CORAL_BLOCK), 1, 8, TradeOffersAccessor.noviceSellTradeExperience(), 3));
        registerable.register(SELL_FIRE_CORAL_BLOCK, sell(items, items.getOrThrow(ItemKeys.FIRE_CORAL_BLOCK), 1, 8, TradeOffersAccessor.noviceSellTradeExperience(), 3));
        registerable.register(SELL_HORN_CORAL_BLOCK, sell(items, items.getOrThrow(ItemKeys.HORN_CORAL_BLOCK), 1, 8, TradeOffersAccessor.noviceSellTradeExperience(), 3));
        registerable.register(SELL_TUBE_CORAL_BLOCK, sell(items, items.getOrThrow(ItemKeys.TUBE_CORAL_BLOCK), 1, 8, TradeOffersAccessor.noviceSellTradeExperience(), 3));
        registerable.register(SELL_VINE, sell(items, items.getOrThrow(ItemKeys.VINE), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_BROWN_MUSHROOM, sell(items, items.getOrThrow(ItemKeys.BROWN_MUSHROOM), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_RED_MUSHROOM, sell(items, items.getOrThrow(ItemKeys.RED_MUSHROOM), 1, TradeOffersAccessor.defaultMaxUses(), TradeOffersAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_LILY_PAD, sell(items, items.getOrThrow(ItemKeys.LILY_PAD), 2, 5, TradeOffersAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_SMALL_DRIPLEAF, sell(items, items.getOrThrow(ItemKeys.SMALL_DRIPLEAF), 2, 5, TradeOffersAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_SAND, sell(items, items.getOrThrow(ItemKeys.SAND), 8, 8, TradeOffersAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_RED_SAND, sell(items, items.getOrThrow(ItemKeys.RED_SAND), 4, 6, TradeOffersAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_POINTED_DRIPSTONE, sell(items, items.getOrThrow(ItemKeys.POINTED_DRIPSTONE), 2, 5, TradeOffersAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_ROOTED_DIRT, sell(items, items.getOrThrow(ItemKeys.ROOTED_DIRT), 2, 5, TradeOffersAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_MOSS_BLOCK, sell(items, items.getOrThrow(ItemKeys.MOSS_BLOCK), 2, 5, TradeOffersAccessor.noviceSellTradeExperience(), 1));
        registerable.register(SELL_TROPICAL_FISH_BUCKET, sell(items, items.getOrThrow(ItemKeys.TROPICAL_FISH_BUCKET), 1, 4, 1, 5));
        registerable.register(SELL_PUFFERFISH_BUCKET, sell(items, items.getOrThrow(ItemKeys.PUFFERFISH_BUCKET), 1, 4, 1, 5));
        registerable.register(SELL_PACKED_ICE, sell(items, items.getOrThrow(ItemKeys.PACKED_ICE), 1, 6, 1, 3));
        registerable.register(SELL_BLUE_ICE, sell(items, items.getOrThrow(ItemKeys.BLUE_ICE), 1, 6, 1, 6));
        registerable.register(SELL_GUNPOWDER, sell(items, items.getOrThrow(ItemKeys.GUNPOWDER), 1, 8, 1, 1));
        registerable.register(SELL_PODZOL, sell(items, items.getOrThrow(ItemKeys.PODZOL), 3, 6, 1, 3));
    }

    private static Trade buy(RegistryEntryLookup<Item> items, RegistryEntry<Item> item, int count, int maxUses, int tradeExperience) {
        return Trade.builder(Trade.Entry.ofEmerald(items))
            .wants(Trade.Entry.of(item, count))
            .maxUses(maxUses)
            .tradeExperience(tradeExperience)
            .build();
    }

    private static Trade buyFromType(RegistryEntryLookup<Item> items, RegistryEntry<Item> item, Map<RegistryEntry<VillagerType>, RegistryEntry<Item>> types) {
        return Trade.builder(Trade.Entry.ofEmerald(items))
            .wants(Trade.Entry.of(item))
            .tradeExperience(TradeOffersAccessor.masterTradeExperience())
            .tradeModifier(ItemFromTypeTradeModifier.of(types))
            .build();
    }

    private static Trade sell(RegistryEntryLookup<Item> items, RegistryEntry<Item> item, int count, int maxUses, int tradeExperience, int price) {
        return Trade.builder(Trade.Entry.of(item, count))
            .wants(Trade.Entry.ofEmerald(items, price))
            .maxUses(maxUses)
            .tradeExperience(tradeExperience)
            .build();
    }

    private static Trade sell(RegistryEntryLookup<Item> items, RegistryEntry<Item> item, int count, RegistryEntry<Item> processedItem, int processedCount, int maxUses, int tradeExperience, float priceMultiplier) {
        return Trade.builder(Trade.Entry.of(processedItem, processedCount))
            .wants(Trade.Entry.ofEmerald(items))
            .wants(Trade.Entry.of(item, count))
            .maxUses(maxUses)
            .tradeExperience(tradeExperience)
            .priceMultiplier(priceMultiplier)
            .build();
    }

    private static Trade sellSuspiciousStew(RegistryEntryLookup<Item> items, RegistryEntry<StatusEffect> statusEffect, int duration) {
        return Trade.builder(
            Trade.Entry.of(items.getOrThrow(ItemKeys.SUSPICIOUS_STEW), 1, SetStewEffectLootFunction.builder()
                .withEffect(statusEffect, ConstantLootNumberProvider.create(duration))
                .build()))
            .wants(Trade.Entry.ofEmerald(items))
            .tradeExperience(15)
            .build();
    }

    private static Trade sellEnchantedItem(RegistryEntryLookup<Item> items, RegistryEntry<Item> item, int tradeExperience, int basePrice) {
        return sellEnchantedItem(items, item, tradeExperience, basePrice, 0.05f);
    }

    private static Trade sellEnchantedItem(RegistryEntryLookup<Item> items, RegistryEntry<Item> item, int tradeExperience, int basePrice, float priceMultiplier) {
        return Trade.builder(Trade.Entry.of(item))
            .wants(Trade.Entry.ofEmerald(items, basePrice))
            .maxUses(3)
            .tradeExperience(tradeExperience)
            .tradeModifier(EnchantWithLevelsTradeModifier.of(0, 5, 19))
            .priceMultiplier(priceMultiplier)
            .build();
    }

    private static Trade sellWithPotion(RegistryEntryLookup<Item> items, RegistryEntryList<Potion> potions, RegistryEntry<Item> item, RegistryEntry<Item> resultItem, int tradeExperience) {
        return Trade.builder(Trade.Entry.of(resultItem, 5, SetRandomPotionItemModifier.of(potions)))
            .wants(Trade.Entry.ofEmerald(items, 2))
            .wants(Trade.Entry.of(item, 5))
            .tradeExperience(tradeExperience)
            .build();
    }

    private static Trade sellDyedItem(RegistryEntryLookup<Item> items, RegistryEntry<Item> item, int price) {
        return sellDyedItem(items, item, price, 1);
    }

    private static Trade sellDyedItem(RegistryEntryLookup<Item> items, RegistryEntry<Item> item, int price, int tradeExperience) {
        return Trade.builder(Trade.Entry.of(item, 1, DyeItemModifier.of(1.0f, 0.3f, 0.2f)))
            .wants(Trade.Entry.ofEmerald(items, price))
            .tradeExperience(tradeExperience)
            .build();
    }

    private static Trade sellMap(RegistryEntryLookup<Item> items, int price, TagKey<Structure> structure, String name, RegistryEntry<MapDecorationType> mapDecorationType, int tradeExperience) {
        AndLootFunction itemModifier = AndLootFunction.create(List.of(
            ExplorationMapLootFunction.builder()
                .withDestination(structure)
                .withDecoration(mapDecorationType)
                .searchRadius(100)
                .build(),
            SetNameLootFunction.builder(Text.translatable(name), SetNameLootFunction.Target.ITEM_NAME)
                .build()
        ));
        return Trade.builder(Trade.Entry.of(items.getOrThrow(ItemKeys.MAP), 1, itemModifier))
            .wants(Trade.Entry.ofEmerald(items, price))
            .tradeExperience(tradeExperience)
            .build();
    }

    private static Trade sellEnchantedBook(RegistryEntryLookup<Item> items, int tradeExperience, RegistryEntryList<Enchantment> enchantments) {
        return Trade.builder(Trade.Entry.of(items.getOrThrow(ItemKeys.BOOK)))
            .wants(Trade.Entry.ofEmerald(items, 2))
            .wants(Trade.Entry.of(items.getOrThrow(ItemKeys.BOOK)))
            .tradeExperience(tradeExperience)
            .tradeModifier(SingleEnchantmentTradeModifier.of(0, 5, 10, 3, enchantments))
            .priceMultiplier(TradeOffersAccessor.highPriceMultiplier())
            .build();
    }

    private static RegistryKey<Trade> of(String id) {
        return RegistryKey.of(ItematicRegistryKeys.TRADE, Identifier.ofVanilla(id));
    }
}
