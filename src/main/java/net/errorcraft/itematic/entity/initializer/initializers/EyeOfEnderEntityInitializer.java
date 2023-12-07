package net.errorcraft.itematic.entity.initializer.initializers;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.entity.initializer.EntityInitializer;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EyeOfEnderEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.StructureTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.event.GameEvent;

public record EyeOfEnderEntityInitializer() implements EntityInitializer<EyeOfEnderEntity> {
    public static final EyeOfEnderEntityInitializer INSTANCE = new EyeOfEnderEntityInitializer();
    public static final Codec<EyeOfEnderEntityInitializer> CODEC = Codec.unit(INSTANCE);

    @Override
    public EntityType<?> type() {
        return EntityType.EYE_OF_ENDER;
    }

    @Override
    public EyeOfEnderEntity create(ActionContext context) {
        ServerWorld world = context.world();
        BlockPos blockPos = this.getBlockPos(context);
        BlockPos strongholdPos = world.locateStructure(StructureTags.EYE_OF_ENDER_LOCATED, blockPos, 100, false);
        if (strongholdPos == null) {
            return null;
        }

        Vec3d pos = this.getPosition(context);
        EyeOfEnderEntity entity = this.createEntity(world, pos, context.stack(), strongholdPos);
        Entity user = context.entity(ActionContextParameter.THIS).orElse(null);
        world.emitGameEvent(GameEvent.PROJECTILE_SHOOT, pos, GameEvent.Emitter.of(user));
        if (user instanceof ServerPlayerEntity serverPlayer) {
            Criteria.USED_ENDER_EYE.trigger(serverPlayer, strongholdPos);
        }

        world.itematic$playSound(null, pos, SoundEvents.ENTITY_ENDER_EYE_LAUNCH, SoundCategory.NEUTRAL, 0.5f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
        world.syncWorldEvent(null, WorldEvents.EYE_OF_ENDER_LAUNCHES, blockPos, 0);
        return entity;
    }

    private Vec3d getPosition(ActionContext context) {
        return context.entity(ActionContextParameter.THIS)
            .map(target -> new Vec3d(target.getX(), target.getBodyY(0.5d), target.getZ()))
            .orElseGet(() -> context.position(ActionContextParameter.THIS));
    }

    private BlockPos getBlockPos(ActionContext context) {
        return context.entity(ActionContextParameter.THIS)
            .map(Entity::getBlockPos)
            .orElseGet(() -> context.blockPos(ActionContextParameter.THIS));
    }

    private EyeOfEnderEntity createEntity(ServerWorld world, Vec3d pos, ItemStack stack, BlockPos strongholdPos) {
        EyeOfEnderEntity entity = new EyeOfEnderEntity(world, pos.getX(), pos.getY(), pos.getZ());
        entity.setItem(stack);
        entity.initTargetPos(strongholdPos);
        return entity;
    }
}
