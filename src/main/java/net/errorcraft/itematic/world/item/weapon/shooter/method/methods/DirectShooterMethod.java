package net.errorcraft.itematic.world.item.weapon.shooter.method.methods;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.mixin.world.item.ProjectileWeaponItemAccessor;
import net.errorcraft.itematic.world.item.behavior.behaviors.ShooterItemBehavior;
import net.errorcraft.itematic.world.item.use.duration.UseDuration;
import net.errorcraft.itematic.world.item.weapon.shooter.method.ShooterMethod;
import net.errorcraft.itematic.world.item.weapon.shooter.method.ShooterMethodType;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.OptionalInt;

public record DirectShooterMethod(Holder<SoundEvent> shootSound) implements ShooterMethod {
    private static final Holder<SoundEvent> DEFAULT_SHOOT_SOUND = BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.ARROW_SHOOT);
    public static final MapCodec<DirectShooterMethod> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        SoundEvent.CODEC.optionalFieldOf("shoot_sound", DEFAULT_SHOOT_SOUND).forGetter(DirectShooterMethod::shootSound)
    ).apply(instance, DirectShooterMethod::new));
    private static final BowItem DUMMY = new BowItem(new Item.Properties());

    public static DirectShooterMethod of() {
        return new DirectShooterMethod(DEFAULT_SHOOT_SOUND);
    }

    @Override
    public ShooterMethodType<?> type() {
        return ShooterMethodType.DIRECT;
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(ItematicDataComponents.SHOOTER_SHOOT_SOUND, this.shootSound);
    }

    @Override
    public boolean tryShoot(ShooterItemBehavior component, ItemStack stack, Level level, LivingEntity user, InteractionHand hand) {
        return false;
    }

    @Override
    public void hold(ShooterItemBehavior shooter, ItemStack stack, Level level, LivingEntity user, int usedTicks) {}

    @Override
    public boolean stop(ShooterItemBehavior shooter, ItemStack stack, Level level, LivingEntity user, int usedTicks) {
        ItemStack ammunition = user.itematic$getAmmunition(stack);
        if (ammunition.isEmpty()) {
            return false;
        }

        float pullProgress = this.pullProgress(usedTicks);
        if (pullProgress < 0.1f) {
            return false;
        }

        List<ItemStack> projectiles = ProjectileWeaponItemAccessor.draw(stack, ammunition, user);
        if (level instanceof ServerLevel serverLevel && !projectiles.isEmpty()) {
            shooter.shoot(serverLevel, user, user.getUsedItemHand(), stack, projectiles, pullProgress * 3.0f, 1.0f, pullProgress == 1.0f, null);
        }

        Holder<SoundEvent> shootSound = stack.get(ItematicDataComponents.SHOOTER_SHOOT_SOUND);
        if (shootSound != null) {
            level.playSound(null, user.getX(), user.getY(), user.getZ(), shootSound.value(), SoundSource.PLAYERS, 1.0f, 1.0f / (level.getRandom().nextFloat() * 0.4f + 1.2f) + pullProgress * 0.5f);
        }

        if (user instanceof Player playerEntity) {
            playerEntity.awardStat(Stats.ITEM_USED.itematic$get(stack.getItemHolder()));
        }

        return true;
    }

    @Override
    public void initializeProjectile(LivingEntity user, Projectile projectile, int index, float power, float uncertainty, float angle, boolean critical, @Nullable LivingEntity target) {
        ShooterMethod.super.initializeProjectile(user, projectile, index, power, uncertainty, angle, critical, target);
        ((ProjectileWeaponItemAccessor) DUMMY).itematic$shootProjectile(user, projectile, index, power, uncertainty, angle, target);
    }

    @Override
    public OptionalInt useDuration(ItemStack stack, LivingEntity user) {
        return OptionalInt.of(UseDuration.INDEFINITE);
    }

    @Override
    public float pullProgress(ItemStack stack, LivingEntity user, int usedTicks) {
        return this.pullProgress(usedTicks);
    }

    private float pullProgress(int usedTicks) {
        return BowItem.getPowerForTime(usedTicks);
    }
}
