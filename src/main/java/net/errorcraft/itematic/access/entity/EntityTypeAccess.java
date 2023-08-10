package net.errorcraft.itematic.access.entity;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.entity.initializer.EntityInitializer;
import net.minecraft.util.math.Direction;

public interface EntityTypeAccess {
    default Codec<? extends EntityInitializer<?>> initializerCodec() {
        return null;
    }
    default void setInitializerCodec(Codec<? extends EntityInitializer<?>> initializerCodec) {}
    default void setInitializer(EntityInitializer<?> initializer, Direction initializerSide) {}
}
