package net.errorcraft.itematic.entity.initializer.initializers;

import net.errorcraft.itematic.entity.initializer.EntityInitializer;
import net.errorcraft.itematic.util.context.ItematicContextParameters;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public record PersistentProjectileEntityInitializer<T extends PersistentProjectileEntity>(OwnerCreator<T> ownerCreator, SimpleCreator<T> simpleCreator) implements EntityInitializer<T> {
    public static <T extends PersistentProjectileEntity> EntityInitializer<T> of(OwnerCreator<T> ownerCreator, SimpleCreator<T> simpleCreator) {
        return new PersistentProjectileEntityInitializer<>(ownerCreator, simpleCreator);
    }

    @Override
    public T create(ActionContext context, SpawnReason reason) {
        LivingEntity user = context.get(LootContextParameters.THIS_ENTITY, LivingEntity.class);
        ItemStack usedStack = context.getOrDefault(LootContextParameters.TOOL, ItemStack.EMPTY);
        usedStack.itematic$damage(1, context);
        if (user != null) {
            ItemStack shooter = user.getActiveItem();
            if (shooter.isEmpty() || shooter == usedStack) {
                shooter = null;
            }

            return this.spawnFromUser(
                context.world(),
                user,
                usedStack.splitUnlessCreative(1, user),
                shooter
            );
        }

        Vec3d pos = context.get(ItematicContextParameters.INTERACTED_POSITION);
        if (pos == null) {
            return null;
        }

        T entity = this.spawnFromPos(
            context.world(),
            pos,
            usedStack.split(1)
        );
        entity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
        return entity;
    }

    private T spawnFromUser(World world, LivingEntity user, ItemStack ammunition, @Nullable ItemStack weapon) {
        return this.ownerCreator.create(
            world,
            user,
            ammunition,
            weapon
        );
    }

    private T spawnFromPos(World world, Vec3d pos, ItemStack ammunition) {
        return this.simpleCreator.create(
            world,
            pos.getX(),
            pos.getY(),
            pos.getZ(),
            ammunition,
            null
        );
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
