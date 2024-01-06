package net.errorcraft.itematic.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.block.BlockKeys;
import net.errorcraft.itematic.block.ItematicBlockTags;
import net.errorcraft.itematic.block.entity.FurnaceBlockEntityUtil;
import net.errorcraft.itematic.enchantment.EnchantmentTags;
import net.errorcraft.itematic.entity.EntityTypeKeys;
import net.errorcraft.itematic.entity.initializer.initializers.*;
import net.errorcraft.itematic.fluid.FluidKeys;
import net.errorcraft.itematic.item.armor.ArmorMaterial;
import net.errorcraft.itematic.item.armor.ArmorMaterialKeys;
import net.errorcraft.itematic.item.color.colors.*;
import net.errorcraft.itematic.item.component.ItemComponentSet;
import net.errorcraft.itematic.item.component.components.*;
import net.errorcraft.itematic.item.dispense.behavior.DispenseBehaviorKeys;
import net.errorcraft.itematic.item.event.ItemEventMap;
import net.errorcraft.itematic.item.event.ItemEvents;
import net.errorcraft.itematic.item.pointer.Pointer;
import net.errorcraft.itematic.item.pointer.PointerKeys;
import net.errorcraft.itematic.item.smithing.template.SmithingTemplate;
import net.errorcraft.itematic.item.smithing.template.SmithingTemplates;
import net.errorcraft.itematic.mixin.item.CrossbowItemAccessor;
import net.errorcraft.itematic.mixin.item.MilkBucketItemAccessor;
import net.errorcraft.itematic.mixin.item.PotionItemAccessor;
import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.errorcraft.itematic.sound.SoundEventKeys;
import net.errorcraft.itematic.util.Vec3dProvider;
import net.errorcraft.itematic.world.action.ActionEntry;
import net.errorcraft.itematic.world.action.ActionRequirements;
import net.errorcraft.itematic.world.action.Actions;
import net.errorcraft.itematic.world.action.actions.*;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameters;
import net.errorcraft.itematic.world.action.sequence.handler.handlers.FirstToPassRequirementsSequenceHandler;
import net.errorcraft.itematic.world.action.sequence.handler.handlers.PassingSequenceHandler;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.client.color.world.FoliageColors;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.decoration.GlowItemFrameEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.SpectralArrowEntity;
import net.minecraft.entity.vehicle.*;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.*;
import net.minecraft.loot.condition.LocationCheckLootCondition;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.predicate.BlockPredicate;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.predicate.entity.LocationPredicate;
import net.minecraft.registry.*;
import net.minecraft.registry.tag.BannerPatternTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.state.property.Properties;
import net.minecraft.util.*;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.Direction;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

public class ItemUtil {
    public static final Codec<Item> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
        ItemBase.CODEC.fieldOf("base").forGetter(Item::itematic$itemBase),
        ItemComponentSet.CODEC.fieldOf("components").forGetter(Item::itematic$components),
        Codecs.createStrictOptionalFieldCodec(ItemEventMap.CODEC, "events", ItemEventMap.EMPTY).forGetter(Item::itematic$events)
    ).apply(instance, ItemUtil::create));

    public static void bootstrap(Registerable<Item> registerable) {
        RegistryEntryLookup<ArmorMaterial> armorMaterials = registerable.getRegistryLookup(ItematicRegistryKeys.ARMOR_MATERIAL);
        RegistryEntryLookup<Item> items = registerable.getRegistryLookup(RegistryKeys.ITEM);
        RegistryEntryLookup<EntityType<?>> entityTypes = registerable.getRegistryLookup(RegistryKeys.ENTITY_TYPE);
        RegistryEntryLookup<Biome> biomes = registerable.getRegistryLookup(RegistryKeys.BIOME);
        RegistryEntryLookup<Block> blocks = registerable.getRegistryLookup(RegistryKeys.BLOCK);
        RegistryEntryLookup<DispenserBehavior> dispenseBehaviors = registerable.getRegistryLookup(ItematicRegistryKeys.DISPENSE_BEHAVIOR);
        RegistryEntryLookup<SoundEvent> soundEvents = registerable.getRegistryLookup(RegistryKeys.SOUND_EVENT);
        RegistryEntryLookup<Fluid> fluids = registerable.getRegistryLookup(RegistryKeys.FLUID);
        RegistryEntryLookup<Pointer> pointers = registerable.getRegistryLookup(ItematicRegistryKeys.POINTER);
        RegistryEntryLookup<ActionEntry> actions = registerable.getRegistryLookup(ItematicRegistryKeys.ACTION);
        RegistryEntryLookup<SmithingTemplate> smithingTemplates = registerable.getRegistryLookup(ItematicRegistryKeys.SMITHING_TEMPLATE);

        registerable.register(ItemKeys.AIR, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.AIR).build())
        ));
        registerable.register(ItemKeys.STONE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.STONE).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.STONE)))
                .build()
        ));
        registerable.register(ItemKeys.GRASS_BLOCK, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.GRASS_BLOCK).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.GRASS_BLOCK)))
                .with(new TintedItemComponent(new GrassItemColor(biomes.getOrThrow(BiomeKeys.PLAINS))))
                .build()
        ));
        registerable.register(ItemKeys.CRIMSON_NYLIUM, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.CRIMSON_NYLIUM).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.CRIMSON_NYLIUM)))
                .build()
        ));
        registerable.register(ItemKeys.WARPED_NYLIUM, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.WARPED_NYLIUM).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.WARPED_NYLIUM)))
                .build()
        ));
        registerable.register(ItemKeys.COBBLESTONE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.COBBLESTONE).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.COBBLESTONE)))
                .build()
        ));
        registerable.register(ItemKeys.OAK_PLANKS, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.OAK_PLANKS).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.OAK_PLANKS)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.SPRUCE_PLANKS, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.SPRUCE_PLANKS).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.SPRUCE_PLANKS)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BIRCH_PLANKS, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BIRCH_PLANKS).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.BIRCH_PLANKS)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.JUNGLE_PLANKS, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.JUNGLE_PLANKS).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.JUNGLE_PLANKS)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.ACACIA_PLANKS, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.ACACIA_PLANKS).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.ACACIA_PLANKS)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.CHERRY_PLANKS, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.CHERRY_PLANKS).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.CHERRY_PLANKS)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.DARK_OAK_PLANKS, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.DARK_OAK_PLANKS).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.DARK_OAK_PLANKS)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.MANGROVE_PLANKS, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.MANGROVE_PLANKS).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.MANGROVE_PLANKS)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BAMBOO_PLANKS, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BAMBOO_PLANKS).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.BAMBOO_PLANKS)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.CRIMSON_PLANKS, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.CRIMSON_PLANKS).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.CRIMSON_PLANKS)))
                .build()
        ));
        registerable.register(ItemKeys.WARPED_PLANKS, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.WARPED_PLANKS).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.WARPED_PLANKS)))
                .build()
        ));
        registerable.register(ItemKeys.BAMBOO_MOSAIC, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BAMBOO_MOSAIC).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.BAMBOO_MOSAIC)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.OAK_SAPLING, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.OAK_SAPLING).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.OAK_SAPLING)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.PLANT_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.SPRUCE_SAPLING, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.SPRUCE_SAPLING).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.SPRUCE_SAPLING)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.PLANT_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BIRCH_SAPLING, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BIRCH_SAPLING).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.BIRCH_SAPLING)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.PLANT_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.JUNGLE_SAPLING, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.JUNGLE_SAPLING).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.JUNGLE_SAPLING)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.PLANT_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.ACACIA_SAPLING, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.ACACIA_SAPLING).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.ACACIA_SAPLING)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.PLANT_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.CHERRY_SAPLING, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.CHERRY_SAPLING).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.CHERRY_SAPLING)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.PLANT_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.DARK_OAK_SAPLING, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.DARK_OAK_SAPLING).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.DARK_OAK_SAPLING)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.PLANT_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.MANGROVE_PROPAGULE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.MANGROVE_PROPAGULE).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.MANGROVE_PROPAGULE)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.PLANT_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.SAND, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.SAND).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.SAND)))
                .build()
        ));
        registerable.register(ItemKeys.COAL_BLOCK, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.COAL_BLOCK).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.COAL_BLOCK)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.COAL_BLOCK_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.OAK_LOG, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.OAK_LOG).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.OAK_LOG)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.SPRUCE_LOG, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.SPRUCE_LOG).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.SPRUCE_LOG)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BIRCH_LOG, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BIRCH_LOG).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.BIRCH_LOG)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.JUNGLE_LOG, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.JUNGLE_LOG).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.JUNGLE_LOG)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.ACACIA_LOG, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.ACACIA_LOG).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.ACACIA_LOG)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.CHERRY_LOG, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.CHERRY_LOG).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.CHERRY_LOG)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.DARK_OAK_LOG, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.DARK_OAK_LOG).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.DARK_OAK_LOG)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.MANGROVE_LOG, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.MANGROVE_LOG).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.MANGROVE_LOG)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.MANGROVE_ROOTS, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.MANGROVE_ROOTS).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.MANGROVE_ROOTS)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.CRIMSON_STEM, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.CRIMSON_STEM).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.CRIMSON_STEM)))
                .build()
        ));
        registerable.register(ItemKeys.WARPED_STEM, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.WARPED_STEM).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.WARPED_STEM)))
                .build()
        ));
        registerable.register(ItemKeys.BAMBOO_BLOCK, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BAMBOO_BLOCK).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.BAMBOO_BLOCK)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.STRIPPED_OAK_LOG, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.STRIPPED_OAK_LOG).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.STRIPPED_OAK_LOG)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.STRIPPED_SPRUCE_LOG, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.STRIPPED_SPRUCE_LOG).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.STRIPPED_SPRUCE_LOG)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.STRIPPED_BIRCH_LOG, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.STRIPPED_BIRCH_LOG).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.STRIPPED_BIRCH_LOG)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.STRIPPED_JUNGLE_LOG, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.STRIPPED_JUNGLE_LOG).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.STRIPPED_JUNGLE_LOG)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.STRIPPED_ACACIA_LOG, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.STRIPPED_ACACIA_LOG).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.STRIPPED_ACACIA_LOG)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.STRIPPED_CHERRY_LOG, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.STRIPPED_CHERRY_LOG).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.STRIPPED_CHERRY_LOG)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.STRIPPED_DARK_OAK_LOG, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.STRIPPED_DARK_OAK_LOG).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.STRIPPED_DARK_OAK_LOG)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.STRIPPED_MANGROVE_LOG, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.STRIPPED_MANGROVE_LOG).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.STRIPPED_MANGROVE_LOG)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.STRIPPED_CRIMSON_STEM, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.STRIPPED_CRIMSON_STEM).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.STRIPPED_CRIMSON_STEM)))
                .build()
        ));
        registerable.register(ItemKeys.STRIPPED_WARPED_STEM, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.STRIPPED_WARPED_STEM).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.STRIPPED_WARPED_STEM)))
                .build()
        ));
        registerable.register(ItemKeys.STRIPPED_OAK_WOOD, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.STRIPPED_OAK_WOOD).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.STRIPPED_OAK_WOOD)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.STRIPPED_SPRUCE_WOOD, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.STRIPPED_SPRUCE_WOOD).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.STRIPPED_SPRUCE_WOOD)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.STRIPPED_BIRCH_WOOD, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.STRIPPED_BIRCH_WOOD).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.STRIPPED_BIRCH_WOOD)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.STRIPPED_JUNGLE_WOOD, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.STRIPPED_JUNGLE_WOOD).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.STRIPPED_JUNGLE_WOOD)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.STRIPPED_ACACIA_WOOD, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.STRIPPED_ACACIA_WOOD).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.STRIPPED_ACACIA_WOOD)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.STRIPPED_CHERRY_WOOD, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.STRIPPED_CHERRY_WOOD).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.STRIPPED_CHERRY_WOOD)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.STRIPPED_DARK_OAK_WOOD, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.STRIPPED_DARK_OAK_WOOD).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.STRIPPED_DARK_OAK_WOOD)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.STRIPPED_MANGROVE_WOOD, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.STRIPPED_MANGROVE_WOOD).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.STRIPPED_MANGROVE_WOOD)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.STRIPPED_CRIMSON_HYPHAE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.STRIPPED_CRIMSON_HYPHAE).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.STRIPPED_CRIMSON_HYPHAE)))
                .build()
        ));
        registerable.register(ItemKeys.STRIPPED_WARPED_HYPHAE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.STRIPPED_WARPED_HYPHAE).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.STRIPPED_WARPED_HYPHAE)))
                .build()
        ));
        registerable.register(ItemKeys.STRIPPED_BAMBOO_BLOCK, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.STRIPPED_BAMBOO_BLOCK).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.STRIPPED_BAMBOO_BLOCK)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.OAK_WOOD, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.OAK_WOOD).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.OAK_WOOD)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.SPRUCE_WOOD, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.SPRUCE_WOOD).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.SPRUCE_WOOD)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BIRCH_WOOD, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BIRCH_WOOD).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.BIRCH_WOOD)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.JUNGLE_WOOD, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.JUNGLE_WOOD).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.JUNGLE_WOOD)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.ACACIA_WOOD, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.ACACIA_WOOD).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.ACACIA_WOOD)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.CHERRY_WOOD, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.CHERRY_WOOD).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.CHERRY_WOOD)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.DARK_OAK_WOOD, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.DARK_OAK_WOOD).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.DARK_OAK_WOOD)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.MANGROVE_WOOD, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.MANGROVE_WOOD).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.MANGROVE_WOOD)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.CRIMSON_HYPHAE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.CRIMSON_HYPHAE).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.CRIMSON_HYPHAE)))
                .build()
        ));
        registerable.register(ItemKeys.WARPED_HYPHAE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.WARPED_HYPHAE).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.WARPED_HYPHAE)))
                .build()
        ));
        registerable.register(ItemKeys.OAK_LEAVES, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.OAK_LEAVES).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.OAK_LEAVES)))
                .with(new CompostableItemComponent(0.3f))
                .with(new TintedItemComponent(new FoliageItemColor(biomes.getOrThrow(BiomeKeys.PLAINS))))
                .build()
        ));
        registerable.register(ItemKeys.SPRUCE_LEAVES, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.SPRUCE_LEAVES).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.SPRUCE_LEAVES)))
                .with(new CompostableItemComponent(0.3f))
                .with(new TintedItemComponent(new ConstantItemColor(FoliageColors.getSpruceColor())))
                .build()
        ));
        registerable.register(ItemKeys.BIRCH_LEAVES, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BIRCH_LEAVES).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.BIRCH_LEAVES)))
                .with(new CompostableItemComponent(0.3f))
                .with(new TintedItemComponent(new ConstantItemColor(FoliageColors.getBirchColor())))
                .build()
        ));
        registerable.register(ItemKeys.JUNGLE_LEAVES, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.JUNGLE_LEAVES).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.JUNGLE_LEAVES)))
                .with(new CompostableItemComponent(0.3f))
                .with(new TintedItemComponent(new FoliageItemColor(biomes.getOrThrow(BiomeKeys.PLAINS))))
                .build()
        ));
        registerable.register(ItemKeys.ACACIA_LEAVES, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.ACACIA_LEAVES).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.ACACIA_LEAVES)))
                .with(new CompostableItemComponent(0.3f))
                .with(new TintedItemComponent(new FoliageItemColor(biomes.getOrThrow(BiomeKeys.PLAINS))))
                .build()
        ));
        registerable.register(ItemKeys.DARK_OAK_LEAVES, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.DARK_OAK_LEAVES).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.DARK_OAK_LEAVES)))
                .with(new CompostableItemComponent(0.3f))
                .with(new TintedItemComponent(new FoliageItemColor(biomes.getOrThrow(BiomeKeys.PLAINS))))
                .build()
        ));
        registerable.register(ItemKeys.MANGROVE_LEAVES, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.MANGROVE_LEAVES).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.MANGROVE_LEAVES)))
                .with(new CompostableItemComponent(0.3f))
                .with(new TintedItemComponent(new ConstantItemColor(FoliageColors.getMangroveColor())))
                .build()
        ));
        registerable.register(ItemKeys.SHORT_GRASS, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.SHORT_GRASS).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.SHORT_GRASS)))
                .with(new CompostableItemComponent(0.3f))
                .with(new TintedItemComponent(new GrassItemColor(biomes.getOrThrow(BiomeKeys.PLAINS))))
                .build()
        ));
        registerable.register(ItemKeys.FERN, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.FERN).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.FERN)))
                .with(new CompostableItemComponent(0.65f))
                .with(new TintedItemComponent(new GrassItemColor(biomes.getOrThrow(BiomeKeys.PLAINS))))
                .build()
        ));
        registerable.register(ItemKeys.AZALEA, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.AZALEA).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.AZALEA)))
                .with(new CompostableItemComponent(0.65f))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.PLANT_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.FLOWERING_AZALEA, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.FLOWERING_AZALEA).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.FLOWERING_AZALEA)))
                .with(new CompostableItemComponent(0.85f))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.PLANT_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.DEAD_BUSH, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.DEAD_BUSH).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.DEAD_BUSH)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.PLANT_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.WHITE_WOOL, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.WHITE_WOOL).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.WHITE_WOOL)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOL_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.ORANGE_WOOL, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.ORANGE_WOOL).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.ORANGE_WOOL)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOL_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.MAGENTA_WOOL, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.MAGENTA_WOOL).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.MAGENTA_WOOL)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOL_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.LIGHT_BLUE_WOOL, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.LIGHT_BLUE_WOOL).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.LIGHT_BLUE_WOOL)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOL_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.YELLOW_WOOL, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.YELLOW_WOOL).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.YELLOW_WOOL)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOL_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.LIME_WOOL, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.LIME_WOOL).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.LIME_WOOL)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOL_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.PINK_WOOL, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.PINK_WOOL).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.PINK_WOOL)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOL_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.GRAY_WOOL, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.GRAY_WOOL).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.GRAY_WOOL)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOL_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.LIGHT_GRAY_WOOL, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.LIGHT_GRAY_WOOL).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.LIGHT_GRAY_WOOL)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOL_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.CYAN_WOOL, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.CYAN_WOOL).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.CYAN_WOOL)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOL_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.PURPLE_WOOL, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.PURPLE_WOOL).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.PURPLE_WOOL)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOL_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BLUE_WOOL, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BLUE_WOOL).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.BLUE_WOOL)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOL_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BROWN_WOOL, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BROWN_WOOL).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.BROWN_WOOL)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOL_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.GREEN_WOOL, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.GREEN_WOOL).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.GREEN_WOOL)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOL_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.RED_WOOL, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.RED_WOOL).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.RED_WOOL)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOL_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BLACK_WOOL, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BLACK_WOOL).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.BLACK_WOOL)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOL_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.CRIMSON_FUNGUS, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.CRIMSON_FUNGUS).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.CRIMSON_FUNGUS)))
                .with(new CompostableItemComponent(0.65f))
                .build()
        ));
        registerable.register(ItemKeys.WARPED_FUNGUS, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.WARPED_FUNGUS).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.WARPED_FUNGUS)))
                .with(new CompostableItemComponent(0.65f))
                .build()
        ));
        registerable.register(ItemKeys.CRIMSON_ROOTS, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.CRIMSON_ROOTS).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.CRIMSON_ROOTS)))
                .with(new CompostableItemComponent(0.65f))
                .build()
        ));
        registerable.register(ItemKeys.WARPED_ROOTS, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.WARPED_ROOTS).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.WARPED_ROOTS)))
                .with(new CompostableItemComponent(0.65f))
                .build()
        ));
        registerable.register(ItemKeys.HANGING_ROOTS, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.HANGING_ROOTS).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.HANGING_ROOTS)))
                .with(new CompostableItemComponent(0.3f))
                .build()
        ));
        registerable.register(ItemKeys.BAMBOO, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BAMBOO).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.BAMBOO)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.BAMBOO_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.OAK_SLAB, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.OAK_SLAB).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.OAK_SLAB)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.SLAB_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.SPRUCE_SLAB, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.SPRUCE_SLAB).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.SPRUCE_SLAB)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.SLAB_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BIRCH_SLAB, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BIRCH_SLAB).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.BIRCH_SLAB)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.SLAB_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.JUNGLE_SLAB, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.JUNGLE_SLAB).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.JUNGLE_SLAB)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.SLAB_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.ACACIA_SLAB, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.ACACIA_SLAB).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.ACACIA_SLAB)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.SLAB_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.CHERRY_SLAB, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.CHERRY_SLAB).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.CHERRY_SLAB)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.SLAB_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.DARK_OAK_SLAB, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.DARK_OAK_SLAB).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.DARK_OAK_SLAB)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.SLAB_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.MANGROVE_SLAB, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.MANGROVE_SLAB).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.MANGROVE_SLAB)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.SLAB_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BAMBOO_SLAB, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BAMBOO_SLAB).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.BAMBOO_SLAB)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.SLAB_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BAMBOO_MOSAIC_SLAB, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BAMBOO_MOSAIC_SLAB).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.BAMBOO_MOSAIC_SLAB)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.SLAB_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.CRIMSON_SLAB, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.CRIMSON_SLAB).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.CRIMSON_SLAB)))
                .build()
        ));
        registerable.register(ItemKeys.WARPED_SLAB, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.WARPED_SLAB).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.WARPED_SLAB)))
                .build()
        ));
        registerable.register(ItemKeys.BRICKS, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BRICKS).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.BRICKS)))
                .build()
        ));
        registerable.register(ItemKeys.BOOKSHELF, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BOOKSHELF).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.BOOKSHELF)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.CHISELED_BOOKSHELF, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.CHISELED_BOOKSHELF).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.CHISELED_BOOKSHELF)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.CHEST, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.CHEST).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.CHEST)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.CRAFTING_TABLE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.CRAFTING_TABLE).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.CRAFTING_TABLE)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.LADDER, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.LADDER).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.LADDER)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.SNOW, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.SNOW).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.SNOW)))
                .build()
        ));
        registerable.register(ItemKeys.JUKEBOX, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.JUKEBOX).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.JUKEBOX)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.OAK_FENCE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.OAK_FENCE).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.OAK_FENCE)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.SPRUCE_FENCE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.SPRUCE_FENCE).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.SPRUCE_FENCE)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BIRCH_FENCE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BIRCH_FENCE).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.BIRCH_FENCE)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.JUNGLE_FENCE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.JUNGLE_FENCE).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.JUNGLE_FENCE)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.ACACIA_FENCE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.ACACIA_FENCE).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.ACACIA_FENCE)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.CHERRY_FENCE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.CHERRY_FENCE).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.CHERRY_FENCE)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.DARK_OAK_FENCE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.DARK_OAK_FENCE).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.DARK_OAK_FENCE)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.MANGROVE_FENCE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.MANGROVE_FENCE).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.MANGROVE_FENCE)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BAMBOO_FENCE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BAMBOO_FENCE).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.BAMBOO_FENCE)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.CRIMSON_FENCE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.CRIMSON_FENCE).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.CRIMSON_FENCE)))
                .build()
        ));
        registerable.register(ItemKeys.WARPED_FENCE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.WARPED_FENCE).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.WARPED_FENCE)))
                .build()
        ));
        registerable.register(ItemKeys.VINE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.VINE).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.VINE)))
                .with(new CompostableItemComponent(0.5f))
                .with(new TintedItemComponent(new FoliageItemColor(biomes.getOrThrow(BiomeKeys.PLAINS))))
                .build()
        ));
        registerable.register(ItemKeys.LILY_PAD, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.LILY_PAD).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.LILY_PAD)))
                .with(new CanPlaceOnFluidsItemComponent(RaycastContext.FluidHandling.SOURCE_ONLY, false, Direction.UP.getVector()))
                .with(new CompostableItemComponent(0.65f))
                .with(new TintedItemComponent(new ConstantItemColor(0x71c35c)))
                .build()
        ));
        registerable.register(ItemKeys.OAK_STAIRS, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.OAK_STAIRS).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.OAK_STAIRS)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.SPRUCE_STAIRS, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.SPRUCE_STAIRS).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.SPRUCE_STAIRS)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BIRCH_STAIRS, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BIRCH_STAIRS).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.BIRCH_STAIRS)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.JUNGLE_STAIRS, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.JUNGLE_STAIRS).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.JUNGLE_STAIRS)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.ACACIA_STAIRS, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.ACACIA_STAIRS).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.ACACIA_STAIRS)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.CHERRY_STAIRS, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.CHERRY_STAIRS).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.CHERRY_STAIRS)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.DARK_OAK_STAIRS, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.DARK_OAK_STAIRS).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.DARK_OAK_STAIRS)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.MANGROVE_STAIRS, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.MANGROVE_STAIRS).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.MANGROVE_STAIRS)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BAMBOO_STAIRS, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BAMBOO_STAIRS).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.BAMBOO_STAIRS)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BAMBOO_MOSAIC_STAIRS, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BAMBOO_MOSAIC_STAIRS).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.BAMBOO_MOSAIC_STAIRS)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.CRIMSON_STAIRS, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.CRIMSON_STAIRS).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.CRIMSON_STAIRS)))
                .build()
        ));
        registerable.register(ItemKeys.WARPED_STAIRS, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.WARPED_STAIRS).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.WARPED_STAIRS)))
                .build()
        ));
        registerable.register(ItemKeys.COMMAND_BLOCK, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.COMMAND_BLOCK).rarity(Rarity.EPIC).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.operator(blocks.getOrThrow(BlockKeys.COMMAND_BLOCK)))
                .build()
        ));
        registerable.register(ItemKeys.BEACON, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BEACON).rarity(Rarity.RARE).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.BEACON)))
                .build()
        ));
        registerable.register(ItemKeys.BARRIER, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BARRIER).rarity(Rarity.EPIC).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.BARRIER)))
                .build()
        ));
        registerable.register(ItemKeys.WHITE_CARPET, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.WHITE_CARPET).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.WHITE_CARPET)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOL_CARPET_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.ORANGE_CARPET, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.ORANGE_CARPET).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.ORANGE_CARPET)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOL_CARPET_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.MAGENTA_CARPET, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.MAGENTA_CARPET).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.MAGENTA_CARPET)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOL_CARPET_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.LIGHT_BLUE_CARPET, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.LIGHT_BLUE_CARPET).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.LIGHT_BLUE_CARPET)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOL_CARPET_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.YELLOW_CARPET, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.YELLOW_CARPET).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.YELLOW_CARPET)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOL_CARPET_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.LIME_CARPET, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.LIME_CARPET).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.LIME_CARPET)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOL_CARPET_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.PINK_CARPET, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.PINK_CARPET).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.PINK_CARPET)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOL_CARPET_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.GRAY_CARPET, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.GRAY_CARPET).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.GRAY_CARPET)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOL_CARPET_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.LIGHT_GRAY_CARPET, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.LIGHT_GRAY_CARPET).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.LIGHT_GRAY_CARPET)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOL_CARPET_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.CYAN_CARPET, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.CYAN_CARPET).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.CYAN_CARPET)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOL_CARPET_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.PURPLE_CARPET, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.PURPLE_CARPET).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.PURPLE_CARPET)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOL_CARPET_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BLUE_CARPET, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BLUE_CARPET).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.BLUE_CARPET)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOL_CARPET_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BROWN_CARPET, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BROWN_CARPET).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.BROWN_CARPET)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOL_CARPET_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.GREEN_CARPET, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.GREEN_CARPET).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.GREEN_CARPET)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOL_CARPET_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.RED_CARPET, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.RED_CARPET).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.RED_CARPET)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOL_CARPET_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BLACK_CARPET, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BLACK_CARPET).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.BLACK_CARPET)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOL_CARPET_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.TALL_GRASS, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.TALL_GRASS).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.TALL_GRASS)))
                .with(new CompostableItemComponent(0.5f))
                .with(new TintedItemComponent(new GrassItemColor(biomes.getOrThrow(BiomeKeys.PLAINS))))
                .build()
        ));
        registerable.register(ItemKeys.LARGE_FERN, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.LARGE_FERN).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.LARGE_FERN)))
                .with(new CompostableItemComponent(0.65f))
                .with(new TintedItemComponent(new GrassItemColor(biomes.getOrThrow(BiomeKeys.PLAINS))))
                .build()
        ));
        registerable.register(ItemKeys.NETHER_WART_BLOCK, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.NETHER_WART_BLOCK).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.NETHER_WART_BLOCK)))
                .with(new CompostableItemComponent(0.85f))
                .build()
        ));
        registerable.register(ItemKeys.WARPED_WART_BLOCK, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.WARPED_WART_BLOCK).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.WARPED_WART_BLOCK)))
                .with(new CompostableItemComponent(0.85f))
                .build()
        ));
        registerable.register(ItemKeys.SCAFFOLDING, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.SCAFFOLDING).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.SCAFFOLDING)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.SCAFFOLDING_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.REDSTONE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.REDSTONE).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.REDSTONE_WIRE)))
                .build()
        ));
        registerable.register(ItemKeys.LECTERN, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.LECTERN).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.LECTERN)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.DAYLIGHT_DETECTOR, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.DAYLIGHT_DETECTOR).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.DAYLIGHT_DETECTOR)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.TRAPPED_CHEST, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.TRAPPED_CHEST).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.TRAPPED_CHEST)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.NOTE_BLOCK, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.NOTE_BLOCK).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.NOTE_BLOCK)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.OAK_BUTTON, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.OAK_BUTTON).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.OAK_BUTTON)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.BUTTON_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.SPRUCE_BUTTON, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.SPRUCE_BUTTON).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.SPRUCE_BUTTON)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.BUTTON_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BIRCH_BUTTON, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BIRCH_BUTTON).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.BIRCH_BUTTON)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.BUTTON_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.JUNGLE_BUTTON, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.JUNGLE_BUTTON).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.JUNGLE_BUTTON)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.BUTTON_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.ACACIA_BUTTON, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.ACACIA_BUTTON).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.ACACIA_BUTTON)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.BUTTON_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.CHERRY_BUTTON, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.CHERRY_BUTTON).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.CHERRY_BUTTON)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.BUTTON_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.DARK_OAK_BUTTON, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.DARK_OAK_BUTTON).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.DARK_OAK_BUTTON)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.BUTTON_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.MANGROVE_BUTTON, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.MANGROVE_BUTTON).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.MANGROVE_BUTTON)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.BUTTON_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BAMBOO_BUTTON, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BAMBOO_BUTTON).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.BAMBOO_BUTTON)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.BUTTON_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.CRIMSON_BUTTON, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.CRIMSON_BUTTON).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.CRIMSON_BUTTON)))
                .build()
        ));
        registerable.register(ItemKeys.WARPED_BUTTON, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.WARPED_BUTTON).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.WARPED_BUTTON)))
                .build()
        ));
        registerable.register(ItemKeys.OAK_PRESSURE_PLATE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.OAK_PRESSURE_PLATE).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.OAK_PRESSURE_PLATE)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.SPRUCE_PRESSURE_PLATE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.SPRUCE_PRESSURE_PLATE).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.SPRUCE_PRESSURE_PLATE)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BIRCH_PRESSURE_PLATE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BIRCH_PRESSURE_PLATE).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.BIRCH_PRESSURE_PLATE)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.JUNGLE_PRESSURE_PLATE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.JUNGLE_PRESSURE_PLATE).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.JUNGLE_PRESSURE_PLATE)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.ACACIA_PRESSURE_PLATE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.ACACIA_PRESSURE_PLATE).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.ACACIA_PRESSURE_PLATE)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.CHERRY_PRESSURE_PLATE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.CHERRY_PRESSURE_PLATE).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.CHERRY_PRESSURE_PLATE)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.DARK_OAK_PRESSURE_PLATE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.DARK_OAK_PRESSURE_PLATE).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.DARK_OAK_PRESSURE_PLATE)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.MANGROVE_PRESSURE_PLATE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.MANGROVE_PRESSURE_PLATE).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.MANGROVE_PRESSURE_PLATE)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BAMBOO_PRESSURE_PLATE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BAMBOO_PRESSURE_PLATE).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.BAMBOO_PRESSURE_PLATE)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.CRIMSON_PRESSURE_PLATE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.CRIMSON_PRESSURE_PLATE).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.CRIMSON_PRESSURE_PLATE)))
                .build()
        ));
        registerable.register(ItemKeys.WARPED_PRESSURE_PLATE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.WARPED_PRESSURE_PLATE).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.WARPED_PRESSURE_PLATE)))
                .build()
        ));
        registerable.register(ItemKeys.OAK_DOOR, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.OAK_DOOR).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.OAK_DOOR)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.DOOR_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.SPRUCE_DOOR, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.SPRUCE_DOOR).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.SPRUCE_DOOR)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.DOOR_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BIRCH_DOOR, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BIRCH_DOOR).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.BIRCH_DOOR)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.DOOR_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.JUNGLE_DOOR, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.JUNGLE_DOOR).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.JUNGLE_DOOR)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.DOOR_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.ACACIA_DOOR, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.ACACIA_DOOR).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.ACACIA_DOOR)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.DOOR_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.CHERRY_DOOR, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.CHERRY_DOOR).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.CHERRY_DOOR)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.DOOR_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.DARK_OAK_DOOR, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.DARK_OAK_DOOR).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.DARK_OAK_DOOR)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.DOOR_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.MANGROVE_DOOR, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.MANGROVE_DOOR).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.MANGROVE_DOOR)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.DOOR_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BAMBOO_DOOR, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BAMBOO_DOOR).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.BAMBOO_DOOR)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.DOOR_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.CRIMSON_DOOR, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.CRIMSON_DOOR).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.CRIMSON_DOOR)))
                .build()
        ));
        registerable.register(ItemKeys.WARPED_DOOR, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.WARPED_DOOR).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.WARPED_DOOR)))
                .build()
        ));
        registerable.register(ItemKeys.OAK_TRAPDOOR, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.OAK_TRAPDOOR).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.OAK_TRAPDOOR)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.SPRUCE_TRAPDOOR, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.SPRUCE_TRAPDOOR).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.SPRUCE_TRAPDOOR)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BIRCH_TRAPDOOR, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BIRCH_TRAPDOOR).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.BIRCH_TRAPDOOR)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.JUNGLE_TRAPDOOR, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.JUNGLE_TRAPDOOR).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.JUNGLE_TRAPDOOR)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.ACACIA_TRAPDOOR, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.ACACIA_TRAPDOOR).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.ACACIA_TRAPDOOR)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.CHERRY_TRAPDOOR, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.CHERRY_TRAPDOOR).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.CHERRY_TRAPDOOR)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.DARK_OAK_TRAPDOOR, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.DARK_OAK_TRAPDOOR).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.DARK_OAK_TRAPDOOR)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.MANGROVE_TRAPDOOR, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.MANGROVE_TRAPDOOR).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.MANGROVE_TRAPDOOR)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BAMBOO_TRAPDOOR, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BAMBOO_TRAPDOOR).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.BAMBOO_TRAPDOOR)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.CRIMSON_TRAPDOOR, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.CRIMSON_TRAPDOOR).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.CRIMSON_TRAPDOOR)))
                .build()
        ));
        registerable.register(ItemKeys.WARPED_TRAPDOOR, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.WARPED_TRAPDOOR).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.WARPED_TRAPDOOR)))
                .build()
        ));
        registerable.register(ItemKeys.OAK_FENCE_GATE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.OAK_FENCE_GATE).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.OAK_FENCE_GATE)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.SPRUCE_FENCE_GATE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.SPRUCE_FENCE_GATE).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.SPRUCE_FENCE_GATE)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BIRCH_FENCE_GATE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BIRCH_FENCE_GATE).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.BIRCH_FENCE_GATE)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.JUNGLE_FENCE_GATE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.JUNGLE_FENCE_GATE).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.JUNGLE_FENCE_GATE)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.ACACIA_FENCE_GATE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.ACACIA_FENCE_GATE).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.ACACIA_FENCE_GATE)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.CHERRY_FENCE_GATE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.CHERRY_FENCE_GATE).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.CHERRY_FENCE_GATE)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.DARK_OAK_FENCE_GATE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.DARK_OAK_FENCE_GATE).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.DARK_OAK_FENCE_GATE)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.MANGROVE_FENCE_GATE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.MANGROVE_FENCE_GATE).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.MANGROVE_FENCE_GATE)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BAMBOO_FENCE_GATE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BAMBOO_FENCE_GATE).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.BAMBOO_FENCE_GATE)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.CRIMSON_FENCE_GATE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.CRIMSON_FENCE_GATE).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.CRIMSON_FENCE_GATE)))
                .build()
        ));
        registerable.register(ItemKeys.WARPED_FENCE_GATE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.WARPED_FENCE_GATE).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.WARPED_FENCE_GATE)))
                .build()
        ));
        registerable.register(ItemKeys.SADDLE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.SADDLE).build(), 1),
            ItemComponentSet.builder()
                .with(SaddleItemComponent.INSTANCE)
                .with(new DispensableItemComponent(dispenseBehaviors.getOrThrow(DispenseBehaviorKeys.SADDLE)))
                .build()
        ));
        registerable.register(ItemKeys.MINECART, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.MINECART).build(), 1),
            ItemComponentSet.builder()
                .with(EntityItemComponent.from(new MinecartEntityInitializer<>(EntityType.MINECART, MinecartEntity::new), dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.CHEST_MINECART, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.CHEST_MINECART).build(), 1),
            ItemComponentSet.builder()
                .with(EntityItemComponent.from(new MinecartEntityInitializer<>(EntityType.CHEST_MINECART, ChestMinecartEntity::new), dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.FURNACE_MINECART, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.FURNACE_MINECART).build(), 1),
            ItemComponentSet.builder()
                .with(EntityItemComponent.from(new MinecartEntityInitializer<>(EntityType.FURNACE_MINECART, FurnaceMinecartEntity::new), dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.TNT_MINECART, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.TNT_MINECART).build(), 1),
            ItemComponentSet.builder()
                .with(EntityItemComponent.from(new MinecartEntityInitializer<>(EntityType.TNT_MINECART, TntMinecartEntity::new), dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.HOPPER_MINECART, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.HOPPER_MINECART).build(), 1),
            ItemComponentSet.builder()
                .with(EntityItemComponent.from(new MinecartEntityInitializer<>(EntityType.HOPPER_MINECART, HopperMinecartEntity::new), dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.CARROT_ON_A_STICK, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.CARROT_ON_A_STICK).build(), 1),
            ItemComponentSet.builder()
                .with(new DamageableItemComponent(25))
                .with(ForgeableItemComponent.of(EnchantmentTags.STEERING_FORGING))
                .with(new SteeringItemComponent(entityTypes.getOrThrow(EntityTypeKeys.PIG), 7))
                .build(),
            ItemEventMap.builder()
                .add(ItemEvents.BREAK_ITEM, ActionEntry.of(new ExchangeItemAction(items.getOrThrow(ItemKeys.FISHING_ROD), false)))
                .build()
        ));
        registerable.register(ItemKeys.WARPED_FUNGUS_ON_A_STICK, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.WARPED_FUNGUS_ON_A_STICK).build(), 1),
            ItemComponentSet.builder()
                .with(new DamageableItemComponent(100))
                .with(ForgeableItemComponent.of(EnchantmentTags.STEERING_FORGING))
                .with(new SteeringItemComponent(entityTypes.getOrThrow(EntityTypeKeys.STRIDER), 1))
                .build(),
            ItemEventMap.builder()
                .add(ItemEvents.BREAK_ITEM, ActionEntry.of(new ExchangeItemAction(items.getOrThrow(ItemKeys.FISHING_ROD), false)))
                .build()
        ));
        registerable.register(ItemKeys.ELYTRA, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.ELYTRA).rarity(Rarity.UNCOMMON).build(), 1),
            ItemComponentSet.builder()
                .with(new DamageableItemComponent(432, true))
                .with(new EquipmentItemComponent(EquipmentSlot.CHEST, true, soundEvents.getOrThrow(SoundEventKeys.ARMOR_EQUIP_ELYTRA)))
                .with(new RepairableItemComponent(ItematicItemTags.REPAIRS_ELYTRA))
                .with(new DispensableItemComponent(dispenseBehaviors.getOrThrow(DispenseBehaviorKeys.ARMOR)))
                .build()
        ));
        registerable.register(ItemKeys.OAK_BOAT, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.OAK_BOAT).build(), 1),
            ItemComponentSet.builder()
                .with(EntityItemComponent.from(new BoatEntityInitializer(BoatEntity.Type.OAK), dispenseBehaviors))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.BOAT_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.OAK_CHEST_BOAT, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.OAK_CHEST_BOAT).build(), 1),
            ItemComponentSet.builder()
                .with(EntityItemComponent.from(new ChestBoatEntityInitializer(BoatEntity.Type.OAK), dispenseBehaviors))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.BOAT_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.SPRUCE_BOAT, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.SPRUCE_BOAT).build(), 1),
            ItemComponentSet.builder()
                .with(EntityItemComponent.from(new BoatEntityInitializer(BoatEntity.Type.SPRUCE), dispenseBehaviors))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.BOAT_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.SPRUCE_CHEST_BOAT, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.SPRUCE_CHEST_BOAT).build(), 1),
            ItemComponentSet.builder()
                .with(EntityItemComponent.from(new ChestBoatEntityInitializer(BoatEntity.Type.SPRUCE), dispenseBehaviors))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.BOAT_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BIRCH_BOAT, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.BIRCH_BOAT).build(), 1),
            ItemComponentSet.builder()
                .with(EntityItemComponent.from(new BoatEntityInitializer(BoatEntity.Type.BIRCH), dispenseBehaviors))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.BOAT_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BIRCH_CHEST_BOAT, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.BIRCH_CHEST_BOAT).build(), 1),
            ItemComponentSet.builder()
                .with(EntityItemComponent.from(new ChestBoatEntityInitializer(BoatEntity.Type.BIRCH), dispenseBehaviors))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.BOAT_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.JUNGLE_BOAT, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.JUNGLE_BOAT).build(), 1),
            ItemComponentSet.builder()
                .with(EntityItemComponent.from(new BoatEntityInitializer(BoatEntity.Type.JUNGLE), dispenseBehaviors))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.BOAT_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.JUNGLE_CHEST_BOAT, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.JUNGLE_CHEST_BOAT).build(), 1),
            ItemComponentSet.builder()
                .with(EntityItemComponent.from(new ChestBoatEntityInitializer(BoatEntity.Type.JUNGLE), dispenseBehaviors))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.BOAT_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.ACACIA_BOAT, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.ACACIA_BOAT).build(), 1),
            ItemComponentSet.builder()
                .with(EntityItemComponent.from(new BoatEntityInitializer(BoatEntity.Type.ACACIA), dispenseBehaviors))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.BOAT_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.ACACIA_CHEST_BOAT, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.ACACIA_CHEST_BOAT).build(), 1),
            ItemComponentSet.builder()
                .with(EntityItemComponent.from(new ChestBoatEntityInitializer(BoatEntity.Type.ACACIA), dispenseBehaviors))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.BOAT_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.CHERRY_BOAT, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.CHERRY_BOAT).build(), 1),
            ItemComponentSet.builder()
                .with(EntityItemComponent.from(new BoatEntityInitializer(BoatEntity.Type.CHERRY), dispenseBehaviors))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.BOAT_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.CHERRY_CHEST_BOAT, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.CHERRY_CHEST_BOAT).build(), 1),
            ItemComponentSet.builder()
                .with(EntityItemComponent.from(new ChestBoatEntityInitializer(BoatEntity.Type.CHERRY), dispenseBehaviors))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.BOAT_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.DARK_OAK_BOAT, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.DARK_OAK_BOAT).build(), 1),
            ItemComponentSet.builder()
                .with(EntityItemComponent.from(new BoatEntityInitializer(BoatEntity.Type.DARK_OAK), dispenseBehaviors))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.BOAT_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.DARK_OAK_CHEST_BOAT, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.DARK_OAK_CHEST_BOAT).build(), 1),
            ItemComponentSet.builder()
                .with(EntityItemComponent.from(new ChestBoatEntityInitializer(BoatEntity.Type.DARK_OAK), dispenseBehaviors))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.BOAT_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.MANGROVE_BOAT, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.MANGROVE_BOAT).build(), 1),
            ItemComponentSet.builder()
                .with(EntityItemComponent.from(new BoatEntityInitializer(BoatEntity.Type.MANGROVE), dispenseBehaviors))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.BOAT_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.MANGROVE_CHEST_BOAT, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.MANGROVE_CHEST_BOAT).build(), 1),
            ItemComponentSet.builder()
                .with(EntityItemComponent.from(new ChestBoatEntityInitializer(BoatEntity.Type.MANGROVE), dispenseBehaviors))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.BOAT_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BAMBOO_RAFT, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.BAMBOO_RAFT).build(), 1),
            ItemComponentSet.builder()
                .with(EntityItemComponent.from(new BoatEntityInitializer(BoatEntity.Type.BAMBOO), dispenseBehaviors))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.BOAT_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BAMBOO_CHEST_RAFT, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.BAMBOO_CHEST_RAFT).build(), 1),
            ItemComponentSet.builder()
                .with(EntityItemComponent.from(new ChestBoatEntityInitializer(BoatEntity.Type.BAMBOO), dispenseBehaviors))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.BOAT_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.TURTLE_HELMET, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.TURTLE_HELMET).build(), 1),
            ItemComponentSet.builder()
                .with(ArmorItemComponent.from(ArmorMaterials.TURTLE, ArmorItem.Type.HELMET, armorMaterials.getOrThrow(ArmorMaterialKeys.TURTLE), soundEvents.getOrThrow(SoundEventKeys.ARMOR_EQUIP_TURTLE)))
                .with(EnchantableItemComponent.enchants(ArmorMaterials.TURTLE, EnchantmentTags.HELMET_ENCHANTING))
                .with(ForgeableItemComponent.of(EnchantmentTags.HELMET_FORGING))
                .with(new RepairableItemComponent(ItematicItemTags.REPAIRS_TURTLE_ARMOR))
                .with(new DispensableItemComponent(dispenseBehaviors.getOrThrow(DispenseBehaviorKeys.ARMOR)))
                .build()
        ));
        registerable.register(ItemKeys.TURTLE_SCUTE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.TURTLE_SCUTE).build())
        ));
        registerable.register(ItemKeys.FLINT_AND_STEEL, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.FLINT_AND_STEEL).build(), 1),
            ItemComponentSet.builder()
                .with(new DamageableItemComponent(64))
                .with(new DispensableItemComponent(dispenseBehaviors.getOrThrow(DispenseBehaviorKeys.USE_ON_BLOCK)))
                .build(),
            ItemEventMap.builder()
                .add(ItemEvents.USE_ON_BLOCK, ActionEntry.of(
                    PassingSequenceHandler.builder()
                        .add(actions.getOrThrow(Actions.LIGHT_BLOCK))
                        .add(DamageItemAction.of(1))
                        .add(PlaySoundAction.builder(ActionContextParameter.TARGET, soundEvents.getOrThrow(SoundEventKeys.FLINT_AND_STEEL_USE), SoundCategory.BLOCKS)
                            .pitch(0.8f, 1.2f)
                            .build())
                ))
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
                .with(new DamageableItemComponent(384))
                .with(new ShooterItemComponent(ItematicItemTags.BOW_AMMUNITION, ItematicItemTags.BOW_AMMUNITION, false))
                .with(new UseDurationItemComponent(72000))
                .with(new UseAnimationItemComponent(UseAction.BOW))
                .with(EnchantableItemComponent.enchants(1, EnchantmentTags.BOW_ENCHANTING))
                .with(ForgeableItemComponent.of(EnchantmentTags.BOW_FORGING))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.ARROW, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.ARROW).build()),
            ItemComponentSet.builder()
                .with(ProjectileItemComponent.persistentProjectile(EntityType.ARROW, ArrowEntity::new, ArrowEntity::new))
                .with(new DispensableItemComponent(dispenseBehaviors.getOrThrow(DispenseBehaviorKeys.PROJECTILE)))
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
        registerable.register(ItemKeys.EMERALD, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.EMERALD).build())
        ));
        registerable.register(ItemKeys.LAPIS_LAZULI, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.LAPIS_LAZULI).build())
        ));
        registerable.register(ItemKeys.QUARTZ, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.QUARTZ).build())
        ));
        registerable.register(ItemKeys.AMETHYST_SHARD, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.AMETHYST_SHARD).build())
        ));
        registerable.register(ItemKeys.IRON_INGOT, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.IRON_INGOT).build())
        ));
        registerable.register(ItemKeys.COPPER_INGOT, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.COPPER_INGOT).build())
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
                .with(DamageableItemComponent.sword(ToolMaterials.WOOD, ItematicItemTags.REPAIRS_WOODEN_TOOL))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.TOOL_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.WOODEN_SHOVEL, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.WOODEN_SHOVEL).build(), 1),
            ItemComponentSet.builder()
                .with(DamageableItemComponent.shovel(ToolMaterials.WOOD, ItematicItemTags.REPAIRS_WOODEN_TOOL))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.TOOL_FUEL_TIME))
                .build(),
            ItemEventMap.builder()
                .add(ItemEvents.USE_ON_BLOCK, actions.getOrThrow(Actions.USE_SHOVEL_ON_BLOCK))
                .build()
        ));
        registerable.register(ItemKeys.WOODEN_PICKAXE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.WOODEN_PICKAXE).build(), 1),
            ItemComponentSet.builder()
                .with(DamageableItemComponent.pickaxe(ToolMaterials.WOOD, ItematicItemTags.REPAIRS_WOODEN_TOOL))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.TOOL_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.WOODEN_AXE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.WOODEN_AXE).build(), 1),
            ItemComponentSet.builder()
                .with(DamageableItemComponent.axe(ToolMaterials.WOOD, ItematicItemTags.REPAIRS_WOODEN_TOOL))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.TOOL_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.WOODEN_HOE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.WOODEN_HOE).build(), 1),
            ItemComponentSet.builder()
                .with(DamageableItemComponent.hoe(ToolMaterials.WOOD, ItematicItemTags.REPAIRS_WOODEN_TOOL))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.TOOL_FUEL_TIME))
                .build(),
            ItemEventMap.builder()
                .add(ItemEvents.USE_ON_BLOCK, actions.getOrThrow(Actions.USE_HOE_ON_BLOCK))
                .build()
        ));
        registerable.register(ItemKeys.STONE_SWORD, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.STONE_SWORD).build(), 1),
            ItemComponentSet.builder()
                .with(DamageableItemComponent.sword(ToolMaterials.STONE, ItematicItemTags.REPAIRS_STONE_TOOL))
                .build()
        ));
        registerable.register(ItemKeys.STONE_SHOVEL, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.STONE_SHOVEL).build(), 1),
            ItemComponentSet.builder()
                .with(DamageableItemComponent.shovel(ToolMaterials.STONE, ItematicItemTags.REPAIRS_STONE_TOOL))
                .build(),
            ItemEventMap.builder()
                .add(ItemEvents.USE_ON_BLOCK, actions.getOrThrow(Actions.USE_SHOVEL_ON_BLOCK))
                .build()
        ));
        registerable.register(ItemKeys.STONE_PICKAXE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.STONE_PICKAXE).build(), 1),
            ItemComponentSet.builder()
                .with(DamageableItemComponent.pickaxe(ToolMaterials.STONE, ItematicItemTags.REPAIRS_STONE_TOOL))
                .build()
        ));
        registerable.register(ItemKeys.STONE_AXE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.STONE_AXE).build(), 1),
            ItemComponentSet.builder()
                .with(DamageableItemComponent.axe(ToolMaterials.STONE, ItematicItemTags.REPAIRS_STONE_TOOL))
                .build()
        ));
        registerable.register(ItemKeys.STONE_HOE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.STONE_HOE).build(), 1),
            ItemComponentSet.builder()
                .with(DamageableItemComponent.hoe(ToolMaterials.STONE, ItematicItemTags.REPAIRS_STONE_TOOL))
                .build(),
            ItemEventMap.builder()
                .add(ItemEvents.USE_ON_BLOCK, actions.getOrThrow(Actions.USE_HOE_ON_BLOCK))
                .build()
        ));
        registerable.register(ItemKeys.GOLDEN_SWORD, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.GOLDEN_SWORD).build(), 1),
            ItemComponentSet.builder()
                .with(DamageableItemComponent.sword(ToolMaterials.GOLD, ItematicItemTags.REPAIRS_GOLDEN_TOOL))
                .build()
        ));
        registerable.register(ItemKeys.GOLDEN_SHOVEL, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.GOLDEN_SHOVEL).build(), 1),
            ItemComponentSet.builder()
                .with(DamageableItemComponent.shovel(ToolMaterials.GOLD, ItematicItemTags.REPAIRS_GOLDEN_TOOL))
                .build(),
            ItemEventMap.builder()
                .add(ItemEvents.USE_ON_BLOCK, actions.getOrThrow(Actions.USE_SHOVEL_ON_BLOCK))
                .build()
        ));
        registerable.register(ItemKeys.GOLDEN_PICKAXE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.GOLDEN_PICKAXE).build(), 1),
            ItemComponentSet.builder()
                .with(DamageableItemComponent.pickaxe(ToolMaterials.GOLD, ItematicItemTags.REPAIRS_GOLDEN_TOOL))
                .build()
        ));
        registerable.register(ItemKeys.GOLDEN_AXE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.GOLDEN_AXE).build(), 1),
            ItemComponentSet.builder()
                .with(DamageableItemComponent.axe(ToolMaterials.GOLD, ItematicItemTags.REPAIRS_GOLDEN_TOOL))
                .build()
        ));
        registerable.register(ItemKeys.GOLDEN_HOE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.GOLDEN_HOE).build(), 1),
            ItemComponentSet.builder()
                .with(DamageableItemComponent.hoe(ToolMaterials.GOLD, ItematicItemTags.REPAIRS_GOLDEN_TOOL))
                .build(),
            ItemEventMap.builder()
                .add(ItemEvents.USE_ON_BLOCK, actions.getOrThrow(Actions.USE_HOE_ON_BLOCK))
                .build()
        ));
        registerable.register(ItemKeys.IRON_SWORD, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.IRON_SWORD).build(), 1),
            ItemComponentSet.builder()
                .with(DamageableItemComponent.sword(ToolMaterials.IRON, ItematicItemTags.REPAIRS_IRON_TOOL))
                .build()
        ));
        registerable.register(ItemKeys.IRON_SHOVEL, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.IRON_SHOVEL).build(), 1),
            ItemComponentSet.builder()
                .with(DamageableItemComponent.shovel(ToolMaterials.IRON, ItematicItemTags.REPAIRS_IRON_TOOL))
                .build(),
            ItemEventMap.builder()
                .add(ItemEvents.USE_ON_BLOCK, actions.getOrThrow(Actions.USE_SHOVEL_ON_BLOCK))
                .build()
        ));
        registerable.register(ItemKeys.IRON_PICKAXE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.IRON_PICKAXE).build(), 1),
            ItemComponentSet.builder()
                .with(DamageableItemComponent.pickaxe(ToolMaterials.IRON, ItematicItemTags.REPAIRS_IRON_TOOL))
                .build()
        ));
        registerable.register(ItemKeys.IRON_AXE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.IRON_AXE).build(), 1),
            ItemComponentSet.builder()
                .with(DamageableItemComponent.axe(ToolMaterials.IRON, ItematicItemTags.REPAIRS_IRON_TOOL))
                .build()
        ));
        registerable.register(ItemKeys.IRON_HOE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.IRON_HOE).build(), 1),
            ItemComponentSet.builder()
                .with(DamageableItemComponent.hoe(ToolMaterials.IRON, ItematicItemTags.REPAIRS_IRON_TOOL))
                .build(),
            ItemEventMap.builder()
                .add(ItemEvents.USE_ON_BLOCK, actions.getOrThrow(Actions.USE_HOE_ON_BLOCK))
                .build()
        ));
        registerable.register(ItemKeys.DIAMOND_SWORD, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.DIAMOND_SWORD).build(), 1),
            ItemComponentSet.builder()
                .with(DamageableItemComponent.sword(ToolMaterials.DIAMOND, ItematicItemTags.REPAIRS_DIAMOND_TOOL))
                .build()
        ));
        registerable.register(ItemKeys.DIAMOND_SHOVEL, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.DIAMOND_SHOVEL).build(), 1),
            ItemComponentSet.builder()
                .with(DamageableItemComponent.shovel(ToolMaterials.DIAMOND, ItematicItemTags.REPAIRS_DIAMOND_TOOL))
                .build(),
            ItemEventMap.builder()
                .add(ItemEvents.USE_ON_BLOCK, actions.getOrThrow(Actions.USE_SHOVEL_ON_BLOCK))
                .build()
        ));
        registerable.register(ItemKeys.DIAMOND_PICKAXE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.DIAMOND_PICKAXE).build(), 1),
            ItemComponentSet.builder()
                .with(DamageableItemComponent.pickaxe(ToolMaterials.DIAMOND, ItematicItemTags.REPAIRS_DIAMOND_TOOL))
                .build()
        ));
        registerable.register(ItemKeys.DIAMOND_AXE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.DIAMOND_AXE).build(), 1),
            ItemComponentSet.builder()
                .with(DamageableItemComponent.axe(ToolMaterials.DIAMOND, ItematicItemTags.REPAIRS_DIAMOND_TOOL))
                .build()
        ));
        registerable.register(ItemKeys.DIAMOND_HOE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.DIAMOND_HOE).build(), 1),
            ItemComponentSet.builder()
                .with(DamageableItemComponent.hoe(ToolMaterials.DIAMOND, ItematicItemTags.REPAIRS_DIAMOND_TOOL))
                .build(),
            ItemEventMap.builder()
                .add(ItemEvents.USE_ON_BLOCK, actions.getOrThrow(Actions.USE_HOE_ON_BLOCK))
                .build()
        ));
        registerable.register(ItemKeys.NETHERITE_SWORD, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.NETHERITE_SWORD).build(), 1),
            ItemComponentSet.builder()
                .with(DamageableItemComponent.sword(ToolMaterials.NETHERITE, ItematicItemTags.REPAIRS_NETHERITE_TOOL))
                .build()
        ));
        registerable.register(ItemKeys.NETHERITE_SHOVEL, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.NETHERITE_SHOVEL).build(), 1),
            ItemComponentSet.builder()
                .with(DamageableItemComponent.shovel(ToolMaterials.NETHERITE, ItematicItemTags.REPAIRS_NETHERITE_TOOL))
                .build(),
            ItemEventMap.builder()
                .add(ItemEvents.USE_ON_BLOCK, actions.getOrThrow(Actions.USE_SHOVEL_ON_BLOCK))
                .build()
        ));
        registerable.register(ItemKeys.NETHERITE_PICKAXE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.NETHERITE_PICKAXE).build(), 1),
            ItemComponentSet.builder()
                .with(DamageableItemComponent.pickaxe(ToolMaterials.NETHERITE, ItematicItemTags.REPAIRS_NETHERITE_TOOL))
                .build()
        ));
        registerable.register(ItemKeys.NETHERITE_AXE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.NETHERITE_AXE).build(), 1),
            ItemComponentSet.builder()
                .with(DamageableItemComponent.axe(ToolMaterials.NETHERITE, ItematicItemTags.REPAIRS_NETHERITE_TOOL))
                .build()
        ));
        registerable.register(ItemKeys.NETHERITE_HOE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.NETHERITE_HOE).build(), 1),
            ItemComponentSet.builder()
                .with(DamageableItemComponent.hoe(ToolMaterials.NETHERITE, ItematicItemTags.REPAIRS_NETHERITE_TOOL))
                .build(),
            ItemEventMap.builder()
                .add(ItemEvents.USE_ON_BLOCK, actions.getOrThrow(Actions.USE_HOE_ON_BLOCK))
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
                .with(FoodItemComponent.from(FoodComponents.MUSHROOM_STEW, items.getOrThrow(ItemKeys.BOWL)))
                .build()
        ));
        registerable.register(ItemKeys.FEATHER, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.FEATHER).build()),
            ItemComponentSet.builder()
                .with(new FireworkShapeModifierItemComponent(FireworkRocketItem.Type.BURST))
                .build()
        ));
        registerable.register(ItemKeys.GUNPOWDER, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.GUNPOWDER).build())
        ));
        registerable.register(ItemKeys.WHEAT_SEEDS, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.WHEAT_SEEDS).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.WHEAT)))
                .with(new CompostableItemComponent(0.3f))
                .build()
        ));
        registerable.register(ItemKeys.WHEAT, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.WHEAT).build())
        ));
        registerable.register(ItemKeys.BREAD, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.BREAD).build()),
            ItemComponentSet.builder()
                .with(FoodItemComponent.from(FoodComponents.BREAD))
                .with(new CompostableItemComponent(0.85f))
                .build()
        ));
        registerable.register(ItemKeys.LEATHER_HELMET, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.LEATHER_HELMET).build(), 1),
            ItemComponentSet.builder()
                .with(ArmorItemComponent.from(ArmorMaterials.LEATHER, ArmorItem.Type.HELMET, armorMaterials.getOrThrow(ArmorMaterialKeys.LEATHER), soundEvents.getOrThrow(SoundEventKeys.ARMOR_EQUIP_LEATHER)))
                .with(EnchantableItemComponent.enchants(ArmorMaterials.LEATHER, EnchantmentTags.HELMET_ENCHANTING))
                .with(ForgeableItemComponent.of(EnchantmentTags.HELMET_FORGING))
                .with(new RepairableItemComponent(ItematicItemTags.REPAIRS_LEATHER_ARMOR))
                .with(new DispensableItemComponent(dispenseBehaviors.getOrThrow(DispenseBehaviorKeys.ARMOR)))
                .with(new DyeableItemComponent(DyeableItem.DEFAULT_COLOR))
                .with(new TintedItemComponent(DyeableItemColor.INSTANCE))
                .build()
        ));
        registerable.register(ItemKeys.LEATHER_CHESTPLATE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.LEATHER_CHESTPLATE).build(), 1),
            ItemComponentSet.builder()
                .with(ArmorItemComponent.from(ArmorMaterials.LEATHER, ArmorItem.Type.CHESTPLATE, armorMaterials.getOrThrow(ArmorMaterialKeys.LEATHER), soundEvents.getOrThrow(SoundEventKeys.ARMOR_EQUIP_LEATHER)))
                .with(EnchantableItemComponent.enchants(ArmorMaterials.LEATHER, EnchantmentTags.CHESTPLATE_ENCHANTING))
                .with(ForgeableItemComponent.of(EnchantmentTags.CHESTPLATE_FORGING))
                .with(new RepairableItemComponent(ItematicItemTags.REPAIRS_LEATHER_ARMOR))
                .with(new DispensableItemComponent(dispenseBehaviors.getOrThrow(DispenseBehaviorKeys.ARMOR)))
                .with(new DyeableItemComponent(DyeableItem.DEFAULT_COLOR))
                .with(new TintedItemComponent(DyeableItemColor.INSTANCE))
                .build()
        ));
        registerable.register(ItemKeys.LEATHER_LEGGINGS, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.LEATHER_LEGGINGS).build(), 1),
            ItemComponentSet.builder()
                .with(ArmorItemComponent.from(ArmorMaterials.LEATHER, ArmorItem.Type.LEGGINGS, armorMaterials.getOrThrow(ArmorMaterialKeys.LEATHER), soundEvents.getOrThrow(SoundEventKeys.ARMOR_EQUIP_LEATHER)))
                .with(EnchantableItemComponent.enchants(ArmorMaterials.LEATHER, EnchantmentTags.LEGGINGS_ENCHANTING))
                .with(ForgeableItemComponent.of(EnchantmentTags.LEGGINGS_FORGING))
                .with(new RepairableItemComponent(ItematicItemTags.REPAIRS_LEATHER_ARMOR))
                .with(new DispensableItemComponent(dispenseBehaviors.getOrThrow(DispenseBehaviorKeys.ARMOR)))
                .with(new DyeableItemComponent(DyeableItem.DEFAULT_COLOR))
                .with(new TintedItemComponent(DyeableItemColor.INSTANCE))
                .build()
        ));
        registerable.register(ItemKeys.LEATHER_BOOTS, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.LEATHER_BOOTS).build(), 1),
            ItemComponentSet.builder()
                .with(ArmorItemComponent.from(ArmorMaterials.LEATHER, ArmorItem.Type.BOOTS, armorMaterials.getOrThrow(ArmorMaterialKeys.LEATHER), soundEvents.getOrThrow(SoundEventKeys.ARMOR_EQUIP_LEATHER)))
                .with(EnchantableItemComponent.enchants(ArmorMaterials.LEATHER, EnchantmentTags.BOOTS_ENCHANTING))
                .with(ForgeableItemComponent.of(EnchantmentTags.BOOTS_FORGING))
                .with(new RepairableItemComponent(ItematicItemTags.REPAIRS_LEATHER_ARMOR))
                .with(new DispensableItemComponent(dispenseBehaviors.getOrThrow(DispenseBehaviorKeys.ARMOR)))
                .with(new DyeableItemComponent(DyeableItem.DEFAULT_COLOR))
                .with(new TintedItemComponent(DyeableItemColor.INSTANCE))
                .build()
        ));
        registerable.register(ItemKeys.CHAINMAIL_HELMET, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.CHAINMAIL_HELMET).build(), 1),
            ItemComponentSet.builder()
                .with(ArmorItemComponent.from(ArmorMaterials.CHAIN, ArmorItem.Type.HELMET, armorMaterials.getOrThrow(ArmorMaterialKeys.CHAINMAIL), soundEvents.getOrThrow(SoundEventKeys.ARMOR_EQUIP_CHAIN)))
                .with(EnchantableItemComponent.enchants(ArmorMaterials.CHAIN, EnchantmentTags.HELMET_ENCHANTING))
                .with(ForgeableItemComponent.of(EnchantmentTags.HELMET_FORGING))
                .with(new RepairableItemComponent(ItematicItemTags.REPAIRS_CHAINMAIL_ARMOR))
                .with(new DispensableItemComponent(dispenseBehaviors.getOrThrow(DispenseBehaviorKeys.ARMOR)))
                .build()
        ));
        registerable.register(ItemKeys.CHAINMAIL_CHESTPLATE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.CHAINMAIL_CHESTPLATE).build(), 1),
            ItemComponentSet.builder()
                .with(ArmorItemComponent.from(ArmorMaterials.CHAIN, ArmorItem.Type.CHESTPLATE, armorMaterials.getOrThrow(ArmorMaterialKeys.CHAINMAIL), soundEvents.getOrThrow(SoundEventKeys.ARMOR_EQUIP_CHAIN)))
                .with(EnchantableItemComponent.enchants(ArmorMaterials.CHAIN, EnchantmentTags.CHESTPLATE_ENCHANTING))
                .with(ForgeableItemComponent.of(EnchantmentTags.CHESTPLATE_FORGING))
                .with(new RepairableItemComponent(ItematicItemTags.REPAIRS_CHAINMAIL_ARMOR))
                .with(new DispensableItemComponent(dispenseBehaviors.getOrThrow(DispenseBehaviorKeys.ARMOR)))
                .build()
        ));
        registerable.register(ItemKeys.CHAINMAIL_LEGGINGS, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.CHAINMAIL_LEGGINGS).build(), 1),
            ItemComponentSet.builder()
                .with(ArmorItemComponent.from(ArmorMaterials.CHAIN, ArmorItem.Type.LEGGINGS, armorMaterials.getOrThrow(ArmorMaterialKeys.CHAINMAIL), soundEvents.getOrThrow(SoundEventKeys.ARMOR_EQUIP_CHAIN)))
                .with(EnchantableItemComponent.enchants(ArmorMaterials.CHAIN, EnchantmentTags.LEGGINGS_ENCHANTING))
                .with(ForgeableItemComponent.of(EnchantmentTags.LEGGINGS_FORGING))
                .with(new RepairableItemComponent(ItematicItemTags.REPAIRS_CHAINMAIL_ARMOR))
                .with(new DispensableItemComponent(dispenseBehaviors.getOrThrow(DispenseBehaviorKeys.ARMOR)))
                .build()
        ));
        registerable.register(ItemKeys.CHAINMAIL_BOOTS, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.CHAINMAIL_BOOTS).build(), 1),
            ItemComponentSet.builder()
                .with(ArmorItemComponent.from(ArmorMaterials.CHAIN, ArmorItem.Type.BOOTS, armorMaterials.getOrThrow(ArmorMaterialKeys.CHAINMAIL), soundEvents.getOrThrow(SoundEventKeys.ARMOR_EQUIP_CHAIN)))
                .with(EnchantableItemComponent.enchants(ArmorMaterials.CHAIN, EnchantmentTags.BOOTS_ENCHANTING))
                .with(ForgeableItemComponent.of(EnchantmentTags.BOOTS_FORGING))
                .with(new RepairableItemComponent(ItematicItemTags.REPAIRS_CHAINMAIL_ARMOR))
                .with(new DispensableItemComponent(dispenseBehaviors.getOrThrow(DispenseBehaviorKeys.ARMOR)))
                .build()
        ));
        registerable.register(ItemKeys.IRON_HELMET, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.IRON_HELMET).build(), 1),
            ItemComponentSet.builder()
                .with(ArmorItemComponent.from(ArmorMaterials.IRON, ArmorItem.Type.HELMET, armorMaterials.getOrThrow(ArmorMaterialKeys.IRON), soundEvents.getOrThrow(SoundEventKeys.ARMOR_EQUIP_IRON)))
                .with(EnchantableItemComponent.enchants(ArmorMaterials.IRON, EnchantmentTags.HELMET_ENCHANTING))
                .with(ForgeableItemComponent.of(EnchantmentTags.HELMET_FORGING))
                .with(new RepairableItemComponent(ItematicItemTags.REPAIRS_IRON_ARMOR))
                .with(new DispensableItemComponent(dispenseBehaviors.getOrThrow(DispenseBehaviorKeys.ARMOR)))
                .build()
        ));
        registerable.register(ItemKeys.IRON_CHESTPLATE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.IRON_CHESTPLATE).build(), 1),
            ItemComponentSet.builder()
                .with(ArmorItemComponent.from(ArmorMaterials.IRON, ArmorItem.Type.CHESTPLATE, armorMaterials.getOrThrow(ArmorMaterialKeys.IRON), soundEvents.getOrThrow(SoundEventKeys.ARMOR_EQUIP_IRON)))
                .with(EnchantableItemComponent.enchants(ArmorMaterials.IRON, EnchantmentTags.CHESTPLATE_ENCHANTING))
                .with(ForgeableItemComponent.of(EnchantmentTags.CHESTPLATE_FORGING))
                .with(new RepairableItemComponent(ItematicItemTags.REPAIRS_IRON_ARMOR))
                .with(new DispensableItemComponent(dispenseBehaviors.getOrThrow(DispenseBehaviorKeys.ARMOR)))
                .build()
        ));
        registerable.register(ItemKeys.IRON_LEGGINGS, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.IRON_LEGGINGS).build(), 1),
            ItemComponentSet.builder()
                .with(ArmorItemComponent.from(ArmorMaterials.IRON, ArmorItem.Type.LEGGINGS, armorMaterials.getOrThrow(ArmorMaterialKeys.IRON), soundEvents.getOrThrow(SoundEventKeys.ARMOR_EQUIP_IRON)))
                .with(EnchantableItemComponent.enchants(ArmorMaterials.IRON, EnchantmentTags.LEGGINGS_ENCHANTING))
                .with(ForgeableItemComponent.of(EnchantmentTags.LEGGINGS_FORGING))
                .with(new RepairableItemComponent(ItematicItemTags.REPAIRS_IRON_ARMOR))
                .with(new DispensableItemComponent(dispenseBehaviors.getOrThrow(DispenseBehaviorKeys.ARMOR)))
                .build()
        ));
        registerable.register(ItemKeys.IRON_BOOTS, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.IRON_BOOTS).build(), 1),
            ItemComponentSet.builder()
                .with(ArmorItemComponent.from(ArmorMaterials.IRON, ArmorItem.Type.BOOTS, armorMaterials.getOrThrow(ArmorMaterialKeys.IRON), soundEvents.getOrThrow(SoundEventKeys.ARMOR_EQUIP_IRON)))
                .with(EnchantableItemComponent.enchants(ArmorMaterials.IRON, EnchantmentTags.BOOTS_ENCHANTING))
                .with(ForgeableItemComponent.of(EnchantmentTags.BOOTS_FORGING))
                .with(new RepairableItemComponent(ItematicItemTags.REPAIRS_IRON_ARMOR))
                .with(new DispensableItemComponent(dispenseBehaviors.getOrThrow(DispenseBehaviorKeys.ARMOR)))
                .build()
        ));
        registerable.register(ItemKeys.DIAMOND_HELMET, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.DIAMOND_HELMET).build(), 1),
            ItemComponentSet.builder()
                .with(ArmorItemComponent.from(ArmorMaterials.DIAMOND, ArmorItem.Type.HELMET, armorMaterials.getOrThrow(ArmorMaterialKeys.DIAMOND), soundEvents.getOrThrow(SoundEventKeys.ARMOR_EQUIP_DIAMOND)))
                .with(EnchantableItemComponent.enchants(ArmorMaterials.DIAMOND, EnchantmentTags.HELMET_ENCHANTING))
                .with(ForgeableItemComponent.of(EnchantmentTags.HELMET_FORGING))
                .with(new RepairableItemComponent(ItematicItemTags.REPAIRS_DIAMOND_ARMOR))
                .with(new DispensableItemComponent(dispenseBehaviors.getOrThrow(DispenseBehaviorKeys.ARMOR)))
                .build()
        ));
        registerable.register(ItemKeys.DIAMOND_CHESTPLATE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.DIAMOND_CHESTPLATE).build(), 1),
            ItemComponentSet.builder()
                .with(ArmorItemComponent.from(ArmorMaterials.DIAMOND, ArmorItem.Type.CHESTPLATE, armorMaterials.getOrThrow(ArmorMaterialKeys.DIAMOND), soundEvents.getOrThrow(SoundEventKeys.ARMOR_EQUIP_DIAMOND)))
                .with(EnchantableItemComponent.enchants(ArmorMaterials.DIAMOND, EnchantmentTags.CHESTPLATE_ENCHANTING))
                .with(ForgeableItemComponent.of(EnchantmentTags.CHESTPLATE_FORGING))
                .with(new RepairableItemComponent(ItematicItemTags.REPAIRS_DIAMOND_ARMOR))
                .with(new DispensableItemComponent(dispenseBehaviors.getOrThrow(DispenseBehaviorKeys.ARMOR)))
                .build()
        ));
        registerable.register(ItemKeys.DIAMOND_LEGGINGS, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.DIAMOND_LEGGINGS).build(), 1),
            ItemComponentSet.builder()
                .with(ArmorItemComponent.from(ArmorMaterials.DIAMOND, ArmorItem.Type.LEGGINGS, armorMaterials.getOrThrow(ArmorMaterialKeys.DIAMOND), soundEvents.getOrThrow(SoundEventKeys.ARMOR_EQUIP_DIAMOND)))
                .with(EnchantableItemComponent.enchants(ArmorMaterials.DIAMOND, EnchantmentTags.LEGGINGS_ENCHANTING))
                .with(ForgeableItemComponent.of(EnchantmentTags.LEGGINGS_FORGING))
                .with(new RepairableItemComponent(ItematicItemTags.REPAIRS_DIAMOND_ARMOR))
                .with(new DispensableItemComponent(dispenseBehaviors.getOrThrow(DispenseBehaviorKeys.ARMOR)))
                .build()
        ));
        registerable.register(ItemKeys.DIAMOND_BOOTS, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.DIAMOND_BOOTS).build(), 1),
            ItemComponentSet.builder()
                .with(ArmorItemComponent.from(ArmorMaterials.DIAMOND, ArmorItem.Type.BOOTS, armorMaterials.getOrThrow(ArmorMaterialKeys.DIAMOND), soundEvents.getOrThrow(SoundEventKeys.ARMOR_EQUIP_DIAMOND)))
                .with(EnchantableItemComponent.enchants(ArmorMaterials.DIAMOND, EnchantmentTags.BOOTS_ENCHANTING))
                .with(ForgeableItemComponent.of(EnchantmentTags.BOOTS_FORGING))
                .with(new RepairableItemComponent(ItematicItemTags.REPAIRS_DIAMOND_ARMOR))
                .with(new DispensableItemComponent(dispenseBehaviors.getOrThrow(DispenseBehaviorKeys.ARMOR)))
                .build()
        ));
        registerable.register(ItemKeys.GOLDEN_HELMET, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.GOLDEN_HELMET).build(), 1),
            ItemComponentSet.builder()
                .with(ArmorItemComponent.from(ArmorMaterials.GOLD, ArmorItem.Type.HELMET, armorMaterials.getOrThrow(ArmorMaterialKeys.GOLD), soundEvents.getOrThrow(SoundEventKeys.ARMOR_EQUIP_GOLD)))
                .with(EnchantableItemComponent.enchants(ArmorMaterials.GOLD, EnchantmentTags.HELMET_ENCHANTING))
                .with(ForgeableItemComponent.of(EnchantmentTags.HELMET_FORGING))
                .with(new RepairableItemComponent(ItematicItemTags.REPAIRS_GOLDEN_ARMOR))
                .with(new DispensableItemComponent(dispenseBehaviors.getOrThrow(DispenseBehaviorKeys.ARMOR)))
                .build()
        ));
        registerable.register(ItemKeys.GOLDEN_CHESTPLATE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.GOLDEN_CHESTPLATE).build(), 1),
            ItemComponentSet.builder()
                .with(ArmorItemComponent.from(ArmorMaterials.GOLD, ArmorItem.Type.CHESTPLATE, armorMaterials.getOrThrow(ArmorMaterialKeys.GOLD), soundEvents.getOrThrow(SoundEventKeys.ARMOR_EQUIP_GOLD)))
                .with(EnchantableItemComponent.enchants(ArmorMaterials.GOLD, EnchantmentTags.CHESTPLATE_ENCHANTING))
                .with(ForgeableItemComponent.of(EnchantmentTags.CHESTPLATE_FORGING))
                .with(new RepairableItemComponent(ItematicItemTags.REPAIRS_GOLDEN_ARMOR))
                .with(new DispensableItemComponent(dispenseBehaviors.getOrThrow(DispenseBehaviorKeys.ARMOR)))
                .build()
        ));
        registerable.register(ItemKeys.GOLDEN_LEGGINGS, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.GOLDEN_LEGGINGS).build(), 1),
            ItemComponentSet.builder()
                .with(ArmorItemComponent.from(ArmorMaterials.GOLD, ArmorItem.Type.LEGGINGS, armorMaterials.getOrThrow(ArmorMaterialKeys.GOLD), soundEvents.getOrThrow(SoundEventKeys.ARMOR_EQUIP_GOLD)))
                .with(EnchantableItemComponent.enchants(ArmorMaterials.GOLD, EnchantmentTags.LEGGINGS_ENCHANTING))
                .with(ForgeableItemComponent.of(EnchantmentTags.LEGGINGS_FORGING))
                .with(new RepairableItemComponent(ItematicItemTags.REPAIRS_GOLDEN_ARMOR))
                .with(new DispensableItemComponent(dispenseBehaviors.getOrThrow(DispenseBehaviorKeys.ARMOR)))
                .build()
        ));
        registerable.register(ItemKeys.GOLDEN_BOOTS, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.GOLDEN_BOOTS).build(), 1),
            ItemComponentSet.builder()
                .with(ArmorItemComponent.from(ArmorMaterials.GOLD, ArmorItem.Type.BOOTS, armorMaterials.getOrThrow(ArmorMaterialKeys.GOLD), soundEvents.getOrThrow(SoundEventKeys.ARMOR_EQUIP_GOLD)))
                .with(EnchantableItemComponent.enchants(ArmorMaterials.GOLD, EnchantmentTags.BOOTS_ENCHANTING))
                .with(ForgeableItemComponent.of(EnchantmentTags.BOOTS_FORGING))
                .with(new RepairableItemComponent(ItematicItemTags.REPAIRS_GOLDEN_ARMOR))
                .with(new DispensableItemComponent(dispenseBehaviors.getOrThrow(DispenseBehaviorKeys.ARMOR)))
                .build()
        ));
        registerable.register(ItemKeys.NETHERITE_HELMET, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.NETHERITE_HELMET).build(), 1),
            ItemComponentSet.builder()
                .with(ArmorItemComponent.from(ArmorMaterials.NETHERITE, ArmorItem.Type.HELMET, armorMaterials.getOrThrow(ArmorMaterialKeys.NETHERITE), soundEvents.getOrThrow(SoundEventKeys.ARMOR_EQUIP_NETHERITE)))
                .with(EnchantableItemComponent.enchants(ArmorMaterials.NETHERITE, EnchantmentTags.HELMET_ENCHANTING))
                .with(ForgeableItemComponent.of(EnchantmentTags.HELMET_FORGING))
                .with(new RepairableItemComponent(ItematicItemTags.REPAIRS_NETHERITE_ARMOR))
                .with(new DispensableItemComponent(dispenseBehaviors.getOrThrow(DispenseBehaviorKeys.ARMOR)))
                .build()
        ));
        registerable.register(ItemKeys.NETHERITE_CHESTPLATE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.NETHERITE_CHESTPLATE).build(), 1),
            ItemComponentSet.builder()
                .with(ArmorItemComponent.from(ArmorMaterials.NETHERITE, ArmorItem.Type.CHESTPLATE, armorMaterials.getOrThrow(ArmorMaterialKeys.NETHERITE), soundEvents.getOrThrow(SoundEventKeys.ARMOR_EQUIP_NETHERITE)))
                .with(EnchantableItemComponent.enchants(ArmorMaterials.NETHERITE, EnchantmentTags.CHESTPLATE_ENCHANTING))
                .with(ForgeableItemComponent.of(EnchantmentTags.CHESTPLATE_FORGING))
                .with(new RepairableItemComponent(ItematicItemTags.REPAIRS_NETHERITE_ARMOR))
                .with(new DispensableItemComponent(dispenseBehaviors.getOrThrow(DispenseBehaviorKeys.ARMOR)))
                .build()
        ));
        registerable.register(ItemKeys.NETHERITE_LEGGINGS, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.NETHERITE_LEGGINGS).build(), 1),
            ItemComponentSet.builder()
                .with(ArmorItemComponent.from(ArmorMaterials.NETHERITE, ArmorItem.Type.LEGGINGS, armorMaterials.getOrThrow(ArmorMaterialKeys.NETHERITE), soundEvents.getOrThrow(SoundEventKeys.ARMOR_EQUIP_NETHERITE)))
                .with(EnchantableItemComponent.enchants(ArmorMaterials.NETHERITE, EnchantmentTags.LEGGINGS_ENCHANTING))
                .with(ForgeableItemComponent.of(EnchantmentTags.LEGGINGS_FORGING))
                .with(new RepairableItemComponent(ItematicItemTags.REPAIRS_NETHERITE_ARMOR))
                .with(new DispensableItemComponent(dispenseBehaviors.getOrThrow(DispenseBehaviorKeys.ARMOR)))
                .build()
        ));
        registerable.register(ItemKeys.NETHERITE_BOOTS, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.NETHERITE_BOOTS).build(), 1),
            ItemComponentSet.builder()
                .with(ArmorItemComponent.from(ArmorMaterials.NETHERITE, ArmorItem.Type.BOOTS, armorMaterials.getOrThrow(ArmorMaterialKeys.NETHERITE), soundEvents.getOrThrow(SoundEventKeys.ARMOR_EQUIP_NETHERITE)))
                .with(EnchantableItemComponent.enchants(ArmorMaterials.NETHERITE, EnchantmentTags.BOOTS_ENCHANTING))
                .with(ForgeableItemComponent.of(EnchantmentTags.BOOTS_FORGING))
                .with(new RepairableItemComponent(ItematicItemTags.REPAIRS_NETHERITE_ARMOR))
                .with(new DispensableItemComponent(dispenseBehaviors.getOrThrow(DispenseBehaviorKeys.ARMOR)))
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
        registerable.register(ItemKeys.PAINTING, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.PAINTING).build()),
            ItemComponentSet.builder()
                .with(new EntityItemComponent(DecorationEntityInitializer.createPainting(EntityType.PAINTING)))
                .build()
        ));
        registerable.register(ItemKeys.GOLDEN_APPLE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.GOLDEN_APPLE).rarity(Rarity.RARE).build()),
            ItemComponentSet.builder()
                .with(FoodItemComponent.from(FoodComponents.GOLDEN_APPLE))
                .build()
        ));
        registerable.register(ItemKeys.ENCHANTED_GOLDEN_APPLE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.ENCHANTED_GOLDEN_APPLE).rarity(Rarity.EPIC).build()),
            ItemComponentSet.builder()
                .with(FoodItemComponent.from(FoodComponents.ENCHANTED_GOLDEN_APPLE))
                .with(GlintItemComponent.of(true))
                .build()
        ));
        registerable.register(ItemKeys.OAK_SIGN, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.OAK_SIGN).build(), 16),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.OAK_SIGN)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.SIGN_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.SPRUCE_SIGN, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.SPRUCE_SIGN).build(), 16),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.SPRUCE_SIGN)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.SIGN_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BIRCH_SIGN, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BIRCH_SIGN).build(), 16),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.BIRCH_SIGN)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.SIGN_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.JUNGLE_SIGN, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.JUNGLE_SIGN).build(), 16),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.JUNGLE_SIGN)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.SIGN_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.ACACIA_SIGN, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.ACACIA_SIGN).build(), 16),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.ACACIA_SIGN)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.SIGN_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.CHERRY_SIGN, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.CHERRY_SIGN).build(), 16),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.CHERRY_SIGN)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.SIGN_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.DARK_OAK_SIGN, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.DARK_OAK_SIGN).build(), 16),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.DARK_OAK_SIGN)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.SIGN_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.MANGROVE_SIGN, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.MANGROVE_SIGN).build(), 16),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.MANGROVE_SIGN)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.SIGN_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BAMBOO_SIGN, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BAMBOO_SIGN).build(), 16),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.BAMBOO_SIGN)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.SIGN_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.CRIMSON_SIGN, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.CRIMSON_SIGN).build(), 16),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.CRIMSON_SIGN)))
                .build()
        ));
        registerable.register(ItemKeys.WARPED_SIGN, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.WARPED_SIGN).build(), 16),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.WARPED_SIGN)))
                .build()
        ));
        registerable.register(ItemKeys.OAK_HANGING_SIGN, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.OAK_HANGING_SIGN).build(), 16),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.OAK_HANGING_SIGN)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.HANGING_SIGN_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.SPRUCE_HANGING_SIGN, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.SPRUCE_HANGING_SIGN).build(), 16),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.SPRUCE_HANGING_SIGN)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.HANGING_SIGN_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BIRCH_HANGING_SIGN, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BIRCH_HANGING_SIGN).build(), 16),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.BIRCH_HANGING_SIGN)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.HANGING_SIGN_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.JUNGLE_HANGING_SIGN, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.JUNGLE_HANGING_SIGN).build(), 16),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.JUNGLE_HANGING_SIGN)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.HANGING_SIGN_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.ACACIA_HANGING_SIGN, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.ACACIA_HANGING_SIGN).build(), 16),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.ACACIA_HANGING_SIGN)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.HANGING_SIGN_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.CHERRY_HANGING_SIGN, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.CHERRY_HANGING_SIGN).build(), 16),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.CHERRY_HANGING_SIGN)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.HANGING_SIGN_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.DARK_OAK_HANGING_SIGN, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.DARK_OAK_HANGING_SIGN).build(), 16),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.DARK_OAK_HANGING_SIGN)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.HANGING_SIGN_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.MANGROVE_HANGING_SIGN, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.MANGROVE_HANGING_SIGN).build(), 16),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.MANGROVE_HANGING_SIGN)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.HANGING_SIGN_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BAMBOO_HANGING_SIGN, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BAMBOO_HANGING_SIGN).build(), 16),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.BAMBOO_HANGING_SIGN)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.HANGING_SIGN_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.CRIMSON_HANGING_SIGN, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.CRIMSON_HANGING_SIGN).build(), 16),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.CRIMSON_HANGING_SIGN)))
                .build()
        ));
        registerable.register(ItemKeys.WARPED_HANGING_SIGN, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.WARPED_HANGING_SIGN).build(), 16),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.WARPED_HANGING_SIGN)))
                .build()
        ));
        registerable.register(ItemKeys.BUCKET, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.BUCKET).build(), 16),
            ItemComponentSet.builder()
                .with(BucketItemComponent.fluid(fluids.getOrThrow(FluidKeys.EMPTY)))
                .with(new DispensableItemComponent(dispenseBehaviors.getOrThrow(DispenseBehaviorKeys.BUCKET)))
                .build()
        ));
        registerable.register(ItemKeys.WATER_BUCKET, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.WATER_BUCKET).build(), 1),
            ItemComponentSet.builder()
                .with(BucketItemComponent.fluid(fluids.getOrThrow(FluidKeys.WATER), soundEvents.getOrThrow(SoundEventKeys.BUCKET_EMPTY)))
                .with(new DispensableItemComponent(dispenseBehaviors.getOrThrow(DispenseBehaviorKeys.BUCKET)))
                .build()
        ));
        registerable.register(ItemKeys.LAVA_BUCKET, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.LAVA_BUCKET).build(), 1),
            ItemComponentSet.builder()
                .with(BucketItemComponent.fluid(fluids.getOrThrow(FluidKeys.LAVA), soundEvents.getOrThrow(SoundEventKeys.BUCKET_EMPTY_LAVA)))
                .with(new DispensableItemComponent(dispenseBehaviors.getOrThrow(DispenseBehaviorKeys.BUCKET)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.LAVA_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.POWDER_SNOW_BUCKET, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.POWDER_SNOW_BUCKET).build(), 1),
            ItemComponentSet.builder()
                .with(BucketItemComponent.block(blocks.getOrThrow(BlockKeys.POWDER_SNOW), soundEvents.getOrThrow(SoundEventKeys.BUCKET_EMPTY_POWDER_SNOW)))
                .with(new DispensableItemComponent(dispenseBehaviors.getOrThrow(DispenseBehaviorKeys.BUCKET)))
                .build()
        ));
        registerable.register(ItemKeys.SNOWBALL, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.SNOWBALL).build(), 16),
            ItemComponentSet.builder()
                .with(new ThrowableItemComponent(1.5f, 0.0f))
                .with(ProjectileItemComponent.of(entityTypes.getOrThrow(EntityTypeKeys.SNOWBALL)))
                .with(new DispensableItemComponent(dispenseBehaviors.getOrThrow(DispenseBehaviorKeys.PROJECTILE)))
                .build()
        ));
        registerable.register(ItemKeys.MILK_BUCKET, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.MILK_BUCKET).build(), 1),
            ItemComponentSet.builder()
                .with(new UseDurationItemComponent(MilkBucketItemAccessor.getMaxUseTime()))
                .with(new UseAnimationItemComponent(UseAction.DRINK))
                .with(ConsumableItemComponent.of(items.getOrThrow(ItemKeys.BUCKET)))
                .build(),
            ItemEventMap.builder()
                .add(ItemEvents.CONSUME_ITEM, ActionEntry.of(new ClearStatusEffectsAction(ActionContextParameter.THIS)))
                .build()
        ));
        registerable.register(ItemKeys.PUFFERFISH_BUCKET, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.PUFFERFISH_BUCKET).build(), 1),
            ItemComponentSet.builder()
                .with(BucketItemComponent.entity(fluids.getOrThrow(FluidKeys.WATER), entityTypes.getOrThrow(EntityTypeKeys.PUFFERFISH), soundEvents.getOrThrow(SoundEventKeys.BUCKET_EMPTY_FISH)))
                .with(new DispensableItemComponent(dispenseBehaviors.getOrThrow(DispenseBehaviorKeys.BUCKET)))
                .build()
        ));
        registerable.register(ItemKeys.SALMON_BUCKET, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.SALMON_BUCKET).build(), 1),
            ItemComponentSet.builder()
                .with(BucketItemComponent.entity(fluids.getOrThrow(FluidKeys.WATER), entityTypes.getOrThrow(EntityTypeKeys.SALMON), soundEvents.getOrThrow(SoundEventKeys.BUCKET_EMPTY_FISH)))
                .with(new DispensableItemComponent(dispenseBehaviors.getOrThrow(DispenseBehaviorKeys.BUCKET)))
                .build()
        ));
        registerable.register(ItemKeys.COD_BUCKET, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.COD_BUCKET).build(), 1),
            ItemComponentSet.builder()
                .with(BucketItemComponent.entity(fluids.getOrThrow(FluidKeys.WATER), entityTypes.getOrThrow(EntityTypeKeys.COD), soundEvents.getOrThrow(SoundEventKeys.BUCKET_EMPTY_FISH)))
                .with(new DispensableItemComponent(dispenseBehaviors.getOrThrow(DispenseBehaviorKeys.BUCKET)))
                .build()
        ));
        registerable.register(ItemKeys.TROPICAL_FISH_BUCKET, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.TROPICAL_FISH_BUCKET).build(), 1),
            ItemComponentSet.builder()
                .with(BucketItemComponent.entity(fluids.getOrThrow(FluidKeys.WATER), entityTypes.getOrThrow(EntityTypeKeys.TROPICAL_FISH), soundEvents.getOrThrow(SoundEventKeys.BUCKET_EMPTY_FISH)))
                .with(new DispensableItemComponent(dispenseBehaviors.getOrThrow(DispenseBehaviorKeys.BUCKET)))
                .build()
        ));
        registerable.register(ItemKeys.AXOLOTL_BUCKET, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.AXOLOTL_BUCKET).build(), 1),
            ItemComponentSet.builder()
                .with(BucketItemComponent.entity(fluids.getOrThrow(FluidKeys.WATER), entityTypes.getOrThrow(EntityTypeKeys.AXOLOTL), soundEvents.getOrThrow(SoundEventKeys.BUCKET_EMPTY_AXOLOTL)))
                .with(new DispensableItemComponent(dispenseBehaviors.getOrThrow(DispenseBehaviorKeys.BUCKET)))
                .build()
        ));
        registerable.register(ItemKeys.TADPOLE_BUCKET, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.TADPOLE_BUCKET).build(), 1),
            ItemComponentSet.builder()
                .with(BucketItemComponent.entity(fluids.getOrThrow(FluidKeys.WATER), entityTypes.getOrThrow(EntityTypeKeys.TADPOLE), soundEvents.getOrThrow(SoundEventKeys.BUCKET_EMPTY_TADPOLE)))
                .with(new DispensableItemComponent(dispenseBehaviors.getOrThrow(DispenseBehaviorKeys.BUCKET)))
                .build()
        ));
        registerable.register(ItemKeys.LEATHER, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.LEATHER).build())
        ));
        registerable.register(ItemKeys.DRIED_KELP_BLOCK, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.DRIED_KELP_BLOCK).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.DRIED_KELP_BLOCK)))
                .with(new CompostableItemComponent(0.5f))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.DRIED_KELP_BLOCK_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.PAPER, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.PAPER).build())
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
                .with(ProjectileItemComponent.of(entityTypes.getOrThrow(EntityTypeKeys.EGG)))
                .with(new DispensableItemComponent(dispenseBehaviors.getOrThrow(DispenseBehaviorKeys.PROJECTILE)))
                .build()
        ));
        registerable.register(ItemKeys.COMPASS, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.COMPASS).build()),
            ItemComponentSet.builder()
                .with(PointableItemComponent.of(pointers.getOrThrow(PointerKeys.SPAWN_LOCATION), Util.createTranslationKey("item", new Identifier("lodestone_compass"))))
                .build(),
            ItemEventMap.builder()
                .add(ItemEvents.USE_ON_BLOCK, ActionEntry.of(
                    ActionRequirements.of(
                        ActionContextParameters.of(ActionContextParameter.THIS, ActionContextParameter.TARGET),
                        LocationCheckLootCondition.builder(
                            LocationPredicate.Builder.create()
                                .block(BlockPredicate.Builder.create()
                                    .blocks(Blocks.LODESTONE)))
                            .build()
                    ),
                    PassingSequenceHandler.builder()
                        .add(SetItemPointerLocationAction.of(ActionContextParameter.TARGET))
                        .add(SwingHandAction.INSTANCE)
                ))
                .build()
        ));
        registerable.register(ItemKeys.RECOVERY_COMPASS, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.RECOVERY_COMPASS).build()),
            ItemComponentSet.builder()
                .with(PointableItemComponent.of(pointers.getOrThrow(PointerKeys.LAST_DEATH)))
                .build()
        ));
        registerable.register(ItemKeys.FISHING_ROD, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.FISHING_ROD).build(), 1),
            ItemComponentSet.builder()
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.CLOCK, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.CLOCK).build())
        ));
        registerable.register(ItemKeys.GLOWSTONE_DUST, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.GLOWSTONE_DUST).build())
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
        registerable.register(ItemKeys.INK_SAC, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.INK_SAC).build()),
            ItemComponentSet.EMPTY,
            ItemEventMap.builder()
                .add(ItemEvents.USE_ON_BLOCK, Actions.glowSign(false))
                .build()
        ));
        registerable.register(ItemKeys.GLOW_INK_SAC, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.GLOW_INK_SAC).build()),
            ItemComponentSet.EMPTY,
            ItemEventMap.builder()
                .add(ItemEvents.USE_ON_BLOCK, Actions.glowSign(true))
                .build()
        ));
        registerable.register(ItemKeys.WHITE_DYE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.WHITE_DYE).build()),
            ItemComponentSet.builder()
                .with(new DyeItemComponent(DyeColor.WHITE))
                .build()
        ));
        registerable.register(ItemKeys.ORANGE_DYE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.ORANGE_DYE).build()),
            ItemComponentSet.builder()
                .with(new DyeItemComponent(DyeColor.ORANGE))
                .build()
        ));
        registerable.register(ItemKeys.MAGENTA_DYE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.MAGENTA_DYE).build()),
            ItemComponentSet.builder()
                .with(new DyeItemComponent(DyeColor.MAGENTA))
                .build()
        ));
        registerable.register(ItemKeys.LIGHT_BLUE_DYE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.LIGHT_BLUE_DYE).build()),
            ItemComponentSet.builder()
                .with(new DyeItemComponent(DyeColor.LIGHT_BLUE))
                .build()
        ));
        registerable.register(ItemKeys.YELLOW_DYE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.YELLOW_DYE).build()),
            ItemComponentSet.builder()
                .with(new DyeItemComponent(DyeColor.YELLOW))
                .build()
        ));
        registerable.register(ItemKeys.LIME_DYE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.LIME_DYE).build()),
            ItemComponentSet.builder()
                .with(new DyeItemComponent(DyeColor.LIME))
                .build()
        ));
        registerable.register(ItemKeys.PINK_DYE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.PINK_DYE).build()),
            ItemComponentSet.builder()
                .with(new DyeItemComponent(DyeColor.PINK))
                .build()
        ));
        registerable.register(ItemKeys.GRAY_DYE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.GRAY_DYE).build()),
            ItemComponentSet.builder()
                .with(new DyeItemComponent(DyeColor.GRAY))
                .build()
        ));
        registerable.register(ItemKeys.LIGHT_GRAY_DYE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.LIGHT_GRAY_DYE).build()),
            ItemComponentSet.builder()
                .with(new DyeItemComponent(DyeColor.LIGHT_GRAY))
                .build()
        ));
        registerable.register(ItemKeys.CYAN_DYE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.CYAN_DYE).build()),
            ItemComponentSet.builder()
                .with(new DyeItemComponent(DyeColor.CYAN))
                .build()
        ));
        registerable.register(ItemKeys.PURPLE_DYE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.PURPLE_DYE).build()),
            ItemComponentSet.builder()
                .with(new DyeItemComponent(DyeColor.PURPLE))
                .build()
        ));
        registerable.register(ItemKeys.BLUE_DYE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.BLUE_DYE).build()),
            ItemComponentSet.builder()
                .with(new DyeItemComponent(DyeColor.BLUE))
                .build()
        ));
        registerable.register(ItemKeys.BROWN_DYE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.BROWN_DYE).build()),
            ItemComponentSet.builder()
                .with(new DyeItemComponent(DyeColor.BROWN))
                .build()
        ));
        registerable.register(ItemKeys.GREEN_DYE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.GREEN_DYE).build()),
            ItemComponentSet.builder()
                .with(new DyeItemComponent(DyeColor.GREEN))
                .build()
        ));
        registerable.register(ItemKeys.RED_DYE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.RED_DYE).build()),
            ItemComponentSet.builder()
                .with(new DyeItemComponent(DyeColor.RED))
                .build()
        ));
        registerable.register(ItemKeys.BLACK_DYE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.BLACK_DYE).build()),
            ItemComponentSet.builder()
                .with(new DyeItemComponent(DyeColor.BLACK))
                .build()
        ));
        registerable.register(ItemKeys.BONE_MEAL, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.BONE_MEAL).build()),
            ItemComponentSet.builder()
                .with(new DispensableItemComponent(dispenseBehaviors.getOrThrow(DispenseBehaviorKeys.FERTILIZE)))
                .build(),
            ItemEventMap.builder()
                .add(ItemEvents.USE_ON_BLOCK, ActionEntry.of(FertilizeAction.INSTANCE))
                .build()
        ));
        registerable.register(ItemKeys.SUGAR, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.SUGAR).build())
        ));
        registerable.register(ItemKeys.COOKIE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.COOKIE).build()),
            ItemComponentSet.builder()
                .with(FoodItemComponent.from(FoodComponents.COOKIE))
                .with(new CompostableItemComponent(0.85f))
                .build()
        ));
        registerable.register(ItemKeys.SHEARS, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.SHEARS).build(), 1),
            ItemComponentSet.builder()
                .with(new DamageableItemComponent(238))
                .with(ToolItemComponent.builder(1)
                    .miningSpeed(15.0f, ItematicBlockTags.SHEARS_SUPER_EFFICIENT, true)
                    .miningSpeed(5.0f, ItematicBlockTags.SHEARS_SLIGHTLY_EFFICIENT, true)
                    .miningSpeed(2.0f, ItematicBlockTags.SHEARS_EFFICIENT, true)
                    .build())
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
        registerable.register(ItemKeys.PUMPKIN_SEEDS, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.PUMPKIN_SEEDS).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.PUMPKIN_STEM)))
                .with(new CompostableItemComponent(0.3f))
                .build()
        ));
        registerable.register(ItemKeys.MELON_SEEDS, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.MELON_SEEDS).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.MELON_STEM)))
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
                .with(ProjectileItemComponent.of(entityTypes.getOrThrow(EntityTypeKeys.ENDER_PEARL)))
                .with(new CooldownItemComponent(20))
                .build()
        ));
        registerable.register(ItemKeys.BLAZE_ROD, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.BLAZE_ROD).build()),
            ItemComponentSet.builder()
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.BLAZE_ROD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.GHAST_TEAR, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.GHAST_TEAR).build())
        ));
        registerable.register(ItemKeys.GOLD_NUGGET, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.GOLD_NUGGET).build()),
            ItemComponentSet.builder()
                .with(new FireworkShapeModifierItemComponent(FireworkRocketItem.Type.STAR))
                .build()
        ));
        registerable.register(ItemKeys.NETHER_WART, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.NETHER_WART).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.NETHER_WART)))
                .with(new CompostableItemComponent(0.65f))
                .build()
        ));
        registerable.register(ItemKeys.POTION, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.POTION).build(), 1),
            ItemComponentSet.builder()
                .with(new UseDurationItemComponent(PotionItemAccessor.getMaxUseTime()))
                .with(new PotionItemComponent())
                .with(new PotionHolderItemComponent())
                .with(new UseAnimationItemComponent(UseAction.DRINK))
                .with(ConsumableItemComponent.of(items.getOrThrow(ItemKeys.GLASS_BOTTLE)))
                .with(new TintedItemComponent(new PotionItemColor()))
                .with(new DispensableItemComponent(dispenseBehaviors.getOrThrow(DispenseBehaviorKeys.POTION)))
                .build()
        ));
        registerable.register(ItemKeys.GLASS_BOTTLE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.GLASS_BOTTLE).build()),
            ItemComponentSet.builder()
                .with(new DispensableItemComponent(dispenseBehaviors.getOrThrow(DispenseBehaviorKeys.BOTTLE)))
                .build()
        ));
        registerable.register(ItemKeys.SPIDER_EYE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.SPIDER_EYE).build()),
            ItemComponentSet.builder()
                .with(FoodItemComponent.from(FoodComponents.SPIDER_EYE))
                .build()
        ));
        registerable.register(ItemKeys.FERMENTED_SPIDER_EYE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.FERMENTED_SPIDER_EYE).build())
        ));
        registerable.register(ItemKeys.BLAZE_POWDER, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.BLAZE_POWDER).build())
        ));
        registerable.register(ItemKeys.MAGMA_CREAM, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.MAGMA_CREAM).build())
        ));
        registerable.register(ItemKeys.BREWING_STAND, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BREWING_STAND).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.BREWING_STAND)))
                .build()
        ));
        registerable.register(ItemKeys.CAULDRON, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.CAULDRON).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.CAULDRON)))
                .build()
        ));
        registerable.register(ItemKeys.ENDER_EYE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.ENDER_EYE).build()),
            ItemComponentSet.builder()
                .with(new ThrowableItemComponent(0.0f, 0.0f))
                .with(ProjectileItemComponent.of(EyeOfEnderEntityInitializer.INSTANCE, 0))
                .with(PreventUseWhenUsedOnTargetItemComponent.forBlock())
                .build(),
            ItemEventMap.builder()
                .add(ItemEvents.USE_ON_BLOCK, ActionEntry.of(
                    ActionRequirements.of(
                        ActionContextParameters.of(ActionContextParameter.THIS, ActionContextParameter.TARGET),
                        LocationCheckLootCondition.builder(
                            LocationPredicate.Builder.create()
                                .block(BlockPredicate.Builder.create()
                                    .blocks(Blocks.END_PORTAL_FRAME)
                                    .state(StatePredicate.Builder.create()
                                        .exactMatch(Properties.EYE, false))))
                            .build()
                    ),
                    PassingSequenceHandler.builder()
                        .add(ModifyBlockStateAction.builder(ActionContextParameter.TARGET)
                            .property(Properties.EYE, true)
                            .pushEntitiesUpwards()
                            .build())
                        .add(DecrementItemAction.of(1))
                        .add(SwingHandAction.INSTANCE)
                        .add(PlaySoundAction.of(ActionContextParameter.TARGET, soundEvents.getOrThrow(SoundEventKeys.END_PORTAL_FRAME_FILL), SoundCategory.BLOCKS))
                        .add(DisplayParticleAction.builder(ActionContextParameter.TARGET, ParticleTypes.SMOKE)
                            .count(16)
                            .offset(Vec3dProvider.of(
                                -0.1875d, 0.1875d,
                                0.8125d, 0.8125d,
                                -0.1875d, 0.1875d))
                            .build())
                        .addOptional(LightEndPortalAction.of(ActionContextParameter.TARGET))
                ))
                .add(ItemEvents.THROW_PROJECTILE, ActionEntry.of(
                    PlaySoundAction.of(ActionContextParameter.THIS, soundEvents.getOrThrow(SoundEventKeys.ENDER_EYE_LAUNCH), SoundCategory.NEUTRAL, 1.0f, 1.2f)
                ))
                .build()
        ));
        registerable.register(ItemKeys.GLISTERING_MELON_SLICE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.GLISTERING_MELON_SLICE).build())
        ));
        registerable.register(ItemKeys.ALLAY_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.ALLAY_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.ALLAY), 0x00daff, 0x00adff, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.AXOLOTL_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.AXOLOTL_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.AXOLOTL), 0xfbc1e3, 0xa62d74, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.BAT_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.BAT_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.BAT), 0x4c3e30, 0x0f0f0f, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.BEE_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.BEE_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.BEE), 0xedc343, 0x43241b, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.BLAZE_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.BLAZE_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.BLAZE), 0xf6b201, 0xfff87e, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.CAT_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.CAT_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.CAT), 0xefc88e, 0x957256, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.CAMEL_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.CAMEL_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.CAMEL), 0xfcc369, 0xcb9337, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.CAVE_SPIDER_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.CAVE_SPIDER_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.CAVE_SPIDER), 0x0c424e, 0xa80e0e, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.CHICKEN_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.CHICKEN_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.CHICKEN), 0xa1a1a1, 0xff0000, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.COD_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.COD_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.COD), 0xc1a76a, 0xe5c48b, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.COW_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.COW_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.COW), 0x443626, 0xa1a1a1, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.CREEPER_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.CREEPER_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.CREEPER), 0x0da70b, 0x000000, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.DOLPHIN_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.DOLPHIN_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.DOLPHIN), 0x223b4d, 0xf9f9f9, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.DONKEY_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.DONKEY_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.DONKEY), 0x534539, 0x867566, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.DROWNED_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.DROWNED_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.DROWNED), 0x8ff1d7, 0x799c65, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.ELDER_GUARDIAN_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.ELDER_GUARDIAN_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.ELDER_GUARDIAN), 0xceccba, 0x747693, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.ENDER_DRAGON_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.ENDER_DRAGON_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.ENDER_DRAGON), 0x1c1c1c, 0xe079fa, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.ENDERMAN_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.ENDERMAN_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.ENDERMAN), 0x161616, 0x000000, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.ENDERMITE_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.ENDERMITE_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.ENDERMITE), 0x161616, 0x6e6e6e, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.EVOKER_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.EVOKER_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.EVOKER), 0x959b9b, 0x1e1c1a, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.FOX_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.FOX_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.FOX), 0xd5b69f, 0xcc6920, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.FROG_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.FROG_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.FROG), 0xd07444, 0xffc77c, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.GHAST_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.GHAST_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.GHAST), 0xf9f9f9, 0xbcbcbc, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.GLOW_SQUID_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.GLOW_SQUID_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.GLOW_SQUID), 0x095656, 0x85f1bc, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.GOAT_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.GOAT_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.GOAT), 0xa5947c, 0x55493e, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.GUARDIAN_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.GUARDIAN_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.GUARDIAN), 0x5a8272, 0xf17d30, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.HOGLIN_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.HOGLIN_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.HOGLIN), 0xc66e55, 0x5f6464, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.HORSE_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.HORSE_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.HORSE), 0xc09e7d, 0xeee500, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.HUSK_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.HUSK_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.HUSK), 0x797061, 0xe6cc94, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.IRON_GOLEM_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.IRON_GOLEM_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.IRON_GOLEM), 0xdbcdc2, 0x74a332, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.LLAMA_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.LLAMA_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.LLAMA), 0xc09e7d, 0x995f40, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.MAGMA_CUBE_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.MAGMA_CUBE_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.MAGMA_CUBE), 0x340000, 0xfcfc00, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.MOOSHROOM_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.MOOSHROOM_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.MOOSHROOM), 0xa00f10, 0xb7b7b7, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.MULE_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.MULE_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.MULE), 0x1b0200, 0x51331d, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.OCELOT_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.OCELOT_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.OCELOT), 0xefde7d, 0x564434, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.PANDA_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.PANDA_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.PANDA), 0xe7e7e7, 0x1b1b22, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.PARROT_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.PARROT_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.PARROT), 0x0da70b, 0xff0000, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.PHANTOM_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.PHANTOM_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.PHANTOM), 0x43518a, 0x88ff00, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.PIG_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.PIG_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.PIG), 0xf0a5a2, 0xdb635f, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.PIGLIN_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.PIGLIN_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.PIGLIN), 0x995f40, 0xf9f3a4, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.PIGLIN_BRUTE_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.PIGLIN_BRUTE_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.PIGLIN_BRUTE), 0x592a10, 0xf9f3a4, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.PILLAGER_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.PILLAGER_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.PILLAGER), 0x532f36, 0x959b9b, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.POLAR_BEAR_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.POLAR_BEAR_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.POLAR_BEAR), 0xeeeede, 0xd5d6cd, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.PUFFERFISH_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.PUFFERFISH_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.PUFFERFISH), 0xf6b201, 0x37c3f2, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.RABBIT_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.RABBIT_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.RABBIT), 0x995f40, 0x734831, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.RAVAGER_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.RAVAGER_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.RAVAGER), 0x757470, 0x5b5049, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.SALMON_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.SALMON_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.SALMON), 0xa00f10, 0x0e8474, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.SHEEP_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.SHEEP_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.SHEEP), 0xe7e7e7, 0xffb5b5, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.SHULKER_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.SHULKER_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.SHULKER), 0x946794, 0x4d3852, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.SILVERFISH_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.SILVERFISH_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.SILVERFISH), 0x6e6e6e, 0x303030, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.SKELETON_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.SKELETON_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.SKELETON), 0xc1c1c1, 0x494949, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.SKELETON_HORSE_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.SKELETON_HORSE_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.SKELETON_HORSE), 0x68684f, 0xe5e5d8, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.SLIME_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.SLIME_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.SLIME), 0x51a03e, 0x7ebf6e, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.SNIFFER_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.SNIFFER_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.SNIFFER), 0x871e09, 0x25ab70, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.SNOW_GOLEM_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.SNOW_GOLEM_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.SNOW_GOLEM), 0xd9f2f2, 0x81a4a4, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.SPIDER_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.SPIDER_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.SPIDER), 0x342d27, 0xa80e0e, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.SQUID_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.SQUID_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.SQUID), 0x223b4d, 0x708899, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.STRAY_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.STRAY_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.STRAY), 0x617677, 0xddeaea, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.STRIDER_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.STRIDER_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.STRIDER), 0x9c3436, 0x4d494d, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.TADPOLE_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.TADPOLE_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.TADPOLE), 0x6d533d, 0x160a00, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.TRADER_LLAMA_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.TRADER_LLAMA_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.TRADER_LLAMA), 0xeaa430, 0x456296, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.TROPICAL_FISH_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.TROPICAL_FISH_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.TROPICAL_FISH), 0xef6915, 0xfff9ef, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.TURTLE_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.TURTLE_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.TURTLE), 0xe7e7e7, 0x00afaf, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.VEX_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.VEX_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.VEX), 0x7a90a4, 0xe8edf1, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.VILLAGER_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.VILLAGER_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.VILLAGER), 0x563c33, 0xbd8b72, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.VINDICATOR_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.VINDICATOR_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.VINDICATOR), 0x959b9b, 0x275e61, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.WANDERING_TRADER_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.WANDERING_TRADER_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.WANDERING_TRADER), 0x456296, 0xeaa430, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.WARDEN_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.WARDEN_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.WARDEN), 0x0f4649, 0x39d6e0, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.WITCH_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.WITCH_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.WITCH), 0x340000, 0x51a03e, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.WITHER_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.WITHER_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.WITHER), 0x141414, 0x4d72a0, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.WITHER_SKELETON_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.WITHER_SKELETON_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.WITHER_SKELETON), 0x141414, 0x474d4d, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.WOLF_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.WOLF_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.WOLF), 0xd7d3d3, 0xceaf96, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.ZOGLIN_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.ZOGLIN_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.ZOGLIN), 0xc66e55, 0xe6e6e6, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.ZOMBIE_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.ZOMBIE_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.ZOMBIE), 0x00afaf, 0x799c65, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.ZOMBIE_HORSE_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.ZOMBIE_HORSE_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.ZOMBIE_HORSE), 0x315234, 0x97c284, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.ZOMBIE_VILLAGER_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.ZOMBIE_VILLAGER_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.ZOMBIE_VILLAGER), 0x563c33, 0x799c65, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.ZOMBIFIED_PIGLIN_SPAWN_EGG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.ZOMBIFIED_PIGLIN_SPAWN_EGG).build()),
            ItemComponentSet.builder()
                .with(SpawnEggItemComponent.from(entityTypes.getOrThrow(EntityTypeKeys.ZOMBIFIED_PIGLIN), 0xea9393, 0x4c7129, dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.EXPERIENCE_BOTTLE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.EXPERIENCE_BOTTLE).rarity(Rarity.UNCOMMON).build()),
            ItemComponentSet.builder()
                .with(new ThrowableItemComponent(0.7f, -20.0f))
                .with(ProjectileItemComponent.of(entityTypes.getOrThrow(EntityTypeKeys.EXPERIENCE_BOTTLE)))
                .with(new DispensableItemComponent(dispenseBehaviors.getOrThrow(DispenseBehaviorKeys.PROJECTILE)))
                .build()
        ));
        registerable.register(ItemKeys.FIRE_CHARGE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.FIRE_CHARGE).build()),
            ItemComponentSet.builder()
                .with(new FireworkShapeModifierItemComponent(FireworkRocketItem.Type.LARGE_BALL))
                .with(ProjectileItemComponent.of(entityTypes.getOrThrow(EntityTypeKeys.SMALL_FIREBALL)))
                .with(new DispensableItemComponent(dispenseBehaviors.getOrThrow(DispenseBehaviorKeys.PROJECTILE)))
                .build(),
            ItemEventMap.builder()
                .add(ItemEvents.USE_ON_BLOCK, ActionEntry.of(
                    PassingSequenceHandler.builder()
                        .add(actions.getOrThrow(Actions.LIGHT_BLOCK))
                        .add(DecrementItemAction.of(1))
                        .add(PlaySoundAction.builder(ActionContextParameter.TARGET, soundEvents.getOrThrow(SoundEventKeys.FIRE_CHARGE_USE), SoundCategory.BLOCKS)
                            .pitch(0.8f, 1.2f)
                            .build())
                ))
                .build()
        ));
        registerable.register(ItemKeys.WRITABLE_BOOK, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.WRITABLE_BOOK).build(), 1),
            ItemComponentSet.builder()
                .with(WritableItemComponent.of(items.getOrThrow(ItemKeys.WRITTEN_BOOK)))
                .build()
        ));
        registerable.register(ItemKeys.WRITTEN_BOOK, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.WRITTEN_BOOK).build(), 16),
            ItemComponentSet.builder()
                .with(TextHolderItemComponent.INSTANCE)
                .with(GlintItemComponent.of(true))
                .build(),
            ItemEventMap.builder()
                .add(ItemEvents.USE, ActionEntry.of(OpenBookFromItemAction.INSTANCE))
                .build()
        ));
        registerable.register(ItemKeys.ITEM_FRAME, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.ITEM_FRAME).build()),
            ItemComponentSet.builder()
                .with(new EntityItemComponent(DecorationEntityInitializer.createItemFrame(EntityType.ITEM_FRAME, ItemFrameEntity::new)))
                .build()
        ));
        registerable.register(ItemKeys.GLOW_ITEM_FRAME, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.GLOW_ITEM_FRAME).build()),
            ItemComponentSet.builder()
                .with(new EntityItemComponent(DecorationEntityInitializer.createItemFrame(EntityType.GLOW_ITEM_FRAME, GlowItemFrameEntity::new)))
                .build()
        ));
        registerable.register(ItemKeys.CARROT, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.CARROT).build()),
            ItemComponentSet.builder()
                .with(FoodItemComponent.from(FoodComponents.CARROT))
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.CARROTS)))
                .with(new CompostableItemComponent(0.65f))
                .build()
        ));
        registerable.register(ItemKeys.POTATO, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.POTATO).build()),
            ItemComponentSet.builder()
                .with(FoodItemComponent.from(FoodComponents.POTATO))
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.POTATOES)))
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
        registerable.register(ItemKeys.SKELETON_SKULL, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.SKELETON_SKULL).rarity(Rarity.UNCOMMON).build()),
            ItemComponentSet.builder()
                .with(EquipmentItemComponent.skull(blocks.getOrThrow(BlockKeys.SKELETON_SKULL), soundEvents))
                .build()
        ));
        registerable.register(ItemKeys.WITHER_SKELETON_SKULL, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.WITHER_SKELETON_SKULL).rarity(Rarity.UNCOMMON).build()),
            ItemComponentSet.builder()
                .with(EquipmentItemComponent.skull(blocks.getOrThrow(BlockKeys.WITHER_SKELETON_SKULL), soundEvents))
                .build()
        ));
        registerable.register(ItemKeys.PLAYER_HEAD, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.PLAYER_HEAD).rarity(Rarity.UNCOMMON).build()),
            ItemComponentSet.builder()
                .with(EquipmentItemComponent.skull(blocks.getOrThrow(BlockKeys.PLAYER_HEAD), soundEvents))
                .build()
        ));
        registerable.register(ItemKeys.ZOMBIE_HEAD, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.ZOMBIE_HEAD).rarity(Rarity.UNCOMMON).build()),
            ItemComponentSet.builder()
                .with(EquipmentItemComponent.skull(blocks.getOrThrow(BlockKeys.ZOMBIE_HEAD), soundEvents))
                .build()
        ));
        registerable.register(ItemKeys.CREEPER_HEAD, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.CREEPER_HEAD).rarity(Rarity.UNCOMMON).build()),
            ItemComponentSet.builder()
                .with(EquipmentItemComponent.skull(blocks.getOrThrow(BlockKeys.CREEPER_HEAD), soundEvents))
                .build()
        ));
        registerable.register(ItemKeys.DRAGON_HEAD, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.DRAGON_HEAD).rarity(Rarity.UNCOMMON).build()),
            ItemComponentSet.builder()
                .with(EquipmentItemComponent.skull(blocks.getOrThrow(BlockKeys.DRAGON_HEAD), soundEvents))
                .build()
        ));
        registerable.register(ItemKeys.PIGLIN_HEAD, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.PIGLIN_HEAD).rarity(Rarity.UNCOMMON).build()),
            ItemComponentSet.builder()
                .with(EquipmentItemComponent.skull(blocks.getOrThrow(BlockKeys.PIGLIN_HEAD), soundEvents))
                .build()
        ));
        registerable.register(ItemKeys.PUMPKIN_PIE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.PUMPKIN_PIE).build()),
            ItemComponentSet.builder()
                .with(FoodItemComponent.from(FoodComponents.PUMPKIN_PIE))
                .with(new CompostableItemComponent(1.0f))
                .build()
        ));
        registerable.register(ItemKeys.FIREWORK_ROCKET, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.FIREWORK_ROCKET).build()),
            ItemComponentSet.builder()
                .with(new FireworkItemComponent())
                .with(new ProjectileItemComponent(FireworkRocketEntityInitializer.INSTANCE, 3, CrossbowItemAccessor.getFireworkRocketSpeed()))
                .with(new DispensableItemComponent(dispenseBehaviors.getOrThrow(DispenseBehaviorKeys.FIREWORK)))
                .build()
        ));
        registerable.register(ItemKeys.FIREWORK_STAR, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.FIREWORK_STAR).build()),
            ItemComponentSet.builder()
                .with(new FireworkExplosionHolderItemComponent())
                .with(new TintedItemComponent(new FireworkItemColor()))
                .build()
        ));
        registerable.register(ItemKeys.ENCHANTED_BOOK, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.ENCHANTED_BOOK).rarity(Rarity.UNCOMMON).build(), 1),
            ItemComponentSet.builder()
                .with(EnchantmentHolderItemComponent.INSTANCE)
                .with(GlintItemComponent.of(true))
                .build()
        ));
        registerable.register(ItemKeys.RABBIT, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.RABBIT).build()),
            ItemComponentSet.builder()
                .with(FoodItemComponent.from(FoodComponents.RABBIT))
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
                .with(FoodItemComponent.from(FoodComponents.RABBIT_STEW, items.getOrThrow(ItemKeys.BOWL)))
                .build()
        ));
        registerable.register(ItemKeys.RABBIT_FOOT, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.RABBIT_FOOT).build())
        ));
        registerable.register(ItemKeys.ARMOR_STAND, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.ARMOR_STAND).build(), 16),
            ItemComponentSet.builder()
                .with(EntityItemComponent.from(new ArmorStandEntityInitializer(), dispenseBehaviors))
                .build()
        ));
        registerable.register(ItemKeys.IRON_HORSE_ARMOR, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.IRON_HORSE_ARMOR).build(), 1),
            ItemComponentSet.builder()
                .with(AnimalArmorItemComponent.of(armorMaterials.getOrThrow(ArmorMaterialKeys.IRON), AnimalArmorItem.Type.EQUESTRIAN))
                .with(new DispensableItemComponent(dispenseBehaviors.getOrThrow(DispenseBehaviorKeys.HORSE_ARMOR)))
                .build()
        ));
        registerable.register(ItemKeys.GOLDEN_HORSE_ARMOR, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.GOLDEN_HORSE_ARMOR).build(), 1),
            ItemComponentSet.builder()
                .with(AnimalArmorItemComponent.of(armorMaterials.getOrThrow(ArmorMaterialKeys.GOLD), AnimalArmorItem.Type.EQUESTRIAN))
                .with(new DispensableItemComponent(dispenseBehaviors.getOrThrow(DispenseBehaviorKeys.HORSE_ARMOR)))
                .build()
        ));
        registerable.register(ItemKeys.DIAMOND_HORSE_ARMOR, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.DIAMOND_HORSE_ARMOR).build(), 1),
            ItemComponentSet.builder()
                .with(AnimalArmorItemComponent.of(armorMaterials.getOrThrow(ArmorMaterialKeys.DIAMOND), AnimalArmorItem.Type.EQUESTRIAN))
                .with(new DispensableItemComponent(dispenseBehaviors.getOrThrow(DispenseBehaviorKeys.HORSE_ARMOR)))
                .build()
        ));
        registerable.register(ItemKeys.LEATHER_HORSE_ARMOR, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.LEATHER_HORSE_ARMOR).build(), 1),
            ItemComponentSet.builder()
                .with(AnimalArmorItemComponent.of(armorMaterials.getOrThrow(ArmorMaterialKeys.LEATHER), AnimalArmorItem.Type.EQUESTRIAN))
                .with(new DispensableItemComponent(dispenseBehaviors.getOrThrow(DispenseBehaviorKeys.HORSE_ARMOR)))
                .with(new DyeableItemComponent(DyeableItem.DEFAULT_COLOR))
                .with(new TintedItemComponent(DyeableItemColor.INSTANCE))
                .build()
        ));
        registerable.register(ItemKeys.LEAD, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.LEAD).build()),
            ItemComponentSet.EMPTY,
            ItemEventMap.builder()
                .add(ItemEvents.USE_ON_BLOCK, ActionEntry.of(
                    PassingSequenceHandler.builder()
                        .add(AttachLeashedEntitiesOnBlockAction.INSTANCE)
                        .add(SwingHandAction.INSTANCE)
                ))
                .build()
        ));
        registerable.register(ItemKeys.NAME_TAG, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.NAME_TAG).build()),
            ItemComponentSet.EMPTY,
            ItemEventMap.builder()
                .add(ItemEvents.USE_ON_ENTITY, ActionEntry.of(
                    PassingSequenceHandler.builder()
                        .add(SetEntityNameFromItemAction.of(ActionContextParameter.TARGET))
                        .add(DecrementItemAction.of(1))
                        .add(SwingHandAction.INSTANCE)
                ))
                .build()
        ));
        registerable.register(ItemKeys.COMMAND_BLOCK_MINECART, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.COMMAND_BLOCK_MINECART).rarity(Rarity.EPIC).build(), 1),
            ItemComponentSet.builder()
                .with(EntityItemComponent.from(new MinecartEntityInitializer<>(EntityType.COMMAND_BLOCK_MINECART, CommandBlockMinecartEntity::new), dispenseBehaviors))
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
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.WHITE_BANNER)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .with(BannerPatternHolderItemComponent.of(DyeColor.WHITE))
                .build()
        ));
        registerable.register(ItemKeys.ORANGE_BANNER, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.ORANGE_BANNER).build(), 16),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.ORANGE_BANNER)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .with(BannerPatternHolderItemComponent.of(DyeColor.ORANGE))
                .build()
        ));
        registerable.register(ItemKeys.MAGENTA_BANNER, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.MAGENTA_BANNER).build(), 16),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.MAGENTA_BANNER)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .with(BannerPatternHolderItemComponent.of(DyeColor.MAGENTA))
                .build()
        ));
        registerable.register(ItemKeys.LIGHT_BLUE_BANNER, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.LIGHT_BLUE_BANNER).build(), 16),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.LIGHT_BLUE_BANNER)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .with(BannerPatternHolderItemComponent.of(DyeColor.LIGHT_BLUE))
                .build()
        ));
        registerable.register(ItemKeys.YELLOW_BANNER, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.YELLOW_BANNER).build(), 16),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.YELLOW_BANNER)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .with(BannerPatternHolderItemComponent.of(DyeColor.YELLOW))
                .build()
        ));
        registerable.register(ItemKeys.LIME_BANNER, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.LIME_BANNER).build(), 16),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.LIME_BANNER)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .with(BannerPatternHolderItemComponent.of(DyeColor.LIME))
                .build()
        ));
        registerable.register(ItemKeys.PINK_BANNER, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.PINK_BANNER).build(), 16),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.PINK_BANNER)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .with(BannerPatternHolderItemComponent.of(DyeColor.PINK))
                .build()
        ));
        registerable.register(ItemKeys.GRAY_BANNER, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.GRAY_BANNER).build(), 16),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.GRAY_BANNER)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .with(BannerPatternHolderItemComponent.of(DyeColor.GRAY))
                .build()
        ));
        registerable.register(ItemKeys.LIGHT_GRAY_BANNER, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.LIGHT_GRAY_BANNER).build(), 16),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.LIGHT_GRAY_BANNER)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .with(BannerPatternHolderItemComponent.of(DyeColor.LIGHT_GRAY))
                .build()
        ));
        registerable.register(ItemKeys.CYAN_BANNER, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.CYAN_BANNER).build(), 16),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.CYAN_BANNER)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .with(BannerPatternHolderItemComponent.of(DyeColor.CYAN))
                .build()
        ));
        registerable.register(ItemKeys.PURPLE_BANNER, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.PURPLE_BANNER).build(), 16),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.PURPLE_BANNER)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .with(BannerPatternHolderItemComponent.of(DyeColor.PURPLE))
                .build()
        ));
        registerable.register(ItemKeys.BLUE_BANNER, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BLUE_BANNER).build(), 16),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.BLUE_BANNER)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .with(BannerPatternHolderItemComponent.of(DyeColor.BLUE))
                .build()
        ));
        registerable.register(ItemKeys.BROWN_BANNER, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BROWN_BANNER).build(), 16),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.BROWN_BANNER)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .with(BannerPatternHolderItemComponent.of(DyeColor.BROWN))
                .build()
        ));
        registerable.register(ItemKeys.GREEN_BANNER, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.GREEN_BANNER).build(), 16),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.GREEN_BANNER)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .with(BannerPatternHolderItemComponent.of(DyeColor.GREEN))
                .build()
        ));
        registerable.register(ItemKeys.RED_BANNER, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.RED_BANNER).build(), 16),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.RED_BANNER)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .with(BannerPatternHolderItemComponent.of(DyeColor.RED))
                .build()
        ));
        registerable.register(ItemKeys.BLACK_BANNER, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BLACK_BANNER).build(), 16),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.BLACK_BANNER)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .with(BannerPatternHolderItemComponent.of(DyeColor.BLACK))
                .build()
        ));
        registerable.register(ItemKeys.END_CRYSTAL, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.END_CRYSTAL).rarity(Rarity.RARE).build()),
            ItemComponentSet.builder()
                .with(new EntityItemComponent(new EndCrystalEntityInitializer()))
                .build()
        ));
        registerable.register(ItemKeys.CHORUS_FRUIT, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.CHORUS_FRUIT).build()),
            ItemComponentSet.builder()
                .with(FoodItemComponent.from(FoodComponents.CHORUS_FRUIT))
                .build(),
            ItemEventMap.builder()
                .add(ItemEvents.CONSUME_ITEM, ActionEntry.of(new TeleportAction(16, ActionContextParameter.THIS)))
                .build()
        ));
        registerable.register(ItemKeys.TORCHFLOWER_SEEDS, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.TORCHFLOWER_SEEDS).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.TORCHFLOWER_CROP)))
                .with(new CompostableItemComponent(0.3f))
                .build()
        ));
        registerable.register(ItemKeys.PITCHER_POD, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.PITCHER_POD).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.PITCHER_CROP)))
                .with(new CompostableItemComponent(0.3f))
                .build()
        ));
        registerable.register(ItemKeys.BEETROOT, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.BEETROOT).build()),
            ItemComponentSet.builder()
                .with(FoodItemComponent.from(FoodComponents.BEETROOT))
                .with(new CompostableItemComponent(0.65f))
                .build()
        ));
        registerable.register(ItemKeys.BEETROOT_SEEDS, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.BEETROOT_SEEDS).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.BEETROOTS)))
                .with(new CompostableItemComponent(0.3f))
                .build()
        ));
        registerable.register(ItemKeys.BEETROOT_SOUP, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.BEETROOT_SOUP).build(), 1),
            ItemComponentSet.builder()
                .with(FoodItemComponent.from(FoodComponents.BEETROOT_SOUP))
                .build()
        ));
        registerable.register(ItemKeys.DRAGON_BREATH, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.DRAGON_BREATH).rarity(Rarity.UNCOMMON).build())
        ));
        registerable.register(ItemKeys.SPLASH_POTION, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.SPLASH_POTION).build(), 1),
            ItemComponentSet.builder()
                .with(new PotionHolderItemComponent())
                .with(new ThrowableItemComponent(0.5f, -20.0f))
                .with(ProjectileItemComponent.of(entityTypes.getOrThrow(EntityTypeKeys.POTION)))
                .with(new DispensableItemComponent(dispenseBehaviors.getOrThrow(DispenseBehaviorKeys.PROJECTILE)))
                .with(new TintedItemComponent(new PotionItemColor()))
                .build()
        ));
        registerable.register(ItemKeys.SPECTRAL_ARROW, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.SPECTRAL_ARROW).build()),
            ItemComponentSet.builder()
                .with(ProjectileItemComponent.persistentProjectile(EntityType.SPECTRAL_ARROW, SpectralArrowEntity::new, SpectralArrowEntity::new))
                .with(new DispensableItemComponent(dispenseBehaviors.getOrThrow(DispenseBehaviorKeys.PROJECTILE)))
                .build()
        ));
        registerable.register(ItemKeys.TIPPED_ARROW, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.TIPPED_ARROW).build()),
            ItemComponentSet.builder()
                .with(ProjectileItemComponent.persistentProjectile(EntityType.ARROW, ArrowEntity::new, ArrowEntity::new))
                .with(new DispensableItemComponent(dispenseBehaviors.getOrThrow(DispenseBehaviorKeys.PROJECTILE)))
                .with(new TintedItemComponent(new PotionItemColor()))
                .build()
        ));
        registerable.register(ItemKeys.LINGERING_POTION, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.LINGERING_POTION).build(), 1),
            ItemComponentSet.builder()
                .with(new PotionHolderItemComponent())
                .with(new ThrowableItemComponent(0.5f, -20.0f))
                .with(ProjectileItemComponent.of(entityTypes.getOrThrow(EntityTypeKeys.POTION)))
                .with(new DispensableItemComponent(dispenseBehaviors.getOrThrow(DispenseBehaviorKeys.PROJECTILE)))
                .with(new TintedItemComponent(new PotionItemColor()))
                .build()
        ));
        registerable.register(ItemKeys.SHIELD, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.SHIELD).build(), 1),
            ItemComponentSet.builder()
                .with(new DamageableItemComponent(336))
                .with(new EquipmentItemComponent(EquipmentSlot.OFFHAND, false, soundEvents.getOrThrow(SoundEventKeys.ARMOR_EQUIP_GENERIC)))
                .with(new RepairableItemComponent(ItematicItemTags.REPAIRS_SHIELD))
                .with(new DispensableItemComponent(dispenseBehaviors.getOrThrow(DispenseBehaviorKeys.ARMOR)))
                .with(new UseAnimationItemComponent(UseAction.BLOCK))
                .with(BannerPatternHolderItemComponent.of())
                .build(),
            ItemEventMap.builder()
                .add(ItemEvents.USE, ActionEntry.of(StartUsingItemAction.INSTANCE))
                .build()
        ));
        registerable.register(ItemKeys.MUSIC_DISC_13, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.MUSIC_DISC_13).rarity(Rarity.EPIC).build(), 1),
            ItemComponentSet.builder()
                .with(RecordItemComponent.of(soundEvents.getOrThrow(SoundEventKeys.MUSIC_DISC_13), ItemKeys.MUSIC_DISC_13, 178 * 20, 1))
                .build()
        ));
        registerable.register(ItemKeys.MUSIC_DISC_CAT, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.MUSIC_DISC_CAT).rarity(Rarity.EPIC).build(), 1),
            ItemComponentSet.builder()
                .with(RecordItemComponent.of(soundEvents.getOrThrow(SoundEventKeys.MUSIC_DISC_CAT), ItemKeys.MUSIC_DISC_CAT, 185 * 20, 2))
                .build()
        ));
        registerable.register(ItemKeys.MUSIC_DISC_BLOCKS, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.MUSIC_DISC_BLOCKS).rarity(Rarity.EPIC).build(), 1),
            ItemComponentSet.builder()
                .with(RecordItemComponent.of(soundEvents.getOrThrow(SoundEventKeys.MUSIC_DISC_BLOCKS), ItemKeys.MUSIC_DISC_BLOCKS, 345 * 20, 3))
                .build()
        ));
        registerable.register(ItemKeys.MUSIC_DISC_CHIRP, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.MUSIC_DISC_CHIRP).rarity(Rarity.EPIC).build(), 1),
            ItemComponentSet.builder()
                .with(RecordItemComponent.of(soundEvents.getOrThrow(SoundEventKeys.MUSIC_DISC_CHIRP), ItemKeys.MUSIC_DISC_CHIRP, 185 * 20, 4))
                .build()
        ));
        registerable.register(ItemKeys.MUSIC_DISC_FAR, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.MUSIC_DISC_FAR).rarity(Rarity.EPIC).build(), 1),
            ItemComponentSet.builder()
                .with(RecordItemComponent.of(soundEvents.getOrThrow(SoundEventKeys.MUSIC_DISC_FAR), ItemKeys.MUSIC_DISC_FAR, 174 * 20, 5))
                .build()
        ));
        registerable.register(ItemKeys.MUSIC_DISC_MALL, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.MUSIC_DISC_MALL).rarity(Rarity.EPIC).build(), 1),
            ItemComponentSet.builder()
                .with(RecordItemComponent.of(soundEvents.getOrThrow(SoundEventKeys.MUSIC_DISC_MALL), ItemKeys.MUSIC_DISC_MALL, 197 * 20, 6))
                .build()
        ));
        registerable.register(ItemKeys.MUSIC_DISC_MELLOHI, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.MUSIC_DISC_MELLOHI).rarity(Rarity.EPIC).build(), 1),
            ItemComponentSet.builder()
                .with(RecordItemComponent.of(soundEvents.getOrThrow(SoundEventKeys.MUSIC_DISC_MELLOHI), ItemKeys.MUSIC_DISC_MELLOHI, 96 * 20, 7))
                .build()
        ));
        registerable.register(ItemKeys.MUSIC_DISC_STAL, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.MUSIC_DISC_STAL).rarity(Rarity.EPIC).build(), 1),
            ItemComponentSet.builder()
                .with(RecordItemComponent.of(soundEvents.getOrThrow(SoundEventKeys.MUSIC_DISC_STAL), ItemKeys.MUSIC_DISC_STAL, 150 * 20, 8))
                .build()
        ));
        registerable.register(ItemKeys.MUSIC_DISC_STRAD, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.MUSIC_DISC_STRAD).rarity(Rarity.EPIC).build(), 1),
            ItemComponentSet.builder()
                .with(RecordItemComponent.of(soundEvents.getOrThrow(SoundEventKeys.MUSIC_DISC_STRAD), ItemKeys.MUSIC_DISC_STRAD, 188 * 20, 9))
                .build()
        ));
        registerable.register(ItemKeys.MUSIC_DISC_WARD, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.MUSIC_DISC_WARD).rarity(Rarity.EPIC).build(), 1),
            ItemComponentSet.builder()
                .with(RecordItemComponent.of(soundEvents.getOrThrow(SoundEventKeys.MUSIC_DISC_WARD), ItemKeys.MUSIC_DISC_WARD, 251 * 20, 10))
                .build()
        ));
        registerable.register(ItemKeys.MUSIC_DISC_11, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.MUSIC_DISC_11).rarity(Rarity.EPIC).build(), 1),
            ItemComponentSet.builder()
                .with(RecordItemComponent.of(soundEvents.getOrThrow(SoundEventKeys.MUSIC_DISC_11), ItemKeys.MUSIC_DISC_11, 71 * 20, 11))
                .build()
        ));
        registerable.register(ItemKeys.MUSIC_DISC_WAIT, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.MUSIC_DISC_WAIT).rarity(Rarity.EPIC).build(), 1),
            ItemComponentSet.builder()
                .with(RecordItemComponent.of(soundEvents.getOrThrow(SoundEventKeys.MUSIC_DISC_WAIT), ItemKeys.MUSIC_DISC_WAIT, 238 * 20, 12))
                .build()
        ));
        registerable.register(ItemKeys.MUSIC_DISC_OTHERSIDE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.MUSIC_DISC_OTHERSIDE).rarity(Rarity.EPIC).build(), 1),
            ItemComponentSet.builder()
                .with(RecordItemComponent.of(soundEvents.getOrThrow(SoundEventKeys.MUSIC_DISC_OTHERSIDE), ItemKeys.MUSIC_DISC_OTHERSIDE, 195 * 20, 14))
                .build()
        ));
        registerable.register(ItemKeys.MUSIC_DISC_RELIC, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.MUSIC_DISC_RELIC).rarity(Rarity.EPIC).build(), 1),
            ItemComponentSet.builder()
                .with(RecordItemComponent.of(soundEvents.getOrThrow(SoundEventKeys.MUSIC_DISC_RELIC), ItemKeys.MUSIC_DISC_RELIC, 218 * 20, 14))
                .build()
        ));
        registerable.register(ItemKeys.MUSIC_DISC_5, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.MUSIC_DISC_5).rarity(Rarity.EPIC).build(), 1),
            ItemComponentSet.builder()
                .with(RecordItemComponent.of(soundEvents.getOrThrow(SoundEventKeys.MUSIC_DISC_5), ItemKeys.MUSIC_DISC_5, 178 * 20, 15))
                .build()
        ));
        registerable.register(ItemKeys.MUSIC_DISC_PIGSTEP, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.MUSIC_DISC_PIGSTEP).rarity(Rarity.EPIC).build(), 1),
            ItemComponentSet.builder()
                .with(RecordItemComponent.of(soundEvents.getOrThrow(SoundEventKeys.MUSIC_DISC_PIGSTEP), ItemKeys.MUSIC_DISC_PIGSTEP, 149 * 20, 13))
                .build()
        ));
        registerable.register(ItemKeys.DISC_FRAGMENT_5, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.DISC_FRAGMENT_5).tooltip(ItemKeys.DISC_FRAGMENT_5).build())
        ));
        registerable.register(ItemKeys.PHANTOM_MEMBRANE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.PHANTOM_MEMBRANE).build())
        ));
        registerable.register(ItemKeys.CROSSBOW, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.CROSSBOW).build(), 1),
            ItemComponentSet.builder()
                .with(new DamageableItemComponent(465))
                .with(new ShooterItemComponent(ItematicItemTags.CROSSBOW_AMMUNITION, ItematicItemTags.BOW_AMMUNITION, true))
                .with(new UseDurationItemComponent(28))
                .with(new UseAnimationItemComponent(UseAction.CROSSBOW))
                .with(EnchantableItemComponent.enchants(1, EnchantmentTags.CROSSBOW_ENCHANTING))
                .with(ForgeableItemComponent.of(EnchantmentTags.CROSSBOW_FORGING))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.SUSPICIOUS_STEW, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.SUSPICIOUS_STEW).build(), 1),
            ItemComponentSet.builder()
                .with(FoodItemComponent.from(FoodComponents.SUSPICIOUS_STEW, items.getOrThrow(ItemKeys.BOWL)))
                .build()
        ));
        registerable.register(ItemKeys.LOOM, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.LOOM).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.LOOM)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.FLOWER_BANNER_PATTERN, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.FLOWER_BANNER_PATTERN).tooltip(ItemKeys.FLOWER_BANNER_PATTERN).build(), 1),
            ItemComponentSet.builder()
                .with(BannerPatternItemComponent.of(BannerPatternTags.FLOWER_PATTERN_ITEM))
                .build()
        ));
        registerable.register(ItemKeys.CREEPER_BANNER_PATTERN, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.CREEPER_BANNER_PATTERN).tooltip(ItemKeys.CREEPER_BANNER_PATTERN).rarity(Rarity.UNCOMMON).build(), 1),
            ItemComponentSet.builder()
                .with(BannerPatternItemComponent.of(BannerPatternTags.CREEPER_PATTERN_ITEM))
                .build()
        ));
        registerable.register(ItemKeys.SKULL_BANNER_PATTERN, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.SKULL_BANNER_PATTERN).tooltip(ItemKeys.SKULL_BANNER_PATTERN).rarity(Rarity.UNCOMMON).build(), 1),
            ItemComponentSet.builder()
                .with(BannerPatternItemComponent.of(BannerPatternTags.SKULL_PATTERN_ITEM))
                .build()
        ));
        registerable.register(ItemKeys.MOJANG_BANNER_PATTERN, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.MOJANG_BANNER_PATTERN).tooltip(ItemKeys.MOJANG_BANNER_PATTERN).rarity(Rarity.EPIC).build(), 1),
            ItemComponentSet.builder()
                .with(BannerPatternItemComponent.of(BannerPatternTags.MOJANG_PATTERN_ITEM))
                .build()
        ));
        registerable.register(ItemKeys.GLOBE_BANNER_PATTERN, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.GLOBE_BANNER_PATTERN).tooltip(ItemKeys.GLOBE_BANNER_PATTERN).build(), 1),
            ItemComponentSet.builder()
                .with(BannerPatternItemComponent.of(BannerPatternTags.GLOBE_PATTERN_ITEM))
                .build()
        ));
        registerable.register(ItemKeys.PIGLIN_BANNER_PATTERN, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.PIGLIN_BANNER_PATTERN).tooltip(ItemKeys.PIGLIN_BANNER_PATTERN).build(), 1),
            ItemComponentSet.builder()
                .with(BannerPatternItemComponent.of(BannerPatternTags.PIGLIN_PATTERN_ITEM))
                .build()
        ));
        registerable.register(ItemKeys.COMPOSTER, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.COMPOSTER).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.COMPOSTER)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.BARREL, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BARREL).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.BARREL)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.CARTOGRAPHY_TABLE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.CARTOGRAPHY_TABLE).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.CARTOGRAPHY_TABLE)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.FLETCHING_TABLE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.FLETCHING_TABLE).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.FLETCHING_TABLE)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.SMITHING_TABLE, create(
            new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.SMITHING_TABLE).build()),
            ItemComponentSet.builder()
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.SMITHING_TABLE)))
                .with(new FuelItemComponent(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                .build()
        ));
        registerable.register(ItemKeys.SWEET_BERRIES, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.SWEET_BERRIES).build()),
            ItemComponentSet.builder()
                .with(FoodItemComponent.from(FoodComponents.SWEET_BERRIES))
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.SWEET_BERRY_BUSH)))
                .with(new CompostableItemComponent(0.3f))
                .build()
        ));
        registerable.register(ItemKeys.GLOW_BERRIES, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.GLOW_BERRIES).build()),
            ItemComponentSet.builder()
                .with(FoodItemComponent.from(FoodComponents.GLOW_BERRIES))
                .with(BlockItemComponent.of(blocks.getOrThrow(BlockKeys.CAVE_VINES)))
                .with(new CompostableItemComponent(0.3f))
                .build()
        ));
        registerable.register(ItemKeys.HONEYCOMB, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.HONEYCOMB).build()),
            ItemComponentSet.builder()
                .with(new DispensableItemComponent(dispenseBehaviors.getOrThrow(DispenseBehaviorKeys.WAX_BLOCK)))
                .build(),
            ItemEventMap.builder()
                .add(ItemEvents.USE_ON_BLOCK, ActionEntry.of(
                    FirstToPassRequirementsSequenceHandler.builder()
                        .add(Actions.waxSign(true))
                        .add(PassingSequenceHandler.builder()
                            .add(new WaxBlockAction(ActionContextParameter.TARGET))
                            .add(DecrementItemAction.of(1))
                            .add(SwingHandAction.INSTANCE))))
                .build()
        ));
        registerable.register(ItemKeys.HONEY_BOTTLE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.HONEY_BOTTLE).build(), 16),
            ItemComponentSet.builder()
                .with(FoodItemComponent.from(FoodComponents.HONEY_BOTTLE, 40, UseAction.DRINK, items.getOrThrow(ItemKeys.GLASS_BOTTLE)))
                .build()
        ));
        registerable.register(ItemKeys.NETHERITE_UPGRADE_SMITHING_TEMPLATE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.NETHERITE_UPGRADE_SMITHING_TEMPLATE).build()),
            ItemComponentSet.builder()
                .with(SmithingTemplateItemComponent.of(smithingTemplates.getOrThrow(SmithingTemplates.NETHERITE_UPGRADE)))
                .build()
        ));
        registerable.register(ItemKeys.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE).build()),
            ItemComponentSet.builder()
                .with(SmithingTemplateItemComponent.of(smithingTemplates.getOrThrow(SmithingTemplates.SENTRY_PATTERN)))
                .build()
        ));
        registerable.register(ItemKeys.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE).build()),
            ItemComponentSet.builder()
                .with(SmithingTemplateItemComponent.of(smithingTemplates.getOrThrow(SmithingTemplates.DUNE_PATTERN)))
                .build()
        ));
        registerable.register(ItemKeys.COAST_ARMOR_TRIM_SMITHING_TEMPLATE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.COAST_ARMOR_TRIM_SMITHING_TEMPLATE).build()),
            ItemComponentSet.builder()
                .with(SmithingTemplateItemComponent.of(smithingTemplates.getOrThrow(SmithingTemplates.COAST_PATTERN)))
                .build()
        ));
        registerable.register(ItemKeys.WILD_ARMOR_TRIM_SMITHING_TEMPLATE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.WILD_ARMOR_TRIM_SMITHING_TEMPLATE).build()),
            ItemComponentSet.builder()
                .with(SmithingTemplateItemComponent.of(smithingTemplates.getOrThrow(SmithingTemplates.WILD_PATTERN)))
                .build()
        ));
        registerable.register(ItemKeys.WARD_ARMOR_TRIM_SMITHING_TEMPLATE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.WARD_ARMOR_TRIM_SMITHING_TEMPLATE).build()),
            ItemComponentSet.builder()
                .with(SmithingTemplateItemComponent.of(smithingTemplates.getOrThrow(SmithingTemplates.WARD_PATTERN)))
                .build()
        ));
        registerable.register(ItemKeys.EYE_ARMOR_TRIM_SMITHING_TEMPLATE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.EYE_ARMOR_TRIM_SMITHING_TEMPLATE).build()),
            ItemComponentSet.builder()
                .with(SmithingTemplateItemComponent.of(smithingTemplates.getOrThrow(SmithingTemplates.EYE_PATTERN)))
                .build()
        ));
        registerable.register(ItemKeys.VEX_ARMOR_TRIM_SMITHING_TEMPLATE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.VEX_ARMOR_TRIM_SMITHING_TEMPLATE).build()),
            ItemComponentSet.builder()
                .with(SmithingTemplateItemComponent.of(smithingTemplates.getOrThrow(SmithingTemplates.VEX_PATTERN)))
                .build()
        ));
        registerable.register(ItemKeys.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE).build()),
            ItemComponentSet.builder()
                .with(SmithingTemplateItemComponent.of(smithingTemplates.getOrThrow(SmithingTemplates.TIDE_PATTERN)))
                .build()
        ));
        registerable.register(ItemKeys.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE).build()),
            ItemComponentSet.builder()
                .with(SmithingTemplateItemComponent.of(smithingTemplates.getOrThrow(SmithingTemplates.SNOUT_PATTERN)))
                .build()
        ));
        registerable.register(ItemKeys.RIB_ARMOR_TRIM_SMITHING_TEMPLATE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.RIB_ARMOR_TRIM_SMITHING_TEMPLATE).build()),
            ItemComponentSet.builder()
                .with(SmithingTemplateItemComponent.of(smithingTemplates.getOrThrow(SmithingTemplates.RIB_PATTERN)))
                .build()
        ));
        registerable.register(ItemKeys.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE).build()),
            ItemComponentSet.builder()
                .with(SmithingTemplateItemComponent.of(smithingTemplates.getOrThrow(SmithingTemplates.SPIRE_PATTERN)))
                .build()
        ));
        registerable.register(ItemKeys.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE).build()),
            ItemComponentSet.builder()
                .with(SmithingTemplateItemComponent.of(smithingTemplates.getOrThrow(SmithingTemplates.WAYFINDER_PATTERN)))
                .build()
        ));
        registerable.register(ItemKeys.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE).build()),
            ItemComponentSet.builder()
                .with(SmithingTemplateItemComponent.of(smithingTemplates.getOrThrow(SmithingTemplates.SHAPER_PATTERN)))
                .build()
        ));
        registerable.register(ItemKeys.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE).build()),
            ItemComponentSet.builder()
                .with(SmithingTemplateItemComponent.of(smithingTemplates.getOrThrow(SmithingTemplates.SILENCE_PATTERN)))
                .build()
        ));
        registerable.register(ItemKeys.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE).build()),
            ItemComponentSet.builder()
                .with(SmithingTemplateItemComponent.of(smithingTemplates.getOrThrow(SmithingTemplates.RAISER_PATTERN)))
                .build()
        ));
        registerable.register(ItemKeys.HOST_ARMOR_TRIM_SMITHING_TEMPLATE, create(
            new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.HOST_ARMOR_TRIM_SMITHING_TEMPLATE).build()),
            ItemComponentSet.builder()
                .with(SmithingTemplateItemComponent.of(smithingTemplates.getOrThrow(SmithingTemplates.HOST_PATTERN)))
                .build()
        ));
    }

    public static RegistryKey<Item> keyFromBlock(Block block) {
        Identifier id = Registries.BLOCK.getId(block);
        return RegistryKey.of(RegistryKeys.ITEM, id);
    }

    private static Item create(ItemBase base) {
        return create(base, ItemComponentSet.EMPTY);
    }

    private static Item create(ItemBase base, ItemComponentSet components) {
        return create(base, components, ItemEventMap.EMPTY);
    }

    private static Item create(ItemBase base, ItemComponentSet components, ItemEventMap events) {
        Item item = new Item(new Item.Settings());
        item.itematic$setItemBase(base);
        item.itematic$setComponents(components);
        item.itematic$setEvents(events);
        return item;
    }
}
