package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.entity.EntitySpawner;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.util.context.ItematicContextParameters;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.component.ComponentChanges;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public record ProjectileItemComponent(EntitySpawner entity) implements ItemComponent<ProjectileItemComponent> {
    public static final Codec<ProjectileItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        EntitySpawner.CODEC.forGetter(ProjectileItemComponent::entity)
    ).apply(instance, ProjectileItemComponent::new));

    public static ProjectileItemComponent of(RegistryEntry<EntityType<?>> entity) {
        return new ProjectileItemComponent(EntitySpawner.of(entity));
    }

    public static ProjectileItemComponent of(RegistryEntry<EntityType<?>> entity, ComponentChanges components) {
        return new ProjectileItemComponent(EntitySpawner.of(entity, components));
    }

    @Override
    public ItemComponentType<ProjectileItemComponent> type() {
        return ItemComponentTypes.PROJECTILE;
    }

    @Override
    public Codec<ProjectileItemComponent> codec() {
        return CODEC;
    }

    public Entity createEntity(World world, LivingEntity user, ItemStack stack, float angleOffset, float speed) {
        if (world.isClient()) {
            return null;
        }

        ActionContext context = ActionContext.builder((ServerWorld) world)
            .stackExchanger(user, stack)
            .add(LootContextParameters.TOOL, stack)
            .add(LootContextParameters.THIS_ENTITY, user)
            .add(LootContextParameters.ORIGIN, user.getPos())
            .add(ItematicContextParameters.INTERACTED_POSITION, user.getEyePos().add(0.0d, -0.1d, 0.0d))
            .build();
        return this.createEntity(context, PositionTarget.INTERACTED_POSITION, angleOffset, speed, 1.0f);
    }

    public Entity createEntity(ActionContext context, PositionTarget position, float angleOffset, float speed, float uncertainty) {
        Vec3d pos = context.get(position.parameter());
        if (pos == null) {
            return null;
        }

        Entity entity = this.entity.create(context, BlockPos.ofFloored(pos), SpawnReason.SPAWN_ITEM_USE);
        if (entity == null) {
            return null;
        }

        entity.refreshPositionAfterTeleport(pos);
        ItemStack stack = context.get(LootContextParameters.TOOL);
        if (stack != null && entity instanceof ThrownItemEntity thrownItemEntity) {
            thrownItemEntity.setItem(stack);
        }

        if (entity instanceof ProjectileEntity projectileEntity) {
            this.initializeProjectile(context, projectileEntity, angleOffset, speed, uncertainty);
        }

        return entity;
    }

    private void initializeProjectile(ActionContext context, ProjectileEntity projectileEntity, float angleOffset, float speed, float uncertainty) {
        Entity user = context.get(LootContextParameters.THIS_ENTITY);
        if (user != null) {
            this.initializeProjectile(projectileEntity, user, angleOffset, speed, uncertainty);
        } else {
            this.initializeProjectile(projectileEntity, context.getOrDefault(ItematicContextParameters.SIDE, Direction.UP), speed, uncertainty);
        }
    }

    private void initializeProjectile(ProjectileEntity entity, Entity user, float angleOffset, float speed, float uncertainty) {
        entity.setOwner(user);
        if (entity instanceof PersistentProjectileEntity persistentProjectileEntity && user instanceof PlayerEntity player && player.isInCreativeMode()) {
            persistentProjectileEntity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
        }

        if (entity instanceof ExplosiveProjectileEntity) {
            return;
        }

        entity.setVelocity(user, user.getPitch(), user.getYaw(), angleOffset, speed, uncertainty);
    }

    private void initializeProjectile(ProjectileEntity entity, Direction side, float speed, float uncertainty) {
        if (entity instanceof ExplosiveProjectileEntity) {
            return;
        }

        entity.setVelocity(side.getOffsetX(), side.getOffsetY(), side.getOffsetZ(), speed, uncertainty);
    }
}
