package errorcraft.itematic.access.item;

import errorcraft.itematic.item.component.ItemComponent;
import errorcraft.itematic.item.component.ItemComponentType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;

import java.util.Optional;

public interface ItemStackAccess {
    default Optional<NbtCompound> getOptionalNbt() { return Optional.empty(); }
    default boolean isOf(RegistryKey<Item> key) {
        return false;
    }
    default void damage(int amount, LivingEntity entity) {}
    default <T extends ItemComponent> boolean hasComponent(ItemComponentType<T> type) {
        return false;
    }
    default <T extends ItemComponent> Optional<T> getComponent(ItemComponentType<T> type) {
        return Optional.empty();
    }
}
