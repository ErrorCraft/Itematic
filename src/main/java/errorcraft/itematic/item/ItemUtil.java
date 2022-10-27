package errorcraft.itematic.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import errorcraft.itematic.mixin.util.registry.RegistryAccessor;
import errorcraft.itematic.registry.BuiltinRegistriesUtil;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.DefaultedRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;

public class ItemUtil {
    public static final DefaultedRegistry<Item> REGISTRY = BuiltinRegistriesUtil.addDefaultedRegistry("air", Registry.ITEM_KEY, ItemUtil::initAndGetDefault);
    public static final Codec<Item> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
        Codec.INT.fieldOf("max_count").forGetter(Item::getMaxCount)
    ).apply(instance, ItemUtil::create));

    public static RegistryEntry<? extends Item> initAndGetDefault(Registry<Item> registry) {
        RegistryEntry<Item> air = BuiltinRegistries.add(registry, ItemKeys.AIR, Items.AIR);
        BuiltinRegistries.add(registry, ItemKeys.STONE, Items.STONE);
        return air;
    }

    public static void init() {
        RegistryAccessor.setItemRegistry(ItemUtil.REGISTRY);
    }

    private static Item create(int maxCount) {
        Item.Settings settings = new Item.Settings().maxCount(maxCount);
        return new Item(settings);
    }
}
