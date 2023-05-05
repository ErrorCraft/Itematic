package net.errorcraft.itematic.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.block.entity.FurnaceBlockEntityUtil;
import net.errorcraft.itematic.enchantment.EnchantmentTags;
import net.errorcraft.itematic.entity.EntityTypeKeys;
import net.errorcraft.itematic.item.armor.ArmorMaterialKeys;
import net.errorcraft.itematic.item.armor.ArmorMaterials;
import net.errorcraft.itematic.item.component.ItemComponentSet;
import net.errorcraft.itematic.item.component.components.*;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.registry.*;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Identifier;

public class ItemUtil {
    public static final Codec<Item> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
        ItemBase.CODEC.fieldOf("base").forGetter(Item::getItemBase),
        ItemComponentSet.CODEC.fieldOf("components").forGetter(Item::getComponents)
    ).apply(instance, ItemUtil::create));

    public static void bootstrap(Registerable<Item> registerable) {
        RegistryEntryLookup<net.errorcraft.itematic.item.armor.ArmorMaterial> armorMaterials = registerable.getRegistryLookup(ArmorMaterials.ARMOR_MATERIAL_KEY);
        RegistryEntryLookup<Item> items = registerable.getRegistryLookup(RegistryKeys.ITEM);
        RegistryEntryLookup<EntityType<?>> entityTypes = registerable.getRegistryLookup(RegistryKeys.ENTITY_TYPE);

        registerable.register(ItemKeys.AIR, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.AIR).build())
        ));
        registerable.register(ItemKeys.STONE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.STONE).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.STONE))
                .build()
        ));
        registerable.register(ItemKeys.GRASS_BLOCK, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.GRASS_BLOCK).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.GRASS_BLOCK))
                .build()
        ));
        registerable.register(ItemKeys.COBBLESTONE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.COBBLESTONE).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.COBBLESTONE))
                .build()
        ));
        registerable.register(ItemKeys.OAK_PLANKS, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.OAK_PLANKS).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.OAK_PLANKS))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.SPRUCE_PLANKS, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.SPRUCE_PLANKS).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.SPRUCE_PLANKS))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BIRCH_PLANKS, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BIRCH_PLANKS).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.BIRCH_PLANKS))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.JUNGLE_PLANKS, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.JUNGLE_PLANKS).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.JUNGLE_PLANKS))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.ACACIA_PLANKS, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.ACACIA_PLANKS).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.ACACIA_PLANKS))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.CHERRY_PLANKS, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.CHERRY_PLANKS).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.CHERRY_PLANKS))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.DARK_OAK_PLANKS, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.DARK_OAK_PLANKS).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.DARK_OAK_PLANKS))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.MANGROVE_PLANKS, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.MANGROVE_PLANKS).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.MANGROVE_PLANKS))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BAMBOO_PLANKS, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BAMBOO_PLANKS).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.BAMBOO_PLANKS))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BAMBOO_MOSAIC, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BAMBOO_MOSAIC).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.BAMBOO_MOSAIC))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.OAK_SAPLING, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.OAK_SAPLING).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.OAK_SAPLING))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.PLANT_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.SPRUCE_SAPLING, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.SPRUCE_SAPLING).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.SPRUCE_SAPLING))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.PLANT_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BIRCH_SAPLING, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BIRCH_SAPLING).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.BIRCH_SAPLING))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.PLANT_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.JUNGLE_SAPLING, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.JUNGLE_SAPLING).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.JUNGLE_SAPLING))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.PLANT_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.ACACIA_SAPLING, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.ACACIA_SAPLING).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.ACACIA_SAPLING))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.PLANT_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.CHERRY_SAPLING, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.CHERRY_SAPLING).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.CHERRY_SAPLING))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.PLANT_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.DARK_OAK_SAPLING, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.DARK_OAK_SAPLING).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.DARK_OAK_SAPLING))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.PLANT_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.MANGROVE_PROPAGULE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.MANGROVE_PROPAGULE).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.MANGROVE_PROPAGULE))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.PLANT_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.SAND, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.SAND).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.SAND))
                .build()
        ));
        registerable.register(ItemKeys.COAL_BLOCK, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.COAL_BLOCK).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.COAL_BLOCK))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.COAL_BLOCK_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.OAK_LOG, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.OAK_LOG).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.OAK_LOG))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.SPRUCE_LOG, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.SPRUCE_LOG).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.SPRUCE_LOG))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BIRCH_LOG, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BIRCH_LOG).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.BIRCH_LOG))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.JUNGLE_LOG, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.JUNGLE_LOG).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.JUNGLE_LOG))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.ACACIA_LOG, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.ACACIA_LOG).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.ACACIA_LOG))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.CHERRY_LOG, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.CHERRY_LOG).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.CHERRY_LOG))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.DARK_OAK_LOG, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.DARK_OAK_LOG).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.DARK_OAK_LOG))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.MANGROVE_LOG, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.MANGROVE_LOG).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.MANGROVE_LOG))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.MANGROVE_ROOTS, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.MANGROVE_ROOTS).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.MANGROVE_ROOTS))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BAMBOO_BLOCK, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BAMBOO_BLOCK).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.BAMBOO_BLOCK))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.STRIPPED_OAK_LOG, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.STRIPPED_OAK_LOG).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.STRIPPED_OAK_LOG))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.STRIPPED_SPRUCE_LOG, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.STRIPPED_SPRUCE_LOG).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.STRIPPED_SPRUCE_LOG))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.STRIPPED_BIRCH_LOG, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.STRIPPED_BIRCH_LOG).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.STRIPPED_BIRCH_LOG))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.STRIPPED_JUNGLE_LOG, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.STRIPPED_JUNGLE_LOG).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.STRIPPED_JUNGLE_LOG))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.STRIPPED_ACACIA_LOG, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.STRIPPED_ACACIA_LOG).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.STRIPPED_ACACIA_LOG))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.STRIPPED_CHERRY_LOG, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.STRIPPED_CHERRY_LOG).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.STRIPPED_CHERRY_LOG))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.STRIPPED_DARK_OAK_LOG, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.STRIPPED_DARK_OAK_LOG).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.STRIPPED_DARK_OAK_LOG))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.STRIPPED_MANGROVE_LOG, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.STRIPPED_MANGROVE_LOG).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.STRIPPED_MANGROVE_LOG))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.STRIPPED_OAK_WOOD, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.STRIPPED_OAK_WOOD).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.STRIPPED_OAK_WOOD))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.STRIPPED_SPRUCE_WOOD, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.STRIPPED_SPRUCE_WOOD).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.STRIPPED_SPRUCE_WOOD))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.STRIPPED_BIRCH_WOOD, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.STRIPPED_BIRCH_WOOD).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.STRIPPED_BIRCH_WOOD))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.STRIPPED_JUNGLE_WOOD, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.STRIPPED_JUNGLE_WOOD).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.STRIPPED_JUNGLE_WOOD))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.STRIPPED_ACACIA_WOOD, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.STRIPPED_ACACIA_WOOD).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.STRIPPED_ACACIA_WOOD))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.STRIPPED_CHERRY_WOOD, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.STRIPPED_CHERRY_WOOD).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.STRIPPED_CHERRY_WOOD))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.STRIPPED_DARK_OAK_WOOD, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.STRIPPED_DARK_OAK_WOOD).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.STRIPPED_DARK_OAK_WOOD))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.STRIPPED_MANGROVE_WOOD, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.STRIPPED_MANGROVE_WOOD).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.STRIPPED_MANGROVE_WOOD))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.STRIPPED_BAMBOO_BLOCK, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.STRIPPED_BAMBOO_BLOCK).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.STRIPPED_BAMBOO_BLOCK))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.OAK_WOOD, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.OAK_WOOD).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.OAK_WOOD))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.SPRUCE_WOOD, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.SPRUCE_WOOD).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.SPRUCE_WOOD))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BIRCH_WOOD, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BIRCH_WOOD).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.BIRCH_WOOD))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.JUNGLE_WOOD, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.JUNGLE_WOOD).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.JUNGLE_WOOD))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.ACACIA_WOOD, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.ACACIA_WOOD).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.ACACIA_WOOD))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.CHERRY_WOOD, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.CHERRY_WOOD).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.CHERRY_WOOD))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.DARK_OAK_WOOD, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.DARK_OAK_WOOD).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.DARK_OAK_WOOD))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.MANGROVE_WOOD, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.MANGROVE_WOOD).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.MANGROVE_WOOD))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.GRASS, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.GRASS).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.GRASS))
                .with(new CompostableItemComponent(0.3f))
                .build()
        ));
        registerable.register(ItemKeys.AZALEA, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.AZALEA).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.AZALEA))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.PLANT_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.FLOWERING_AZALEA, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.FLOWERING_AZALEA).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.FLOWERING_AZALEA))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.PLANT_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.DEAD_BUSH, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.DEAD_BUSH).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.DEAD_BUSH))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.PLANT_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.WHITE_WOOL, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.WHITE_WOOL).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.WHITE_WOOL))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOL_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.ORANGE_WOOL, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.ORANGE_WOOL).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.ORANGE_WOOL))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOL_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.MAGENTA_WOOL, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.MAGENTA_WOOL).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.MAGENTA_WOOL))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOL_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.LIGHT_BLUE_WOOL, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.LIGHT_BLUE_WOOL).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.LIGHT_BLUE_WOOL))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOL_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.YELLOW_WOOL, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.YELLOW_WOOL).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.YELLOW_WOOL))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOL_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.LIME_WOOL, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.LIME_WOOL).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.LIME_WOOL))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOL_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.PINK_WOOL, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.PINK_WOOL).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.PINK_WOOL))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOL_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.GRAY_WOOL, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.GRAY_WOOL).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.GRAY_WOOL))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOL_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.LIGHT_GRAY_WOOL, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.LIGHT_GRAY_WOOL).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.LIGHT_GRAY_WOOL))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOL_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.CYAN_WOOL, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.CYAN_WOOL).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.CYAN_WOOL))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOL_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.PURPLE_WOOL, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.PURPLE_WOOL).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.PURPLE_WOOL))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOL_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BLUE_WOOL, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BLUE_WOOL).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.BLUE_WOOL))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOL_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BROWN_WOOL, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BROWN_WOOL).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.BROWN_WOOL))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOL_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.GREEN_WOOL, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.GREEN_WOOL).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.GREEN_WOOL))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOL_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.RED_WOOL, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.RED_WOOL).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.RED_WOOL))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOL_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BLACK_WOOL, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BLACK_WOOL).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.BLACK_WOOL))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOL_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BAMBOO, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BAMBOO).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.BAMBOO))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.BAMBOO_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.OAK_SLAB, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.OAK_SLAB).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.OAK_SLAB))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.SLAB_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.SPRUCE_SLAB, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.SPRUCE_SLAB).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.SPRUCE_SLAB))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.SLAB_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BIRCH_SLAB, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BIRCH_SLAB).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.BIRCH_SLAB))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.SLAB_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.JUNGLE_SLAB, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.JUNGLE_SLAB).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.JUNGLE_SLAB))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.SLAB_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.ACACIA_SLAB, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.ACACIA_SLAB).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.ACACIA_SLAB))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.SLAB_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.CHERRY_SLAB, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.CHERRY_SLAB).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.CHERRY_SLAB))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.SLAB_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.DARK_OAK_SLAB, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.DARK_OAK_SLAB).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.DARK_OAK_SLAB))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.SLAB_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.MANGROVE_SLAB, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.MANGROVE_SLAB).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.MANGROVE_SLAB))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.SLAB_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BAMBOO_SLAB, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BAMBOO_SLAB).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.BAMBOO_SLAB))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.SLAB_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BAMBOO_MOSAIC_SLAB, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BAMBOO_MOSAIC_SLAB).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.BAMBOO_MOSAIC_SLAB))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.SLAB_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BOOKSHELF, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BOOKSHELF).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.BOOKSHELF))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.CHISELED_BOOKSHELF, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.CHISELED_BOOKSHELF).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.CHISELED_BOOKSHELF))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.CHEST, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.CHEST).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.CHEST))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.CRAFTING_TABLE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.CRAFTING_TABLE).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.CRAFTING_TABLE))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.LADDER, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.LADDER).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.LADDER))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.SNOW, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.SNOW).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.SNOW))
                .build()
        ));
        registerable.register(ItemKeys.JUKEBOX, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.JUKEBOX).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.JUKEBOX))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.OAK_FENCE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.OAK_FENCE).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.OAK_FENCE))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.SPRUCE_FENCE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.SPRUCE_FENCE).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.SPRUCE_FENCE))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BIRCH_FENCE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BIRCH_FENCE).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.BIRCH_FENCE))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.JUNGLE_FENCE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.JUNGLE_FENCE).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.JUNGLE_FENCE))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.ACACIA_FENCE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.ACACIA_FENCE).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.ACACIA_FENCE))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.CHERRY_FENCE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.CHERRY_FENCE).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.CHERRY_FENCE))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.DARK_OAK_FENCE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.DARK_OAK_FENCE).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.DARK_OAK_FENCE))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.MANGROVE_FENCE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.MANGROVE_FENCE).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.MANGROVE_FENCE))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BAMBOO_FENCE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BAMBOO_FENCE).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.BAMBOO_FENCE))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.OAK_STAIRS, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.OAK_STAIRS).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.OAK_STAIRS))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.SPRUCE_STAIRS, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.SPRUCE_STAIRS).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.SPRUCE_STAIRS))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BIRCH_STAIRS, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BIRCH_STAIRS).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.BIRCH_STAIRS))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.JUNGLE_STAIRS, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.JUNGLE_STAIRS).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.JUNGLE_STAIRS))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.ACACIA_STAIRS, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.ACACIA_STAIRS).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.ACACIA_STAIRS))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.CHERRY_STAIRS, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.CHERRY_STAIRS).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.CHERRY_STAIRS))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.DARK_OAK_STAIRS, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.DARK_OAK_STAIRS).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.DARK_OAK_STAIRS))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.MANGROVE_STAIRS, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.MANGROVE_STAIRS).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.MANGROVE_STAIRS))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BAMBOO_STAIRS, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BAMBOO_STAIRS).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.BAMBOO_STAIRS))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BAMBOO_MOSAIC_STAIRS, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BAMBOO_MOSAIC_STAIRS).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.BAMBOO_MOSAIC_STAIRS))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BARRIER, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BARRIER).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.BARRIER))
                .build()
        ));
        registerable.register(ItemKeys.WHITE_CARPET, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.WHITE_CARPET).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.WHITE_CARPET))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOL_CARPET_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.ORANGE_CARPET, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.ORANGE_CARPET).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.ORANGE_CARPET))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOL_CARPET_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.MAGENTA_CARPET, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.MAGENTA_CARPET).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.MAGENTA_CARPET))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOL_CARPET_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.LIGHT_BLUE_CARPET, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.LIGHT_BLUE_CARPET).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.LIGHT_BLUE_CARPET))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOL_CARPET_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.YELLOW_CARPET, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.YELLOW_CARPET).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.YELLOW_CARPET))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOL_CARPET_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.LIME_CARPET, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.LIME_CARPET).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.LIME_CARPET))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOL_CARPET_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.PINK_CARPET, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.PINK_CARPET).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.PINK_CARPET))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOL_CARPET_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.GRAY_CARPET, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.GRAY_CARPET).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.GRAY_CARPET))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOL_CARPET_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.LIGHT_GRAY_CARPET, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.LIGHT_GRAY_CARPET).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.LIGHT_GRAY_CARPET))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOL_CARPET_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.CYAN_CARPET, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.CYAN_CARPET).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.CYAN_CARPET))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOL_CARPET_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.PURPLE_CARPET, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.PURPLE_CARPET).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.PURPLE_CARPET))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOL_CARPET_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BLUE_CARPET, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BLUE_CARPET).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.BLUE_CARPET))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOL_CARPET_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BROWN_CARPET, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BROWN_CARPET).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.BROWN_CARPET))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOL_CARPET_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.GREEN_CARPET, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.GREEN_CARPET).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.GREEN_CARPET))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOL_CARPET_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.RED_CARPET, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.RED_CARPET).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.RED_CARPET))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOL_CARPET_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BLACK_CARPET, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BLACK_CARPET).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.BLACK_CARPET))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOL_CARPET_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.SCAFFOLDING, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.SCAFFOLDING).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.SCAFFOLDING))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.SCAFFOLDING_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.REDSTONE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.REDSTONE).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.REDSTONE_WIRE))
                .build()
        ));
        registerable.register(ItemKeys.LECTERN, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.LECTERN).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.LECTERN))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.DAYLIGHT_DETECTOR, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.DAYLIGHT_DETECTOR).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.DAYLIGHT_DETECTOR))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.TRAPPED_CHEST, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.TRAPPED_CHEST).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.TRAPPED_CHEST))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.NOTE_BLOCK, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.NOTE_BLOCK).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.NOTE_BLOCK))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.OAK_BUTTON, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.OAK_BUTTON).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.OAK_BUTTON))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.BUTTON_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.SPRUCE_BUTTON, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.SPRUCE_BUTTON).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.SPRUCE_BUTTON))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.BUTTON_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BIRCH_BUTTON, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BIRCH_BUTTON).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.BIRCH_BUTTON))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.BUTTON_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.JUNGLE_BUTTON, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.JUNGLE_BUTTON).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.JUNGLE_BUTTON))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.BUTTON_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.ACACIA_BUTTON, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.ACACIA_BUTTON).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.ACACIA_BUTTON))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.BUTTON_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.CHERRY_BUTTON, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.CHERRY_BUTTON).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.CHERRY_BUTTON))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.BUTTON_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.DARK_OAK_BUTTON, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.DARK_OAK_BUTTON).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.DARK_OAK_BUTTON))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.BUTTON_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.MANGROVE_BUTTON, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.MANGROVE_BUTTON).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.MANGROVE_BUTTON))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.BUTTON_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BAMBOO_BUTTON, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BAMBOO_BUTTON).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.BAMBOO_BUTTON))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.BUTTON_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.OAK_PRESSURE_PLATE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.OAK_PRESSURE_PLATE).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.OAK_PRESSURE_PLATE))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.SPRUCE_PRESSURE_PLATE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.SPRUCE_PRESSURE_PLATE).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.SPRUCE_PRESSURE_PLATE))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BIRCH_PRESSURE_PLATE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BIRCH_PRESSURE_PLATE).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.BIRCH_PRESSURE_PLATE))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.JUNGLE_PRESSURE_PLATE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.JUNGLE_PRESSURE_PLATE).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.JUNGLE_PRESSURE_PLATE))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.ACACIA_PRESSURE_PLATE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.ACACIA_PRESSURE_PLATE).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.ACACIA_PRESSURE_PLATE))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.CHERRY_PRESSURE_PLATE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.CHERRY_PRESSURE_PLATE).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.CHERRY_PRESSURE_PLATE))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.DARK_OAK_PRESSURE_PLATE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.DARK_OAK_PRESSURE_PLATE).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.DARK_OAK_PRESSURE_PLATE))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.MANGROVE_PRESSURE_PLATE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.MANGROVE_PRESSURE_PLATE).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.MANGROVE_PRESSURE_PLATE))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BAMBOO_PRESSURE_PLATE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BAMBOO_PRESSURE_PLATE).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.BAMBOO_PRESSURE_PLATE))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.OAK_DOOR, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.OAK_DOOR).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.OAK_DOOR))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.DOOR_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.SPRUCE_DOOR, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.SPRUCE_DOOR).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.SPRUCE_DOOR))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.DOOR_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BIRCH_DOOR, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BIRCH_DOOR).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.BIRCH_DOOR))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.DOOR_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.JUNGLE_DOOR, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.JUNGLE_DOOR).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.JUNGLE_DOOR))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.DOOR_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.ACACIA_DOOR, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.ACACIA_DOOR).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.ACACIA_DOOR))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.DOOR_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.CHERRY_DOOR, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.CHERRY_DOOR).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.CHERRY_DOOR))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.DOOR_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.DARK_OAK_DOOR, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.DARK_OAK_DOOR).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.DARK_OAK_DOOR))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.DOOR_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.MANGROVE_DOOR, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.MANGROVE_DOOR).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.MANGROVE_DOOR))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.DOOR_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BAMBOO_DOOR, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BAMBOO_DOOR).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.BAMBOO_DOOR))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.DOOR_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.OAK_TRAPDOOR, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.OAK_TRAPDOOR).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.OAK_TRAPDOOR))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.SPRUCE_TRAPDOOR, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.SPRUCE_TRAPDOOR).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.SPRUCE_TRAPDOOR))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BIRCH_TRAPDOOR, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BIRCH_TRAPDOOR).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.BIRCH_TRAPDOOR))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.JUNGLE_TRAPDOOR, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.JUNGLE_TRAPDOOR).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.JUNGLE_TRAPDOOR))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.ACACIA_TRAPDOOR, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.ACACIA_TRAPDOOR).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.ACACIA_TRAPDOOR))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.CHERRY_TRAPDOOR, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.CHERRY_TRAPDOOR).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.CHERRY_TRAPDOOR))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.DARK_OAK_TRAPDOOR, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.DARK_OAK_TRAPDOOR).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.DARK_OAK_TRAPDOOR))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.MANGROVE_TRAPDOOR, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.MANGROVE_TRAPDOOR).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.MANGROVE_TRAPDOOR))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BAMBOO_TRAPDOOR, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BAMBOO_TRAPDOOR).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.BAMBOO_TRAPDOOR))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.OAK_FENCE_GATE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.OAK_FENCE_GATE).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.OAK_FENCE_GATE))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.SPRUCE_FENCE_GATE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.SPRUCE_FENCE_GATE).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.SPRUCE_FENCE_GATE))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BIRCH_FENCE_GATE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BIRCH_FENCE_GATE).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.BIRCH_FENCE_GATE))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.JUNGLE_FENCE_GATE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.JUNGLE_FENCE_GATE).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.JUNGLE_FENCE_GATE))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.ACACIA_FENCE_GATE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.ACACIA_FENCE_GATE).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.ACACIA_FENCE_GATE))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.CHERRY_FENCE_GATE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.CHERRY_FENCE_GATE).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.CHERRY_FENCE_GATE))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.DARK_OAK_FENCE_GATE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.DARK_OAK_FENCE_GATE).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.DARK_OAK_FENCE_GATE))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.MANGROVE_FENCE_GATE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.MANGROVE_FENCE_GATE).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.MANGROVE_FENCE_GATE))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BAMBOO_FENCE_GATE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BAMBOO_FENCE_GATE).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.BAMBOO_FENCE_GATE))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.OAK_BOAT, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.OAK_BOAT).build(), 1),
            ItemComponentSet.builder()
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.BOAT_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.OAK_CHEST_BOAT, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.OAK_CHEST_BOAT).build(), 1),
            ItemComponentSet.builder()
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.BOAT_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.SPRUCE_BOAT, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.SPRUCE_BOAT).build(), 1),
            ItemComponentSet.builder()
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.BOAT_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.SPRUCE_CHEST_BOAT, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.SPRUCE_CHEST_BOAT).build(), 1),
            ItemComponentSet.builder()
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.BOAT_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BIRCH_BOAT, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.BIRCH_BOAT).build(), 1),
            ItemComponentSet.builder()
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.BOAT_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BIRCH_CHEST_BOAT, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.BIRCH_CHEST_BOAT).build(), 1),
            ItemComponentSet.builder()
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.BOAT_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.JUNGLE_BOAT, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.JUNGLE_BOAT).build(), 1),
            ItemComponentSet.builder()
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.BOAT_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.JUNGLE_CHEST_BOAT, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.JUNGLE_CHEST_BOAT).build(), 1),
            ItemComponentSet.builder()
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.BOAT_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.ACACIA_BOAT, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.ACACIA_BOAT).build(), 1),
            ItemComponentSet.builder()
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.BOAT_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.ACACIA_CHEST_BOAT, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.ACACIA_CHEST_BOAT).build(), 1),
            ItemComponentSet.builder()
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.BOAT_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.CHERRY_BOAT, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.CHERRY_BOAT).build(), 1),
            ItemComponentSet.builder()
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.BOAT_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.CHERRY_CHEST_BOAT, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.CHERRY_CHEST_BOAT).build(), 1),
            ItemComponentSet.builder()
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.BOAT_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.DARK_OAK_BOAT, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.DARK_OAK_BOAT).build(), 1),
            ItemComponentSet.builder()
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.BOAT_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.DARK_OAK_CHEST_BOAT, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.DARK_OAK_CHEST_BOAT).build(), 1),
            ItemComponentSet.builder()
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.BOAT_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.MANGROVE_BOAT, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.MANGROVE_BOAT).build(), 1),
            ItemComponentSet.builder()
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.BOAT_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.MANGROVE_CHEST_BOAT, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.MANGROVE_CHEST_BOAT).build(), 1),
            ItemComponentSet.builder()
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.BOAT_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BAMBOO_RAFT, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.BAMBOO_RAFT).build(), 1),
            ItemComponentSet.builder()
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.BOAT_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BAMBOO_CHEST_RAFT, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.BAMBOO_CHEST_RAFT).build(), 1),
            ItemComponentSet.builder()
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.BOAT_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.APPLE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.APPLE).build()),
            ItemComponentSet.builder()
                .with(FoodItemComponent.from(FoodComponents.APPLE))
                .with(new CompostableItemComponent(0.65f))
                .build()
        ));
        registerable.register(ItemKeys.BOW, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.BOW).build(), 1),
            ItemComponentSet.builder()
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.COAL, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.COAL).build()),
            ItemComponentSet.builder()
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.COAL_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.CHARCOAL, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.CHARCOAL).build()),
            ItemComponentSet.builder()
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.COAL_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.DIAMOND, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.DIAMOND).build())
        ));
        registerable.register(ItemKeys.LAPIS_LAZULI, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.LAPIS_LAZULI).build())
        ));
        registerable.register(ItemKeys.IRON_INGOT, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.IRON_INGOT).build())
        ));
        registerable.register(ItemKeys.GOLD_INGOT, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.GOLD_INGOT).build())
        ));
        registerable.register(ItemKeys.NETHERITE_INGOT, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.NETHERITE_INGOT).build())
        ));
        registerable.register(ItemKeys.WOODEN_SWORD, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.WOODEN_SWORD).build(), 1),
            ItemComponentSet.builder()
                .with(ToolItemComponent.from(ToolMaterials.WOOD, 2))
                .with(new WeaponItemComponent(1))
                .with(EnchantableItemComponent.enchants(ToolMaterials.WOOD, EnchantmentTags.SWORD_ENCHANTING))
                .with(ForgeableItemComponent.of(EnchantmentTags.SWORD_FORGING))
                .with(new RepairableItemComponent(ItemTagsUtil.REPAIRS_WOODEN_TOOL))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.TOOL_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.WOODEN_SHOVEL, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.WOODEN_SHOVEL).build(), 1),
            ItemComponentSet.builder()
                .with(ToolItemComponent.from(ToolMaterials.WOOD, 1, BlockTags.SHOVEL_MINEABLE))
                .with(new WeaponItemComponent(2))
                .with(EnchantableItemComponent.enchants(ToolMaterials.WOOD, EnchantmentTags.SHOVEL_ENCHANTING))
                .with(ForgeableItemComponent.of(EnchantmentTags.SHOVEL_FORGING))
                .with(new RepairableItemComponent(ItemTagsUtil.REPAIRS_WOODEN_TOOL))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.TOOL_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.WOODEN_PICKAXE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.WOODEN_PICKAXE).build(), 1),
            ItemComponentSet.builder()
                .with(ToolItemComponent.from(ToolMaterials.WOOD, 1, BlockTags.PICKAXE_MINEABLE))
                .with(new WeaponItemComponent(2))
                .with(EnchantableItemComponent.enchants(ToolMaterials.WOOD, EnchantmentTags.PICKAXE_ENCHANTING))
                .with(ForgeableItemComponent.of(EnchantmentTags.PICKAXE_FORGING))
                .with(new RepairableItemComponent(ItemTagsUtil.REPAIRS_WOODEN_TOOL))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.TOOL_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.WOODEN_AXE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.WOODEN_AXE).build(), 1),
            ItemComponentSet.builder()
                .with(ToolItemComponent.from(ToolMaterials.WOOD, 1, BlockTags.AXE_MINEABLE))
                .with(new WeaponItemComponent(2))
                .with(EnchantableItemComponent.enchants(ToolMaterials.WOOD, EnchantmentTags.AXE_ENCHANTING))
                .with(ForgeableItemComponent.of(EnchantmentTags.AXE_FORGING))
                .with(new RepairableItemComponent(ItemTagsUtil.REPAIRS_WOODEN_TOOL))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.TOOL_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.WOODEN_HOE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.WOODEN_HOE).build(), 1),
            ItemComponentSet.builder()
                .with(ToolItemComponent.from(ToolMaterials.WOOD, 1, BlockTags.HOE_MINEABLE))
                .with(new WeaponItemComponent(2))
                .with(EnchantableItemComponent.enchants(ToolMaterials.WOOD, EnchantmentTags.HOE_ENCHANTING))
                .with(ForgeableItemComponent.of(EnchantmentTags.HOE_FORGING))
                .with(new RepairableItemComponent(ItemTagsUtil.REPAIRS_WOODEN_TOOL))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.TOOL_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.STONE_SWORD, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.STONE_SWORD).build(), 1),
            ItemComponentSet.builder()
                .with(ToolItemComponent.from(ToolMaterials.STONE, 2))
                .with(new WeaponItemComponent(1))
                .with(EnchantableItemComponent.enchants(ToolMaterials.STONE, EnchantmentTags.SWORD_ENCHANTING))
                .with(ForgeableItemComponent.of(EnchantmentTags.SWORD_FORGING))
                .with(new RepairableItemComponent(ItemTagsUtil.REPAIRS_STONE_TOOL))
                .build()
        ));
        registerable.register(ItemKeys.STONE_SHOVEL, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.STONE_SHOVEL).build(), 1),
            ItemComponentSet.builder()
                .with(ToolItemComponent.from(ToolMaterials.STONE, 1, BlockTags.SHOVEL_MINEABLE))
                .with(new WeaponItemComponent(2))
                .with(EnchantableItemComponent.enchants(ToolMaterials.STONE, EnchantmentTags.SHOVEL_ENCHANTING))
                .with(ForgeableItemComponent.of(EnchantmentTags.SHOVEL_FORGING))
                .with(new RepairableItemComponent(ItemTagsUtil.REPAIRS_STONE_TOOL))
                .build()
        ));
        registerable.register(ItemKeys.STONE_PICKAXE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.STONE_PICKAXE).build(), 1),
            ItemComponentSet.builder()
                .with(ToolItemComponent.from(ToolMaterials.STONE, 1, BlockTags.PICKAXE_MINEABLE))
                .with(new WeaponItemComponent(2))
                .with(EnchantableItemComponent.enchants(ToolMaterials.STONE, EnchantmentTags.PICKAXE_ENCHANTING))
                .with(ForgeableItemComponent.of(EnchantmentTags.PICKAXE_FORGING))
                .with(new RepairableItemComponent(ItemTagsUtil.REPAIRS_STONE_TOOL))
                .build()
        ));
        registerable.register(ItemKeys.STONE_AXE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.STONE_AXE).build(), 1),
            ItemComponentSet.builder()
                .with(ToolItemComponent.from(ToolMaterials.STONE, 1, BlockTags.AXE_MINEABLE))
                .with(new WeaponItemComponent(2))
                .with(EnchantableItemComponent.enchants(ToolMaterials.STONE, EnchantmentTags.AXE_ENCHANTING))
                .with(ForgeableItemComponent.of(EnchantmentTags.AXE_FORGING))
                .with(new RepairableItemComponent(ItemTagsUtil.REPAIRS_STONE_TOOL))
                .build()
        ));
        registerable.register(ItemKeys.STONE_HOE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.STONE_HOE).build(), 1),
            ItemComponentSet.builder()
                .with(ToolItemComponent.from(ToolMaterials.STONE, 1, BlockTags.HOE_MINEABLE))
                .with(new WeaponItemComponent(2))
                .with(EnchantableItemComponent.enchants(ToolMaterials.STONE, EnchantmentTags.HOE_ENCHANTING))
                .with(ForgeableItemComponent.of(EnchantmentTags.HOE_FORGING))
                .with(new RepairableItemComponent(ItemTagsUtil.REPAIRS_STONE_TOOL))
                .build()
        ));
        registerable.register(ItemKeys.GOLDEN_SWORD, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.GOLDEN_SWORD).build(), 1),
            ItemComponentSet.builder()
                .with(ToolItemComponent.from(ToolMaterials.GOLD, 2))
                .with(new WeaponItemComponent(1))
                .with(EnchantableItemComponent.enchants(ToolMaterials.GOLD, EnchantmentTags.SWORD_ENCHANTING))
                .with(ForgeableItemComponent.of(EnchantmentTags.SWORD_FORGING))
                .with(new RepairableItemComponent(ItemTagsUtil.REPAIRS_GOLDEN_TOOL))
                .build()
        ));
        registerable.register(ItemKeys.GOLDEN_SHOVEL, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.GOLDEN_SHOVEL).build(), 1),
            ItemComponentSet.builder()
                .with(ToolItemComponent.from(ToolMaterials.GOLD, 1, BlockTags.SHOVEL_MINEABLE))
                .with(new WeaponItemComponent(2))
                .with(EnchantableItemComponent.enchants(ToolMaterials.GOLD, EnchantmentTags.SHOVEL_ENCHANTING))
                .with(ForgeableItemComponent.of(EnchantmentTags.SHOVEL_FORGING))
                .with(new RepairableItemComponent(ItemTagsUtil.REPAIRS_GOLDEN_TOOL))
                .build()
        ));
        registerable.register(ItemKeys.GOLDEN_PICKAXE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.GOLDEN_PICKAXE).build(), 1),
            ItemComponentSet.builder()
                .with(ToolItemComponent.from(ToolMaterials.GOLD, 1, BlockTags.PICKAXE_MINEABLE))
                .with(new WeaponItemComponent(2))
                .with(EnchantableItemComponent.enchants(ToolMaterials.GOLD, EnchantmentTags.PICKAXE_ENCHANTING))
                .with(ForgeableItemComponent.of(EnchantmentTags.PICKAXE_FORGING))
                .with(new RepairableItemComponent(ItemTagsUtil.REPAIRS_GOLDEN_TOOL))
                .build()
        ));
        registerable.register(ItemKeys.GOLDEN_AXE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.GOLDEN_AXE).build(), 1),
            ItemComponentSet.builder()
                .with(ToolItemComponent.from(ToolMaterials.GOLD, 1, BlockTags.AXE_MINEABLE))
                .with(new WeaponItemComponent(2))
                .with(EnchantableItemComponent.enchants(ToolMaterials.GOLD, EnchantmentTags.AXE_ENCHANTING))
                .with(ForgeableItemComponent.of(EnchantmentTags.AXE_FORGING))
                .with(new RepairableItemComponent(ItemTagsUtil.REPAIRS_GOLDEN_TOOL))
                .build()
        ));
        registerable.register(ItemKeys.GOLDEN_HOE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.GOLDEN_HOE).build(), 1),
            ItemComponentSet.builder()
                .with(ToolItemComponent.from(ToolMaterials.GOLD, 1, BlockTags.HOE_MINEABLE))
                .with(new WeaponItemComponent(2))
                .with(EnchantableItemComponent.enchants(ToolMaterials.GOLD, EnchantmentTags.HOE_ENCHANTING))
                .with(ForgeableItemComponent.of(EnchantmentTags.HOE_FORGING))
                .with(new RepairableItemComponent(ItemTagsUtil.REPAIRS_GOLDEN_TOOL))
                .build()
        ));
        registerable.register(ItemKeys.IRON_SWORD, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.IRON_SWORD).build(), 1),
            ItemComponentSet.builder()
                .with(ToolItemComponent.from(ToolMaterials.IRON, 2))
                .with(new WeaponItemComponent(1))
                .with(EnchantableItemComponent.enchants(ToolMaterials.IRON, EnchantmentTags.SWORD_ENCHANTING))
                .with(ForgeableItemComponent.of(EnchantmentTags.SWORD_FORGING))
                .with(new RepairableItemComponent(ItemTagsUtil.REPAIRS_IRON_TOOL))
                .build()
        ));
        registerable.register(ItemKeys.IRON_SHOVEL, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.IRON_SHOVEL).build(), 1),
            ItemComponentSet.builder()
                .with(ToolItemComponent.from(ToolMaterials.IRON, 1, BlockTags.SHOVEL_MINEABLE))
                .with(new WeaponItemComponent(2))
                .with(EnchantableItemComponent.enchants(ToolMaterials.IRON, EnchantmentTags.SHOVEL_ENCHANTING))
                .with(ForgeableItemComponent.of(EnchantmentTags.SHOVEL_FORGING))
                .with(new RepairableItemComponent(ItemTagsUtil.REPAIRS_IRON_TOOL))
                .build()
        ));
        registerable.register(ItemKeys.IRON_PICKAXE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.IRON_PICKAXE).build(), 1),
            ItemComponentSet.builder()
                .with(ToolItemComponent.from(ToolMaterials.IRON, 1, BlockTags.PICKAXE_MINEABLE))
                .with(new WeaponItemComponent(2))
                .with(EnchantableItemComponent.enchants(ToolMaterials.IRON, EnchantmentTags.PICKAXE_ENCHANTING))
                .with(ForgeableItemComponent.of(EnchantmentTags.PICKAXE_FORGING))
                .with(new RepairableItemComponent(ItemTagsUtil.REPAIRS_IRON_TOOL))
                .build()
        ));
        registerable.register(ItemKeys.IRON_AXE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.IRON_AXE).build(), 1),
            ItemComponentSet.builder()
                .with(ToolItemComponent.from(ToolMaterials.IRON, 1, BlockTags.AXE_MINEABLE))
                .with(new WeaponItemComponent(2))
                .with(EnchantableItemComponent.enchants(ToolMaterials.IRON, EnchantmentTags.AXE_ENCHANTING))
                .with(ForgeableItemComponent.of(EnchantmentTags.AXE_FORGING))
                .with(new RepairableItemComponent(ItemTagsUtil.REPAIRS_IRON_TOOL))
                .build()
        ));
        registerable.register(ItemKeys.IRON_HOE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.IRON_HOE).build(), 1),
            ItemComponentSet.builder()
                .with(ToolItemComponent.from(ToolMaterials.IRON, 1, BlockTags.HOE_MINEABLE))
                .with(new WeaponItemComponent(2))
                .with(EnchantableItemComponent.enchants(ToolMaterials.IRON, EnchantmentTags.HOE_ENCHANTING))
                .with(ForgeableItemComponent.of(EnchantmentTags.HOE_FORGING))
                .with(new RepairableItemComponent(ItemTagsUtil.REPAIRS_IRON_TOOL))
                .build()
        ));
        registerable.register(ItemKeys.DIAMOND_SWORD, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.DIAMOND_SWORD).build(), 1),
            ItemComponentSet.builder()
                .with(ToolItemComponent.from(ToolMaterials.DIAMOND, 2))
                .with(new WeaponItemComponent(1))
                .with(EnchantableItemComponent.enchants(ToolMaterials.DIAMOND, EnchantmentTags.SWORD_ENCHANTING))
                .with(ForgeableItemComponent.of(EnchantmentTags.SWORD_FORGING))
                .with(new RepairableItemComponent(ItemTagsUtil.REPAIRS_DIAMOND_TOOL))
                .build()
        ));
        registerable.register(ItemKeys.DIAMOND_SHOVEL, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.DIAMOND_SHOVEL).build(), 1),
            ItemComponentSet.builder()
                .with(ToolItemComponent.from(ToolMaterials.DIAMOND, 1, BlockTags.SHOVEL_MINEABLE))
                .with(new WeaponItemComponent(2))
                .with(EnchantableItemComponent.enchants(ToolMaterials.DIAMOND, EnchantmentTags.SHOVEL_ENCHANTING))
                .with(ForgeableItemComponent.of(EnchantmentTags.SHOVEL_FORGING))
                .with(new RepairableItemComponent(ItemTagsUtil.REPAIRS_DIAMOND_TOOL))
                .build()
        ));
        registerable.register(ItemKeys.DIAMOND_PICKAXE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.DIAMOND_PICKAXE).build(), 1),
            ItemComponentSet.builder()
                .with(ToolItemComponent.from(ToolMaterials.DIAMOND, 1, BlockTags.PICKAXE_MINEABLE))
                .with(new WeaponItemComponent(2))
                .with(EnchantableItemComponent.enchants(ToolMaterials.DIAMOND, EnchantmentTags.PICKAXE_ENCHANTING))
                .with(ForgeableItemComponent.of(EnchantmentTags.PICKAXE_FORGING))
                .with(new RepairableItemComponent(ItemTagsUtil.REPAIRS_DIAMOND_TOOL))
                .build()
        ));
        registerable.register(ItemKeys.DIAMOND_AXE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.DIAMOND_AXE).build(), 1),
            ItemComponentSet.builder()
                .with(ToolItemComponent.from(ToolMaterials.DIAMOND, 1, BlockTags.AXE_MINEABLE))
                .with(new WeaponItemComponent(2))
                .with(EnchantableItemComponent.enchants(ToolMaterials.DIAMOND, EnchantmentTags.AXE_ENCHANTING))
                .with(ForgeableItemComponent.of(EnchantmentTags.AXE_FORGING))
                .with(new RepairableItemComponent(ItemTagsUtil.REPAIRS_DIAMOND_TOOL))
                .build()
        ));
        registerable.register(ItemKeys.DIAMOND_HOE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.DIAMOND_HOE).build(), 1),
            ItemComponentSet.builder()
                .with(ToolItemComponent.from(ToolMaterials.DIAMOND, 1, BlockTags.HOE_MINEABLE))
                .with(new WeaponItemComponent(2))
                .with(EnchantableItemComponent.enchants(ToolMaterials.DIAMOND, EnchantmentTags.HOE_ENCHANTING))
                .with(ForgeableItemComponent.of(EnchantmentTags.HOE_FORGING))
                .with(new RepairableItemComponent(ItemTagsUtil.REPAIRS_DIAMOND_TOOL))
                .build()
        ));
        registerable.register(ItemKeys.NETHERITE_SWORD, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.NETHERITE_SWORD).build(), 1),
            ItemComponentSet.builder()
                .with(ToolItemComponent.from(ToolMaterials.NETHERITE, 2))
                .with(new WeaponItemComponent(1))
                .with(EnchantableItemComponent.enchants(ToolMaterials.NETHERITE, EnchantmentTags.SWORD_ENCHANTING))
                .with(ForgeableItemComponent.of(EnchantmentTags.SWORD_FORGING))
                .with(new RepairableItemComponent(ItemTagsUtil.REPAIRS_NETHERITE_TOOL))
                .build()
        ));
        registerable.register(ItemKeys.NETHERITE_SHOVEL, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.NETHERITE_SHOVEL).build(), 1),
            ItemComponentSet.builder()
                .with(ToolItemComponent.from(ToolMaterials.NETHERITE, 1, BlockTags.SHOVEL_MINEABLE))
                .with(new WeaponItemComponent(2))
                .with(EnchantableItemComponent.enchants(ToolMaterials.NETHERITE, EnchantmentTags.SHOVEL_ENCHANTING))
                .with(ForgeableItemComponent.of(EnchantmentTags.SHOVEL_FORGING))
                .with(new RepairableItemComponent(ItemTagsUtil.REPAIRS_NETHERITE_TOOL))
                .build()
        ));
        registerable.register(ItemKeys.NETHERITE_PICKAXE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.NETHERITE_PICKAXE).build(), 1),
            ItemComponentSet.builder()
                .with(ToolItemComponent.from(ToolMaterials.NETHERITE, 1, BlockTags.PICKAXE_MINEABLE))
                .with(new WeaponItemComponent(2))
                .with(EnchantableItemComponent.enchants(ToolMaterials.NETHERITE, EnchantmentTags.PICKAXE_ENCHANTING))
                .with(ForgeableItemComponent.of(EnchantmentTags.PICKAXE_FORGING))
                .with(new RepairableItemComponent(ItemTagsUtil.REPAIRS_NETHERITE_TOOL))
                .build()
        ));
        registerable.register(ItemKeys.NETHERITE_AXE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.NETHERITE_AXE).build(), 1),
            ItemComponentSet.builder()
                .with(ToolItemComponent.from(ToolMaterials.NETHERITE, 1, BlockTags.AXE_MINEABLE))
                .with(new WeaponItemComponent(2))
                .with(EnchantableItemComponent.enchants(ToolMaterials.NETHERITE, EnchantmentTags.AXE_ENCHANTING))
                .with(ForgeableItemComponent.of(EnchantmentTags.AXE_FORGING))
                .with(new RepairableItemComponent(ItemTagsUtil.REPAIRS_NETHERITE_TOOL))
                .build()
        ));
        registerable.register(ItemKeys.NETHERITE_HOE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.NETHERITE_HOE).build(), 1),
            ItemComponentSet.builder()
                .with(ToolItemComponent.from(ToolMaterials.NETHERITE, 1, BlockTags.HOE_MINEABLE))
                .with(new WeaponItemComponent(2))
                .with(EnchantableItemComponent.enchants(ToolMaterials.NETHERITE, EnchantmentTags.HOE_ENCHANTING))
                .with(ForgeableItemComponent.of(EnchantmentTags.HOE_FORGING))
                .with(new RepairableItemComponent(ItemTagsUtil.REPAIRS_NETHERITE_TOOL))
                .build()
        ));
        registerable.register(ItemKeys.STICK, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.STICK).build()),
            ItemComponentSet.builder()
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.SMALL_WOODEN_ITEM_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BOWL, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.BOWL).build()),
            ItemComponentSet.builder()
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.SMALL_WOODEN_ITEM_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.MUSHROOM_STEW, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.MUSHROOM_STEW).build(), 1),
            ItemComponentSet.builder()
                .with(FoodItemComponent.from(FoodComponents.MUSHROOM_STEW))
                .build()
        ));
        registerable.register(ItemKeys.FEATHER, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.FEATHER).build()),
            ItemComponentSet.builder()
                .with(new TestItemComponent(true))
                .build()
        ));
        registerable.register(ItemKeys.BREAD, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.BREAD).build()),
            ItemComponentSet.builder()
                .with(FoodItemComponent.from(FoodComponents.BREAD))
                .with(new CompostableItemComponent(0.85f))
                .build()
        ));
        registerable.register(ItemKeys.IRON_HELMET, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.IRON_HELMET).build(), 1),
            ItemComponentSet.builder()
                .with(ArmorItemComponent.from(net.minecraft.item.ArmorMaterials.IRON, ArmorItem.Type.HELMET, armorMaterials.getOrThrow(ArmorMaterialKeys.IRON)))
                .with(EnchantableItemComponent.enchants(net.minecraft.item.ArmorMaterials.IRON, EnchantmentTags.HELMET_ENCHANTING))
                .with(ForgeableItemComponent.of(EnchantmentTags.HELMET_FORGING))
                .with(new RepairableItemComponent(ItemTagsUtil.REPAIRS_IRON_ARMOR))
                .build()
        ));
        registerable.register(ItemKeys.IRON_CHESTPLATE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.IRON_CHESTPLATE).build(), 1),
            ItemComponentSet.builder()
                .with(ArmorItemComponent.from(net.minecraft.item.ArmorMaterials.IRON, ArmorItem.Type.CHESTPLATE, armorMaterials.getOrThrow(ArmorMaterialKeys.IRON)))
                .with(EnchantableItemComponent.enchants(net.minecraft.item.ArmorMaterials.IRON, EnchantmentTags.CHESTPLATE_ENCHANTING))
                .with(ForgeableItemComponent.of(EnchantmentTags.CHESTPLATE_FORGING))
                .with(new RepairableItemComponent(ItemTagsUtil.REPAIRS_IRON_ARMOR))
                .build()
        ));
        registerable.register(ItemKeys.IRON_LEGGINGS, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.IRON_LEGGINGS).build(), 1),
            ItemComponentSet.builder()
                .with(ArmorItemComponent.from(net.minecraft.item.ArmorMaterials.IRON, ArmorItem.Type.LEGGINGS, armorMaterials.getOrThrow(ArmorMaterialKeys.IRON)))
                .with(EnchantableItemComponent.enchants(net.minecraft.item.ArmorMaterials.IRON, EnchantmentTags.LEGGINGS_ENCHANTING))
                .with(ForgeableItemComponent.of(EnchantmentTags.LEGGINGS_FORGING))
                .with(new RepairableItemComponent(ItemTagsUtil.REPAIRS_IRON_ARMOR))
                .build()
        ));
        registerable.register(ItemKeys.IRON_BOOTS, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.IRON_BOOTS).build(), 1),
            ItemComponentSet.builder()
                .with(ArmorItemComponent.from(net.minecraft.item.ArmorMaterials.IRON, ArmorItem.Type.BOOTS, armorMaterials.getOrThrow(ArmorMaterialKeys.IRON)))
                .with(EnchantableItemComponent.enchants(net.minecraft.item.ArmorMaterials.IRON, EnchantmentTags.BOOTS_ENCHANTING))
                .with(ForgeableItemComponent.of(EnchantmentTags.BOOTS_FORGING))
                .with(new RepairableItemComponent(ItemTagsUtil.REPAIRS_IRON_ARMOR))
                .build()
        ));
        registerable.register(ItemKeys.PORKCHOP, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.PORKCHOP).build()),
            ItemComponentSet.builder()
                .with(FoodItemComponent.from(FoodComponents.PORKCHOP))
                .build()
        ));
        registerable.register(ItemKeys.COOKED_PORKCHOP, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.COOKED_PORKCHOP).build()),
            ItemComponentSet.builder()
                .with(FoodItemComponent.from(FoodComponents.COOKED_PORKCHOP))
                .build()
        ));
        registerable.register(ItemKeys.GOLDEN_APPLE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.GOLDEN_APPLE).build()),
            ItemComponentSet.builder()
                .with(FoodItemComponent.from(FoodComponents.GOLDEN_APPLE))
                .build()
        ));
        registerable.register(ItemKeys.ENCHANTED_GOLDEN_APPLE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.ENCHANTED_GOLDEN_APPLE).build()),
            ItemComponentSet.builder()
                .with(FoodItemComponent.from(FoodComponents.ENCHANTED_GOLDEN_APPLE))
                .build()
        ));
        registerable.register(ItemKeys.OAK_SIGN, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.OAK_SIGN).build(), 16),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.OAK_SIGN))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.SIGN_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.SPRUCE_SIGN, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.SPRUCE_SIGN).build(), 16),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.SPRUCE_SIGN))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.SIGN_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BIRCH_SIGN, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BIRCH_SIGN).build(), 16),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.BIRCH_SIGN))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.SIGN_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.JUNGLE_SIGN, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.JUNGLE_SIGN).build(), 16),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.JUNGLE_SIGN))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.SIGN_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.ACACIA_SIGN, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.ACACIA_SIGN).build(), 16),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.ACACIA_SIGN))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.SIGN_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.CHERRY_SIGN, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.CHERRY_SIGN).build(), 16),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.CHERRY_SIGN))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.SIGN_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.DARK_OAK_SIGN, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.DARK_OAK_SIGN).build(), 16),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.DARK_OAK_SIGN))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.SIGN_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.MANGROVE_SIGN, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.MANGROVE_SIGN).build(), 16),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.MANGROVE_SIGN))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.SIGN_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BAMBOO_SIGN, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BAMBOO_SIGN).build(), 16),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.BAMBOO_SIGN))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.SIGN_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.OAK_HANGING_SIGN, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.OAK_HANGING_SIGN).build(), 16),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.OAK_HANGING_SIGN))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.HANGING_SIGN_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.SPRUCE_HANGING_SIGN, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.SPRUCE_HANGING_SIGN).build(), 16),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.SPRUCE_HANGING_SIGN))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.HANGING_SIGN_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BIRCH_HANGING_SIGN, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BIRCH_HANGING_SIGN).build(), 16),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.BIRCH_HANGING_SIGN))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.HANGING_SIGN_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.JUNGLE_HANGING_SIGN, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.JUNGLE_HANGING_SIGN).build(), 16),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.JUNGLE_HANGING_SIGN))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.HANGING_SIGN_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.ACACIA_HANGING_SIGN, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.ACACIA_HANGING_SIGN).build(), 16),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.ACACIA_HANGING_SIGN))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.HANGING_SIGN_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.CHERRY_HANGING_SIGN, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.CHERRY_HANGING_SIGN).build(), 16),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.CHERRY_HANGING_SIGN))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.HANGING_SIGN_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.DARK_OAK_HANGING_SIGN, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.DARK_OAK_HANGING_SIGN).build(), 16),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.DARK_OAK_HANGING_SIGN))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.HANGING_SIGN_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.MANGROVE_HANGING_SIGN, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.MANGROVE_HANGING_SIGN).build(), 16),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.MANGROVE_HANGING_SIGN))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.HANGING_SIGN_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BAMBOO_HANGING_SIGN, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BAMBOO_HANGING_SIGN).build(), 16),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.BAMBOO_HANGING_SIGN))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.HANGING_SIGN_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.WATER_BUCKET, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.WATER_BUCKET).build(), 1)
        ));
        registerable.register(ItemKeys.LAVA_BUCKET, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.LAVA_BUCKET).build(), 1),
            ItemComponentSet.builder()
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.LAVA_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.SNOWBALL, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.SNOWBALL).build(), 16),
            ItemComponentSet.builder()
                .with(new ThrowableItemComponent(1.5f, 0.0f))
                .with(new ProjectileItemComponent(entityTypes.getOrThrow(EntityTypeKeys.SNOWBALL)))
                .build()
        ));
        registerable.register(ItemKeys.DRIED_KELP_BLOCK, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.DRIED_KELP_BLOCK).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.DRIED_KELP_BLOCK))
                .with(new CompostableItemComponent(0.5f))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.DRIED_KELP_BLOCK_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BOOK, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.BOOK).build()),
            ItemComponentSet.builder()
                .with(EnchantableItemComponent.transforms(1, items.getOrThrow(ItemKeys.ENCHANTED_BOOK)))
                .build()
        ));
        registerable.register(ItemKeys.EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.EGG).build(), 16),
            ItemComponentSet.builder()
                .with(new ThrowableItemComponent(1.5f, 0.0f))
                .with(new ProjectileItemComponent(entityTypes.getOrThrow(EntityTypeKeys.EGG)))
                .build()
        ));
        registerable.register(ItemKeys.FISHING_ROD, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.FISHING_ROD).build(), 1),
            ItemComponentSet.builder()
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.COD, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.COD).build()),
            ItemComponentSet.builder()
                .with(FoodItemComponent.from(FoodComponents.COD))
                .build()
        ));
        registerable.register(ItemKeys.SALMON, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.SALMON).build()),
            ItemComponentSet.builder()
                .with(FoodItemComponent.from(FoodComponents.SALMON))
                .build()
        ));
        registerable.register(ItemKeys.TROPICAL_FISH, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.TROPICAL_FISH).build()),
            ItemComponentSet.builder()
                .with(FoodItemComponent.from(FoodComponents.TROPICAL_FISH))
                .build()
        ));
        registerable.register(ItemKeys.PUFFERFISH, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.PUFFERFISH).build()),
            ItemComponentSet.builder()
                .with(FoodItemComponent.from(FoodComponents.PUFFERFISH))
                .build()
        ));
        registerable.register(ItemKeys.COOKED_COD, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.COOKED_COD).build()),
            ItemComponentSet.builder()
                .with(FoodItemComponent.from(FoodComponents.COOKED_COD))
                .build()
        ));
        registerable.register(ItemKeys.COOKED_SALMON, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.COOKED_SALMON).build()),
            ItemComponentSet.builder()
                .with(FoodItemComponent.from(FoodComponents.COOKED_SALMON))
                .build()
        ));
        registerable.register(ItemKeys.COOKIE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.COOKIE).build()),
            ItemComponentSet.builder()
                .with(FoodItemComponent.from(FoodComponents.COOKIE))
                .with(new CompostableItemComponent(0.85f))
                .build()
        ));
        registerable.register(ItemKeys.MELON_SLICE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.MELON_SLICE).build()),
            ItemComponentSet.builder()
                .with(FoodItemComponent.from(FoodComponents.MELON_SLICE))
                .with(new CompostableItemComponent(0.5f))
                .build()
        ));
        registerable.register(ItemKeys.DRIED_KELP, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.DRIED_KELP).build()),
            ItemComponentSet.builder()
                .with(FoodItemComponent.from(FoodComponents.DRIED_KELP))
                .with(new CompostableItemComponent(0.3f))
                .build()
        ));
        registerable.register(ItemKeys.BEEF, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.BEEF).build()),
            ItemComponentSet.builder()
                .with(FoodItemComponent.from(FoodComponents.BEEF))
                .build()
        ));
        registerable.register(ItemKeys.COOKED_BEEF, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.COOKED_BEEF).build()),
            ItemComponentSet.builder()
                .with(FoodItemComponent.from(FoodComponents.COOKED_BEEF))
                .build()
        ));
        registerable.register(ItemKeys.CHICKEN, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.CHICKEN).build()),
            ItemComponentSet.builder()
                .with(FoodItemComponent.from(FoodComponents.CHICKEN))
                .build()
        ));
        registerable.register(ItemKeys.COOKED_CHICKEN, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.COOKED_CHICKEN).build()),
            ItemComponentSet.builder()
                .with(FoodItemComponent.from(FoodComponents.COOKED_CHICKEN))
                .build()
        ));
        registerable.register(ItemKeys.ROTTEN_FLESH, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.ROTTEN_FLESH).build()),
            ItemComponentSet.builder()
                .with(FoodItemComponent.from(FoodComponents.ROTTEN_FLESH))
                .build()
        ));
        registerable.register(ItemKeys.ENDER_PEARL, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.ENDER_PEARL).build(), 16),
            ItemComponentSet.builder()
                .with(new ThrowableItemComponent(1.5f, 0.0f))
                .with(new ProjectileItemComponent(entityTypes.getOrThrow(EntityTypeKeys.ENDER_PEARL)))
                .with(new CooldownItemComponent(20))
                .build()
        ));
        registerable.register(ItemKeys.BLAZE_ROD, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.BLAZE_ROD).build()),
            ItemComponentSet.builder()
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.BLAZE_ROD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.SPIDER_EYE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.SPIDER_EYE).build()),
            ItemComponentSet.builder()
                .with(FoodItemComponent.from(FoodComponents.SPIDER_EYE))
                .build()
        ));
        registerable.register(ItemKeys.PIG_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.PIG_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(new EntityItemComponent(EntityType.PIG))
                .build()
        ));
        registerable.register(ItemKeys.EXPERIENCE_BOTTLE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.EXPERIENCE_BOTTLE).build()),
            ItemComponentSet.builder()
                .with(new ThrowableItemComponent(0.7f, -20.0f))
                .with(new ProjectileItemComponent(entityTypes.getOrThrow(EntityTypeKeys.EXPERIENCE_BOTTLE)))
                .build()
        ));
        registerable.register(ItemKeys.CARROT, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.CARROT).build()),
            ItemComponentSet.builder()
                .with(FoodItemComponent.from(FoodComponents.CARROT))
                .with(new BlockItemComponent(Blocks.CARROTS))
                .with(new CompostableItemComponent(0.65f))
                .build()
        ));
        registerable.register(ItemKeys.POTATO, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.POTATO).build()),
            ItemComponentSet.builder()
                .with(FoodItemComponent.from(FoodComponents.POTATO))
                .with(new BlockItemComponent(Blocks.POTATOES))
                .with(new CompostableItemComponent(0.65f))
                .build()
        ));
        registerable.register(ItemKeys.BAKED_POTATO, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.BAKED_POTATO).build()),
            ItemComponentSet.builder()
                .with(FoodItemComponent.from(FoodComponents.BAKED_POTATO))
                .with(new CompostableItemComponent(0.85f))
                .build()
        ));
        registerable.register(ItemKeys.POISONOUS_POTATO, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.POISONOUS_POTATO).build()),
            ItemComponentSet.builder()
                .with(FoodItemComponent.from(FoodComponents.POISONOUS_POTATO))
                .build()
        ));
        registerable.register(ItemKeys.GOLDEN_CARROT, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.GOLDEN_CARROT).build()),
            ItemComponentSet.builder()
                .with(FoodItemComponent.from(FoodComponents.GOLDEN_CARROT))
                .build()
        ));
        registerable.register(ItemKeys.PUMPKIN_PIE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.PUMPKIN_PIE).build()),
            ItemComponentSet.builder()
                .with(FoodItemComponent.from(FoodComponents.PUMPKIN_PIE))
                .with(new CompostableItemComponent(1.0f))
                .build()
        ));
        registerable.register(ItemKeys.ENCHANTED_BOOK, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.ENCHANTED_BOOK).build(), 1),
            ItemComponentSet.builder()
                .with(new EnchantmentHolderItemComponent(EnchantedBookItem.STORED_ENCHANTMENTS_KEY))
                .with(new FoilItemComponent(true))
                .build()
        ));
        registerable.register(ItemKeys.RABBIT, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.RABBIT).build()),
            ItemComponentSet.builder()
                .with(FoodItemComponent.from(FoodComponents.RABBIT_STEW))
                .build()
        ));
        registerable.register(ItemKeys.COOKED_RABBIT, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.COOKED_RABBIT).build()),
            ItemComponentSet.builder()
                .with(FoodItemComponent.from(FoodComponents.COOKED_RABBIT))
                .build()
        ));
        registerable.register(ItemKeys.RABBIT_STEW, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.RABBIT_STEW).build(), 1),
            ItemComponentSet.builder()
                .with(FoodItemComponent.from(FoodComponents.RABBIT_STEW))
                .build()
        ));
        registerable.register(ItemKeys.MUTTON, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.MUTTON).build()),
            ItemComponentSet.builder()
                .with(FoodItemComponent.from(FoodComponents.MUTTON))
                .build()
        ));
        registerable.register(ItemKeys.COOKED_MUTTON, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.COOKED_MUTTON).build()),
            ItemComponentSet.builder()
                .with(FoodItemComponent.from(FoodComponents.COOKED_MUTTON))
                .build()
        ));
        registerable.register(ItemKeys.WHITE_BANNER, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.WHITE_BANNER).build(), 16),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.WHITE_BANNER))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.ORANGE_BANNER, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.ORANGE_BANNER).build(), 16),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.ORANGE_BANNER))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.MAGENTA_BANNER, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.MAGENTA_BANNER).build(), 16),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.MAGENTA_BANNER))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.LIGHT_BLUE_BANNER, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.LIGHT_BLUE_BANNER).build(), 16),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.LIGHT_BLUE_BANNER))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.YELLOW_BANNER, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.YELLOW_BANNER).build(), 16),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.YELLOW_BANNER))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.LIME_BANNER, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.LIME_BANNER).build(), 16),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.LIME_BANNER))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.PINK_BANNER, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.PINK_BANNER).build(), 16),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.PINK_BANNER))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.GRAY_BANNER, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.GRAY_BANNER).build(), 16),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.GRAY_BANNER))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.LIGHT_GRAY_BANNER, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.LIGHT_GRAY_BANNER).build(), 16),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.LIGHT_GRAY_BANNER))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.CYAN_BANNER, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.CYAN_BANNER).build(), 16),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.CYAN_BANNER))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.PURPLE_BANNER, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.PURPLE_BANNER).build(), 16),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.PURPLE_BANNER))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BLUE_BANNER, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BLUE_BANNER).build(), 16),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.BLUE_BANNER))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BROWN_BANNER, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BROWN_BANNER).build(), 16),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.BROWN_BANNER))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.GREEN_BANNER, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.GREEN_BANNER).build(), 16),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.GREEN_BANNER))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.RED_BANNER, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.RED_BANNER).build(), 16),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.RED_BANNER))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BLACK_BANNER, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BLACK_BANNER).build(), 16),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.BLACK_BANNER))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.CHORUS_FRUIT, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.CHORUS_FRUIT).build()),
            ItemComponentSet.builder()
                .with(FoodItemComponent.from(FoodComponents.CHORUS_FRUIT))
                .build()
        ));
        registerable.register(ItemKeys.BEETROOT, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.BEETROOT).build()),
            ItemComponentSet.builder()
                .with(FoodItemComponent.from(FoodComponents.BEETROOT))
                .with(new CompostableItemComponent(0.65f))
                .build()
        ));
        registerable.register(ItemKeys.BEETROOT_SOUP, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.BEETROOT_SOUP).build(), 1),
            ItemComponentSet.builder()
                .with(FoodItemComponent.from(FoodComponents.BEETROOT_SOUP))
                .build()
        ));
        registerable.register(ItemKeys.SHIELD, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.SHIELD).build(), 1),
            ItemComponentSet.builder()
                .with(new EquipmentItemComponent(EquipmentSlot.OFFHAND, false))
                .with(new RepairableItemComponent(ItemTagsUtil.REPAIRS_SHIELD))
                .build()
        ));
        registerable.register(ItemKeys.CROSSBOW, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.CROSSBOW).build(), 1),
            ItemComponentSet.builder()
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.SUSPICIOUS_STEW, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.SUSPICIOUS_STEW).build(), 1),
            ItemComponentSet.builder()
                .with(FoodItemComponent.from(FoodComponents.SUSPICIOUS_STEW))
                .build()
        ));
        registerable.register(ItemKeys.LOOM, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.LOOM).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.LOOM))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.COMPOSTER, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.COMPOSTER).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.COMPOSTER))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BARREL, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BARREL).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.BARREL))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.CARTOGRAPHY_TABLE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.CARTOGRAPHY_TABLE).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.CARTOGRAPHY_TABLE))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.FLETCHING_TABLE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.FLETCHING_TABLE).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.FLETCHING_TABLE))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.SMITHING_TABLE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.SMITHING_TABLE).build()),
            ItemComponentSet.builder()
                .with(new BlockItemComponent(Blocks.SMITHING_TABLE))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.SWEET_BERRIES, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.SWEET_BERRIES).build()),
            ItemComponentSet.builder()
                .with(FoodItemComponent.from(FoodComponents.SWEET_BERRIES))
                .with(new BlockItemComponent(Blocks.SWEET_BERRY_BUSH))
                .with(new CompostableItemComponent(0.3f))
                .build()
        ));
        registerable.register(ItemKeys.GLOW_BERRIES, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.GLOW_BERRIES).build()),
            ItemComponentSet.builder()
                .with(FoodItemComponent.from(FoodComponents.GLOW_BERRIES))
                .with(new BlockItemComponent(Blocks.CAVE_VINES))
                .with(new CompostableItemComponent(0.3f))
                .build()
        ));
        registerable.register(ItemKeys.HONEY_BOTTLE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.HONEY_BOTTLE).build()),
            ItemComponentSet.builder()
                .with(FoodItemComponent.from(FoodComponents.HONEY_BOTTLE, 40))
                .build()
        ));
    }

    public static RegistryKey<Item> keyFromBlock(Block block) {
        Identifier id = Registries.BLOCK.getId(block);
        return RegistryKey.of(RegistryKeys.ITEM, id);
    }

    private static Item create(ItemBase base) {
        return create(base, new ItemComponentSet());
    }

    private static Item create(ItemBase base, ItemComponentSet components) {
        Item item = new Item(new Item.Settings());
        item.setItemBase(base);
        item.setComponents(components);
        return item;
    }
}
