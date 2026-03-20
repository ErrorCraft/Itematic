package net.errorcraft.itematic.entity.initializer.initializers;

import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.entity.initializer.EntityInitializer;
import net.errorcraft.itematic.world.action.context.NewActionContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;

public record SimpleEntityInitializer<T extends Entity>(EntityType<T> type) implements EntityInitializer<T> {
    public static <U extends Entity> SimpleEntityInitializer<U> of(EntityType<U> type) {
        return new SimpleEntityInitializer<>(type);
    }

    @Override
    public T create(NewActionContext context, SpawnReason reason) {
        return this.type.create(context.world(), reason);
    }

    public static <U extends Entity> MapCodec<EntityInitializer<U>> createCodec(EntityType<U> type) {
        return MapCodec.unit(of(type));
    }
}
