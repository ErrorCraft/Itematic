package net.errorcraft.itematic.entity.initializer.initializers;

import net.errorcraft.itematic.entity.initializer.EntityInitializer;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.boss.dragon.EnderDragonFight;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

public class EndCrystalEntityInitializer implements EntityInitializer<EndCrystalEntity> {
    public static final EndCrystalEntityInitializer INSTANCE = new EndCrystalEntityInitializer();

    private EndCrystalEntityInitializer() {}

    @Override
    public EndCrystalEntity create(ActionContext context, SpawnReason reason) {
        World world = context.world();
        EndCrystalEntity entity = EntityType.END_CRYSTAL.create(world, reason);
        if (entity == null) {
            return null;
        }

        entity.setShowBottom(false);
        if (context.world() instanceof ServerWorld serverWorld) {
            this.tryRespawnEnderDragon(serverWorld);
        }

        return entity;
    }

    private void tryRespawnEnderDragon(ServerWorld world) {
        EnderDragonFight enderDragonFight = world.getEnderDragonFight();
        if (enderDragonFight != null) {
            enderDragonFight.respawnDragon();
        }
    }
}
