package net.errorcraft.itematic.access.entity;

import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.entity.initializer.EntityInitializer;
import net.errorcraft.itematic.entity.initializer.EntityInitializerCodecCreator;
import net.minecraft.entity.Entity;

public interface EntityTypeBuilderAccess<T extends Entity> {
    default void itematic$initializerCodec(MapCodec<? extends EntityInitializer<T>> codec) {}
    default void itematic$initializerCodec(EntityInitializerCodecCreator<T> creator) {}
}
