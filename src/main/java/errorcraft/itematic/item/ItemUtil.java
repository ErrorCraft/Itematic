package errorcraft.itematic.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registerable;

public class ItemUtil {
    public static final Codec<Item> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
        Codec.INT.fieldOf("max_count").forGetter(Item::getMaxCount)
    ).apply(instance, ItemUtil::create));

    public static void bootstrap(Registerable<Item> registerable) {
        registerable.register(ItemKeys.AIR, Items.AIR);
        registerable.register(ItemKeys.STONE, Items.STONE);
    }

    private static Item create(int maxCount) {
        Item.Settings settings = new Item.Settings().maxCount(maxCount);
        return new Item(settings);
    }
}
