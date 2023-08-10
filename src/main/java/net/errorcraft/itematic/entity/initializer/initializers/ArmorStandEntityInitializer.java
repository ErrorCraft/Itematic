package net.errorcraft.itematic.entity.initializer.initializers;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.entity.initializer.EntityInitializer;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public record ArmorStandEntityInitializer() implements EntityInitializer<ArmorStandEntity> {
    public static final ArmorStandEntityInitializer INSTANCE = new ArmorStandEntityInitializer();
    public static final Codec<ArmorStandEntityInitializer> CODEC = Codec.unit(INSTANCE);

    @Override
    public EntityType<?> type() {
        return EntityType.ARMOR_STAND;
    }

    @Override
    public ArmorStandEntity create(ActionContext context) {
        if (!mayCreate(context)) {
            return null;
        }
        Vec3d position = context.position();
        ServerWorld world = context.world();
        ArmorStandEntity entity = new ArmorStandEntity(world, position.getX(), position.getY(), position.getZ());
        float angle = getRoundedAngle(context.target().map(Entity::getYaw).orElse(0.0f));
        entity.refreshPositionAndAngles(entity.getX(), entity.getY(), entity.getZ(), angle, 0.0f);
        world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_ARMOR_STAND_PLACE, SoundCategory.BLOCKS, 0.75f, 0.8f);
        return entity;
    }

    private static boolean mayCreate(ActionContext context) {
        if (context.target().isEmpty()) {
            return true;
        }
        ServerWorld world = context.world();
        if (context.side() == Direction.DOWN) {
            return false;
        }
        Vec3d position = context.position();
        Box box = EntityType.ARMOR_STAND.getDimensions().getBoxAt(position.getX(), position.getY(), position.getZ());
        return world.isSpaceEmpty(null, box) && world.getOtherEntities(null, box).isEmpty();
    }

    private static float getRoundedAngle(float angle) {
        return MathHelper.floor((MathHelper.wrapDegrees(angle - 180.0f) + 22.5f) / 45.0f) * 45.0f;
    }
}
