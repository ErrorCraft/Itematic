package net.errorcraft.itematic.entity.initializer.initializers;

import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.entity.initializer.EntityInitializer;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;

public record SimpleEntityInitializer<T extends Entity>(EntityType<T> type) implements EntityInitializer<T> {
    public static <U extends Entity> SimpleEntityInitializer<U> of(EntityType<U> type) {
        return new SimpleEntityInitializer<>(type);
    }

    @Override
    public T create(ActionContext context) {
        return this.type.create(context.world());
    }

    public static <U extends Entity> MapCodec<EntityInitializer<U>> createCodec(EntityType<U> type) {
        return MapCodec.unit(of(type));
    }
}
