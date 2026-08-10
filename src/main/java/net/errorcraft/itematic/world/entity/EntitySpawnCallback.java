package net.errorcraft.itematic.world.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

@FunctionalInterface
public interface EntitySpawnCallback {
    void accept(Entity entity, ItemStack stack);

    static EntitySpawnCallback combine(EntitySpawnCallback first, @Nullable EntitySpawnCallback second) {
        if (second == null) {
            return first;
        }

        return (entity, stack) -> {
            first.accept(entity, stack);
            second.accept(entity, stack);
        };
    }
}
