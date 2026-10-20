package net.errorcraft.itematic.world.item.component;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.util.context.ItematicContextKeys;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.errorcraft.itematic.world.entity.spawn.EntitySpawner;
import net.errorcraft.itematic.world.item.ItemStacks;
import net.minecraft.core.Direction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public record ProjectileComponent(EntitySpawner spawner) {
    public static final Codec<ProjectileComponent> CODEC = EntitySpawner.CODEC.xmap(
        ProjectileComponent::new,
        ProjectileComponent::spawner
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, ProjectileComponent> STREAM_CODEC = EntitySpawner.STREAM_CODEC.map(
        ProjectileComponent::new,
        ProjectileComponent::spawner
    );

    @Nullable
    public static Entity spawn(ItemStack stack, Level level, LivingEntity owner, float angleOffset, float speed) {
        ProjectileComponent projectile = stack.get(ItematicDataComponents.PROJECTILE);
        if (projectile == null) {
            return null;
        }

        return projectile.spawnEntity(level, owner, stack, angleOffset, speed);
    }

    @Nullable
    public static Entity spawn(ActionContext context, PositionTarget position, float angleOffset, float speed, float uncertainty) {
        ItemInstance item = context.get(LootContextParams.TOOL);
        if (item == null) {
            return null;
        }

        ProjectileComponent projectile = item.get(ItematicDataComponents.PROJECTILE);
        if (projectile == null) {
            return null;
        }

        return projectile.spawnEntity(context, position, angleOffset, speed, uncertainty);
    }

    @Nullable
    private Entity spawnEntity(Level level, LivingEntity user, ItemStack stack, float angleOffset, float speed) {
        if (level.isClientSide()) {
            return null;
        }

        ActionContext context = ActionContext.builder(level)
            .stackExchanger(user, stack)
            .add(LootContextParams.TOOL, stack)
            .add(LootContextParams.THIS_ENTITY, user)
            .add(LootContextParams.ORIGIN, user.position())
            .add(ItematicContextKeys.INTERACTED_POSITION, user.getEyePosition().add(0.0d, -0.1d, 0.0d))
            .build();
        return this.spawnEntity(context, PositionTarget.INTERACTED, angleOffset, speed, 1.0f);
    }

    @Nullable
    private Entity spawnEntity(ActionContext context, PositionTarget position, float angleOffset, float speed, float uncertainty) {
        Vec3 pos = context.get(position.contextParam());
        if (pos == null) {
            return null;
        }

        return this.spawner.spawn(
            context,
            pos,
            null,
            EntitySpawnReason.SPAWN_ITEM_USE,
            (projectile, stack) -> {
                if (projectile instanceof ThrowableItemProjectile thrownItemEntity) {
                    thrownItemEntity.setItem(stack);
                }

                if (projectile instanceof Projectile projectileEntity) {
                    this.initializeProjectile(context, projectileEntity, angleOffset, speed, uncertainty);
                }
            },
            false
        );
    }

    private void initializeProjectile(ActionContext context, Projectile projectile, float angleOffset, float speed, float uncertainty) {
        Entity user = context.get(LootContextParams.THIS_ENTITY);
        if (user != null) {
            initializeProjectile(projectile, user, angleOffset, speed, uncertainty);
        } else {
            initializeProjectile(projectile, context.getOrDefault(ItematicContextKeys.SIDE, Direction.UP), speed, uncertainty);
        }

        if (context.level() instanceof ServerLevel serverLevel) {
            projectile.applyOnProjectileSpawned(
                serverLevel,
                context.getOrDefault(LootContextParams.TOOL, ItemStacks::fromItemInstance, ItemStack.EMPTY)
            );
        }
    }

    private static void initializeProjectile(Projectile entity, Entity user, float angleOffset, float speed, float uncertainty) {
        entity.setOwner(user);
        if (entity instanceof AbstractArrow persistentProjectileEntity && user instanceof Player player && player.hasInfiniteMaterials()) {
            persistentProjectileEntity.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
        }

        entity.shootFromRotation(user, user.getXRot(), user.getYRot(), angleOffset, speed, uncertainty);
    }

    private static void initializeProjectile(Projectile entity, Direction side, float speed, float uncertainty) {
        entity.shoot(side.getStepX(), side.getStepY(), side.getStepZ(), speed, uncertainty);
    }
}
