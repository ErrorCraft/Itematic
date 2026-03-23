package net.errorcraft.itematic.entity.initializer.initializers;

import net.errorcraft.itematic.entity.initializer.EntityInitializer;
import net.errorcraft.itematic.util.context.ItematicContextParameters;
import net.errorcraft.itematic.world.action.context.NewActionContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class ArmorStandEntityInitializer implements EntityInitializer<ArmorStandEntity> {
    public static final ArmorStandEntityInitializer INSTANCE = new ArmorStandEntityInitializer();

    private ArmorStandEntityInitializer() {}

    @Override
    public ArmorStandEntity create(NewActionContext context, SpawnReason reason) {
        Vec3d position = context.get(ItematicContextParameters.INTERACTED_POSITION);
        if (position == null) {
            return null;
        }

        if (!mayCreate(context, position)) {
            return null;
        }

        ServerWorld world = context.world();
        ArmorStandEntity entity = new ArmorStandEntity(world, position.getX(), position.getY(), position.getZ());
        float angle = getRoundedAngle(context);
        entity.refreshPositionAndAngles(entity.getX(), entity.getY(), entity.getZ(), angle, 0.0f);
        world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_ARMOR_STAND_PLACE, SoundCategory.BLOCKS, 0.75f, 0.8f);
        return entity;
    }

    private static boolean mayCreate(NewActionContext context, Vec3d position) {
        if (!context.has(LootContextParameters.THIS_ENTITY)) {
            return true;
        }

        ServerWorld world = context.world();
        if (context.get(ItematicContextParameters.SIDE) == Direction.DOWN) {
            return false;
        }

        Box box = EntityType.ARMOR_STAND.getDimensions().getBoxAt(position.getX(), position.getY(), position.getZ());
        return world.isSpaceEmpty(null, box) && world.getOtherEntities(null, box).isEmpty();
    }

    private static float getRoundedAngle(NewActionContext context) {
        Entity entity = context.get(LootContextParameters.THIS_ENTITY);
        float angle = entity != null ? entity.getYaw() : 0.0f;
        return MathHelper.floor((MathHelper.wrapDegrees(angle - 180.0f) + 22.5f) / 45.0f) * 45.0f;
    }
}
