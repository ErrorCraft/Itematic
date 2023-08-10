package net.errorcraft.itematic.entity.initializer.initializers;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.entity.initializer.EntityInitializer;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;

public record SimpleEntityInitializer<T extends Entity>(EntityType<T> type) implements EntityInitializer<T> {
    @Override
    public T create(ActionContext context) {
        return this.type.create(context.world());
    }

    public static <U extends Entity> Codec<EntityInitializer<U>> createCodec(EntityType<U> type) {
        return Codec.unit(new SimpleEntityInitializer<>(type));
    }
}
