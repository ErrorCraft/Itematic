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
import net.errorcraft.itematic.mixin.item.CrossbowItemAccessor;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.MutableActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public record ProjectileItemComponent(EntityInitializer<?> entity, int damage, float chargedSpeed) implements ItemComponent<ProjectileItemComponent> {
    public static final Codec<ProjectileItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        EntityInitializer.CODEC.fieldOf("entity").forGetter(ProjectileItemComponent::entity),
        Codec.INT.optionalFieldOf("damage", 0).forGetter(ProjectileItemComponent::damage),
        Codec.FLOAT.optionalFieldOf("charged_speed", CrossbowItemAccessor.getDefaultSpeed()).forGetter(ProjectileItemComponent::chargedSpeed)
    ).apply(instance, ProjectileItemComponent::new));

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
        MutableActionContext context = MutableActionContext.stackUsage((ServerWorld) world, stack, ItemStackConsumer.EMPTY)
            .entityPosition(ActionContextParameter.THIS, user)
            .position(ActionContextParameter.TARGET, user.getEyePos().add(0.0d, -0.1d, 0.0d));
        return this.createEntity(context, angleOffset, speed, 1.0f);
    }

    public Entity createEntity(World world, Position position, ItemStack stack, float speed, float variation) {
        if (world.isClient()) {
            return null;
        }
        ActionContext context = MutableActionContext.stackUsage((ServerWorld) world, stack, ItemStackConsumer.EMPTY)
            .position(ActionContextParameter.TARGET, position);
        return this.createEntity(context, 0.0f, speed, variation);
    }

    public static ProjectileItemComponent of(EntityInitializer<?> entity, int damage) {
        return new ProjectileItemComponent(entity, damage, CrossbowItemAccessor.getDefaultSpeed());
    }

    public static ProjectileItemComponent of(RegistryEntry<EntityType<?>> entity) {
        return of(new SimpleEntityInitializer<>(entity.value()), 0);
    }

    public static <U extends PersistentProjectileEntity> ProjectileItemComponent persistentProjectile(EntityType<U> entityType, PersistentProjectileEntityInitializer.OwnerCreator<U> ownerCreator, PersistentProjectileEntityInitializer.SimpleCreator<U> simpleCreator) {
        return of(new PersistentProjectileEntityInitializer<>(entityType, ownerCreator, simpleCreator), 1);
    }

    private Entity createEntity(ActionContext context, float angleOffset, float speed, float variation) {
        Entity entity = this.entity.create(context);
        if (entity == null) {
            return null;
        }
        if (entity instanceof ThrownItemEntity thrownItemEntity) {
            thrownItemEntity.setItem(context.stack());
        }
        if (entity instanceof ProjectileEntity projectileEntity) {
            this.initializeProjectile(context, projectileEntity, angleOffset, speed, variation);
        }
        return entity;
    }

    private void initializeProjectile(ActionContext context, ProjectileEntity projectileEntity, float angleOffset, float speed, float variation) {
        context.entity(ActionContextParameter.THIS).ifPresentOrElse(
            user -> this.initializeProjectile(projectileEntity, user, angleOffset, speed, variation),
            () -> this.initializeProjectile(projectileEntity, context.position(ActionContextParameter.TARGET), speed, variation)
        );
    }

    private void initializeProjectile(ProjectileEntity entity, Vec3d position, float speed, float variation) {
        entity.setVelocity(position.getX(), position.getY() + 0.1f, position.getZ(), speed, variation);
    }

    private void initializeProjectile(ProjectileEntity entity, Entity user, float angleOffset, float speed, float variation) {
        entity.setOwner(user);
        entity.setVelocity(user, user.getPitch(), user.getYaw(), angleOffset, speed, variation);
    }
}
