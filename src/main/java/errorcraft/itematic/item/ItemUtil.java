package errorcraft.itematic.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import errorcraft.itematic.item.component.ItemComponentSet;
import errorcraft.itematic.item.component.components.*;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.FoodComponents;
import net.minecraft.item.Item;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ItemUtil {
    public static final Codec<Item> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
        ItemBase.CODEC.fieldOf("base").forGetter(Item::getItemBase),
        ItemComponentSet.CODEC.fieldOf("components").forGetter(Item::getComponents)
    ).apply(instance, ItemUtil::create));

    public static void bootstrap(Registerable<Item> registerable) {
        registerable.register(ItemKeys.AIR, create(new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.AIR).build(), 64)));
        registerable.register(ItemKeys.STONE, create(new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.STONE).build(), 64), ItemComponentSet.builder().with(new BlockItemComponent(Blocks.STONE)).build()));
        registerable.register(ItemKeys.GRASS_BLOCK, create(new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.GRASS_BLOCK).build(), 64), ItemComponentSet.builder().with(new BlockItemComponent(Blocks.GRASS_BLOCK)).build()));
        registerable.register(ItemKeys.SAND, create(new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.SAND).build(), 64), ItemComponentSet.builder().with(new BlockItemComponent(Blocks.SAND)).build()));
        registerable.register(ItemKeys.GRASS, create(new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.GRASS).build(), 64), ItemComponentSet.builder().with(new BlockItemComponent(Blocks.GRASS)).build()));
        registerable.register(ItemKeys.OAK_SLAB, create(new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.OAK_SLAB).build(), 64), ItemComponentSet.builder().with(new BlockItemComponent(Blocks.OAK_SLAB)).build()));
        registerable.register(ItemKeys.SNOW, create(new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.SNOW).build(), 64), ItemComponentSet.builder().with(new BlockItemComponent(Blocks.SNOW)).build()));
        registerable.register(ItemKeys.BARRIER, create(new ItemBase(ItemBaseDisplay.Builder.forBlock(ItemKeys.BARRIER).build(), 64), ItemComponentSet.builder().with(new BlockItemComponent(Blocks.BARRIER)).build()));
        registerable.register(ItemKeys.REDSTONE, create(new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.REDSTONE).build(), 64), ItemComponentSet.builder().with(new BlockItemComponent(Blocks.REDSTONE_WIRE)).build()));
        registerable.register(ItemKeys.APPLE, create(new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.APPLE).build(), 64), ItemComponentSet.builder().with(FoodItemComponent.from(FoodComponents.APPLE)).build()));
        registerable.register(ItemKeys.IRON_PICKAXE, create(new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.IRON_PICKAXE).build(), 1), ItemComponentSet.builder().with(new DamageableItemComponent(100), new ToolItemComponent(1)).build()));
        registerable.register(ItemKeys.FEATHER, create(new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.FEATHER).build(), 64), ItemComponentSet.builder().with(new TestItemComponent(true)).build()));
        registerable.register(ItemKeys.WATER_BUCKET, create(new ItemBase(ItemBaseDisplay.Builder.forItem(ItemKeys.WATER_BUCKET).build(), 1)));
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
