package net.errorcraft.itematic.entity.initializer.initializers;

import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.entity.initializer.EntityInitializer;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public record PersistentProjectileEntityInitializer<T extends PersistentProjectileEntity>(EntityType<T> type, OwnerCreator<T> ownerCreator, SimpleCreator<T> simpleCreator) implements EntityInitializer<T> {
    @Override
    public T create(ActionContext context) {
        if (context.entity(ActionContextParameter.THIS).orElse(null) instanceof LivingEntity entity) {
            return this.ownerCreator.create(context.world(), entity, context.stack().copyWithCount(1));
        }
        Vec3d pos = context.position(ActionContextParameter.TARGET);
        T entity = this.simpleCreator.create(context.world(), pos.getX(), pos.getY(), pos.getZ(), context.stack().copyWithCount(1));
        entity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
        return entity;
    }

    public static <U extends PersistentProjectileEntity> MapCodec<EntityInitializer<U>> createCodec(EntityType<U> type, OwnerCreator<U> ownerCreator, SimpleCreator<U> simpleCreator) {
        return MapCodec.unit(new PersistentProjectileEntityInitializer<>(type, ownerCreator, simpleCreator));
    }

    @FunctionalInterface
    public interface OwnerCreator<T extends PersistentProjectileEntity> {
        T create(World world, LivingEntity owner, ItemStack stack);
    }

    @FunctionalInterface
    public interface SimpleCreator<T extends PersistentProjectileEntity> {
        T create(World world, double x, double y, double z, ItemStack stack);
    }
}
