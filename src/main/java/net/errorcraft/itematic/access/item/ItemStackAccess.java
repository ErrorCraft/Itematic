package net.errorcraft.itematic.access.item;

import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.function.Predicate;

public interface ItemStackAccess {
    default RegistryKey<Item> getKey() {
        return null;
    }
    default Optional<NbtCompound> getOptionalNbt() { return Optional.empty(); }
    default boolean isOf(RegistryKey<Item> key) {
        return false;
    }
    default boolean itemKeyMatches(Predicate<RegistryKey<Item>> predicate) {
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
    default boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
        return false;
    }
}
