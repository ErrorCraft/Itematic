package net.errorcraft.itematic.entity.initializer.initializers;

import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.entity.initializer.EntityInitializer;
import net.errorcraft.itematic.util.context.ItematicContextParameters;
import net.errorcraft.itematic.world.action.context.NewActionContext;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public record PersistentProjectileEntityInitializer<T extends PersistentProjectileEntity>(EntityType<T> type, OwnerCreator<T> ownerCreator, SimpleCreator<T> simpleCreator) implements EntityInitializer<T> {
    @Override
    public T create(NewActionContext context, SpawnReason reason) {
        if (context.get(LootContextParameters.THIS_ENTITY) instanceof LivingEntity entity) {
            ItemStack shooter = entity.getActiveItem();
            if (shooter.isEmpty()) {
                shooter = null;
            }

            return this.ownerCreator.create(
                context.world(),
                entity,
                context.getOrDefault(LootContextParameters.TOOL, ItemStack.EMPTY).copyWithCount(1),
                shooter
            );
        }

        Vec3d pos = context.get(ItematicContextParameters.INTERACTED_POSITION);
        if (pos == null) {
            return null;
        }

        T entity = this.simpleCreator.create(
            context.world(),
            pos.getX(),
            pos.getY(),
            pos.getZ(),
            context.getOrDefault(LootContextParameters.TOOL, ItemStack.EMPTY).copyWithCount(1),
            null
        );
        entity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
        return entity;
    }

    public static <U extends PersistentProjectileEntity> MapCodec<EntityInitializer<U>> createCodec(EntityType<U> type, OwnerCreator<U> ownerCreator, SimpleCreator<U> simpleCreator) {
        return MapCodec.unit(new PersistentProjectileEntityInitializer<>(type, ownerCreator, simpleCreator));
    }

    @FunctionalInterface
    public interface OwnerCreator<T extends PersistentProjectileEntity> {
        T create(World world, LivingEntity owner, ItemStack ammunition, @Nullable ItemStack weapon);
    }

    @FunctionalInterface
    public interface SimpleCreator<T extends PersistentProjectileEntity> {
        T create(World world, double x, double y, double z, ItemStack ammunition, @Nullable ItemStack weapon);
    }
}
