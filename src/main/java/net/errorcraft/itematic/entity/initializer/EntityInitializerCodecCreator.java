package net.errorcraft.itematic.entity.initializer;

import com.mojang.serialization.Codec;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;

@FunctionalInterface
public interface EntityInitializerCodecCreator<T extends Entity> {
    Codec<EntityInitializer<T>> create(EntityType<T> type);
}
