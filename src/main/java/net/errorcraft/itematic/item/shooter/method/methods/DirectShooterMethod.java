package net.errorcraft.itematic.item.shooter.method.methods;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.component.ItematicDataComponentTypes;
import net.errorcraft.itematic.component.type.UseDurationDataComponent;
import net.errorcraft.itematic.item.component.components.ShooterItemComponent;
import net.errorcraft.itematic.item.shooter.method.ShooterMethod;
import net.errorcraft.itematic.item.shooter.method.ShooterMethodType;
import net.errorcraft.itematic.item.shooter.method.ShooterMethodTypes;
import net.errorcraft.itematic.mixin.item.RangedWeaponItemAccessor;
import net.minecraft.component.ComponentMap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.OptionalInt;

public record DirectShooterMethod(RegistryEntry<SoundEvent> shootSound) implements ShooterMethod {
    private static final RegistryEntry<SoundEvent> DEFAULT_SHOOT_SOUND = Registries.SOUND_EVENT.getEntry(SoundEvents.ENTITY_ARROW_SHOOT);
    public static final MapCodec<DirectShooterMethod> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        SoundEvent.ENTRY_CODEC.optionalFieldOf("shoot_sound", DEFAULT_SHOOT_SOUND).forGetter(DirectShooterMethod::shootSound)
    ).apply(instance, DirectShooterMethod::new));
    private static final BowItem DUMMY = new BowItem(new Item.Settings());

    public static DirectShooterMethod of() {
        return new DirectShooterMethod(DEFAULT_SHOOT_SOUND);
    }

    @Override
    public ShooterMethodType<?> type() {
        return ShooterMethodTypes.DIRECT;
    }

    @Override
    public void addComponents(ComponentMap.Builder builder) {
        builder.add(ItematicDataComponentTypes.SHOOTER_SHOOT_SOUND, this.shootSound);
    }

    @Override
    public boolean tryShoot(ShooterItemComponent component, ItemStack stack, World world, LivingEntity user, Hand hand) {
        return false;
    }

    @Override
    public void hold(ShooterItemComponent shooter, ItemStack stack, World world, LivingEntity user, int usedTicks) {}

    @Override
    public void stop(ShooterItemComponent shooter, ItemStack stack, World world, LivingEntity user, int usedTicks) {
        ItemStack ammunition = user.itematic$getAmmunition(stack);
        if (ammunition.isEmpty()) {
            return;
        }

        float pullProgress = this.pullProgress(usedTicks);
        if (pullProgress < 0.1f) {
            return;
        }

        List<ItemStack> projectiles = RangedWeaponItemAccessor.load(stack, ammunition, user);
        if (world instanceof ServerWorld serverWorld && !projectiles.isEmpty()) {
            shooter.shoot(serverWorld, user, user.getActiveHand(), stack, projectiles, pullProgress * 3.0f, 1.0f, pullProgress == 1.0f, null);
        }

        RegistryEntry<SoundEvent> shootSound = stack.get(ItematicDataComponentTypes.SHOOTER_SHOOT_SOUND);
        if (shootSound != null) {
            world.playSound(null, user.getX(), user.getY(), user.getZ(), shootSound.value(), SoundCategory.PLAYERS, 1.0f, 1.0f / (world.getRandom().nextFloat() * 0.4f + 1.2f) + pullProgress * 0.5f);
        }

        if (user instanceof PlayerEntity playerEntity) {
            playerEntity.incrementStat(Stats.USED.itematic$getOrCreateStat(stack.getRegistryEntry()));
        }
    }

    @Override
    public void initializeProjectile(LivingEntity user, ProjectileEntity projectile, int index, float power, float uncertainty, float angle, boolean critical, @Nullable LivingEntity target) {
        ShooterMethod.super.initializeProjectile(user, projectile, index, power, uncertainty, angle, critical, target);
        ((RangedWeaponItemAccessor) DUMMY).shoot(user, projectile, index, power, uncertainty, angle, target);
    }

    @Override
    public OptionalInt useDuration(ItemStack stack, LivingEntity user) {
        return OptionalInt.of(UseDurationDataComponent.INDEFINITE_USE_DURATION);
    }

    @Override
    public float pullProgress(ItemStack stack, LivingEntity user, int usedTicks) {
        return this.pullProgress(usedTicks);
    }

    private float pullProgress(int usedTicks) {
        return BowItem.getPullProgress(usedTicks);
    }
}
