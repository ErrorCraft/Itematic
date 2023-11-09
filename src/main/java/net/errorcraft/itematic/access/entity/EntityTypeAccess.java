package net.errorcraft.itematic.access.entity;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.entity.initializer.EntityInitializer;
import net.errorcraft.itematic.world.action.context.ActionContext;

public interface EntityTypeAccess {
    default Codec<? extends EntityInitializer<?>> itematic$initializerCodec() {
        return null;
    }
    default void itematic$setInitializerCodec(Codec<? extends EntityInitializer<?>> initializerCodec) {}
    default void itematic$setInitializer(EntityInitializer<?> initializer, ActionContext actionContext) {}
}
