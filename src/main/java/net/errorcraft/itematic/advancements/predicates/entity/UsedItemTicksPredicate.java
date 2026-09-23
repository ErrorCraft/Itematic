package net.errorcraft.itematic.advancements.predicates.entity;

import com.mojang.serialization.Codec;
import net.minecraft.advancements.predicates.MinMaxBounds;
import net.minecraft.advancements.predicates.entity.EntitySubPredicate;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public record UsedItemTicksPredicate(MinMaxBounds.Ints ticks) implements EntitySubPredicate {
    public static final Codec<UsedItemTicksPredicate> CODEC = MinMaxBounds.Ints.CODEC.xmap(
        UsedItemTicksPredicate::new,
        UsedItemTicksPredicate::ticks
    );

    @Override
    public boolean matches(Entity entity, ServerLevel level, @Nullable Vec3 position) {
        return entity instanceof LivingEntity livingEntity
            && this.ticks.matches(livingEntity.itematic$usedItemTicks());
    }
}
