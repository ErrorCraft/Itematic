package net.errorcraft.itematic.entity.initializer.initializers;

import net.errorcraft.itematic.entity.initializer.EntityInitializer;
import net.errorcraft.itematic.util.context.ItematicContextParameters;
import net.errorcraft.itematic.world.action.context.NewActionContext;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EyeOfEnderEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.registry.tag.StructureTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.event.GameEvent;

public class EyeOfEnderEntityInitializer implements EntityInitializer<EyeOfEnderEntity> {
    public static final EyeOfEnderEntityInitializer INSTANCE = new EyeOfEnderEntityInitializer();

    private EyeOfEnderEntityInitializer() {}

    @Override
    public EyeOfEnderEntity create(NewActionContext context, SpawnReason reason) {
        ServerWorld world = context.world();
        BlockPos blockPos = this.getBlockPos(context);
        if (blockPos == null) {
            return null;
        }

        BlockPos strongholdPos = world.locateStructure(
            StructureTags.EYE_OF_ENDER_LOCATED,
            blockPos,
            100,
            false
        );
        if (strongholdPos == null) {
            return null;
        }

        Vec3d pos = this.getPosition(context);
        EyeOfEnderEntity entity = this.createEntity(world, pos, context.get(LootContextParameters.TOOL), strongholdPos);
        Entity user = context.get(LootContextParameters.THIS_ENTITY);
        world.emitGameEvent(GameEvent.PROJECTILE_SHOOT, pos, GameEvent.Emitter.of(user));
        if (user instanceof ServerPlayerEntity serverPlayer) {
            Criteria.USED_ENDER_EYE.trigger(serverPlayer, strongholdPos);
        }

        return entity;
    }

    private Vec3d getPosition(NewActionContext context) {
        Entity entity = context.get(LootContextParameters.THIS_ENTITY);
        if (entity != null) {
            return new Vec3d(entity.getX(), entity.getBodyY(0.5d), entity.getZ());
        }

        return context.get(ItematicContextParameters.INTERACTED_POSITION);
    }

    private BlockPos getBlockPos(NewActionContext context) {
        Entity entity = context.get(LootContextParameters.THIS_ENTITY);
        if (entity != null) {
            return entity.getBlockPos();
        }

        return context.getBlockPos(ItematicContextParameters.INTERACTED_POSITION);
    }

    private EyeOfEnderEntity createEntity(ServerWorld world, Vec3d pos, ItemStack stack, BlockPos strongholdPos) {
        EyeOfEnderEntity entity = new EyeOfEnderEntity(world, pos.getX(), pos.getY(), pos.getZ());
        if (stack != null) {
            entity.setItem(stack);
        }

        entity.initTargetPos(strongholdPos);
        return entity;
    }
}
