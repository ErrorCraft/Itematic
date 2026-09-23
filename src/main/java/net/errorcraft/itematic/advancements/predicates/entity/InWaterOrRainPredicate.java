package net.errorcraft.itematic.advancements.predicates.entity;

import com.mojang.serialization.Codec;
import net.minecraft.advancements.predicates.entity.EntitySubPredicate;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public record InWaterOrRainPredicate(boolean inWaterOrRain) implements EntitySubPredicate {
    public static final Codec<InWaterOrRainPredicate> CODEC = Codec.BOOL.xmap(
        InWaterOrRainPredicate::new,
        InWaterOrRainPredicate::inWaterOrRain
    );

    @Override
    public boolean matches(Entity entity, ServerLevel level, @Nullable Vec3 position) {
        return entity.isInWaterOrRain() == this.inWaterOrRain;
    }
}
