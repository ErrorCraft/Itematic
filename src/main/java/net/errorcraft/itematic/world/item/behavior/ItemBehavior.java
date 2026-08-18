package net.errorcraft.itematic.world.item.behavior;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.registry.ItematicRegistries;
import net.errorcraft.itematic.serialization.SetMapCodec;
import net.errorcraft.itematic.world.ItemResult;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Set;
import java.util.function.Consumer;

public interface ItemBehavior<T extends ItemBehavior<T>> {
    SetMapCodec<ItemBehaviorType<?>, ItemBehavior<?>> SET_MAP_CODEC = SetMapCodec.ofRegistry(ItematicRegistries.ITEM_BEHAVIOR_TYPE, ItemBehaviorType::codec, ItemBehavior::codec, ItemBehavior::type);
    Codec<Set<ItemBehavior<?>>> SET_CODEC = SET_MAP_CODEC.codec();

    ItemBehaviorType<T> type();
    Codec<T> codec();

    default ItemResult use(Level world, Player user, InteractionHand hand, ItemStack stack, ItemStackExchanger stackExchanger) {
        return ItemResult.PASS;
    }

    default ItemResult useOnBlock(UseOnContext context, ItemStackExchanger stackExchanger) {
        return ItemResult.PASS;
    }

    default ItemResult useOnEntity(Player user, LivingEntity target, InteractionHand hand, ItemStack stack, ItemStackExchanger stackExchanger) {
        return ItemResult.PASS;
    }

    default void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker, ItemStackExchanger stackExchanger) {
    }

    default boolean postMine(ItemStack stack, Level world, BlockState state, BlockPos pos, LivingEntity miner, ItemStackExchanger stackExchanger) {
        return false;
    }

    default void using(ItemStack stack, Level world, LivingEntity user, int usedTicks, int remainingUseTicks) {}

    default boolean stopUsing(ItemStack stack, Level world, LivingEntity user, int usedTicks, int remainingUseTicks, ItemStackExchanger stackExchanger) {
        return false;
    }

    default void finishUsing(Level world, LivingEntity user, ItemStack stack, int usedTicks, ItemStackExchanger stackExchanger) {}

    default boolean clickOnSlot(ItemStack stack, Slot slot, ClickAction clickType, Player user) {
        return false;
    }

    default boolean clickedOnWithStack(ItemStack stack, ItemStack cursorStack, Slot slot, ClickAction clickType, Player user, ItemStackExchanger stackExchanger) {
        return false;
    }

    default void onCraft(ItemStack stack, Level world) {}

    default void addComponents(DataComponentMap.Builder builder) {}

    default void appendTooltip(ItemStack stack, Item.TooltipContext context, Consumer<Component> builder, TooltipFlag tooltipFlag) {}
}
