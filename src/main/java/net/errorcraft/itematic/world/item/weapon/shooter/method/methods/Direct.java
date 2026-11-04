package net.errorcraft.itematic.world.item.weapon.shooter.method.methods;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.mixin.world.item.ProjectileWeaponItemAccessor;
import net.errorcraft.itematic.world.item.use.duration.UseDuration;
import net.errorcraft.itematic.world.item.weapon.shooter.method.ShooterMethod;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
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
import java.util.Optional;
import java.util.OptionalInt;

public class Direct implements ShooterMethod<Direct> {
    public static final Direct INSTANCE = new Direct();
    private static final BowItem DUMMY = new BowItem(new Item.Properties());

    private Direct() {}

    @Override
    public MapCodec<? extends ShooterMethod.Properties<Direct>> propertiesCodec() {
        return Properties.CODEC;
    }

    @Override
    public boolean tryShoot(ItemStack stack, Level level, LivingEntity user, InteractionHand hand) {
        return false;
    }

    @Override
    public void hold(ItemStack stack, Level level, LivingEntity user, int usedTicks) {}

    @Override
    public boolean stop(ItemStack stack, Level level, LivingEntity user, int usedTicks) {
        ItemStack ammunition = user.itematic$getAmmunition(stack);
        if (ammunition.isEmpty()) {
            return false;
        }

        float pullProgress = BowItem.getPowerForTime(usedTicks);
        if (pullProgress < 0.1f) {
            return false;
        }

        List<ItemStack> projectiles = ProjectileWeaponItemAccessor.draw(stack, ammunition, user);
        if (level instanceof ServerLevel serverLevel && !projectiles.isEmpty()) {
            this.shoot(serverLevel, user, user.getUsedItemHand(), stack, projectiles, pullProgress * 3.0f, 1.0f, pullProgress == 1.0f, null);
        }

        Holder<SoundEvent> shootSound = stack.get(ItematicDataComponents.SHOOTER_SHOOT_SOUND);
        if (shootSound != null) {
            level.playSound(null, user.getX(), user.getY(), user.getZ(), shootSound.value(), SoundSource.PLAYERS, 1.0f, 1.0f / (level.getRandom().nextFloat() * 0.4f + 1.2f) + pullProgress * 0.5f);
        }

        if (user instanceof Player playerEntity) {
            playerEntity.awardStat(Stats.ITEM_USED.itematic$get(stack.typeHolder()));
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

    public record Properties(Optional<Holder<SoundEvent>> shootSound) implements ShooterMethod.Properties<Direct> {
        public static final MapCodec<Properties> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            SoundEvent.CODEC.optionalFieldOf("shoot_sound").forGetter(Properties::shootSound)
        ).apply(instance, Properties::new));

        public static Properties of(Holder<SoundEvent> shootSound) {
            return new Properties(Optional.of(shootSound));
        }

        @Override
        public Direct method() {
            return INSTANCE;
        }

        @Override
        public void addComponents(DataComponentMap.Builder builder) {
            this.shootSound.ifPresent(shootSound -> builder.set(ItematicDataComponents.SHOOTER_SHOOT_SOUND, shootSound));
        }
    }
}
