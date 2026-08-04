package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.entity.spawn.EntitySpawner;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.util.context.ItematicContextParameters;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

public record ProjectileItemComponent(EntitySpawner entity) implements ItemComponent<ProjectileItemComponent> {
    public static final Codec<ProjectileItemComponent> CODEC = EntitySpawner.CODEC.xmap(
        ProjectileItemComponent::new,
        ProjectileItemComponent::entity
    );

    public static ProjectileItemComponent of(Holder<EntityType<?>> entity) {
        return new ProjectileItemComponent(EntitySpawner.of(entity));
    }

    public static ProjectileItemComponent of(Holder<EntityType<?>> entity, DataComponentPatch components) {
        return new ProjectileItemComponent(EntitySpawner.builder(entity).components(components).build());
    }

    @Override
    public ItemComponentType<ProjectileItemComponent> type() {
        return ItemComponentTypes.PROJECTILE;
    }

    @Override
    public Codec<ProjectileItemComponent> codec() {
        return CODEC;
    }

    public Entity spawnEntity(Level world, LivingEntity user, ItemStack stack, float angleOffset, float speed) {
        if (world.isClientSide()) {
            return null;
        }

        ActionContext context = ActionContext.builder(world)
            .stackExchanger(user, stack)
            .add(LootContextParams.TOOL, stack)
            .add(LootContextParams.THIS_ENTITY, user)
            .add(LootContextParams.ORIGIN, user.position())
            .add(ItematicContextParameters.INTERACTED_POSITION, user.getEyePosition().add(0.0d, -0.1d, 0.0d))
            .build();
        return this.spawnEntity(context, PositionTarget.INTERACTED, angleOffset, speed, 1.0f);
    }

    public Entity spawnEntity(ActionContext context, PositionTarget position, float angleOffset, float speed, float uncertainty) {
        Vec3 pos = context.get(position.contextParam());
        if (pos == null) {
            return null;
        }

        return this.entity.spawn(
            context,
            pos,
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

    private void initializeProjectile(ActionContext context, Projectile projectileEntity, float angleOffset, float speed, float uncertainty) {
        Entity user = context.get(LootContextParams.THIS_ENTITY);
        if (user != null) {
            initializeProjectile(projectileEntity, user, angleOffset, speed, uncertainty);
        } else {
            initializeProjectile(projectileEntity, context.getOrDefault(ItematicContextParameters.SIDE, Direction.UP), speed, uncertainty);
        }

        if (context.world() instanceof ServerLevel serverWorld) {
            projectileEntity.applyOnProjectileSpawned(
                serverWorld,
                context.getOrDefault(LootContextParams.TOOL, ItemStack.EMPTY)
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
