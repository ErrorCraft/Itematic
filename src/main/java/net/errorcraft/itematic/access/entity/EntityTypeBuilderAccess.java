package net.errorcraft.itematic.access.entity;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.entity.initializer.EntityInitializer;
import net.errorcraft.itematic.entity.initializer.EntityInitializerCodecCreator;
import net.minecraft.entity.Entity;

public interface EntityTypeBuilderAccess<T extends Entity> {
    default void initializerCodec(Codec<EntityInitializer<T>> codec) {}
    default void initializerCodec(EntityInitializerCodecCreator<T> creator) {}
}
