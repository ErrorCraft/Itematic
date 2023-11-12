package net.errorcraft.itematic.entity.initializer.initializers;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.entity.initializer.EntityInitializer;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.util.math.Vec3d;

public record FireworkRocketEntityInitializer() implements EntityInitializer<FireworkRocketEntity> {
    public static final FireworkRocketEntityInitializer INSTANCE = new FireworkRocketEntityInitializer();
    public static final Codec<FireworkRocketEntityInitializer> CODEC = Codec.unit(INSTANCE);

    @Override
    public EntityType<?> type() {
        return EntityType.FIREWORK_ROCKET;
    }

    @Override
    public FireworkRocketEntity create(ActionContext context) {
        Vec3d pos = context.position(ActionContextParameter.TARGET);
        return new FireworkRocketEntity(context.world(), pos.getX(), pos.getY(), pos.getZ(), context.stack().copyWithCount(1));
    }
}
