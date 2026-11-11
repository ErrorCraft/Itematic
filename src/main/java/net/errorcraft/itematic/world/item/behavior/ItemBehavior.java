package net.errorcraft.itematic.world.item.behavior;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.core.registries.ItematicBuiltInRegistries;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Map;

public interface ItemBehavior<T extends ItemBehavior<T>> {
    Codec<Map<ItemBehaviorType<?>, ItemBehavior<?>>> MAP_CODEC = Codec.dispatchedMap(
        ItematicBuiltInRegistries.ITEM_BEHAVIOR_TYPE.byNameCodec(),
        ItemBehaviorType::codec
    );

    ItemBehaviorType<T> type();
    default void using(ItemStack stack, Level level, LivingEntity user, int usedTicks, int remainingUseTicks) {}
    default void onCraft(ItemStack stack, Level level) {}
    default void addComponents(DataComponentMap.Builder builder) {}
}
