package net.errorcraft.itematic.access.item;

import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.event.ItemEvent;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;

public interface ItemStackAccess {
    default RegistryKey<Item> key() {
        return null;
    }
    default Optional<NbtCompound> nbt() {
        return Optional.empty();
    }
    default boolean isOf(RegistryKey<Item> key) {
        return false;
    }
    default void damage(int amount, LivingEntity entity) {}
    default void damage(int amount, LivingEntity entity, Hand hand) {}
    default <T extends ItemComponent> boolean hasComponent(ItemComponentType<T> type) {
        return false;
    }
    default <T extends ItemComponent> Optional<T> getComponent(ItemComponentType<T> type) {
        return Optional.empty();
    }
    default void invokeEvent(ItemEvent event, ActionContext context) {}
    default boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
        return false;
    }
    default boolean isNetworkSynced() {
        return false;
    }
    default boolean mayStartUsing(World world, PlayerEntity user, Hand hand, ItemStack stack) {
        return false;
    }
}
