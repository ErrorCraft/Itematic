package net.errorcraft.itematic.world.entity.initializer.initializers;

import net.errorcraft.itematic.util.context.ItematicContextParameters;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.entity.initializer.EntityInitializer;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public record ArrowEntityInitializer<T extends AbstractArrow>(OwnerCreator<T> ownerCreator, SimpleCreator<T> simpleCreator) implements EntityInitializer<T> {
    public static <T extends AbstractArrow> EntityInitializer<T> of(OwnerCreator<T> ownerCreator, SimpleCreator<T> simpleCreator) {
        return new ArrowEntityInitializer<>(ownerCreator, simpleCreator);
    }

    @Override
    public T create(ActionContext context, EntitySpawnReason reason) {
        if (context.get(LootContextParams.THIS_ENTITY) instanceof LivingEntity entity) {
            ItemStack shooter = entity.getUseItem();
            if (shooter.isEmpty()) {
                shooter = null;
            }

            return this.ownerCreator.create(
                context.world(),
                entity,
                context.getOrDefault(LootContextParams.TOOL, ItemStack.EMPTY).copyWithCount(1),
                shooter
            );
        }

        Vec3 pos = context.get(ItematicContextParameters.INTERACTED_POSITION);
        if (pos == null) {
            return null;
        }

        T entity = this.simpleCreator.create(
            context.world(),
            pos.x(),
            pos.y(),
            pos.z(),
            context.getOrDefault(LootContextParams.TOOL, ItemStack.EMPTY).copyWithCount(1),
            null
        );
        entity.pickup = AbstractArrow.Pickup.ALLOWED;
        return entity;
    }

    @FunctionalInterface
    public interface OwnerCreator<T extends AbstractArrow> {
        T create(Level level, LivingEntity owner, ItemStack ammunition, @Nullable ItemStack weapon);
    }

    @FunctionalInterface
    public interface SimpleCreator<T extends AbstractArrow> {
        T create(Level level, double x, double y, double z, ItemStack ammunition, @Nullable ItemStack weapon);
    }
}
