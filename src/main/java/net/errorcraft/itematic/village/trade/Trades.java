package net.errorcraft.itematic.village.trade;

import net.errorcraft.itematic.entity.effect.StatusEffectKeys;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;
import net.minecraft.loot.function.EnchantWithLevelsLootFunction;
import net.minecraft.loot.function.SetStewEffectLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class Trades {
    public static final RegistryKey<Trade> BUY_WHEAT = of("buy_wheat");
    public static final RegistryKey<Trade> BUY_POTATO = of("buy_potato");
    public static final RegistryKey<Trade> BUY_CARROT = of("buy_carrot");
    public static final RegistryKey<Trade> BUY_BEETROOT = of("buy_beetroot");
    public static final RegistryKey<Trade> SELL_BREAD = of("sell_bread");
    public static final RegistryKey<Trade> SELL_PUMPKIN_PIE = of("sell_pumpkin_pie");
    public static final RegistryKey<Trade> SELL_APPLE = of("sell_apple");
    public static final RegistryKey<Trade> SELL_COOKIE = of("sell_cookie");
    public static final RegistryKey<Trade> SELL_NIGHT_VISION_SUSPICIOUS_STEW = of("sell_night_vision_suspicious_stew");
    public static final RegistryKey<Trade> SELL_JUMP_BOOST_SUSPICIOUS_STEW = of("sell_jump_boost_suspicious_stew");
    public static final RegistryKey<Trade> SELL_WEAKNESS_SUSPICIOUS_STEW = of("sell_weakness_suspicious_stew");
    public static final RegistryKey<Trade> SELL_BLINDNESS_SUSPICIOUS_STEW = of("sell_blindness_suspicious_stew");
    public static final RegistryKey<Trade> SELL_POISON_SUSPICIOUS_STEW = of("sell_poison_suspicious_stew");
    public static final RegistryKey<Trade> SELL_SATURATION_SUSPICIOUS_STEW = of("sell_saturation_suspicious_stew");
    public static final RegistryKey<Trade> SELL_GOLDEN_CARROT = of("sell_golden_carrot");
    public static final RegistryKey<Trade> SELL_GLISTERING_MELON_SLICE = of("sell_glistering_melon_slice");
    public static final RegistryKey<Trade> BUY_COAL = of("buy_coal");
    public static final RegistryKey<Trade> SELL_COD_BUCKET = of("sell_cod_bucket");
    public static final RegistryKey<Trade> BUY_COD = of("buy_cod");
    public static final RegistryKey<Trade> BUY_SALMON = of("buy_salmon");
    public static final RegistryKey<Trade> SELL_ENCHANTED_FISHING_ROD = of("sell_enchanted_fishing_rod");
    public static final RegistryKey<Trade> BUY_TROPICAL_FISH = of("buy_tropical_fish");
    public static final RegistryKey<Trade> BUY_PUFFERFISH = of("buy_pufferfish");
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
    public static final RegistryKey<Trade> SELL_BOW = of("sell_bow");
    public static final RegistryKey<Trade> SELL_CROSSBOW = of("sell_crossbow");
    public static final RegistryKey<Trade> BUY_FEATHER = of("buy_feather");
    public static final RegistryKey<Trade> SELL_ENCHANTED_BOW = of("sell_enchanted_bow");
    public static final RegistryKey<Trade> SELL_ENCHANTED_CROSSBOW = of("sell_enchanted_crossbow");
    public static final RegistryKey<Trade> BUY_PAPER = of("buy_paper");
    public static final RegistryKey<Trade> SELL_BOOKSHELF = of("sell_bookshelf");
    public static final RegistryKey<Trade> BUY_BOOK = of("buy_book");
    public static final RegistryKey<Trade> BUY_INK_SAC = of("buy_ink_sac");
    public static final RegistryKey<Trade> SELL_CLOCK = of("sell_clock");
    public static final RegistryKey<Trade> SELL_COMPASS = of("sell_compass");
    public static final RegistryKey<Trade> BUY_COMPASS = of("buy_compass");
    public static final RegistryKey<Trade> SELL_ITEM_FRAME = of("sell_item_frame");
    public static final RegistryKey<Trade> BUY_ROTTEN_FLESH = of("buy_rotten_flesh");
    public static final RegistryKey<Trade> SELL_REDSTONE = of("sell_redstone");
    public static final RegistryKey<Trade> BUY_GOLD_INGOT = of("buy_gold_ingot");
    public static final RegistryKey<Trade> SELL_LAPIS_LAZULI = of("sell_lapis_lazuli");
    public static final RegistryKey<Trade> BUY_RABBIT_FOOT = of("buy_rabbit_foot");
    public static final RegistryKey<Trade> BUY_SCUTE = of("buy_scute");
    public static final RegistryKey<Trade> BUY_GLASS_BOTTLE = of("buy_glass_bottle");
    public static final RegistryKey<Trade> SELL_ENDER_PEARL = of("sell_ender_pearl");
    public static final RegistryKey<Trade> BUY_NETHER_WART = of("buy_nether_wart");
    public static final RegistryKey<Trade> SELL_EXPERIENCE_BOTTLE = of("sell_experience_bottle");
    public static final RegistryKey<Trade> BUY_COAL_CHEAPER = of("buy_coal_cheaper");
    public static final RegistryKey<Trade> SELL_IRON_LEGGINGS = of("sell_iron_leggings");
    public static final RegistryKey<Trade> SELL_IRON_BOOTS = of("sell_iron_boots");
    public static final RegistryKey<Trade> SELL_IRON_HELMET = of("sell_iron_helmet");
    public static final RegistryKey<Trade> SELL_IRON_CHESTPLATE = of("sell_iron_chestplate");
    public static final RegistryKey<Trade> BUY_IRON_INGOT = of("buy_iron_ingot");
    public static final RegistryKey<Trade> SELL_CHAINMAIL_BOOTS = of("sell_chainmail_boots");
    public static final RegistryKey<Trade> SELL_CHAINMAIL_LEGGINGS = of("sell_chainmail_leggings");
    public static final RegistryKey<Trade> BUY_LAVA_BUCKET = of("buy_lava_bucket");
    public static final RegistryKey<Trade> BUY_DIAMOND = of("buy_diamond");
    public static final RegistryKey<Trade> SELL_CHAINMAIL_HELMET = of("sell_chainmail_helmet");
    public static final RegistryKey<Trade> SELL_CHAINMAIL_CHESTPLATE = of("sell_chainmail_chestplate");
    public static final RegistryKey<Trade> SELL_SHIELD = of("sell_shield");
    public static final RegistryKey<Trade> SELL_ENCHANTED_DIAMOND_LEGGINGS = of("sell_enchanted_diamond_leggings");
    public static final RegistryKey<Trade> SELL_ENCHANTED_DIAMOND_BOOTS = of("sell_enchanted_diamond_boots");
    public static final RegistryKey<Trade> SELL_ENCHANTED_DIAMOND_HELMET = of("sell_enchanted_diamond_helmet");
    public static final RegistryKey<Trade> SELL_ENCHANTED_DIAMOND_CHESTPLATE = of("sell_enchanted_diamond_chestplate");
    public static final RegistryKey<Trade> SELL_IRON_AXE = of("sell_iron_axe");
    public static final RegistryKey<Trade> SELL_ENCHANTED_IRON_SWORD = of("sell_enchanted_iron_sword");
    public static final RegistryKey<Trade> BUY_DIAMOND_MORE_EXPERIENCE = of("buy_diamond_more_experience");
    public static final RegistryKey<Trade> SELL_ENCHANTED_DIAMOND_AXE = of("sell_enchanted_diamond_axe");
    public static final RegistryKey<Trade> SELL_ENCHANTED_DIAMOND_SWORD = of("sell_enchanted_diamond_sword");
    public static final RegistryKey<Trade> SELL_STONE_AXE = of("sell_stone_axe");
    public static final RegistryKey<Trade> SELL_STONE_SHOVEL = of("sell_stone_shovel");
    public static final RegistryKey<Trade> SELL_STONE_PICKAXE = of("sell_stone_pickaxe");
    public static final RegistryKey<Trade> SELL_STONE_HOE = of("sell_stone_hoe");
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
    public static final RegistryKey<Trade> SELL_SADDLE = of("sell_saddle");
    public static final RegistryKey<Trade> BUY_STONE = of("buy_stone");
    public static final RegistryKey<Trade> SELL_FERN = of("sell_fern");
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
    public static final RegistryKey<Trade> SELL_VINE = of("sell_vine");
    public static final RegistryKey<Trade> SELL_LILY_PAD = of("sell_lily_pad");
    public static final RegistryKey<Trade> SELL_SAND = of("sell_sand");
    public static final RegistryKey<Trade> SELL_TROPICAL_FISH_BUCKET = of("sell_tropical_fish_bucket");
    public static final RegistryKey<Trade> SELL_PUFFERFISH_BUCKET = of("sell_pufferfish_bucket");
    public static final RegistryKey<Trade> SELL_GUNPOWDER = of("sell_gunpowder");

    private Trades() {}

    public static void bootstrap(Registerable<Trade> registerable) {
        RegistryEntryLookup<Item> items = registerable.getRegistryLookup(RegistryKeys.ITEM);
        RegistryEntryLookup<StatusEffect> statusEffects = registerable.getRegistryLookup(RegistryKeys.STATUS_EFFECT);

        registerable.register(BUY_WHEAT, buy(items, items.getOrThrow(ItemKeys.WHEAT), 20, 16, 2));
        registerable.register(BUY_POTATO, buy(items, items.getOrThrow(ItemKeys.POTATO), 26, 16, 2));
        registerable.register(BUY_CARROT, buy(items, items.getOrThrow(ItemKeys.CARROT), 22, 16, 2));
        registerable.register(BUY_BEETROOT, buy(items, items.getOrThrow(ItemKeys.BEETROOT), 15, 16, 2));
        registerable.register(SELL_BREAD, sell(items, items.getOrThrow(ItemKeys.BREAD), 6, 16, 1, 1));
        registerable.register(SELL_PUMPKIN_PIE, sell(items, items.getOrThrow(ItemKeys.PUMPKIN_PIE), 4, 12, 5, 1));
        registerable.register(SELL_APPLE, sell(items, items.getOrThrow(ItemKeys.APPLE), 4, 16, 5, 1));
        registerable.register(SELL_COOKIE, sell(items, items.getOrThrow(ItemKeys.COOKIE), 18, 12, 10, 3));
        registerable.register(SELL_NIGHT_VISION_SUSPICIOUS_STEW, sellSuspiciousStew(items, statusEffects.getOrThrow(StatusEffectKeys.NIGHT_VISION), 100));
        registerable.register(SELL_JUMP_BOOST_SUSPICIOUS_STEW, sellSuspiciousStew(items, statusEffects.getOrThrow(StatusEffectKeys.JUMP_BOOST), 160));
        registerable.register(SELL_WEAKNESS_SUSPICIOUS_STEW, sellSuspiciousStew(items, statusEffects.getOrThrow(StatusEffectKeys.WEAKNESS), 140));
        registerable.register(SELL_BLINDNESS_SUSPICIOUS_STEW, sellSuspiciousStew(items, statusEffects.getOrThrow(StatusEffectKeys.BLINDNESS), 120));
        registerable.register(SELL_POISON_SUSPICIOUS_STEW, sellSuspiciousStew(items, statusEffects.getOrThrow(StatusEffectKeys.POISON), 280));
        registerable.register(SELL_SATURATION_SUSPICIOUS_STEW, sellSuspiciousStew(items, statusEffects.getOrThrow(StatusEffectKeys.SATURATION), 7));
        registerable.register(SELL_GOLDEN_CARROT, sell(items, items.getOrThrow(ItemKeys.GOLDEN_CARROT), 3, 12, 30, 3));
        registerable.register(SELL_GLISTERING_MELON_SLICE, sell(items, items.getOrThrow(ItemKeys.GLISTERING_MELON_SLICE), 3, 12, 30, 4));
        registerable.register(BUY_COAL, buy(items, items.getOrThrow(ItemKeys.COAL), 10, 16, 2));
        registerable.register(SELL_COD_BUCKET, sell(items, items.getOrThrow(ItemKeys.COD_BUCKET), 1, 16, 1, 3));
        registerable.register(BUY_COD, buy(items, items.getOrThrow(ItemKeys.COD), 15, 16, 10));
        registerable.register(BUY_SALMON, buy(items, items.getOrThrow(ItemKeys.SALMON), 13, 16, 20));
        registerable.register(SELL_ENCHANTED_FISHING_ROD, sellEnchantedItem(items, items.getOrThrow(ItemKeys.FISHING_ROD), 10, 3, 0.2f));
        registerable.register(BUY_TROPICAL_FISH, buy(items, items.getOrThrow(ItemKeys.TROPICAL_FISH), 6, 12, 30));
        registerable.register(BUY_PUFFERFISH, buy(items, items.getOrThrow(ItemKeys.PUFFERFISH), 4, 12, 30));
        registerable.register(BUY_WHITE_WOOL, buy(items, items.getOrThrow(ItemKeys.WHITE_WOOL), 18, 16, 2));
        registerable.register(BUY_BROWN_WOOL, buy(items, items.getOrThrow(ItemKeys.BROWN_WOOL), 18, 16, 2));
        registerable.register(BUY_BLACK_WOOL, buy(items, items.getOrThrow(ItemKeys.BLACK_WOOL), 18, 16, 2));
        registerable.register(BUY_GRAY_WOOL, buy(items, items.getOrThrow(ItemKeys.GRAY_WOOL), 18, 16, 2));
        registerable.register(SELL_SHEARS, sell(items, items.getOrThrow(ItemKeys.SHEARS), 1, 12, 1, 2));
        registerable.register(BUY_WHITE_DYE, buy(items, items.getOrThrow(ItemKeys.WHITE_DYE), 12, 16, 10));
        registerable.register(BUY_GRAY_DYE, buy(items, items.getOrThrow(ItemKeys.GRAY_DYE), 12, 16, 10));
        registerable.register(BUY_BLACK_DYE, buy(items, items.getOrThrow(ItemKeys.BLACK_DYE), 12, 16, 10));
        registerable.register(BUY_LIGHT_BLUE_DYE, buy(items, items.getOrThrow(ItemKeys.LIGHT_BLUE_DYE), 12, 16, 10));
        registerable.register(BUY_LIME_DYE, buy(items, items.getOrThrow(ItemKeys.LIME_DYE), 12, 16, 10));
        registerable.register(SELL_WHITE_WOOL, sell(items, items.getOrThrow(ItemKeys.WHITE_WOOL), 1, 16, 5, 1));
        registerable.register(SELL_ORANGE_WOOL, sell(items, items.getOrThrow(ItemKeys.ORANGE_WOOL), 1, 16, 5, 1));
        registerable.register(SELL_MAGENTA_WOOL, sell(items, items.getOrThrow(ItemKeys.MAGENTA_WOOL), 1, 16, 5, 1));
        registerable.register(SELL_LIGHT_BLUE_WOOL, sell(items, items.getOrThrow(ItemKeys.LIGHT_BLUE_WOOL), 1, 16, 5, 1));
        registerable.register(SELL_YELLOW_WOOL, sell(items, items.getOrThrow(ItemKeys.YELLOW_WOOL), 1, 16, 5, 1));
        registerable.register(SELL_LIME_WOOL, sell(items, items.getOrThrow(ItemKeys.LIME_WOOL), 1, 16, 5, 1));
        registerable.register(SELL_PINK_WOOL, sell(items, items.getOrThrow(ItemKeys.PINK_WOOL), 1, 16, 5, 1));
        registerable.register(SELL_GRAY_WOOL, sell(items, items.getOrThrow(ItemKeys.GRAY_WOOL), 1, 16, 5, 1));
        registerable.register(SELL_LIGHT_GRAY_WOOL, sell(items, items.getOrThrow(ItemKeys.LIGHT_GRAY_WOOL), 1, 16, 5, 1));
        registerable.register(SELL_CYAN_WOOL, sell(items, items.getOrThrow(ItemKeys.CYAN_WOOL), 1, 16, 5, 1));
        registerable.register(SELL_PURPLE_WOOL, sell(items, items.getOrThrow(ItemKeys.PURPLE_WOOL), 1, 16, 5, 1));
        registerable.register(SELL_BLUE_WOOL, sell(items, items.getOrThrow(ItemKeys.BLUE_WOOL), 1, 16, 5, 1));
        registerable.register(SELL_BROWN_WOOL, sell(items, items.getOrThrow(ItemKeys.BROWN_WOOL), 1, 16, 5, 1));
        registerable.register(SELL_GREEN_WOOL, sell(items, items.getOrThrow(ItemKeys.GREEN_WOOL), 1, 16, 5, 1));
        registerable.register(SELL_RED_WOOL, sell(items, items.getOrThrow(ItemKeys.RED_WOOL), 1, 16, 5, 1));
        registerable.register(SELL_BLACK_WOOL, sell(items, items.getOrThrow(ItemKeys.BLACK_WOOL), 1, 16, 5, 1));
        registerable.register(SELL_WHITE_CARPET, sell(items, items.getOrThrow(ItemKeys.WHITE_CARPET), 4, 16, 5, 1));
        registerable.register(SELL_ORANGE_CARPET, sell(items, items.getOrThrow(ItemKeys.ORANGE_CARPET), 4, 16, 5, 1));
        registerable.register(SELL_MAGENTA_CARPET, sell(items, items.getOrThrow(ItemKeys.MAGENTA_CARPET), 4, 16, 5, 1));
        registerable.register(SELL_LIGHT_BLUE_CARPET, sell(items, items.getOrThrow(ItemKeys.LIGHT_BLUE_CARPET), 4, 16, 5, 1));
        registerable.register(SELL_YELLOW_CARPET, sell(items, items.getOrThrow(ItemKeys.YELLOW_CARPET), 4, 16, 5, 1));
        registerable.register(SELL_LIME_CARPET, sell(items, items.getOrThrow(ItemKeys.LIME_CARPET), 4, 16, 5, 1));
        registerable.register(SELL_PINK_CARPET, sell(items, items.getOrThrow(ItemKeys.PINK_CARPET), 4, 16, 5, 1));
        registerable.register(SELL_GRAY_CARPET, sell(items, items.getOrThrow(ItemKeys.GRAY_CARPET), 4, 16, 5, 1));
        registerable.register(SELL_LIGHT_GRAY_CARPET, sell(items, items.getOrThrow(ItemKeys.LIGHT_GRAY_CARPET), 4, 16, 5, 1));
        registerable.register(SELL_CYAN_CARPET, sell(items, items.getOrThrow(ItemKeys.CYAN_CARPET), 4, 16, 5, 1));
        registerable.register(SELL_PURPLE_CARPET, sell(items, items.getOrThrow(ItemKeys.PURPLE_CARPET), 4, 16, 5, 1));
        registerable.register(SELL_BLUE_CARPET, sell(items, items.getOrThrow(ItemKeys.BLUE_CARPET), 4, 16, 5, 1));
        registerable.register(SELL_BROWN_CARPET, sell(items, items.getOrThrow(ItemKeys.BROWN_CARPET), 4, 16, 5, 1));
        registerable.register(SELL_GREEN_CARPET, sell(items, items.getOrThrow(ItemKeys.GREEN_CARPET), 4, 16, 5, 1));
        registerable.register(SELL_RED_CARPET, sell(items, items.getOrThrow(ItemKeys.RED_CARPET), 4, 16, 5, 1));
        registerable.register(SELL_BLACK_CARPET, sell(items, items.getOrThrow(ItemKeys.BLACK_CARPET), 4, 16, 5, 1));
        registerable.register(BUY_YELLOW_DYE, buy(items, items.getOrThrow(ItemKeys.YELLOW_DYE), 12, 16, 20));
        registerable.register(BUY_LIGHT_GRAY_DYE, buy(items, items.getOrThrow(ItemKeys.LIGHT_GRAY_DYE), 12, 16, 20));
        registerable.register(BUY_ORANGE_DYE, buy(items, items.getOrThrow(ItemKeys.ORANGE_DYE), 12, 16, 20));
        registerable.register(BUY_RED_DYE, buy(items, items.getOrThrow(ItemKeys.RED_DYE), 12, 16, 20));
        registerable.register(BUY_PINK_DYE, buy(items, items.getOrThrow(ItemKeys.PINK_DYE), 12, 16, 20));
        registerable.register(BUY_BROWN_DYE, buy(items, items.getOrThrow(ItemKeys.BROWN_DYE), 12, 16, 30));
        registerable.register(BUY_PURPLE_DYE, buy(items, items.getOrThrow(ItemKeys.PURPLE_DYE), 12, 16, 30));
        registerable.register(BUY_BLUE_DYE, buy(items, items.getOrThrow(ItemKeys.BLUE_DYE), 12, 16, 30));
        registerable.register(BUY_GREEN_DYE, buy(items, items.getOrThrow(ItemKeys.GREEN_DYE), 12, 16, 30));
        registerable.register(BUY_MAGENTA_DYE, buy(items, items.getOrThrow(ItemKeys.MAGENTA_DYE), 12, 16, 30));
        registerable.register(BUY_CYAN_DYE, buy(items, items.getOrThrow(ItemKeys.CYAN_DYE), 12, 16, 30));
        registerable.register(SELL_WHITE_BANNER, sell(items, items.getOrThrow(ItemKeys.WHITE_BANNER), 1, 12, 15, 3));
        registerable.register(SELL_BLUE_BANNER, sell(items, items.getOrThrow(ItemKeys.BLUE_BANNER), 1, 12, 15, 3));
        registerable.register(SELL_LIGHT_BLUE_BANNER, sell(items, items.getOrThrow(ItemKeys.LIGHT_BLUE_BANNER), 1, 12, 15, 3));
        registerable.register(SELL_RED_BANNER, sell(items, items.getOrThrow(ItemKeys.RED_BANNER), 1, 12, 15, 3));
        registerable.register(SELL_PINK_BANNER, sell(items, items.getOrThrow(ItemKeys.PINK_BANNER), 1, 12, 15, 3));
        registerable.register(SELL_GREEN_BANNER, sell(items, items.getOrThrow(ItemKeys.GREEN_BANNER), 1, 12, 15, 3));
        registerable.register(SELL_LIME_BANNER, sell(items, items.getOrThrow(ItemKeys.LIME_BANNER), 1, 12, 15, 3));
        registerable.register(SELL_GRAY_BANNER, sell(items, items.getOrThrow(ItemKeys.GRAY_BANNER), 1, 12, 15, 3));
        registerable.register(SELL_BLACK_BANNER, sell(items, items.getOrThrow(ItemKeys.BLACK_BANNER), 1, 12, 15, 3));
        registerable.register(SELL_PURPLE_BANNER, sell(items, items.getOrThrow(ItemKeys.PURPLE_BANNER), 1, 12, 15, 3));
        registerable.register(SELL_MAGENTA_BANNER, sell(items, items.getOrThrow(ItemKeys.MAGENTA_BANNER), 1, 12, 15, 3));
        registerable.register(SELL_CYAN_BANNER, sell(items, items.getOrThrow(ItemKeys.CYAN_BANNER), 1, 12, 15, 3));
        registerable.register(SELL_BROWN_BANNER, sell(items, items.getOrThrow(ItemKeys.BROWN_BANNER), 1, 12, 15, 3));
        registerable.register(SELL_YELLOW_BANNER, sell(items, items.getOrThrow(ItemKeys.YELLOW_BANNER), 1, 12, 15, 3));
        registerable.register(SELL_ORANGE_BANNER, sell(items, items.getOrThrow(ItemKeys.ORANGE_BANNER), 1, 12, 15, 3));
        registerable.register(SELL_LIGHT_GRAY_BANNER, sell(items, items.getOrThrow(ItemKeys.LIGHT_GRAY_BANNER), 1, 12, 15, 3));
        registerable.register(SELL_PAINTING, sell(items, items.getOrThrow(ItemKeys.PAINTING), 3, 12, 30, 2));
        registerable.register(BUY_STICK, buy(items, items.getOrThrow(ItemKeys.STICK), 32, 16, 2));
        registerable.register(SELL_ARROW, sell(items, items.getOrThrow(ItemKeys.ARROW), 16, 12, 1, 1));
        registerable.register(SELL_BOW, sell(items, items.getOrThrow(ItemKeys.BOW), 1, 12, 5, 2));
        registerable.register(SELL_CROSSBOW, sell(items, items.getOrThrow(ItemKeys.CROSSBOW), 1, 12, 10, 3));
        registerable.register(BUY_FEATHER, buy(items, items.getOrThrow(ItemKeys.FEATHER), 24, 16, 30));
        registerable.register(SELL_ENCHANTED_BOW, sellEnchantedItem(items, items.getOrThrow(ItemKeys.BOW), 15, 2));
        registerable.register(SELL_ENCHANTED_CROSSBOW, sellEnchantedItem(items, items.getOrThrow(ItemKeys.CROSSBOW), 15, 3));
        registerable.register(BUY_PAPER, buy(items, items.getOrThrow(ItemKeys.PAPER), 24, 16, 2));
        registerable.register(SELL_BOOKSHELF, sell(items, items.getOrThrow(ItemKeys.BOOKSHELF), 1, 12, 1, 9));
        registerable.register(BUY_BOOK, buy(items, items.getOrThrow(ItemKeys.BOOK), 4, 12, 10));
        registerable.register(BUY_INK_SAC, buy(items, items.getOrThrow(ItemKeys.INK_SAC), 5, 12, 20));
        registerable.register(SELL_CLOCK, sell(items, items.getOrThrow(ItemKeys.CLOCK), 1, 12, 15, 5));
        registerable.register(SELL_COMPASS, sell(items, items.getOrThrow(ItemKeys.COMPASS), 1, 12, 15, 4));
        registerable.register(BUY_COMPASS, buy(items, items.getOrThrow(ItemKeys.COMPASS), 1, 12, 20));
        registerable.register(SELL_ITEM_FRAME, sell(items, items.getOrThrow(ItemKeys.ITEM_FRAME), 1, 12, 15, 7));
        registerable.register(BUY_ROTTEN_FLESH, buy(items, items.getOrThrow(ItemKeys.ROTTEN_FLESH), 32, 16, 2));
        registerable.register(SELL_REDSTONE, sell(items, items.getOrThrow(ItemKeys.REDSTONE), 2, 12, 1, 1));
        registerable.register(BUY_GOLD_INGOT, buy(items, items.getOrThrow(ItemKeys.GOLD_INGOT), 3, 12, 10));
        registerable.register(SELL_LAPIS_LAZULI, sell(items, items.getOrThrow(ItemKeys.LAPIS_LAZULI), 1, 12, 5, 1));
        registerable.register(BUY_RABBIT_FOOT, buy(items, items.getOrThrow(ItemKeys.RABBIT_FOOT), 2, 12, 20));
        registerable.register(BUY_SCUTE, buy(items, items.getOrThrow(ItemKeys.TURTLE_SCUTE), 4, 12, 30));
        registerable.register(BUY_GLASS_BOTTLE, buy(items, items.getOrThrow(ItemKeys.GLASS_BOTTLE), 9, 12, 30));
        registerable.register(SELL_ENDER_PEARL, sell(items, items.getOrThrow(ItemKeys.ENDER_PEARL), 1, 12, 15, 5));
        registerable.register(BUY_NETHER_WART, buy(items, items.getOrThrow(ItemKeys.NETHER_WART), 22, 12, 30));
        registerable.register(SELL_EXPERIENCE_BOTTLE, sell(items, items.getOrThrow(ItemKeys.EXPERIENCE_BOTTLE), 1, 12, 30, 3));
        registerable.register(BUY_COAL_CHEAPER, buy(items, items.getOrThrow(ItemKeys.COAL), 15, 16, 2));
        registerable.register(SELL_IRON_LEGGINGS, sell(items, items.getOrThrow(ItemKeys.IRON_LEGGINGS), 1, 12, 1, 7));
        registerable.register(SELL_IRON_BOOTS, sell(items, items.getOrThrow(ItemKeys.IRON_BOOTS), 1, 12, 1, 4));
        registerable.register(SELL_IRON_HELMET, sell(items, items.getOrThrow(ItemKeys.IRON_HELMET), 1, 12, 1, 5));
        registerable.register(SELL_IRON_CHESTPLATE, sell(items, items.getOrThrow(ItemKeys.IRON_CHESTPLATE), 1, 12, 1, 9));
        registerable.register(BUY_IRON_INGOT, buy(items, items.getOrThrow(ItemKeys.IRON_INGOT), 4, 12, 10));
        registerable.register(SELL_CHAINMAIL_BOOTS, sell(items, items.getOrThrow(ItemKeys.CHAINMAIL_BOOTS), 1, 12, 5, 1));
        registerable.register(SELL_CHAINMAIL_LEGGINGS, sell(items, items.getOrThrow(ItemKeys.CHAINMAIL_LEGGINGS), 1, 12, 5, 3));
        registerable.register(BUY_LAVA_BUCKET, buy(items, items.getOrThrow(ItemKeys.LAVA_BUCKET), 1, 12, 20));
        registerable.register(BUY_DIAMOND, buy(items, items.getOrThrow(ItemKeys.DIAMOND), 1, 12, 20));
        registerable.register(SELL_CHAINMAIL_HELMET, sell(items, items.getOrThrow(ItemKeys.CHAINMAIL_HELMET), 1, 12, 10, 1));
        registerable.register(SELL_CHAINMAIL_CHESTPLATE, sell(items, items.getOrThrow(ItemKeys.CHAINMAIL_CHESTPLATE), 1, 12, 10, 4));
        registerable.register(SELL_SHIELD, sell(items, items.getOrThrow(ItemKeys.SHIELD), 1, 12, 10, 5));
        registerable.register(SELL_ENCHANTED_DIAMOND_LEGGINGS, sellEnchantedItem(items, items.getOrThrow(ItemKeys.DIAMOND_LEGGINGS), 15, 14, 0.2f));
        registerable.register(SELL_ENCHANTED_DIAMOND_BOOTS, sellEnchantedItem(items, items.getOrThrow(ItemKeys.DIAMOND_BOOTS), 15, 8, 0.2f));
        registerable.register(SELL_ENCHANTED_DIAMOND_HELMET, sellEnchantedItem(items, items.getOrThrow(ItemKeys.DIAMOND_HELMET), 30, 8, 0.2f));
        registerable.register(SELL_ENCHANTED_DIAMOND_CHESTPLATE, sellEnchantedItem(items, items.getOrThrow(ItemKeys.DIAMOND_CHESTPLATE), 30, 16, 0.2f));
        registerable.register(SELL_IRON_AXE, sell(items, items.getOrThrow(ItemKeys.IRON_AXE), 1, 12, 1, 3));
        registerable.register(SELL_ENCHANTED_IRON_SWORD, sellEnchantedItem(items, items.getOrThrow(ItemKeys.IRON_SWORD), 1, 2));
        registerable.register(BUY_DIAMOND_MORE_EXPERIENCE, buy(items, items.getOrThrow(ItemKeys.DIAMOND), 1, 12, 30));
        registerable.register(SELL_ENCHANTED_DIAMOND_AXE, sellEnchantedItem(items, items.getOrThrow(ItemKeys.DIAMOND_AXE), 15, 12, 0.2f));
        registerable.register(SELL_ENCHANTED_DIAMOND_SWORD, sellEnchantedItem(items, items.getOrThrow(ItemKeys.DIAMOND_SWORD), 30, 8, 0.2f));
        registerable.register(SELL_STONE_AXE, sell(items, items.getOrThrow(ItemKeys.STONE_AXE), 1, 12, 1, 1));
        registerable.register(SELL_STONE_SHOVEL, sell(items, items.getOrThrow(ItemKeys.STONE_SHOVEL), 1, 12, 1, 1));
        registerable.register(SELL_STONE_PICKAXE, sell(items, items.getOrThrow(ItemKeys.STONE_PICKAXE), 1, 12, 1, 1));
        registerable.register(SELL_STONE_HOE, sell(items, items.getOrThrow(ItemKeys.STONE_HOE), 1, 12, 1, 1));
        registerable.register(SELL_ENCHANTED_IRON_AXE, sellEnchantedItem(items, items.getOrThrow(ItemKeys.IRON_AXE), 10, 1, 0.2f));
        registerable.register(SELL_ENCHANTED_IRON_SHOVEL, sellEnchantedItem(items, items.getOrThrow(ItemKeys.IRON_SHOVEL), 10, 2, 0.2f));
        registerable.register(SELL_ENCHANTED_IRON_PICKAXE, sellEnchantedItem(items, items.getOrThrow(ItemKeys.IRON_PICKAXE), 10, 3, 0.2f));
        registerable.register(SELL_DIAMOND_HOE, sell(items, items.getOrThrow(ItemKeys.DIAMOND_HOE), 1, 3, 10, 4));
        registerable.register(SELL_ENCHANTED_DIAMOND_SHOVEL, sellEnchantedItem(items, items.getOrThrow(ItemKeys.DIAMOND_SHOVEL), 15, 5, 0.2f));
        registerable.register(SELL_ENCHANTED_DIAMOND_PICKAXE, sellEnchantedItem(items, items.getOrThrow(ItemKeys.DIAMOND_PICKAXE), 30, 13, 0.2f));
        registerable.register(BUY_CHICKEN, buy(items, items.getOrThrow(ItemKeys.CHICKEN), 14, 16, 2));
        registerable.register(BUY_PORKCHOP, buy(items, items.getOrThrow(ItemKeys.PORKCHOP), 7, 16, 2));
        registerable.register(BUY_RABBIT, buy(items, items.getOrThrow(ItemKeys.RABBIT), 4, 16, 2));
        registerable.register(SELL_RABBIT_STEW, sell(items, items.getOrThrow(ItemKeys.RABBIT_STEW), 1, 12, 1, 1));
        registerable.register(SELL_COOKED_PORKCHOP, sell(items, items.getOrThrow(ItemKeys.COOKED_PORKCHOP), 5, 16, 5, 1));
        registerable.register(SELL_COOKED_CHICKEN, sell(items, items.getOrThrow(ItemKeys.COOKED_CHICKEN), 8, 16, 5, 1));
        registerable.register(BUY_MUTTON, buy(items, items.getOrThrow(ItemKeys.MUTTON), 7, 16, 20));
        registerable.register(BUY_BEEF, buy(items, items.getOrThrow(ItemKeys.BEEF), 10, 16, 20));
        registerable.register(BUY_DRIED_KELP_BLOCK, buy(items, items.getOrThrow(ItemKeys.DRIED_KELP_BLOCK), 10, 12, 30));
        registerable.register(BUY_SWEET_BERRIES, buy(items, items.getOrThrow(ItemKeys.SWEET_BERRIES), 10, 12, 30));
        registerable.register(BUY_LEATHER, buy(items, items.getOrThrow(ItemKeys.LEATHER), 6, 16, 2));
        registerable.register(SELL_SADDLE, sell(items, items.getOrThrow(ItemKeys.SADDLE), 1, 12, 30, 6));
        registerable.register(BUY_STONE, buy(items, items.getOrThrow(ItemKeys.STONE), 20, 16, 10));
        registerable.register(SELL_FERN, sell(items, items.getOrThrow(ItemKeys.FERN), 1, 12, 1, 1));
        registerable.register(SELL_WHEAT_SEEDS, sell(items, items.getOrThrow(ItemKeys.WHEAT_SEEDS), 1, 12, 1, 1));
        registerable.register(SELL_BEETROOT_SEEDS, sell(items, items.getOrThrow(ItemKeys.BEETROOT_SEEDS), 1, 12, 1, 1));
        registerable.register(SELL_PUMPKIN_SEEDS, sell(items, items.getOrThrow(ItemKeys.PUMPKIN_SEEDS), 1, 12, 1, 1));
        registerable.register(SELL_MELON_SEEDS, sell(items, items.getOrThrow(ItemKeys.MELON_SEEDS), 1, 12, 1, 1));
        registerable.register(SELL_ACACIA_SAPLING, sell(items, items.getOrThrow(ItemKeys.ACACIA_SAPLING), 1, 8, 1, 5));
        registerable.register(SELL_BIRCH_SAPLING, sell(items, items.getOrThrow(ItemKeys.BIRCH_SAPLING), 1, 8, 1, 5));
        registerable.register(SELL_DARK_OAK_SAPLING, sell(items, items.getOrThrow(ItemKeys.DARK_OAK_SAPLING), 1, 8, 1, 5));
        registerable.register(SELL_JUNGLE_SAPLING, sell(items, items.getOrThrow(ItemKeys.JUNGLE_SAPLING), 1, 8, 1, 5));
        registerable.register(SELL_OAK_SAPLING, sell(items, items.getOrThrow(ItemKeys.OAK_SAPLING), 1, 8, 1, 5));
        registerable.register(SELL_SPRUCE_SAPLING, sell(items, items.getOrThrow(ItemKeys.SPRUCE_SAPLING), 1, 8, 1, 5));
        registerable.register(SELL_CHERRY_SAPLING, sell(items, items.getOrThrow(ItemKeys.CHERRY_SAPLING), 1, 8, 1, 5));
        registerable.register(SELL_MANGROVE_PROPAGULE, sell(items, items.getOrThrow(ItemKeys.MANGROVE_PROPAGULE), 1, 8, 1, 5));
        registerable.register(SELL_RED_DYE, sell(items, items.getOrThrow(ItemKeys.RED_DYE), 3, 12, 1, 1));
        registerable.register(SELL_WHITE_DYE, sell(items, items.getOrThrow(ItemKeys.WHITE_DYE), 3, 12, 1, 1));
        registerable.register(SELL_BLUE_DYE, sell(items, items.getOrThrow(ItemKeys.BLUE_DYE), 3, 12, 1, 1));
        registerable.register(SELL_PINK_DYE, sell(items, items.getOrThrow(ItemKeys.PINK_DYE), 3, 12, 1, 1));
        registerable.register(SELL_BLACK_DYE, sell(items, items.getOrThrow(ItemKeys.BLACK_DYE), 3, 12, 1, 1));
        registerable.register(SELL_GREEN_DYE, sell(items, items.getOrThrow(ItemKeys.GREEN_DYE), 3, 12, 1, 1));
        registerable.register(SELL_LIGHT_GRAY_DYE, sell(items, items.getOrThrow(ItemKeys.LIGHT_GRAY_DYE), 3, 12, 1, 1));
        registerable.register(SELL_MAGENTA_DYE, sell(items, items.getOrThrow(ItemKeys.MAGENTA_DYE), 3, 12, 1, 1));
        registerable.register(SELL_YELLOW_DYE, sell(items, items.getOrThrow(ItemKeys.YELLOW_DYE), 3, 12, 1, 1));
        registerable.register(SELL_GRAY_DYE, sell(items, items.getOrThrow(ItemKeys.GRAY_DYE), 3, 12, 1, 1));
        registerable.register(SELL_PURPLE_DYE, sell(items, items.getOrThrow(ItemKeys.PURPLE_DYE), 3, 12, 1, 1));
        registerable.register(SELL_LIGHT_BLUE_DYE, sell(items, items.getOrThrow(ItemKeys.LIGHT_BLUE_DYE), 3, 12, 1, 1));
        registerable.register(SELL_LIME_DYE, sell(items, items.getOrThrow(ItemKeys.LIME_DYE), 3, 12, 1, 1));
        registerable.register(SELL_ORANGE_DYE, sell(items, items.getOrThrow(ItemKeys.ORANGE_DYE), 3, 12, 1, 1));
        registerable.register(SELL_BROWN_DYE, sell(items, items.getOrThrow(ItemKeys.BROWN_DYE), 3, 12, 1, 1));
        registerable.register(SELL_CYAN_DYE, sell(items, items.getOrThrow(ItemKeys.CYAN_DYE), 3, 12, 1, 1));
        registerable.register(SELL_VINE, sell(items, items.getOrThrow(ItemKeys.VINE), 1, 12, 1, 1));
        registerable.register(SELL_LILY_PAD, sell(items, items.getOrThrow(ItemKeys.LILY_PAD), 2, 5, 1, 1));
        registerable.register(SELL_SAND, sell(items, items.getOrThrow(ItemKeys.SAND), 8, 8, 1, 1));
        registerable.register(SELL_TROPICAL_FISH_BUCKET, sell(items, items.getOrThrow(ItemKeys.TROPICAL_FISH_BUCKET), 1, 4, 1, 5));
        registerable.register(SELL_PUFFERFISH_BUCKET, sell(items, items.getOrThrow(ItemKeys.PUFFERFISH_BUCKET), 1, 4, 1, 5));
        registerable.register(SELL_GUNPOWDER, sell(items, items.getOrThrow(ItemKeys.GUNPOWDER), 1, 8, 1, 1));
    }

    private static Trade buy(RegistryEntryLookup<Item> items, RegistryEntry<Item> item, int count, int maxUses, int tradeExperience) {
        return Trade.builder(Trade.Entry.ofEmerald(items))
            .wants(Trade.Entry.of(item, count))
            .maxUses(maxUses)
            .tradeExperience(tradeExperience)
            .build();
    }

    private static Trade sell(RegistryEntryLookup<Item> items, RegistryEntry<Item> item, int count, int maxUses, int tradeExperience, int price) {
        return Trade.builder(Trade.Entry.of(item, count))
            .wants(Trade.Entry.ofEmerald(items, price))
            .maxUses(maxUses)
            .tradeExperience(tradeExperience)
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
        return Trade.builder(Trade.Entry.of(item, 1, EnchantWithLevelsLootFunction.builder(UniformLootNumberProvider.create(5, 20)).build()))
            .wants(Trade.Entry.ofEmerald(items, basePrice + 5, basePrice + 20))
            .maxUses(3)
            .tradeExperience(tradeExperience)
            .priceMultiplier(priceMultiplier)
            .build();
    }

    private static RegistryKey<Trade> of(String id) {
        return RegistryKey.of(ItematicRegistryKeys.TRADE, new Identifier(id));
    }
}
