package errorcraft.itematic.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import errorcraft.itematic.item.armor.ArmorMaterial;
import errorcraft.itematic.item.armor.ArmorMaterialKeys;
import errorcraft.itematic.item.armor.ArmorMaterials;
import errorcraft.itematic.item.component.ItemComponentSet;
import errorcraft.itematic.item.component.components.*;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.FoodComponents;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.*;
import net.minecraft.util.Identifier;

public class ItemUtil {
    public static final Codec<Item> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
        ItemBase.CODEC.fieldOf("base").forGetter(Item::getItemBase),
        ItemComponentSet.CODEC.fieldOf("components").forGetter(Item::getComponents)
    ).apply(instance, ItemUtil::create));

    public static void bootstrap(Registerable<Item> registerable) {
        RegistryEntryLookup<ArmorMaterial> armorMaterials = registerable.getRegistryLookup(ArmorMaterials.ARMOR_MATERIAL_KEY);

        registerable.register(ItemKeys.AIR, create(new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.AIR).build(), 64)));
        registerable.register(ItemKeys.STONE, create(new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.STONE).build(), 64), ItemComponentSet.builder().with(new BlockItemComponent(Blocks.STONE)).build()));
        registerable.register(ItemKeys.GRASS_BLOCK, create(new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.GRASS_BLOCK).build(), 64), ItemComponentSet.builder().with(new BlockItemComponent(Blocks.GRASS_BLOCK)).build()));
        registerable.register(ItemKeys.SAND, create(new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.SAND).build(), 64), ItemComponentSet.builder().with(new BlockItemComponent(Blocks.SAND)).build()));
        registerable.register(ItemKeys.GRASS, create(new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.GRASS).build(), 64), ItemComponentSet.builder().with(new BlockItemComponent(Blocks.GRASS), new CompostableItemComponent(0.3f)).build()));
        registerable.register(ItemKeys.OAK_SLAB, create(new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.OAK_SLAB).build(), 64), ItemComponentSet.builder().with(new BlockItemComponent(Blocks.OAK_SLAB)).build()));
        registerable.register(ItemKeys.SNOW, create(new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.SNOW).build(), 64), ItemComponentSet.builder().with(new BlockItemComponent(Blocks.SNOW)).build()));
        registerable.register(ItemKeys.BARRIER, create(new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BARRIER).build(), 64), ItemComponentSet.builder().with(new BlockItemComponent(Blocks.BARRIER)).build()));
        registerable.register(ItemKeys.REDSTONE, create(new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.REDSTONE).build(), 64), ItemComponentSet.builder().with(new BlockItemComponent(Blocks.REDSTONE_WIRE)).build()));
        registerable.register(ItemKeys.APPLE, create(new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.APPLE).build(), 64), ItemComponentSet.builder().with(FoodItemComponent.from(FoodComponents.APPLE)).with(new CompostableItemComponent(0.65f)).build()));
        registerable.register(ItemKeys.IRON_SWORD, create(new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.IRON_SWORD).build(), 1), ItemComponentSet.builder().with(ToolItemComponent.from(ToolMaterials.IRON, 2, false)).with(new WeaponItemComponent(1)).build()));
        registerable.register(ItemKeys.IRON_PICKAXE, create(new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.IRON_PICKAXE).build(), 1), ItemComponentSet.builder().with(ToolItemComponent.from(ToolMaterials.IRON, 1)).with(new WeaponItemComponent(2)).build()));
        registerable.register(ItemKeys.MUSHROOM_STEW, create(new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.MUSHROOM_STEW).build(), 1), ItemComponentSet.builder().with(FoodItemComponent.from(FoodComponents.MUSHROOM_STEW)).build()));
        registerable.register(ItemKeys.FEATHER, create(new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.FEATHER).build(), 64), ItemComponentSet.builder().with(new TestItemComponent(true)).build()));
        registerable.register(ItemKeys.BREAD, create(new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.BREAD).build(), 64), ItemComponentSet.builder().with(FoodItemComponent.from(FoodComponents.BREAD)).with(new CompostableItemComponent(0.85f)).build()));
        registerable.register(ItemKeys.IRON_HELMET, create(new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.IRON_HELMET).build(), 1), ItemComponentSet.builder().with(ArmorItemComponent.from(net.minecraft.item.ArmorMaterials.IRON, ArmorItem.Type.HELMET, armorMaterials.getOrThrow(ArmorMaterialKeys.IRON))).build()));
        registerable.register(ItemKeys.PORKCHOP, create(new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.PORKCHOP).build(), 64), ItemComponentSet.builder().with(FoodItemComponent.from(FoodComponents.PORKCHOP)).build()));
        registerable.register(ItemKeys.COOKED_PORKCHOP, create(new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.COOKED_PORKCHOP).build(), 64), ItemComponentSet.builder().with(FoodItemComponent.from(FoodComponents.COOKED_PORKCHOP)).build()));
        registerable.register(ItemKeys.GOLDEN_APPLE, create(new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.GOLDEN_APPLE).build(), 64), ItemComponentSet.builder().with(FoodItemComponent.from(FoodComponents.GOLDEN_APPLE)).build()));
        registerable.register(ItemKeys.ENCHANTED_GOLDEN_APPLE, create(new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.ENCHANTED_GOLDEN_APPLE).build(), 64), ItemComponentSet.builder().with(FoodItemComponent.from(FoodComponents.ENCHANTED_GOLDEN_APPLE)).build()));
        registerable.register(ItemKeys.WATER_BUCKET, create(new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.WATER_BUCKET).build(), 1)));
        registerable.register(ItemKeys.COD, create(new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.COD).build(), 64), ItemComponentSet.builder().with(FoodItemComponent.from(FoodComponents.COD)).build()));
        registerable.register(ItemKeys.SALMON, create(new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.SALMON).build(), 64), ItemComponentSet.builder().with(FoodItemComponent.from(FoodComponents.SALMON)).build()));
        registerable.register(ItemKeys.TROPICAL_FISH, create(new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.TROPICAL_FISH).build(), 64), ItemComponentSet.builder().with(FoodItemComponent.from(FoodComponents.TROPICAL_FISH)).build()));
        registerable.register(ItemKeys.PUFFERFISH, create(new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.PUFFERFISH).build(), 64), ItemComponentSet.builder().with(FoodItemComponent.from(FoodComponents.PUFFERFISH)).build()));
        registerable.register(ItemKeys.COOKED_COD, create(new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.COOKED_COD).build(), 64), ItemComponentSet.builder().with(FoodItemComponent.from(FoodComponents.COOKED_COD)).build()));
        registerable.register(ItemKeys.COOKED_SALMON, create(new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.COOKED_SALMON).build(), 64), ItemComponentSet.builder().with(FoodItemComponent.from(FoodComponents.COOKED_SALMON)).build()));
        registerable.register(ItemKeys.COOKIE, create(new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.COOKIE).build(), 64), ItemComponentSet.builder().with(FoodItemComponent.from(FoodComponents.COOKIE)).with(new CompostableItemComponent(0.85f)).build()));
        registerable.register(ItemKeys.MELON_SLICE, create(new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.MELON_SLICE).build(), 64), ItemComponentSet.builder().with(FoodItemComponent.from(FoodComponents.MELON_SLICE)).with(new CompostableItemComponent(0.5f)).build()));
        registerable.register(ItemKeys.DRIED_KELP, create(new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.DRIED_KELP).build(), 64), ItemComponentSet.builder().with(FoodItemComponent.from(FoodComponents.DRIED_KELP)).with(new CompostableItemComponent(0.3f)).build()));
        registerable.register(ItemKeys.BEEF, create(new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.BEEF).build(), 64), ItemComponentSet.builder().with(FoodItemComponent.from(FoodComponents.BEEF)).build()));
        registerable.register(ItemKeys.COOKED_BEEF, create(new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.COOKED_BEEF).build(), 64), ItemComponentSet.builder().with(FoodItemComponent.from(FoodComponents.COOKED_BEEF)).build()));
        registerable.register(ItemKeys.CHICKEN, create(new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.CHICKEN).build(), 64), ItemComponentSet.builder().with(FoodItemComponent.from(FoodComponents.CHICKEN)).build()));
        registerable.register(ItemKeys.COOKED_CHICKEN, create(new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.COOKED_CHICKEN).build(), 64), ItemComponentSet.builder().with(FoodItemComponent.from(FoodComponents.COOKED_CHICKEN)).build()));
        registerable.register(ItemKeys.ROTTEN_FLESH, create(new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.ROTTEN_FLESH).build(), 64), ItemComponentSet.builder().with(FoodItemComponent.from(FoodComponents.ROTTEN_FLESH)).build()));
        registerable.register(ItemKeys.SPIDER_EYE, create(new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.SPIDER_EYE).build(), 64), ItemComponentSet.builder().with(FoodItemComponent.from(FoodComponents.SPIDER_EYE)).build()));
        registerable.register(ItemKeys.PIG_SPAWN_EGG, create(new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.PIG_SPAWN_EGG).build(), 64), ItemComponentSet.builder().with(new EntityItemComponent(EntityType.PIG)).build()));
        registerable.register(ItemKeys.CARROT, create(new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.CARROT).build(), 64), ItemComponentSet.builder().with(FoodItemComponent.from(FoodComponents.CARROT)).with(new BlockItemComponent(Blocks.CARROTS), new CompostableItemComponent(0.65f)).build()));
        registerable.register(ItemKeys.POTATO, create(new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.POTATO).build(), 64), ItemComponentSet.builder().with(FoodItemComponent.from(FoodComponents.POTATO)).with(new BlockItemComponent(Blocks.POTATOES), new CompostableItemComponent(0.65f)).build()));
        registerable.register(ItemKeys.BAKED_POTATO, create(new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.BAKED_POTATO).build(), 64), ItemComponentSet.builder().with(FoodItemComponent.from(FoodComponents.BAKED_POTATO)).with(new CompostableItemComponent(0.85f)).build()));
        registerable.register(ItemKeys.POISONOUS_POTATO, create(new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.POISONOUS_POTATO).build(), 64), ItemComponentSet.builder().with(FoodItemComponent.from(FoodComponents.POISONOUS_POTATO)).build()));
        registerable.register(ItemKeys.GOLDEN_CARROT, create(new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.GOLDEN_CARROT).build(), 64), ItemComponentSet.builder().with(FoodItemComponent.from(FoodComponents.GOLDEN_CARROT)).build()));
        registerable.register(ItemKeys.PUMPKIN_PIE, create(new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.PUMPKIN_PIE).build(), 64), ItemComponentSet.builder().with(FoodItemComponent.from(FoodComponents.PUMPKIN_PIE)).with(new CompostableItemComponent(1.0f)).build()));
        registerable.register(ItemKeys.RABBIT, create(new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.RABBIT).build(), 64), ItemComponentSet.builder().with(FoodItemComponent.from(FoodComponents.RABBIT_STEW)).build()));
        registerable.register(ItemKeys.COOKED_RABBIT, create(new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.COOKED_RABBIT).build(), 64), ItemComponentSet.builder().with(FoodItemComponent.from(FoodComponents.COOKED_RABBIT)).build()));
        registerable.register(ItemKeys.RABBIT_STEW, create(new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.RABBIT_STEW).build(), 1), ItemComponentSet.builder().with(FoodItemComponent.from(FoodComponents.RABBIT_STEW)).build()));
        registerable.register(ItemKeys.MUTTON, create(new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.MUTTON).build(), 64), ItemComponentSet.builder().with(FoodItemComponent.from(FoodComponents.MUTTON)).build()));
        registerable.register(ItemKeys.COOKED_MUTTON, create(new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.COOKED_MUTTON).build(), 64), ItemComponentSet.builder().with(FoodItemComponent.from(FoodComponents.COOKED_MUTTON)).build()));
        registerable.register(ItemKeys.CHORUS_FRUIT, create(new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.CHORUS_FRUIT).build(), 64), ItemComponentSet.builder().with(FoodItemComponent.from(FoodComponents.CHORUS_FRUIT)).build()));
        registerable.register(ItemKeys.BEETROOT, create(new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.BEETROOT).build(), 64), ItemComponentSet.builder().with(FoodItemComponent.from(FoodComponents.BEETROOT)).with(new CompostableItemComponent(0.65f)).build()));
        registerable.register(ItemKeys.BEETROOT_SOUP, create(new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.BEETROOT_SOUP).build(), 1), ItemComponentSet.builder().with(FoodItemComponent.from(FoodComponents.BEETROOT_SOUP)).build()));
        registerable.register(ItemKeys.SHIELD, create(new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.SHIELD).build(), 1), ItemComponentSet.builder().with(new EquipmentItemComponent(EquipmentSlot.OFFHAND, false)).build()));
        registerable.register(ItemKeys.SUSPICIOUS_STEW, create(new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.SUSPICIOUS_STEW).build(), 1), ItemComponentSet.builder().with(FoodItemComponent.from(FoodComponents.SUSPICIOUS_STEW)).build()));
        registerable.register(ItemKeys.SWEET_BERRIES, create(new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.SWEET_BERRIES).build(), 64), ItemComponentSet.builder().with(FoodItemComponent.from(FoodComponents.SWEET_BERRIES)).with(new BlockItemComponent(Blocks.SWEET_BERRY_BUSH), new CompostableItemComponent(0.3f)).build()));
        registerable.register(ItemKeys.GLOW_BERRIES, create(new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.GLOW_BERRIES).build(), 64), ItemComponentSet.builder().with(FoodItemComponent.from(FoodComponents.GLOW_BERRIES)).with(new BlockItemComponent(Blocks.CAVE_VINES), new CompostableItemComponent(0.3f)).build()));
        registerable.register(ItemKeys.HONEY_BOTTLE, create(new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.HONEY_BOTTLE).build(), 64), ItemComponentSet.builder().with(FoodItemComponent.from(FoodComponents.HONEY_BOTTLE, 40)).build()));
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
