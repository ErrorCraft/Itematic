package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.entity.initializer.EntityInitializer;
import net.errorcraft.itematic.entity.initializer.initializers.PersistentProjectileEntityInitializer;
import net.errorcraft.itematic.entity.initializer.initializers.SimpleEntityInitializer;
import net.errorcraft.itematic.item.ItemStackConsumer;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;

public record ProjectileItemComponent(EntityInitializer<?> entity) implements ItemComponent<ProjectileItemComponent> {
    public static final Codec<ProjectileItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        EntityInitializer.CODEC.fieldOf("entity").forGetter(ProjectileItemComponent::entity)
    ).apply(instance, ProjectileItemComponent::new));

    public static ProjectileItemComponent of(EntityInitializer<?> entity) {
        return new ProjectileItemComponent(entity);
    }

    public static ProjectileItemComponent of(RegistryEntry<EntityType<?>> entity) {
        return of(SimpleEntityInitializer.of(entity.value()));
    }

    public static <U extends PersistentProjectileEntity> ProjectileItemComponent persistentProjectile(EntityType<U> entityType, PersistentProjectileEntityInitializer.OwnerCreator<U> ownerCreator, PersistentProjectileEntityInitializer.SimpleCreator<U> simpleCreator) {
        return of(new PersistentProjectileEntityInitializer<>(entityType, ownerCreator, simpleCreator));
    }

    @Override
    public ItemComponentType<ProjectileItemComponent> type() {
        return ItemComponentTypes.PROJECTILE;
    }

    @Override
    public Codec<ProjectileItemComponent> codec() {
        return CODEC;
    }

    public Entity createEntity(World world, Entity user, ItemStack stack, float angleOffset, float speed) {
        if (world.isClient()) {
            return null;
        }
        return this.createEntity((ServerWorld) world, user, stack, ItemStackConsumer.EMPTY, angleOffset, speed, 1.0f);
    }

    public Entity createEntity(ServerWorld world, Entity user, ItemStack stack, ItemStackConsumer resultStackConsumer, float angleOffset, float speed, float uncertainty) {
        ActionContext context = ActionContext.builder(world, stack, resultStackConsumer)
            .entityPosition(ActionContextParameter.THIS, user)
            .position(ActionContextParameter.TARGET, user.getEyePos().add(0.0d, -0.1d, 0.0d))
            .build();
        return this.createEntity(context, ActionContextParameter.TARGET, angleOffset, speed, uncertainty);
    }

    public Entity createEntity(World world, Position position, ItemStack stack, float speed, float uncertainty) {
        if (world.isClient()) {
            return null;
        }
        ActionContext context = ActionContext.builder((ServerWorld) world, stack, ItemStackConsumer.EMPTY)
            .position(ActionContextParameter.TARGET, position)
            .build();
        return this.createEntity(context, ActionContextParameter.TARGET, 0.0f, speed, uncertainty);
    }

    public Entity createEntity(ActionContext context, ActionContextParameter position, float angleOffset, float speed, float uncertainty) {
        Entity entity = this.entity.create(context);
        if (entity == null) {
            return null;
        }
        entity.refreshPositionAfterTeleport(context.position(position));
        if (entity instanceof ThrownItemEntity thrownItemEntity) {
            thrownItemEntity.setItem(context.stack());
        }
        if (entity instanceof ProjectileEntity projectileEntity) {
            this.initializeProjectile(context, projectileEntity, angleOffset, speed, uncertainty);
        }
        return entity;
    }

    private void initializeProjectile(ActionContext context, ProjectileEntity projectileEntity, float angleOffset, float speed, float uncertainty) {
        context.entity(ActionContextParameter.THIS).ifPresentOrElse(
            user -> this.initializeProjectile(projectileEntity, user, angleOffset, speed, uncertainty),
            () -> this.initializeProjectile(projectileEntity, context.side(), speed, uncertainty)
        );
    }

    private void initializeProjectile(ProjectileEntity entity, Direction side, float speed, float uncertainty) {
        if (entity instanceof ExplosiveProjectileEntity) {
            return;
        }
        entity.setVelocity(side.getOffsetX(), side.getOffsetY(), side.getOffsetZ(), speed, uncertainty);
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
}
