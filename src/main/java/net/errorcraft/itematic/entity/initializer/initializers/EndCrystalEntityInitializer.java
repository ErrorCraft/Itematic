package net.errorcraft.itematic.entity.initializer.initializers;

import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.block.ItematicBlockTags;
import net.errorcraft.itematic.entity.initializer.EntityInitializer;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.boss.dragon.EnderDragonFight;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import java.util.List;

public record EndCrystalEntityInitializer() implements EntityInitializer<EndCrystalEntity> {
    public static final EndCrystalEntityInitializer INSTANCE = new EndCrystalEntityInitializer();
    public static final MapCodec<EndCrystalEntityInitializer> CODEC = MapCodec.unit(INSTANCE);
    private static final double HORIZONTAL_SEARCH_DISTANCE = 1.0d;
    private static final double VERTICAL_SEARCH_DISTANCE = 2.0d;

    @Override
    public EntityType<?> type() {
        return EntityType.END_CRYSTAL;
    }

    @Override
    public EndCrystalEntity create(ActionContext context) {
        ServerWorld world = context.world();
        BlockPos pos = context.blockPos(ActionContextParameter.TARGET);
        if (!world.getBlockState(pos.down()).isIn(ItematicBlockTags.END_CRYSTAL_SPAWNABLE_ON)) {
            return null;
        }
        return this.trySpawn(world, pos);
    }

    private EndCrystalEntity trySpawn(ServerWorld world, BlockPos pos) {
        if (!world.isAir(pos)) {
            return null;
        }
        double x = pos.getX();
        double y = pos.getY();
        double z = pos.getZ();
        List<Entity> v = world.getOtherEntities(null, new Box(x, y, z, x + HORIZONTAL_SEARCH_DISTANCE, y + VERTICAL_SEARCH_DISTANCE, z + HORIZONTAL_SEARCH_DISTANCE));
        if (!v.isEmpty()) {
            return null;
        }
        EndCrystalEntity endCrystalEntity = new EndCrystalEntity(world, x + 0.5d, y, z + 0.5d);
        endCrystalEntity.setShowBottom(false);
        EnderDragonFight enderDragonFight = world.getEnderDragonFight();
        if (enderDragonFight != null) {
            enderDragonFight.respawnDragon();
        }
        return endCrystalEntity;
    }
}
