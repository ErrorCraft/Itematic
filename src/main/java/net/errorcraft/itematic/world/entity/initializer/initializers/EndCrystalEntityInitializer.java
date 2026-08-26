package net.errorcraft.itematic.world.entity.initializer.initializers;

import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.entity.initializer.EntityInitializer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.end.EnderDragonFight;
import org.jspecify.annotations.Nullable;

public class EndCrystalEntityInitializer implements EntityInitializer<EndCrystal> {
    public static final EndCrystalEntityInitializer INSTANCE = new EndCrystalEntityInitializer();

    private EndCrystalEntityInitializer() {}

    @Override
    public @Nullable EndCrystal create(ActionContext context, EntitySpawnReason reason) {
        Level level = context.level();
        EndCrystal entity = EntityType.END_CRYSTAL.create(level, reason);
        if (entity == null) {
            return null;
        }

        entity.setShowBottom(false);
        if (context.level() instanceof ServerLevel serverLevel) {
            this.tryRespawnEnderDragon(serverLevel);
        }

        return entity;
    }

    private void tryRespawnEnderDragon(ServerLevel level) {
        EnderDragonFight enderDragonFight = level.getDragonFight();
        if (enderDragonFight != null) {
            enderDragonFight.tryRespawn();
        }
    }
}
