package net.errorcraft.itematic.entity.initializer;

import com.mojang.serialization.MapCodec;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;

@FunctionalInterface
public interface EntityInitializerCodecCreator<T extends Entity> {
    MapCodec<? extends EntityInitializer<T>> create(EntityType<T> type);
}
