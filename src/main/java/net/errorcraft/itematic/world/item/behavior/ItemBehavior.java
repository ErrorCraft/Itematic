package net.errorcraft.itematic.world.item.behavior;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.core.registries.ItematicBuiltInRegistries;
import net.minecraft.core.component.DataComponentMap;

import java.util.Map;

public interface ItemBehavior<T extends ItemBehavior<T>> {
    Codec<Map<ItemBehaviorType<?>, ItemBehavior<?>>> MAP_CODEC = Codec.dispatchedMap(
        ItematicBuiltInRegistries.ITEM_BEHAVIOR_TYPE.byNameCodec(),
        ItemBehaviorType::codec
    );

    ItemBehaviorType<T> type();
    void addComponents(DataComponentMap.Builder builder);
}
