package net.errorcraft.itematic.world.item.behavior;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.core.registries.ItematicBuiltInRegistries;
import net.errorcraft.itematic.world.ItemResult;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.util.Map;

public interface ItemBehavior<T extends ItemBehavior<T>> {
    Codec<Map<ItemBehaviorType<?>, ItemBehavior<?>>> MAP_CODEC = Codec.dispatchedMap(
        ItematicBuiltInRegistries.ITEM_BEHAVIOR_TYPE.byNameCodec(),
        ItemBehaviorType::codec
    );

    ItemBehaviorType<T> type();
    default ItemResult use(Level level, Player user, InteractionHand hand, ItemStack stack, ItemStackExchanger stackExchanger) {
        return ItemResult.PASS;
    }
    default ItemResult useOnBlock(UseOnContext context, ItemStackExchanger stackExchanger) {
        return ItemResult.PASS;
    }
    default ItemResult useOnEntity(Player user, LivingEntity target, InteractionHand hand, ItemStack stack, ItemStackExchanger stackExchanger) {
        return ItemResult.PASS;
    }
    default void using(ItemStack stack, Level level, LivingEntity user, int usedTicks, int remainingUseTicks) {}
    default boolean stopUsing(ItemStack stack, Level level, LivingEntity user, int usedTicks, int remainingUseTicks, ItemStackExchanger stackExchanger) {
        return false;
    }
    default boolean clickOnSlot(ItemStack stack, Slot slot, ClickAction clickAction, Player user) {
        return false;
    }
    default boolean clickedOnWithStack(ItemStack stack, ItemStack cursorStack, Slot slot, ClickAction clickAction, Player user, ItemStackExchanger stackExchanger) {
        return false;
    }
    default void onCraft(ItemStack stack, Level level) {}
    default void addComponents(DataComponentMap.Builder builder) {}
}
