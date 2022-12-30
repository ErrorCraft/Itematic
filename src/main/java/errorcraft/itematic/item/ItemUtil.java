package errorcraft.itematic.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.Item;
import net.minecraft.registry.Registerable;

public class ItemUtil {
    public static final Codec<Item> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
        ItemBase.CODEC.fieldOf("base").forGetter(Item::getItemBase)
    ).apply(instance, ItemUtil::create));

    public static void bootstrap(Registerable<Item> registerable) {
        registerable.register(ItemKeys.AIR, create(new ItemBase(64)));
        registerable.register(ItemKeys.STONE, create(new ItemBase(64)));
        registerable.register(ItemKeys.GRASS_BLOCK, create(new ItemBase(64)));
        registerable.register(ItemKeys.SAND, create(new ItemBase(64)));
        registerable.register(ItemKeys.GRASS, create(new ItemBase(64)));
        registerable.register(ItemKeys.SNOW, create(new ItemBase(64)));
        registerable.register(ItemKeys.BARRIER, create(new ItemBase(64)));
        registerable.register(ItemKeys.REDSTONE, create(new ItemBase(64)));
        registerable.register(ItemKeys.FEATHER, create(new ItemBase(64)));
        registerable.register(ItemKeys.WATER_BUCKET, create(new ItemBase(1)));
    }

    private static Item create(ItemBase base) {
        Item item = new Item(new Item.Settings());
        item.setItemBase(base);
        return item;
    }
}
