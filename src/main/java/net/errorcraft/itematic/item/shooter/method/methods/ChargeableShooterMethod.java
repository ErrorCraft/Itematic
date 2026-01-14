package net.errorcraft.itematic.item.shooter.method.methods;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.component.ItematicDataComponentTypes;
import net.errorcraft.itematic.item.component.components.ShooterItemComponent;
import net.errorcraft.itematic.item.shooter.ChargingSoundsUtil;
import net.errorcraft.itematic.item.shooter.method.ShooterMethod;
import net.errorcraft.itematic.item.shooter.method.ShooterMethodType;
import net.errorcraft.itematic.item.shooter.method.ShooterMethodTypes;
import net.errorcraft.itematic.mixin.item.CrossbowItemAccessor;
import net.errorcraft.itematic.mixin.item.RangedWeaponItemAccessor;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.component.type.ChargedProjectilesComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.OptionalInt;

public record ChargeableShooterMethod(CrossbowItem.LoadingSounds defaultChargingSounds) implements ShooterMethod {
    public static final MapCodec<ChargeableShooterMethod> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        CrossbowItem.LoadingSounds.CODEC.fieldOf("default_charging_sounds").forGetter(ChargeableShooterMethod::defaultChargingSounds)
    ).apply(instance, ChargeableShooterMethod::new));
    private static final float DEFAULT_CHARGE_TIME = CrossbowItemAccessor.defaultChargeTime();
    private static final float START_SOUND_PROGRESS = CrossbowItemAccessor.startSoundProgress();
    private static final float MID_SOUND_PROGRESS = CrossbowItemAccessor.midSoundProgress();
    private static final int EXTRA_USE_TIME = 3;
    private static final CrossbowItem DUMMY = new CrossbowItem(new Item.Settings());

    public static ChargeableShooterMethod of(CrossbowItem.LoadingSounds defaultChargingSounds) {
        return new ChargeableShooterMethod(defaultChargingSounds);
    }

    @Override
    public ShooterMethodType<?> type() {
        return ShooterMethodTypes.CHARGEABLE;
    }

    @Override
    public void addComponents(ComponentMap.Builder builder) {
        builder.add(DataComponentTypes.CHARGED_PROJECTILES, ChargedProjectilesComponent.DEFAULT);
        builder.add(ItematicDataComponentTypes.SHOOTER_DEFAULT_CHARGING_SOUNDS, this.defaultChargingSounds);
    }

    @Override
    public boolean tryShoot(ShooterItemComponent component, ItemStack stack, World world, LivingEntity user, Hand hand) {
        if (!CrossbowItem.isCharged(stack)) {
            return false;
        }

        float chargedSpeed = stack.getOrDefault(DataComponentTypes.CHARGED_PROJECTILES, ChargedProjectilesComponent.DEFAULT)
            .itematic$getChargedSpeed();
        shoot(component, world, user, hand, stack, chargedSpeed, 1.0f, null);
        return true;
    }

    @Override
    public void hold(ShooterItemComponent shooter, ItemStack stack, World world, LivingEntity user, int usedTicks) {
        if (world.isClient()) {
            return;
        }

        int chargeTime = getChargeTime(stack, user);
        if (usedTicks >= chargeTime) {
            return;
        }

        CrossbowItem.LoadingSounds chargingSounds = this.chargingSounds(stack);
        if (usedTicks == getChargeTimeAt(chargeTime, START_SOUND_PROGRESS)) {
            chargingSounds.start().ifPresent(sound -> world.playSound(null, user.getX(), user.getY(), user.getZ(), sound.value(), user.getSoundCategory(), 0.5f, 1.0f));
            return;
        }

        if (usedTicks == getChargeTimeAt(chargeTime, MID_SOUND_PROGRESS)) {
            chargingSounds.mid().ifPresent(sound -> world.playSound(null, user.getX(), user.getY(), user.getZ(), sound.value(), user.getSoundCategory(), 0.5f, 1.0f));
        }
    }

    @Override
    public void stop(ShooterItemComponent shooter, ItemStack stack, World world, LivingEntity user, int usedTicks) {
        if (usedTicks < getChargeTime(stack, user)) {
            return;
        }

        if (CrossbowItem.isCharged(stack) || !CrossbowItemAccessor.loadProjectiles(user, stack)) {
            return;
        }

        CrossbowItem.LoadingSounds chargingSounds = this.chargingSounds(stack);
        float pitch = MathHelper.lerp(world.getRandom().nextFloat(), 0.87f, 1.2f);
        chargingSounds.end().ifPresent(sound -> world.playSound(null, user.getX(), user.getY(), user.getZ(), sound.value(), user.getSoundCategory(), 1.0f, pitch));
    }

    @Override
    public void initializeProjectile(LivingEntity user, ProjectileEntity projectile, int index, float power, float uncertainty, float angle, boolean critical, @Nullable LivingEntity target) {
        ShooterMethod.super.initializeProjectile(user, projectile, index, power, uncertainty, angle, critical, target);
        if (projectile instanceof PersistentProjectileEntity persistentProjectile) {
            persistentProjectile.setSound(SoundEvents.ITEM_CROSSBOW_HIT);
        }

        ((RangedWeaponItemAccessor) DUMMY).shoot(user, projectile, index, power, uncertainty, angle, target);
    }

    @Override
    public OptionalInt useDuration(ItemStack stack, LivingEntity user) {
        if (CrossbowItem.isCharged(stack)) {
            return OptionalInt.empty();
        }

        return OptionalInt.of(getChargeTime(stack, user) + EXTRA_USE_TIME);
    }

    @Override
    public float pullProgress(ItemStack stack, LivingEntity user, int usedTicks) {
        return ((float)usedTicks) / getChargeTime(stack, user);
    }

    public void shoot(ShooterItemComponent shooter, World world, LivingEntity user, Hand hand, ItemStack stack, float speed, float divergence, @Nullable LivingEntity livingEntity) {
        if (!(world instanceof ServerWorld serverWorld)) {
            return;
        }

        ChargedProjectilesComponent chargedProjectiles = stack.set(DataComponentTypes.CHARGED_PROJECTILES, ChargedProjectilesComponent.DEFAULT);
        if (chargedProjectiles == null || chargedProjectiles.isEmpty()) {
            return;
        }

        shooter.shoot(serverWorld, user, hand, stack, chargedProjectiles.getProjectiles(), speed, divergence, user instanceof PlayerEntity, livingEntity);
        if (user instanceof ServerPlayerEntity player) {
            Criteria.SHOT_CROSSBOW.trigger(player, stack);
            player.incrementStat(Stats.USED.itematic$getOrCreateStat(stack.getRegistryEntry()));
        }
    }

    public static int getChargeTime(ItemStack stack, LivingEntity user) {
        // TODO: Place the default charge time into a data component and change all CrossbowItem::getPullTime references to this one.
        //  Alternatively, modify the original method's hard values with the ones from the data component.
        //  What to do if the component is removed or does not exist otherwise? Maybe return 0 or -1 as an invalid state or check beforehand.
        float chargeTime = user.getWorld() instanceof ServerWorld serverWorld ?
            EnchantmentHelper.getCrossbowChargeTime(serverWorld, stack, user, DEFAULT_CHARGE_TIME) :
            DEFAULT_CHARGE_TIME;
        return MathHelper.floor(chargeTime * 20);
    }

    private static int getChargeTimeAt(int chargeTime, float progress) {
        return MathHelper.floor(progress * chargeTime);
    }

    private CrossbowItem.LoadingSounds chargingSounds(ItemStack stack) {
        return EnchantmentHelper.getEffect(stack, EnchantmentEffectComponentTypes.CROSSBOW_CHARGING_SOUNDS)
            .orElseGet(() -> stack.getOrDefault(ItematicDataComponentTypes.SHOOTER_DEFAULT_CHARGING_SOUNDS, ChargingSoundsUtil.EMPTY));
    }
}
