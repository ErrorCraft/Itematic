package net.errorcraft.itematic.entity;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@FunctionalInterface
public interface EntitySpawnCallback {
    void accept(Entity entity, ItemStack stack);

    static EntitySpawnCallback combine(EntitySpawnCallback first, @Nullable EntitySpawnCallback second) {
        Objects.requireNonNull(first);
        if (second == null) {
            return first;
        }

        return (entity, stack) -> {
            first.accept(entity, stack);
            second.accept(entity, stack);
        };
    }
}
