package net.errorcraft.itematic.item.group.entry.provider;

import net.errorcraft.itematic.enchantment.EnchantmentUtil;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.ItemTagsUtil;
import net.errorcraft.itematic.item.group.entry.ItemGroupEntry;
import net.errorcraft.itematic.item.group.entry.entries.StackItemGroupEntry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.EnumSet;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ItemGroupEntryProviders {
    private ItemGroupEntryProviders() {}

    public static void bootstrap(Registerable<ItemGroupEntryProvider> registerable) {
        RegistryEntryLookup<Item> items = registerable.getRegistryLookup(RegistryKeys.ITEM);

        registerable.register(ItemGroupEntryProviderKeys.BUILDING_BLOCKS, ItemGroupEntryProvider.of(
            ItemGroupEntry.tag(ItemTagsUtil.WOODEN_BUILDING_BLOCKS),
            ItemGroupEntry.simple(ItemKeys.STONE),
            ItemGroupEntry.simple(ItemKeys.COBBLESTONE),
            ItemGroupEntry.simple(ItemKeys.BRICKS)
        ));
        registerable.register(ItemGroupEntryProviderKeys.COLORED_BLOCKS, ItemGroupEntryProvider.of(
            ItemGroupEntry.tag(ItemTagsUtil.WOOL),
            ItemGroupEntry.tag(ItemTagsUtil.WOOL_CARPETS),
            ItemGroupEntry.tag(ItemTagsUtil.BANNERS)
        ));
        registerable.register(ItemGroupEntryProviderKeys.NATURAL_BLOCKS, ItemGroupEntryProvider.of(
            ItemGroupEntry.simple(ItemKeys.GRASS_BLOCK),
            ItemGroupEntry.simple(ItemKeys.SAND),
            ItemGroupEntry.simple(ItemKeys.SNOW),
            ItemGroupEntry.simple(ItemKeys.STONE),
            ItemGroupEntry.tag(ItemTagsUtil.WOOD_BLOCKS),
            ItemGroupEntry.tag(ItemTagsUtil.LEAVES),
            ItemGroupEntry.tag(ItemTagsUtil.SAPLINGS),
            ItemGroupEntry.tag(ItemTagsUtil.PLANTS),
            ItemGroupEntry.tag(ItemTagsUtil.SEEDS),
            ItemGroupEntry.simple(ItemKeys.LILY_PAD),
            ItemGroupEntry.simple(ItemKeys.DRIED_KELP_BLOCK)
        ));
        registerable.register(ItemGroupEntryProviderKeys.FUNCTIONAL_BLOCKS, ItemGroupEntryProvider.of(
            ItemGroupEntry.simple(ItemKeys.CRAFTING_TABLE),
            ItemGroupEntry.simple(ItemKeys.CARTOGRAPHY_TABLE),
            ItemGroupEntry.simple(ItemKeys.FLETCHING_TABLE),
            ItemGroupEntry.simple(ItemKeys.SMITHING_TABLE),
            ItemGroupEntry.simple(ItemKeys.LOOM),
            ItemGroupEntry.simple(ItemKeys.COMPOSTER),
            ItemGroupEntry.simple(ItemKeys.NOTE_BLOCK),
            ItemGroupEntry.simple(ItemKeys.JUKEBOX),
            ItemGroupEntry.simple(ItemKeys.END_CRYSTAL),
            ItemGroupEntry.simple(ItemKeys.BREWING_STAND),
            ItemGroupEntry.simple(ItemKeys.CAULDRON),
            ItemGroupEntry.simple(ItemKeys.LADDER),
            ItemGroupEntry.simple(ItemKeys.SCAFFOLDING),
            ItemGroupEntry.simple(ItemKeys.ARMOR_STAND),
            ItemGroupEntry.simple(ItemKeys.ITEM_FRAME),
            ItemGroupEntry.simple(ItemKeys.GLOW_ITEM_FRAME),
            ItemGroupEntry.simple(ItemKeys.PAINTING),
            ItemGroupEntry.simple(ItemKeys.BOOKSHELF),
            ItemGroupEntry.simple(ItemKeys.CHISELED_BOOKSHELF),
            ItemGroupEntry.simple(ItemKeys.LECTERN),
            ItemGroupEntry.tag(ItemTagsUtil.SIGNS),
            ItemGroupEntry.simple(ItemKeys.CHEST),
            ItemGroupEntry.simple(ItemKeys.BARREL),
            ItemGroupEntry.tag(ItemTagsUtil.BANNERS),
            ItemGroupEntry.tag(ItemTagsUtil.HEADS)
        ));
        registerable.register(ItemGroupEntryProviderKeys.REDSTONE_BLOCKS, ItemGroupEntryProvider.of(
            ItemGroupEntry.simple(ItemKeys.REDSTONE),
            ItemGroupEntry.simple(ItemKeys.OAK_BUTTON),
            ItemGroupEntry.simple(ItemKeys.OAK_PRESSURE_PLATE),
            ItemGroupEntry.simple(ItemKeys.WHITE_WOOL),
            ItemGroupEntry.simple(ItemKeys.LECTERN),
            ItemGroupEntry.simple(ItemKeys.DAYLIGHT_DETECTOR),
            ItemGroupEntry.simple(ItemKeys.CHEST),
            ItemGroupEntry.simple(ItemKeys.BARREL),
            ItemGroupEntry.simple(ItemKeys.CHISELED_BOOKSHELF),
            ItemGroupEntry.simple(ItemKeys.TRAPPED_CHEST),
            ItemGroupEntry.simple(ItemKeys.JUKEBOX),
            ItemGroupEntry.simple(ItemKeys.NOTE_BLOCK),
            ItemGroupEntry.simple(ItemKeys.COMPOSTER),
            ItemGroupEntry.simple(ItemKeys.CAULDRON),
            ItemGroupEntry.tag(ItemTagsUtil.MINECARTS),
            ItemGroupEntry.simple(ItemKeys.OAK_CHEST_BOAT),
            ItemGroupEntry.simple(ItemKeys.BAMBOO_CHEST_RAFT),
            ItemGroupEntry.simple(ItemKeys.OAK_DOOR),
            ItemGroupEntry.simple(ItemKeys.OAK_FENCE_GATE),
            ItemGroupEntry.simple(ItemKeys.OAK_TRAPDOOR),
            ItemGroupEntry.simple(ItemKeys.ARMOR_STAND)
        ));
        registerable.register(ItemGroupEntryProviderKeys.TOOLS_AND_UTILITIES, ItemGroupEntryProvider.of(
            ItemGroupEntry.tag(ItemTagsUtil.TOOLS),
            ItemGroupEntry.tag(ItemTagsUtil.BUCKETS),
            ItemGroupEntry.simple(ItemKeys.FISHING_ROD),
            ItemGroupEntry.simple(ItemKeys.FIRE_CHARGE),
            ItemGroupEntry.simple(ItemKeys.BONE_MEAL),
            ItemGroupEntry.simple(ItemKeys.COMPASS),
            ItemGroupEntry.simple(ItemKeys.CLOCK),
            ItemGroupEntry.simple(ItemKeys.ENDER_PEARL),
            ItemGroupEntry.tag(ItemTagsUtil.BOATS),
            ItemGroupEntry.tag(ItemTagsUtil.MINECARTS),
            ItemGroupEntry.tag(ItemTagsUtil.MUSIC_DISCS)
        ));
        registerable.register(ItemGroupEntryProviderKeys.COMBAT, ItemGroupEntryProvider.builder()
            .add(ItemTagsUtil.SWORDS)
            .add(ItemTagsUtil.AXES)
            .add(ItemKeys.SHIELD)
            .add(ItemTagsUtil.ARMOR)
            .add(ItemKeys.END_CRYSTAL)
            .add(ItemKeys.SNOWBALL)
            .add(ItemKeys.EGG)
            .add(ItemKeys.BOW)
            .add(ItemKeys.CROSSBOW)
            .add(ItemKeys.ARROW)
            .add(ItemKeys.SPECTRAL_ARROW)
            .add(potions(items.getOrThrow(ItemKeys.TIPPED_ARROW)))
            .build()
        );
        registerable.register(ItemGroupEntryProviderKeys.FOOD_AND_DRINKS, ItemGroupEntryProvider.builder()
            .add(ItemTagsUtil.FOOD)
            .add(ItemKeys.MILK_BUCKET)
            .add(ItemKeys.HONEY_BOTTLE)
            .add(potions(items.getOrThrow(ItemKeys.POTION)))
            .add(potions(items.getOrThrow(ItemKeys.SPLASH_POTION)))
            .add(potions(items.getOrThrow(ItemKeys.LINGERING_POTION)))
            .build()
        );
        registerable.register(ItemGroupEntryProviderKeys.INGREDIENTS, ItemGroupEntryProvider.builder()
            .add(ItemKeys.COAL)
            .add(ItemKeys.CHARCOAL)
            .add(ItemKeys.LAPIS_LAZULI)
            .add(ItemKeys.DIAMOND)
            .add(ItemKeys.GOLD_NUGGET)
            .add(ItemKeys.IRON_INGOT)
            .add(ItemKeys.GOLD_INGOT)
            .add(ItemKeys.NETHERITE_INGOT)
            .add(ItemKeys.STICK)
            .add(ItemKeys.WHEAT)
            .add(ItemKeys.BONE_MEAL)
            .add(ItemKeys.FEATHER)
            .add(ItemKeys.SNOWBALL)
            .add(ItemKeys.EGG)
            .add(ItemKeys.LEATHER)
            .add(ItemKeys.SCUTE)
            .add(ItemKeys.FIRE_CHARGE)
            .add(ItemKeys.BLAZE_ROD)
            .add(ItemKeys.ENDER_PEARL)
            .add(ItemKeys.DISC_FRAGMENT_5)
            .add(ItemTagsUtil.DYES)
            .add(ItemKeys.BOWL)
            .add(ItemKeys.PAPER)
            .add(ItemKeys.BOOK)
            .add(ItemKeys.FIREWORK_STAR)
            .add(ItemTagsUtil.BREWING_INGREDIENTS)
            .add(ItemKeys.EXPERIENCE_BOTTLE)
            .add(enchantedBooks(items, EnumSet.allOf(EnchantmentTarget.class)))
            .build()
        );
        registerable.register(ItemGroupEntryProviderKeys.SPAWN_EGGS, ItemGroupEntryProvider.of(
            ItemGroupEntry.simple(ItemKeys.ALLAY_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.AXOLOTL_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.BAT_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.BEE_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.BLAZE_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.CAMEL_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.CAT_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.CAVE_SPIDER_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.CHICKEN_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.COD_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.COW_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.CREEPER_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.DOLPHIN_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.DONKEY_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.DROWNED_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.ELDER_GUARDIAN_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.ENDERMAN_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.ENDERMITE_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.EVOKER_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.FOX_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.FROG_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.GHAST_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.GLOW_SQUID_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.GOAT_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.GUARDIAN_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.HOGLIN_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.HORSE_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.HUSK_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.IRON_GOLEM_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.LLAMA_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.MAGMA_CUBE_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.MOOSHROOM_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.MULE_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.OCELOT_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.PANDA_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.PARROT_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.PHANTOM_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.PIG_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.PIGLIN_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.PIGLIN_BRUTE_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.PILLAGER_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.POLAR_BEAR_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.PUFFERFISH_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.RABBIT_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.RAVAGER_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.SALMON_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.SHEEP_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.SHULKER_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.SILVERFISH_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.SKELETON_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.SKELETON_HORSE_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.SLIME_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.SNIFFER_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.SNOW_GOLEM_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.SPIDER_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.SQUID_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.STRAY_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.STRIDER_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.TADPOLE_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.TRADER_LLAMA_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.TROPICAL_FISH_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.TURTLE_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.VEX_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.VILLAGER_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.VINDICATOR_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.WANDERING_TRADER_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.WARDEN_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.WITCH_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.WITHER_SKELETON_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.WOLF_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.ZOGLIN_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.ZOMBIE_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.ZOMBIE_HORSE_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.ZOMBIE_VILLAGER_SPAWN_EGG),
            ItemGroupEntry.simple(ItemKeys.ZOMBIFIED_PIGLIN_SPAWN_EGG)
        ));
        registerable.register(ItemGroupEntryProviderKeys.OP_BLOCKS, ItemGroupEntryProvider.of(
            ItemGroupEntry.requiresPermissions(ItemKeys.COMMAND_BLOCK),
            ItemGroupEntry.requiresPermissions(ItemKeys.COMMAND_BLOCK_MINECART),
            ItemGroupEntry.requiresPermissions(ItemKeys.BARRIER)
        ));
    }

    private static ItemGroupEntry[] potions(RegistryEntry<Item> item) {
        return Registries.POTION.streamEntries()
            .filter(entry -> !entry.matchesKey(Potions.EMPTY_KEY))
            .map(entry -> StackItemGroupEntry.fromStack(PotionUtil.setPotion(new ItemStack(item), entry.value())))
            .toArray(ItemGroupEntry[]::new);
    }

    private static ItemGroupEntry[] enchantedBooks(RegistryEntryLookup<Item> items, Set<EnchantmentTarget> targets) {
        RegistryEntry.Reference<Item> enchantedBook = items.getOrThrow(ItemKeys.ENCHANTED_BOOK);
        return enchantments(targets).flatMap(enchantment -> IntStream.rangeClosed(enchantment.getMinLevel(), enchantment.getMaxLevel())
            .mapToObj(level -> StackItemGroupEntry.fromStack(EnchantmentUtil.addEnchantment(new ItemStack(enchantedBook), new EnchantmentLevelEntry(enchantment, level)), getStackVisibility(enchantment, level))))
            .toArray(ItemGroupEntry[]::new);
    }

    private static Stream<Enchantment> enchantments(Set<EnchantmentTarget> targets) {
        return Registries.ENCHANTMENT.streamEntries()
            .map(RegistryEntry::value)
            .filter(enchantment -> targets.contains(enchantment.target));
    }

    private static ItemGroup.StackVisibility getStackVisibility(Enchantment enchantment, int level) {
        if (enchantment.getMaxLevel() == level) {
            return ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS;
        }
        return ItemGroup.StackVisibility.SEARCH_TAB_ONLY;
    }
}
