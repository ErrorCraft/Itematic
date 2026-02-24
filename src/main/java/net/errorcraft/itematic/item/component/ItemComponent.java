package net.errorcraft.itematic.item.component;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.ItemResult;
import net.errorcraft.itematic.item.ItemStackConsumer;
import net.errorcraft.itematic.registry.ItematicRegistries;
import net.errorcraft.itematic.serialization.SetMapCodec;
import net.minecraft.block.BlockState;
import net.minecraft.component.ComponentMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Set;

public interface ItemComponent<T extends ItemComponent<T>> {
    SetMapCodec<ItemComponentType<?>, ItemComponent<?>> SET_MAP_CODEC = SetMapCodec.ofRegistry(ItematicRegistries.ITEM_COMPONENT_TYPE, ItemComponentType::codec, ItemComponent::codec, ItemComponent::type);
    Codec<Set<ItemComponent<?>>> SET_CODEC = SET_MAP_CODEC.codec();

    ItemComponentType<T> type();
    Codec<T> codec();

    default ItemResult use(World world, PlayerEntity user, Hand hand, ItemStack stack, ItemStackConsumer resultStackConsumer) {
        return ItemResult.PASS;
    }

    default ItemResult useOnBlock(ItemUsageContext context, ItemStackConsumer resultStackConsumer) {
        return ItemResult.PASS;
    }

    default ItemResult useOnEntity(PlayerEntity user, LivingEntity target, Hand hand, ItemStack stack, ItemStackConsumer resultStackConsumer) {
        return ItemResult.PASS;
    }

    default boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker, ItemStackConsumer resultStackConsumer) {
        return false;
    }

    default boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner, ItemStackConsumer resultStackConsumer) {
        return false;
    }

    default void using(ItemStack stack, World world, LivingEntity user, int usedTicks, int remainingUseTicks) {}

    default boolean stopUsing(ItemStack stack, World world, LivingEntity user, int usedTicks, int remainingUseTicks, ItemStackConsumer resultStackConsumer) {
        return false;
    }

    default void finishUsing(World world, LivingEntity user, ItemStack stack, int usedTicks, ItemStackConsumer resultStackConsumer) {}

    default void inventoryTick(ItemStack stack, World world, Entity holder, int slot, boolean selected) {}

    default boolean clickOnSlot(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity user) {
        return false;
    }

    default boolean clickedOnWithStack(ItemStack stack, ItemStack cursorStack, Slot slot, ClickType clickType, PlayerEntity user, ItemStackConsumer resultStackConsumer) {
        return false;
    }

    default void onCraft(ItemStack stack, World world) {}

    default void addComponents(ComponentMap.Builder builder) {}

    default void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {}
}
