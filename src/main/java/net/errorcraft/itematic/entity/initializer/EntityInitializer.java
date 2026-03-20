package net.errorcraft.itematic.entity.initializer;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.world.action.context.NewActionContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.registry.Registries;

public interface EntityInitializer<T extends Entity> {
    Codec<EntityInitializer<?>> CODEC = Registries.ENTITY_TYPE.getCodec().dispatch(EntityInitializer::type, EntityType::itematic$initializerCodec);
    EntityType<?> type();
    T create(NewActionContext context, SpawnReason reason);
}
