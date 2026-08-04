package net.errorcraft.itematic.entity.initializer.initializers;

import net.errorcraft.itematic.entity.initializer.EntityInitializer;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.end.EndDragonFight;

public class EndCrystalEntityInitializer implements EntityInitializer<EndCrystal> {
    public static final EndCrystalEntityInitializer INSTANCE = new EndCrystalEntityInitializer();

    private EndCrystalEntityInitializer() {}

    @Override
    public EndCrystal create(ActionContext context, EntitySpawnReason reason) {
        Level world = context.world();
        EndCrystal entity = EntityType.END_CRYSTAL.create(world, reason);
        if (entity == null) {
            return null;
        }

        entity.setShowBottom(false);
        if (context.world() instanceof ServerLevel serverWorld) {
            this.tryRespawnEnderDragon(serverWorld);
        }

        return entity;
    }

    private void tryRespawnEnderDragon(ServerLevel world) {
        EndDragonFight enderDragonFight = world.getDragonFight();
        if (enderDragonFight != null) {
            enderDragonFight.tryRespawn();
        }
    }
}
