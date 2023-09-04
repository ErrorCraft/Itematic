package net.errorcraft.itematic.data;

import net.errorcraft.itematic.block.BlockKeys;
import net.errorcraft.itematic.block.ItematicBlockTags;
import net.errorcraft.itematic.client.render.TexturedRenderLayersUtil;
import net.errorcraft.itematic.enchantment.EnchantmentTags;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.ItemTagsUtil;
import net.errorcraft.itematic.item.group.entry.provider.ItemGroupEntryProvider;
import net.errorcraft.itematic.item.group.entry.provider.ItemGroupEntryProviderKeys;
import net.errorcraft.itematic.item.group.entry.provider.ItemGroupEntryProviderTags;
import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.client.texture.atlas.AtlasSource;
import net.minecraft.client.texture.atlas.AtlasSourceManager;
import net.minecraft.data.DataOutput;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

@SuppressWarnings("UnstableApiUsage")
public class ItematicData implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(ItemProvider::new);
        pack.addProvider(ArmorMaterialProvider::new);
        pack.addProvider(AtlasProvider::new);
        pack.addProvider(EnchantmentTagProvider::new);
        pack.addProvider(ItemTagProvider::new);
        pack.addProvider(BlockTagProvider::new);
        pack.addProvider(ItemGroupEntryProviderProvider::new);
        pack.addProvider(ItemGroupEntryProviderTagProvider::new);
    }

    private static <T> void addAll(FabricDynamicRegistryProvider.Entries entries, RegistryWrapper.Impl<T> registry) {
        registry.streamKeys().forEach(key -> entries.add(registry, key));
    }

    private static class ItemProvider extends FabricDynamicRegistryProvider {
        public ItemProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
            addAll(entries, registries.getWrapperOrThrow(RegistryKeys.ITEM));
        }

        @Override
        public String getName() {
            return "Items";
        }
    }

    private static class ArmorMaterialProvider extends FabricDynamicRegistryProvider {
        public ArmorMaterialProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
            addAll(entries, registries.getWrapperOrThrow(ItematicRegistryKeys.ARMOR_MATERIAL));
        }

        @Override
        public String getName() {
            return "Armor Materials";
        }
    }

    private static class AtlasProvider extends FabricCodecDataProvider<List<AtlasSource>> {
        protected AtlasProvider(FabricDataOutput dataOutput) {
            super(dataOutput, DataOutput.OutputType.RESOURCE_PACK, "atlases", AtlasSourceManager.LIST_CODEC);
        }

        @Override
        protected void configure(BiConsumer<Identifier, List<AtlasSource>> provider) {
            TexturedRenderLayersUtil.bootstrap(provider);
        }

        @Override
        public String getName() {
            return "Atlases";
        }
    }

    private static class EnchantmentTagProvider extends FabricTagProvider<Enchantment> {
        public EnchantmentTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, RegistryKeys.ENCHANTMENT, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup arg) {
            RegistryWrapper.Impl<Enchantment> registry = arg.getWrapperOrThrow(RegistryKeys.ENCHANTMENT);

            this.getOrCreateTagBuilder(EnchantmentTags.ARMOR)
                .add(getAll(registry, enchantment -> enchantment.target == EnchantmentTarget.ARMOR));
            this.getOrCreateTagBuilder(EnchantmentTags.CURSED)
                .add(getAll(registry, Enchantment::isCursed));
            this.getOrCreateTagBuilder(EnchantmentTags.BOOTS_ENCHANTING)
                .addTag(EnchantmentTags.ARMOR)
                .add(getAll(registry, EnchantmentTarget.ARMOR_FEET, false));
            this.forgingTag(registry, EnchantmentTags.BOOTS_FORGING, EnchantmentTags.BOOTS_ENCHANTING, EnchantmentTarget.ARMOR_FEET)
                .add(Enchantments.THORNS);
            this.getOrCreateTagBuilder(EnchantmentTags.LEGGINGS_ENCHANTING)
                .addTag(EnchantmentTags.ARMOR)
                .add(getAll(registry, EnchantmentTarget.ARMOR_LEGS, false));
            this.forgingTag(registry, EnchantmentTags.LEGGINGS_FORGING, EnchantmentTags.LEGGINGS_ENCHANTING, EnchantmentTarget.ARMOR_LEGS)
                .add(Enchantments.THORNS);
            this.getOrCreateTagBuilder(EnchantmentTags.CHESTPLATE_ENCHANTING)
                .addTag(EnchantmentTags.ARMOR)
                .add(getAll(registry, EnchantmentTarget.ARMOR_CHEST, false));
            this.forgingTag(registry, EnchantmentTags.CHESTPLATE_FORGING, EnchantmentTags.CHESTPLATE_ENCHANTING, EnchantmentTarget.ARMOR_CHEST);
            this.getOrCreateTagBuilder(EnchantmentTags.HELMET_ENCHANTING)
                .addTag(EnchantmentTags.ARMOR)
                .add(getAll(registry, EnchantmentTarget.ARMOR_HEAD, false));
            this.forgingTag(registry, EnchantmentTags.HELMET_FORGING, EnchantmentTags.HELMET_ENCHANTING, EnchantmentTarget.ARMOR_HEAD)
                .add(Enchantments.THORNS);
            this.getOrCreateTagBuilder(EnchantmentTags.SWORD_ENCHANTING)
                .add(getAll(registry, EnchantmentTarget.WEAPON, false));
            this.forgingTag(registry, EnchantmentTags.SWORD_FORGING, EnchantmentTags.SWORD_ENCHANTING, EnchantmentTarget.WEAPON);
            this.getOrCreateTagBuilder(EnchantmentTags.SHOVEL_ENCHANTING)
                .add(getAll(registry, EnchantmentTarget.DIGGER, false));
            this.forgingTag(registry, EnchantmentTags.SHOVEL_FORGING, EnchantmentTags.SHOVEL_ENCHANTING, EnchantmentTarget.DIGGER);
            this.getOrCreateTagBuilder(EnchantmentTags.PICKAXE_ENCHANTING)
                .add(getAll(registry, EnchantmentTarget.DIGGER, false));
            this.forgingTag(registry, EnchantmentTags.PICKAXE_FORGING, EnchantmentTags.PICKAXE_ENCHANTING, EnchantmentTarget.DIGGER);
            this.getOrCreateTagBuilder(EnchantmentTags.AXE_ENCHANTING)
                .add(getAll(registry, EnchantmentTarget.DIGGER, false));
            this.forgingTag(registry, EnchantmentTags.AXE_FORGING, EnchantmentTags.AXE_ENCHANTING, EnchantmentTarget.DIGGER)
                .add(Enchantments.BANE_OF_ARTHROPODS, Enchantments.SHARPNESS, Enchantments.SMITE);
            this.getOrCreateTagBuilder(EnchantmentTags.HOE_ENCHANTING)
                .add(getAll(registry, EnchantmentTarget.DIGGER, false));
            this.forgingTag(registry, EnchantmentTags.HOE_FORGING, EnchantmentTags.HOE_ENCHANTING, EnchantmentTarget.DIGGER);
            this.getOrCreateTagBuilder(EnchantmentTags.BOW_ENCHANTING)
                .add(getAll(registry, EnchantmentTarget.BOW, false));
            this.forgingTag(registry, EnchantmentTags.BOW_FORGING, EnchantmentTags.BOW_ENCHANTING, EnchantmentTarget.BOW);
            this.getOrCreateTagBuilder(EnchantmentTags.CROSSBOW_ENCHANTING)
                .add(getAll(registry, EnchantmentTarget.CROSSBOW, false));
            this.forgingTag(registry, EnchantmentTags.CROSSBOW_FORGING, EnchantmentTags.CROSSBOW_ENCHANTING, EnchantmentTarget.CROSSBOW);
        }

        private FabricTagProvider<Enchantment>.FabricTagBuilder forgingTag(RegistryWrapper.Impl<Enchantment> registry, TagKey<Enchantment> tag, TagKey<Enchantment> enchantingTag, EnchantmentTarget enchantmentTarget) {
            return this.getOrCreateTagBuilder(tag)
                .addTag(enchantingTag)
                .addTag(EnchantmentTags.CURSED)
                .add(getAll(registry, enchantmentTarget, true));
        }

        private static Identifier[] getAll(RegistryWrapper.Impl<Enchantment> registry, EnchantmentTarget enchantmentTarget, boolean treasure) {
            return getAll(registry, enchantment -> isTargetable(enchantment, enchantmentTarget) && enchantment.isTreasure() == treasure);
        }

        private static Identifier[] getAll(RegistryWrapper.Impl<Enchantment> registry, Predicate<Enchantment> predicate) {
            return registry.streamEntries()
                .filter(entry -> predicate.test(entry.value()))
                .map(RegistryEntry.Reference::registryKey)
                .map(RegistryKey::getValue)
                .toArray(Identifier[]::new);
        }

        private static boolean isTargetable(Enchantment enchantment, EnchantmentTarget enchantmentTarget) {
            return enchantment.target == enchantmentTarget || enchantment.target == EnchantmentTarget.BREAKABLE;
        }
    }

    private static class ItemTagProvider extends FabricTagProvider<Item> {
        public ItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, RegistryKeys.ITEM, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup arg) {
            this.getOrCreateTagBuilder(ItemTagsUtil.REPAIRS_LEATHER_ARMOR)
                .add(ItemKeys.LEATHER);
            this.getOrCreateTagBuilder(ItemTagsUtil.REPAIRS_CHAINMAIL_ARMOR)
                .add(ItemKeys.IRON_INGOT);
            this.getOrCreateTagBuilder(ItemTagsUtil.REPAIRS_IRON_ARMOR)
                .add(ItemKeys.IRON_INGOT);
            this.getOrCreateTagBuilder(ItemTagsUtil.REPAIRS_GOLDEN_ARMOR)
                .add(ItemKeys.GOLD_INGOT);
            this.getOrCreateTagBuilder(ItemTagsUtil.REPAIRS_DIAMOND_ARMOR)
                .add(ItemKeys.DIAMOND);
            this.getOrCreateTagBuilder(ItemTagsUtil.REPAIRS_NETHERITE_ARMOR)
                .add(ItemKeys.NETHERITE_INGOT);
            this.getOrCreateTagBuilder(ItemTagsUtil.REPAIRS_TURTLE_ARMOR)
                .add(ItemKeys.SCUTE);
            this.getOrCreateTagBuilder(ItemTagsUtil.REPAIRS_WOODEN_TOOL)
                .forceAddTag(ItemTags.PLANKS);
            this.getOrCreateTagBuilder(ItemTagsUtil.REPAIRS_STONE_TOOL)
                .forceAddTag(ItemTags.STONE_TOOL_MATERIALS);
            this.getOrCreateTagBuilder(ItemTagsUtil.REPAIRS_GOLDEN_TOOL)
                .add(ItemKeys.GOLD_INGOT);
            this.getOrCreateTagBuilder(ItemTagsUtil.REPAIRS_IRON_TOOL)
                .add(ItemKeys.IRON_INGOT);
            this.getOrCreateTagBuilder(ItemTagsUtil.REPAIRS_DIAMOND_TOOL)
                .add(ItemKeys.DIAMOND);
            this.getOrCreateTagBuilder(ItemTagsUtil.REPAIRS_NETHERITE_TOOL)
                .add(ItemKeys.NETHERITE_INGOT);
            this.getOrCreateTagBuilder(ItemTagsUtil.REPAIRS_SHIELD)
                .forceAddTag(ItemTags.PLANKS);
            this.getOrCreateTagBuilder(ItemTagsUtil.FURNACE_MINECART_FUEL)
                .add(ItemKeys.COAL, ItemKeys.CHARCOAL);
            this.getOrCreateTagBuilder(ItemTagsUtil.BOW_AMMUNITION)
                .forceAddTag(ItemTags.ARROWS);
            this.getOrCreateTagBuilder(ItemTagsUtil.CROSSBOW_AMMUNITION)
                .addTag(ItemTagsUtil.BOW_AMMUNITION)
                .add(ItemKeys.FIREWORK_ROCKET);
            this.getOrCreateTagBuilder(ItemTagsUtil.HORSE_BREEDING_ITEMS)
                .add(ItemKeys.GOLDEN_CARROT)
                .add(ItemKeys.GOLDEN_APPLE)
                .add(ItemKeys.ENCHANTED_GOLDEN_APPLE);
            this.getOrCreateTagBuilder(ItemTagsUtil.HORSE_TEMPTING_ITEMS)
                .add(ItemKeys.WHEAT)
                .add(ItemKeys.SUGAR)
                .add(ItemKeys.APPLE)
                .addTag(ItemTagsUtil.HORSE_BREEDING_ITEMS);
            this.getOrCreateTagBuilder(ItemTagsUtil.CAT_BREEDING_ITEMS)
                .add(ItemKeys.COD)
                .add(ItemKeys.SALMON);
            this.getOrCreateTagBuilder(ItemTagsUtil.CAT_TEMPTING_ITEMS)
                .addTag(ItemTagsUtil.CAT_BREEDING_ITEMS);
            this.getOrCreateTagBuilder(ItemTagsUtil.OCELOT_BREEDING_ITEMS)
                .add(ItemKeys.COD)
                .add(ItemKeys.SALMON);
            this.getOrCreateTagBuilder(ItemTagsUtil.OCELOT_TEMPTING_ITEMS)
                .addTag(ItemTagsUtil.OCELOT_BREEDING_ITEMS);
            this.getOrCreateTagBuilder(ItemTagsUtil.PIG_BREEDING_ITEMS)
                .add(ItemKeys.CARROT)
                .add(ItemKeys.POTATO)
                .add(ItemKeys.BEETROOT);
            this.getOrCreateTagBuilder(ItemTagsUtil.PIG_TEMPTING_ITEMS)
                .addTag(ItemTagsUtil.PIG_BREEDING_ITEMS);
            this.getOrCreateTagBuilder(ItemTagsUtil.RABBIT_BREEDING_ITEMS)
                .add(ItemKeys.CARROT)
                .add(ItemKeys.GOLDEN_CARROT);
            this.getOrCreateTagBuilder(ItemTagsUtil.CHICKEN_BREEDING_ITEMS)
                .add(ItemKeys.WHEAT_SEEDS)
                .add(ItemKeys.MELON_SEEDS)
                .add(ItemKeys.PUMPKIN_SEEDS)
                .add(ItemKeys.BEETROOT_SEEDS)
                .add(ItemKeys.TORCHFLOWER_SEEDS)
                .add(ItemKeys.PITCHER_POD);
            this.getOrCreateTagBuilder(ItemTagsUtil.CHICKEN_TEMPTING_ITEMS)
                .addTag(ItemTagsUtil.CHICKEN_BREEDING_ITEMS);
            this.getOrCreateTagBuilder(ItemTagsUtil.PARROT_TAMING_ITEMS)
                .add(ItemKeys.WHEAT_SEEDS)
                .add(ItemKeys.MELON_SEEDS)
                .add(ItemKeys.PUMPKIN_SEEDS)
                .add(ItemKeys.BEETROOT_SEEDS)
                .add(ItemKeys.TORCHFLOWER_SEEDS)
                .add(ItemKeys.PITCHER_POD);
            this.getOrCreateTagBuilder(ItemTagsUtil.COW_TEMPTING_ITEMS)
                .add(ItemKeys.WHEAT);
            this.getOrCreateTagBuilder(ItemTagsUtil.SHEEP_TEMPTING_ITEMS)
                .add(ItemKeys.WHEAT);
            this.getOrCreateTagBuilder(ItemTagsUtil.LLAMA_BREEDING_ITEMS)
                .add(ItemKeys.WHEAT);
            this.getOrCreateTagBuilder(ItemTagsUtil.VILLAGER_GATHERABLE_ITEMS)
                .add(ItemKeys.BREAD)
                .add(ItemKeys.POTATO)
                .add(ItemKeys.CARROT)
                .add(ItemKeys.WHEAT)
                .add(ItemKeys.WHEAT_SEEDS)
                .add(ItemKeys.BEETROOT)
                .add(ItemKeys.BEETROOT_SEEDS)
                .add(ItemKeys.TORCHFLOWER_SEEDS)
                .add(ItemKeys.PITCHER_POD);
            this.getOrCreateTagBuilder(ItemTagsUtil.FARMER_VILLAGER_GATHERABLE_ITEMS)
                .add(ItemKeys.WHEAT)
                .add(ItemKeys.WHEAT_SEEDS)
                .add(ItemKeys.BEETROOT_SEEDS)
                .add(ItemKeys.BONE_MEAL);
            this.getOrCreateTagBuilder(ItemTagsUtil.OAK_BUILDING_BLOCKS)
                .add(ItemKeys.OAK_LOG)
                .add(ItemKeys.OAK_WOOD)
                .add(ItemKeys.STRIPPED_OAK_LOG)
                .add(ItemKeys.STRIPPED_OAK_WOOD)
                .add(ItemKeys.OAK_PLANKS)
                .add(ItemKeys.OAK_STAIRS)
                .add(ItemKeys.OAK_SLAB)
                .add(ItemKeys.OAK_FENCE)
                .add(ItemKeys.OAK_FENCE_GATE)
                .add(ItemKeys.OAK_DOOR)
                .add(ItemKeys.OAK_TRAPDOOR)
                .add(ItemKeys.OAK_PRESSURE_PLATE)
                .add(ItemKeys.OAK_BUTTON);
            this.getOrCreateTagBuilder(ItemTagsUtil.SPRUCE_BUILDING_BLOCKS)
                .add(ItemKeys.SPRUCE_LOG)
                .add(ItemKeys.SPRUCE_WOOD)
                .add(ItemKeys.STRIPPED_SPRUCE_LOG)
                .add(ItemKeys.STRIPPED_SPRUCE_WOOD)
                .add(ItemKeys.SPRUCE_PLANKS)
                .add(ItemKeys.SPRUCE_STAIRS)
                .add(ItemKeys.SPRUCE_SLAB)
                .add(ItemKeys.SPRUCE_FENCE)
                .add(ItemKeys.SPRUCE_FENCE_GATE)
                .add(ItemKeys.SPRUCE_DOOR)
                .add(ItemKeys.SPRUCE_TRAPDOOR)
                .add(ItemKeys.SPRUCE_PRESSURE_PLATE)
                .add(ItemKeys.SPRUCE_BUTTON);
            this.getOrCreateTagBuilder(ItemTagsUtil.BIRCH_BUILDING_BLOCKS)
                .add(ItemKeys.BIRCH_LOG)
                .add(ItemKeys.BIRCH_WOOD)
                .add(ItemKeys.STRIPPED_BIRCH_LOG)
                .add(ItemKeys.STRIPPED_BIRCH_WOOD)
                .add(ItemKeys.BIRCH_PLANKS)
                .add(ItemKeys.BIRCH_STAIRS)
                .add(ItemKeys.BIRCH_SLAB)
                .add(ItemKeys.BIRCH_FENCE)
                .add(ItemKeys.BIRCH_FENCE_GATE)
                .add(ItemKeys.BIRCH_DOOR)
                .add(ItemKeys.BIRCH_TRAPDOOR)
                .add(ItemKeys.BIRCH_PRESSURE_PLATE)
                .add(ItemKeys.BIRCH_BUTTON);
            this.getOrCreateTagBuilder(ItemTagsUtil.JUNGLE_BUILDING_BLOCKS)
                .add(ItemKeys.JUNGLE_LOG)
                .add(ItemKeys.JUNGLE_WOOD)
                .add(ItemKeys.STRIPPED_JUNGLE_LOG)
                .add(ItemKeys.STRIPPED_JUNGLE_WOOD)
                .add(ItemKeys.JUNGLE_PLANKS)
                .add(ItemKeys.JUNGLE_STAIRS)
                .add(ItemKeys.JUNGLE_SLAB)
                .add(ItemKeys.JUNGLE_FENCE)
                .add(ItemKeys.JUNGLE_FENCE_GATE)
                .add(ItemKeys.JUNGLE_DOOR)
                .add(ItemKeys.JUNGLE_TRAPDOOR)
                .add(ItemKeys.JUNGLE_PRESSURE_PLATE)
                .add(ItemKeys.JUNGLE_BUTTON);
            this.getOrCreateTagBuilder(ItemTagsUtil.ACACIA_BUILDING_BLOCKS)
                .add(ItemKeys.ACACIA_LOG)
                .add(ItemKeys.ACACIA_WOOD)
                .add(ItemKeys.STRIPPED_ACACIA_LOG)
                .add(ItemKeys.STRIPPED_ACACIA_WOOD)
                .add(ItemKeys.ACACIA_PLANKS)
                .add(ItemKeys.ACACIA_STAIRS)
                .add(ItemKeys.ACACIA_SLAB)
                .add(ItemKeys.ACACIA_FENCE)
                .add(ItemKeys.ACACIA_FENCE_GATE)
                .add(ItemKeys.ACACIA_DOOR)
                .add(ItemKeys.ACACIA_TRAPDOOR)
                .add(ItemKeys.ACACIA_PRESSURE_PLATE)
                .add(ItemKeys.ACACIA_BUTTON);
            this.getOrCreateTagBuilder(ItemTagsUtil.DARK_OAK_BUILDING_BLOCKS)
                .add(ItemKeys.DARK_OAK_LOG)
                .add(ItemKeys.DARK_OAK_WOOD)
                .add(ItemKeys.STRIPPED_DARK_OAK_LOG)
                .add(ItemKeys.STRIPPED_DARK_OAK_WOOD)
                .add(ItemKeys.DARK_OAK_PLANKS)
                .add(ItemKeys.DARK_OAK_STAIRS)
                .add(ItemKeys.DARK_OAK_SLAB)
                .add(ItemKeys.DARK_OAK_FENCE)
                .add(ItemKeys.DARK_OAK_FENCE_GATE)
                .add(ItemKeys.DARK_OAK_DOOR)
                .add(ItemKeys.DARK_OAK_TRAPDOOR)
                .add(ItemKeys.DARK_OAK_PRESSURE_PLATE)
                .add(ItemKeys.DARK_OAK_BUTTON);
            this.getOrCreateTagBuilder(ItemTagsUtil.MANGROVE_BUILDING_BLOCKS)
                .add(ItemKeys.MANGROVE_LOG)
                .add(ItemKeys.MANGROVE_WOOD)
                .add(ItemKeys.STRIPPED_MANGROVE_LOG)
                .add(ItemKeys.STRIPPED_MANGROVE_WOOD)
                .add(ItemKeys.MANGROVE_PLANKS)
                .add(ItemKeys.MANGROVE_STAIRS)
                .add(ItemKeys.MANGROVE_SLAB)
                .add(ItemKeys.MANGROVE_FENCE)
                .add(ItemKeys.MANGROVE_FENCE_GATE)
                .add(ItemKeys.MANGROVE_DOOR)
                .add(ItemKeys.MANGROVE_TRAPDOOR)
                .add(ItemKeys.MANGROVE_PRESSURE_PLATE)
                .add(ItemKeys.MANGROVE_BUTTON);
            this.getOrCreateTagBuilder(ItemTagsUtil.CHERRY_BUILDING_BLOCKS)
                .add(ItemKeys.CHERRY_LOG)
                .add(ItemKeys.CHERRY_WOOD)
                .add(ItemKeys.STRIPPED_CHERRY_LOG)
                .add(ItemKeys.STRIPPED_CHERRY_WOOD)
                .add(ItemKeys.CHERRY_PLANKS)
                .add(ItemKeys.CHERRY_STAIRS)
                .add(ItemKeys.CHERRY_SLAB)
                .add(ItemKeys.CHERRY_FENCE)
                .add(ItemKeys.CHERRY_FENCE_GATE)
                .add(ItemKeys.CHERRY_DOOR)
                .add(ItemKeys.CHERRY_TRAPDOOR)
                .add(ItemKeys.CHERRY_PRESSURE_PLATE)
                .add(ItemKeys.CHERRY_BUTTON);
            this.getOrCreateTagBuilder(ItemTagsUtil.BAMBOO_BUILDING_BLOCKS)
                .add(ItemKeys.BAMBOO_BLOCK)
                .add(ItemKeys.STRIPPED_BAMBOO_BLOCK)
                .add(ItemKeys.BAMBOO_PLANKS)
                .add(ItemKeys.BAMBOO_MOSAIC)
                .add(ItemKeys.BAMBOO_STAIRS)
                .add(ItemKeys.BAMBOO_MOSAIC_STAIRS)
                .add(ItemKeys.BAMBOO_SLAB)
                .add(ItemKeys.BAMBOO_MOSAIC_SLAB)
                .add(ItemKeys.BAMBOO_FENCE)
                .add(ItemKeys.BAMBOO_FENCE_GATE)
                .add(ItemKeys.BAMBOO_DOOR)
                .add(ItemKeys.BAMBOO_TRAPDOOR)
                .add(ItemKeys.BAMBOO_PRESSURE_PLATE)
                .add(ItemKeys.BAMBOO_BUTTON);
            this.getOrCreateTagBuilder(ItemTagsUtil.WOODEN_BUILDING_BLOCKS)
                .addTag(ItemTagsUtil.OAK_BUILDING_BLOCKS)
                .addTag(ItemTagsUtil.SPRUCE_BUILDING_BLOCKS)
                .addTag(ItemTagsUtil.BIRCH_BUILDING_BLOCKS)
                .addTag(ItemTagsUtil.JUNGLE_BUILDING_BLOCKS)
                .addTag(ItemTagsUtil.ACACIA_BUILDING_BLOCKS)
                .addTag(ItemTagsUtil.DARK_OAK_BUILDING_BLOCKS)
                .addTag(ItemTagsUtil.MANGROVE_BUILDING_BLOCKS)
                .addTag(ItemTagsUtil.CHERRY_BUILDING_BLOCKS)
                .addTag(ItemTagsUtil.BAMBOO_BUILDING_BLOCKS);
            this.getOrCreateTagBuilder(ItemTagsUtil.WOOL)
                .add(ItemKeys.WHITE_WOOL)
                .add(ItemKeys.LIGHT_GRAY_WOOL)
                .add(ItemKeys.GRAY_WOOL)
                .add(ItemKeys.BLACK_WOOL)
                .add(ItemKeys.BROWN_WOOL)
                .add(ItemKeys.RED_WOOL)
                .add(ItemKeys.ORANGE_WOOL)
                .add(ItemKeys.YELLOW_WOOL)
                .add(ItemKeys.LIME_WOOL)
                .add(ItemKeys.GREEN_WOOL)
                .add(ItemKeys.CYAN_WOOL)
                .add(ItemKeys.LIGHT_BLUE_WOOL)
                .add(ItemKeys.BLUE_WOOL)
                .add(ItemKeys.PURPLE_WOOL)
                .add(ItemKeys.MAGENTA_WOOL)
                .add(ItemKeys.PINK_WOOL);
            this.getOrCreateTagBuilder(ItemTagsUtil.WOOL_CARPETS)
                .add(ItemKeys.WHITE_CARPET)
                .add(ItemKeys.LIGHT_GRAY_CARPET)
                .add(ItemKeys.GRAY_CARPET)
                .add(ItemKeys.BLACK_CARPET)
                .add(ItemKeys.BROWN_CARPET)
                .add(ItemKeys.RED_CARPET)
                .add(ItemKeys.ORANGE_CARPET)
                .add(ItemKeys.YELLOW_CARPET)
                .add(ItemKeys.LIME_CARPET)
                .add(ItemKeys.GREEN_CARPET)
                .add(ItemKeys.CYAN_CARPET)
                .add(ItemKeys.LIGHT_BLUE_CARPET)
                .add(ItemKeys.BLUE_CARPET)
                .add(ItemKeys.PURPLE_CARPET)
                .add(ItemKeys.MAGENTA_CARPET)
                .add(ItemKeys.PINK_CARPET);
            this.getOrCreateTagBuilder(ItemTagsUtil.BANNERS)
                .add(ItemKeys.WHITE_BANNER)
                .add(ItemKeys.LIGHT_GRAY_BANNER)
                .add(ItemKeys.GRAY_BANNER)
                .add(ItemKeys.BLACK_BANNER)
                .add(ItemKeys.BROWN_BANNER)
                .add(ItemKeys.RED_BANNER)
                .add(ItemKeys.ORANGE_BANNER)
                .add(ItemKeys.YELLOW_BANNER)
                .add(ItemKeys.LIME_BANNER)
                .add(ItemKeys.GREEN_BANNER)
                .add(ItemKeys.CYAN_BANNER)
                .add(ItemKeys.LIGHT_BLUE_BANNER)
                .add(ItemKeys.BLUE_BANNER)
                .add(ItemKeys.PURPLE_BANNER)
                .add(ItemKeys.MAGENTA_BANNER)
                .add(ItemKeys.PINK_BANNER);
            this.getOrCreateTagBuilder(ItemTagsUtil.WOOD_BLOCKS)
                .add(ItemKeys.OAK_LOG)
                .add(ItemKeys.SPRUCE_LOG)
                .add(ItemKeys.BIRCH_LOG)
                .add(ItemKeys.JUNGLE_LOG)
                .add(ItemKeys.ACACIA_LOG)
                .add(ItemKeys.DARK_OAK_LOG)
                .add(ItemKeys.MANGROVE_LOG)
                .add(ItemKeys.MANGROVE_ROOTS)
                .add(ItemKeys.CHERRY_LOG);
            this.getOrCreateTagBuilder(ItemTagsUtil.LEAVES)
                .add(ItemKeys.OAK_LEAVES)
                .add(ItemKeys.SPRUCE_LEAVES)
                .add(ItemKeys.BIRCH_LEAVES)
                .add(ItemKeys.JUNGLE_LEAVES)
                .add(ItemKeys.ACACIA_LEAVES)
                .add(ItemKeys.DARK_OAK_LEAVES)
                .add(ItemKeys.MANGROVE_LEAVES);
            this.getOrCreateTagBuilder(ItemTagsUtil.SAPLINGS)
                .add(ItemKeys.OAK_SAPLING)
                .add(ItemKeys.SPRUCE_SAPLING)
                .add(ItemKeys.BIRCH_SAPLING)
                .add(ItemKeys.JUNGLE_SAPLING)
                .add(ItemKeys.ACACIA_SAPLING)
                .add(ItemKeys.DARK_OAK_SAPLING)
                .add(ItemKeys.MANGROVE_PROPAGULE)
                .add(ItemKeys.CHERRY_SAPLING);
            this.getOrCreateTagBuilder(ItemTagsUtil.PLANTS)
                .add(ItemKeys.AZALEA)
                .add(ItemKeys.FLOWERING_AZALEA)
                .add(ItemKeys.GRASS)
                .add(ItemKeys.FERN)
                .add(ItemKeys.DEAD_BUSH)
                .add(ItemKeys.BAMBOO)
                .add(ItemKeys.VINE)
                .add(ItemKeys.TALL_GRASS)
                .add(ItemKeys.LARGE_FERN);
            this.getOrCreateTagBuilder(ItemTagsUtil.SEEDS)
                .add(ItemKeys.WHEAT_SEEDS)
                .add(ItemKeys.PUMPKIN_SEEDS)
                .add(ItemKeys.MELON_SEEDS)
                .add(ItemKeys.BEETROOT_SEEDS)
                .add(ItemKeys.TORCHFLOWER_SEEDS)
                .add(ItemKeys.PITCHER_POD)
                .add(ItemKeys.GLOW_BERRIES)
                .add(ItemKeys.SWEET_BERRIES)
                .add(ItemKeys.NETHER_WART);
            this.getOrCreateTagBuilder(ItemTagsUtil.SIGNS)
                .add(ItemKeys.OAK_SIGN)
                .add(ItemKeys.OAK_HANGING_SIGN)
                .add(ItemKeys.SPRUCE_SIGN)
                .add(ItemKeys.SPRUCE_HANGING_SIGN)
                .add(ItemKeys.BIRCH_SIGN)
                .add(ItemKeys.BIRCH_HANGING_SIGN)
                .add(ItemKeys.JUNGLE_SIGN)
                .add(ItemKeys.JUNGLE_HANGING_SIGN)
                .add(ItemKeys.ACACIA_SIGN)
                .add(ItemKeys.ACACIA_HANGING_SIGN)
                .add(ItemKeys.DARK_OAK_SIGN)
                .add(ItemKeys.DARK_OAK_HANGING_SIGN)
                .add(ItemKeys.MANGROVE_SIGN)
                .add(ItemKeys.MANGROVE_HANGING_SIGN)
                .add(ItemKeys.CHERRY_SIGN)
                .add(ItemKeys.CHERRY_HANGING_SIGN)
                .add(ItemKeys.BAMBOO_SIGN)
                .add(ItemKeys.BAMBOO_HANGING_SIGN);
            this.getOrCreateTagBuilder(ItemTagsUtil.HEADS)
                .add(ItemKeys.SKELETON_SKULL)
                .add(ItemKeys.WITHER_SKELETON_SKULL)
                .add(ItemKeys.PLAYER_HEAD)
                .add(ItemKeys.ZOMBIE_HEAD)
                .add(ItemKeys.CREEPER_HEAD)
                .add(ItemKeys.PIGLIN_HEAD)
                .add(ItemKeys.DRAGON_HEAD);
            this.getOrCreateTagBuilder(ItemTagsUtil.MINECARTS)
                .add(ItemKeys.MINECART)
                .add(ItemKeys.HOPPER_MINECART)
                .add(ItemKeys.CHEST_MINECART)
                .add(ItemKeys.FURNACE_MINECART)
                .add(ItemKeys.TNT_MINECART);
            this.getOrCreateTagBuilder(ItemTagsUtil.TOOLS)
                .add(ItemKeys.WOODEN_SHOVEL)
                .add(ItemKeys.WOODEN_PICKAXE)
                .add(ItemKeys.WOODEN_AXE)
                .add(ItemKeys.WOODEN_HOE)
                .add(ItemKeys.STONE_SHOVEL)
                .add(ItemKeys.STONE_PICKAXE)
                .add(ItemKeys.STONE_AXE)
                .add(ItemKeys.STONE_HOE)
                .add(ItemKeys.IRON_SHOVEL)
                .add(ItemKeys.IRON_PICKAXE)
                .add(ItemKeys.IRON_AXE)
                .add(ItemKeys.IRON_HOE)
                .add(ItemKeys.GOLDEN_SHOVEL)
                .add(ItemKeys.GOLDEN_PICKAXE)
                .add(ItemKeys.GOLDEN_AXE)
                .add(ItemKeys.GOLDEN_HOE)
                .add(ItemKeys.DIAMOND_SHOVEL)
                .add(ItemKeys.DIAMOND_PICKAXE)
                .add(ItemKeys.DIAMOND_AXE)
                .add(ItemKeys.DIAMOND_HOE)
                .add(ItemKeys.NETHERITE_SHOVEL)
                .add(ItemKeys.NETHERITE_PICKAXE)
                .add(ItemKeys.NETHERITE_AXE)
                .add(ItemKeys.NETHERITE_HOE);
            this.getOrCreateTagBuilder(ItemTagsUtil.BUCKETS)
                .add(ItemKeys.BUCKET)
                .add(ItemKeys.WATER_BUCKET)
                .add(ItemKeys.COD_BUCKET)
                .add(ItemKeys.SALMON_BUCKET)
                .add(ItemKeys.TROPICAL_FISH_BUCKET)
                .add(ItemKeys.PUFFERFISH_BUCKET)
                .add(ItemKeys.AXOLOTL_BUCKET)
                .add(ItemKeys.TADPOLE_BUCKET)
                .add(ItemKeys.LAVA_BUCKET)
                .add(ItemKeys.POWDER_SNOW_BUCKET)
                .add(ItemKeys.MILK_BUCKET);
            this.getOrCreateTagBuilder(ItemTagsUtil.BOATS)
                .add(ItemKeys.OAK_BOAT)
                .add(ItemKeys.OAK_CHEST_BOAT)
                .add(ItemKeys.SPRUCE_BOAT)
                .add(ItemKeys.SPRUCE_CHEST_BOAT)
                .add(ItemKeys.BIRCH_BOAT)
                .add(ItemKeys.BIRCH_CHEST_BOAT)
                .add(ItemKeys.JUNGLE_BOAT)
                .add(ItemKeys.JUNGLE_CHEST_BOAT)
                .add(ItemKeys.ACACIA_BOAT)
                .add(ItemKeys.ACACIA_CHEST_BOAT)
                .add(ItemKeys.DARK_OAK_BOAT)
                .add(ItemKeys.DARK_OAK_CHEST_BOAT)
                .add(ItemKeys.MANGROVE_BOAT)
                .add(ItemKeys.MANGROVE_CHEST_BOAT)
                .add(ItemKeys.CHERRY_BOAT)
                .add(ItemKeys.CHERRY_CHEST_BOAT)
                .add(ItemKeys.BAMBOO_RAFT)
                .add(ItemKeys.BAMBOO_CHEST_RAFT);
            this.getOrCreateTagBuilder(ItemTagsUtil.MUSIC_DISCS)
                .add(ItemKeys.MUSIC_DISC_13)
                .add(ItemKeys.MUSIC_DISC_CAT)
                .add(ItemKeys.MUSIC_DISC_BLOCKS)
                .add(ItemKeys.MUSIC_DISC_CHIRP)
                .add(ItemKeys.MUSIC_DISC_FAR)
                .add(ItemKeys.MUSIC_DISC_MALL)
                .add(ItemKeys.MUSIC_DISC_MELLOHI)
                .add(ItemKeys.MUSIC_DISC_STAL)
                .add(ItemKeys.MUSIC_DISC_STRAD)
                .add(ItemKeys.MUSIC_DISC_WARD)
                .add(ItemKeys.MUSIC_DISC_11)
                .add(ItemKeys.MUSIC_DISC_WAIT)
                .add(ItemKeys.MUSIC_DISC_OTHERSIDE)
                .add(ItemKeys.MUSIC_DISC_RELIC)
                .add(ItemKeys.MUSIC_DISC_5)
                .add(ItemKeys.MUSIC_DISC_PIGSTEP);
            this.getOrCreateTagBuilder(ItemTagsUtil.SWORDS)
                .add(ItemKeys.WOODEN_SWORD)
                .add(ItemKeys.STONE_SWORD)
                .add(ItemKeys.IRON_SWORD)
                .add(ItemKeys.GOLDEN_SWORD)
                .add(ItemKeys.DIAMOND_SWORD)
                .add(ItemKeys.NETHERITE_SWORD);
            this.getOrCreateTagBuilder(ItemTagsUtil.AXES)
                .add(ItemKeys.WOODEN_AXE)
                .add(ItemKeys.STONE_AXE)
                .add(ItemKeys.IRON_AXE)
                .add(ItemKeys.GOLDEN_AXE)
                .add(ItemKeys.DIAMOND_AXE)
                .add(ItemKeys.NETHERITE_AXE);
            this.getOrCreateTagBuilder(ItemTagsUtil.ARMOR)
                .add(ItemKeys.LEATHER_HELMET)
                .add(ItemKeys.LEATHER_CHESTPLATE)
                .add(ItemKeys.LEATHER_LEGGINGS)
                .add(ItemKeys.LEATHER_BOOTS)
                .add(ItemKeys.CHAINMAIL_HELMET)
                .add(ItemKeys.CHAINMAIL_CHESTPLATE)
                .add(ItemKeys.CHAINMAIL_LEGGINGS)
                .add(ItemKeys.CHAINMAIL_BOOTS)
                .add(ItemKeys.IRON_HELMET)
                .add(ItemKeys.IRON_CHESTPLATE)
                .add(ItemKeys.IRON_LEGGINGS)
                .add(ItemKeys.IRON_BOOTS)
                .add(ItemKeys.GOLDEN_HELMET)
                .add(ItemKeys.GOLDEN_CHESTPLATE)
                .add(ItemKeys.GOLDEN_LEGGINGS)
                .add(ItemKeys.GOLDEN_BOOTS)
                .add(ItemKeys.DIAMOND_HELMET)
                .add(ItemKeys.DIAMOND_CHESTPLATE)
                .add(ItemKeys.DIAMOND_LEGGINGS)
                .add(ItemKeys.DIAMOND_BOOTS)
                .add(ItemKeys.NETHERITE_HELMET)
                .add(ItemKeys.NETHERITE_CHESTPLATE)
                .add(ItemKeys.NETHERITE_LEGGINGS)
                .add(ItemKeys.NETHERITE_BOOTS)
                .add(ItemKeys.TURTLE_HELMET);
            this.getOrCreateTagBuilder(ItemTagsUtil.FOOD)
                .add(ItemKeys.APPLE)
                .add(ItemKeys.GOLDEN_APPLE)
                .add(ItemKeys.ENCHANTED_GOLDEN_APPLE)
                .add(ItemKeys.MELON_SLICE)
                .add(ItemKeys.SWEET_BERRIES)
                .add(ItemKeys.GLOW_BERRIES)
                .add(ItemKeys.CHORUS_FRUIT)
                .add(ItemKeys.CARROT)
                .add(ItemKeys.GOLDEN_CARROT)
                .add(ItemKeys.POTATO)
                .add(ItemKeys.BAKED_POTATO)
                .add(ItemKeys.POISONOUS_POTATO)
                .add(ItemKeys.BEETROOT)
                .add(ItemKeys.DRIED_KELP)
                .add(ItemKeys.BEEF)
                .add(ItemKeys.COOKED_BEEF)
                .add(ItemKeys.PORKCHOP)
                .add(ItemKeys.COOKED_PORKCHOP)
                .add(ItemKeys.MUTTON)
                .add(ItemKeys.COOKED_MUTTON)
                .add(ItemKeys.CHICKEN)
                .add(ItemKeys.COOKED_CHICKEN)
                .add(ItemKeys.RABBIT)
                .add(ItemKeys.COOKED_RABBIT)
                .add(ItemKeys.COD)
                .add(ItemKeys.COOKED_COD)
                .add(ItemKeys.SALMON)
                .add(ItemKeys.COOKED_SALMON)
                .add(ItemKeys.TROPICAL_FISH)
                .add(ItemKeys.PUFFERFISH)
                .add(ItemKeys.BREAD)
                .add(ItemKeys.COOKIE)
                .add(ItemKeys.PUMPKIN_PIE)
                .add(ItemKeys.ROTTEN_FLESH)
                .add(ItemKeys.SPIDER_EYE)
                .add(ItemKeys.MUSHROOM_STEW)
                .add(ItemKeys.BEETROOT_SOUP)
                .add(ItemKeys.RABBIT_STEW);
            this.getOrCreateTagBuilder(ItemTagsUtil.DYES)
                .add(ItemKeys.WHITE_DYE)
                .add(ItemKeys.LIGHT_GRAY_DYE)
                .add(ItemKeys.GRAY_DYE)
                .add(ItemKeys.BLACK_DYE)
                .add(ItemKeys.BROWN_DYE)
                .add(ItemKeys.RED_DYE)
                .add(ItemKeys.ORANGE_DYE)
                .add(ItemKeys.YELLOW_DYE)
                .add(ItemKeys.LIME_DYE)
                .add(ItemKeys.GREEN_DYE)
                .add(ItemKeys.CYAN_DYE)
                .add(ItemKeys.LIGHT_BLUE_DYE)
                .add(ItemKeys.BLUE_DYE)
                .add(ItemKeys.PURPLE_DYE)
                .add(ItemKeys.MAGENTA_DYE)
                .add(ItemKeys.PINK_DYE);
            this.getOrCreateTagBuilder(ItemTagsUtil.BREWING_INGREDIENTS)
                .add(ItemKeys.GLASS_BOTTLE)
                .add(ItemKeys.NETHER_WART)
                .add(ItemKeys.REDSTONE)
                .add(ItemKeys.GLOWSTONE_DUST)
                .add(ItemKeys.GUNPOWDER)
                .add(ItemKeys.DRAGON_BREATH)
                .add(ItemKeys.FERMENTED_SPIDER_EYE)
                .add(ItemKeys.BLAZE_POWDER)
                .add(ItemKeys.SUGAR)
                .add(ItemKeys.RABBIT_FOOT)
                .add(ItemKeys.GLISTERING_MELON_SLICE)
                .add(ItemKeys.SPIDER_EYE)
                .add(ItemKeys.PUFFERFISH)
                .add(ItemKeys.MAGMA_CREAM)
                .add(ItemKeys.GOLDEN_CARROT)
                .add(ItemKeys.GHAST_TEAR)
                .add(ItemKeys.TURTLE_HELMET)
                .add(ItemKeys.PHANTOM_MEMBRANE);
        }
    }

    private static class BlockTagProvider extends FabricTagProvider<Block> {
        public BlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, RegistryKeys.BLOCK, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup arg) {
            this.getOrCreateTagBuilder(ItematicBlockTags.END_CRYSTAL_SPAWNABLE_ON)
                .add(BlockKeys.BEDROCK)
                .add(BlockKeys.OBSIDIAN);
        }
    }

    private static class ItemGroupEntryProviderProvider extends FabricDynamicRegistryProvider {
        public ItemGroupEntryProviderProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
            addAll(entries, registries.getWrapperOrThrow(ItematicRegistryKeys.ITEM_GROUP_ENTRY_PROVIDER));
        }

        @Override
        public String getName() {
            return "Item Group Entry Providers";
        }
    }

    private static class ItemGroupEntryProviderTagProvider extends FabricTagProvider<ItemGroupEntryProvider> {
        public ItemGroupEntryProviderTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, ItematicRegistryKeys.ITEM_GROUP_ENTRY_PROVIDER, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup arg) {
            this.getOrCreateTagBuilder(ItemGroupEntryProviderTags.BUILDING_BLOCKS)
                .add(ItemGroupEntryProviderKeys.BUILDING_BLOCKS);
            this.getOrCreateTagBuilder(ItemGroupEntryProviderTags.COLORED_BLOCKS)
                .add(ItemGroupEntryProviderKeys.COLORED_BLOCKS);
            this.getOrCreateTagBuilder(ItemGroupEntryProviderTags.NATURAL_BLOCKS)
                .add(ItemGroupEntryProviderKeys.NATURAL_BLOCKS);
            this.getOrCreateTagBuilder(ItemGroupEntryProviderTags.FUNCTIONAL_BLOCKS)
                .add(ItemGroupEntryProviderKeys.FUNCTIONAL_BLOCKS);
            this.getOrCreateTagBuilder(ItemGroupEntryProviderTags.REDSTONE_BLOCKS)
                .add(ItemGroupEntryProviderKeys.REDSTONE_BLOCKS);
            this.getOrCreateTagBuilder(ItemGroupEntryProviderTags.TOOLS_AND_UTILITIES)
                .add(ItemGroupEntryProviderKeys.TOOLS_AND_UTILITIES);
            this.getOrCreateTagBuilder(ItemGroupEntryProviderTags.COMBAT)
                .add(ItemGroupEntryProviderKeys.COMBAT);
            this.getOrCreateTagBuilder(ItemGroupEntryProviderTags.FOOD_AND_DRINKS)
                .add(ItemGroupEntryProviderKeys.FOOD_AND_DRINKS);
            this.getOrCreateTagBuilder(ItemGroupEntryProviderTags.INGREDIENTS)
                .add(ItemGroupEntryProviderKeys.INGREDIENTS);
            this.getOrCreateTagBuilder(ItemGroupEntryProviderTags.SPAWN_EGGS)
                .add(ItemGroupEntryProviderKeys.SPAWN_EGGS);
            this.getOrCreateTagBuilder(ItemGroupEntryProviderTags.OP_BLOCKS)
                .add(ItemGroupEntryProviderKeys.OP_BLOCKS);
        }
    }
}
