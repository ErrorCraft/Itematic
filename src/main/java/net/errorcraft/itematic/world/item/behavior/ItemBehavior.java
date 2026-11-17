package net.errorcraft.itematic.world.item.behavior;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.core.registries.ItematicBuiltInRegistries;
import net.minecraft.core.component.DataComponentMap;

import java.util.Map;
import java.util.function.Function;

public interface ItemBehavior {
    Codec<Map<Codec<? extends ItemBehavior>, ItemBehavior>> CODEC = Codec.dispatchedMap(
        ItematicBuiltInRegistries.ITEM_BEHAVIOR_TYPE.byNameCodec(),
        Function.identity()
    );

    void addComponents(DataComponentMap.Builder builder);
}
