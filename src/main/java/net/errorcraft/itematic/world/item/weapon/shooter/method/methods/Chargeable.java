package net.errorcraft.itematic.world.item.weapon.shooter.method.methods;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.mixin.world.item.CrossbowItemAccessor;
import net.errorcraft.itematic.mixin.world.item.ProjectileWeaponItemAccessor;
import net.errorcraft.itematic.world.item.weapon.shooter.ChargedPowerRules;
import net.errorcraft.itematic.world.item.weapon.shooter.ChargingSounds;
import net.errorcraft.itematic.world.item.weapon.shooter.method.ShooterMethod;
import net.minecraft.advancements.triggers.CriteriaTriggers;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.OptionalInt;

public class Chargeable implements ShooterMethod<Chargeable> {
    public static final Chargeable INSTANCE = new Chargeable();
    private static final float START_SOUND_PROGRESS = CrossbowItemAccessor.startSoundProgress();
    private static final float MID_SOUND_PROGRESS = CrossbowItemAccessor.midSoundProgress();
    private static final int EXTRA_USE_TIME = 3;
    private static final CrossbowItem DUMMY = new CrossbowItem(new Item.Properties());

    private Chargeable() {}

    @Override
    public MapCodec<? extends ShooterMethod.Properties<Chargeable>> propertiesCodec() {
        return Properties.CODEC;
    }

    @Override
    public boolean tryShoot(ItemStack stack, Level level, LivingEntity user, InteractionHand hand) {
        if (!CrossbowItem.isCharged(stack)) {
            return false;
        }

        ChargedPowerRules chargedPowerRules = stack.get(ItematicDataComponents.SHOOTER_CHARGED_POWER_RULES);
        if (chargedPowerRules == null) {
            return false;
        }

        this.shoot(level, user, hand, stack, chargedPowerRules.power(stack), 1.0f, null);
        return true;
    }

    @Override
    public void hold(ItemStack stack, Level level, LivingEntity user, int usedTicks) {
        if (level.isClientSide()) {
            return;
        }

        int chargeTime = CrossbowItem.getChargeDuration(stack, user);
        if (usedTicks >= chargeTime) {
            return;
        }

        CrossbowItem.ChargingSounds chargingSounds = this.chargingSounds(stack);
        if (usedTicks == getChargeTimeAt(chargeTime, START_SOUND_PROGRESS)) {
            chargingSounds.start().ifPresent(sound -> level.playSound(null, user.getX(), user.getY(), user.getZ(), sound.value(), user.getSoundSource(), 0.5f, 1.0f));
            return;
        }

        if (usedTicks == getChargeTimeAt(chargeTime, MID_SOUND_PROGRESS)) {
            chargingSounds.mid().ifPresent(sound -> level.playSound(null, user.getX(), user.getY(), user.getZ(), sound.value(), user.getSoundSource(), 0.5f, 1.0f));
        }
    }

    @Override
    public boolean stop(ItemStack stack, Level level, LivingEntity user, int usedTicks) {
        if (usedTicks < CrossbowItem.getChargeDuration(stack, user)) {
            return false;
        }

        if (CrossbowItem.isCharged(stack) || !chargeProjectiles(user, stack)) {
            return false;
        }

        CrossbowItem.ChargingSounds chargingSounds = this.chargingSounds(stack);
        float pitch = Mth.lerp(level.getRandom().nextFloat(), 0.87f, 1.2f);
        chargingSounds.end().ifPresent(sound -> level.playSound(null, user.getX(), user.getY(), user.getZ(), sound.value(), user.getSoundSource(), 1.0f, pitch));
        return true;
    }

    @Override
    public void initializeProjectile(LivingEntity user, Projectile projectile, int index, float power, float uncertainty, float angle, boolean critical, @Nullable LivingEntity target) {
        ShooterMethod.super.initializeProjectile(user, projectile, index, power, uncertainty, angle, critical, target);
        if (projectile instanceof AbstractArrow persistentProjectile) {
            persistentProjectile.setSoundEvent(SoundEvents.CROSSBOW_HIT);
        }

        ((ProjectileWeaponItemAccessor) DUMMY).itematic$shootProjectile(user, projectile, index, power, uncertainty, angle, target);
    }

    @Override
    public OptionalInt useDuration(ItemStack stack, LivingEntity user) {
        if (CrossbowItem.isCharged(stack)) {
            return OptionalInt.empty();
        }

        int useDuration = Math.max(CrossbowItem.getChargeDuration(stack, user), 1) + EXTRA_USE_TIME;
        return OptionalInt.of(useDuration);
    }

    public void shoot(Level level, LivingEntity user, InteractionHand hand, ItemStack stack, float power, float divergence, @Nullable LivingEntity targetOverride) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }

        ChargedProjectiles chargedProjectiles = stack.set(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY);
        if (chargedProjectiles == null || chargedProjectiles.isEmpty()) {
            return;
        }

        this.shoot(serverLevel, user, hand, stack, chargedProjectiles.itemCopies().toList(), power, divergence, user instanceof Player, targetOverride);
        if (user instanceof ServerPlayer player) {
            CriteriaTriggers.SHOT_CROSSBOW.trigger(player, stack);
            player.awardStat(Stats.ITEM_USED.itematic$get(stack.typeHolder()));
        }
    }

    private static int getChargeTimeAt(int chargeTime, float progress) {
        return Mth.floor(progress * chargeTime);
    }

    private CrossbowItem.ChargingSounds chargingSounds(ItemStack stack) {
        return EnchantmentHelper.pickHighestLevel(stack, EnchantmentEffectComponents.CROSSBOW_CHARGING_SOUNDS)
            .orElseGet(() -> stack.getOrDefault(ItematicDataComponents.SHOOTER_DEFAULT_CHARGING_SOUNDS, ChargingSounds.EMPTY));
    }

    private static boolean chargeProjectiles(LivingEntity user, ItemStack stack) {
        List<ItemStack> projectiles = ProjectileWeaponItemAccessor.draw(stack, user.itematic$getAmmunition(stack), user);
        if (projectiles.isEmpty()) {
            return false;
        }

        stack.set(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.ofNonEmpty(projectiles));
        return true;
    }

    public record Properties(float defaultChargeTime, CrossbowItem.ChargingSounds defaultChargingSounds, ChargedPowerRules chargedPowerRules) implements ShooterMethod.Properties<Chargeable> {
        public static final MapCodec<Properties> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ExtraCodecs.NON_NEGATIVE_FLOAT.fieldOf("default_charge_time").forGetter(Properties::defaultChargeTime),
            CrossbowItem.ChargingSounds.CODEC.fieldOf("default_charging_sounds").forGetter(Properties::defaultChargingSounds),
            ChargedPowerRules.CODEC.fieldOf("charged_power_rules").forGetter(Properties::chargedPowerRules)
        ).apply(instance, Properties::new));

        public static Properties of(CrossbowItem.ChargingSounds defaultChargingSounds, ChargedPowerRules.Rule... chargedPowerRules) {
            return new Properties(
                CrossbowItemAccessor.defaultChargeTime(),
                defaultChargingSounds,
                new ChargedPowerRules(
                    List.of(chargedPowerRules),
                    CrossbowItemAccessor.defaultPower()
                )
            );
        }

        @Override
        public Chargeable method() {
            return INSTANCE;
        }

        @Override
        public void addComponents(DataComponentMap.Builder builder) {
            builder.set(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY);
            builder.set(ItematicDataComponents.SHOOTER_DEFAULT_CHARGE_TIME, this.defaultChargeTime);
            builder.set(ItematicDataComponents.SHOOTER_DEFAULT_CHARGING_SOUNDS, this.defaultChargingSounds);
            builder.set(ItematicDataComponents.SHOOTER_CHARGED_POWER_RULES, this.chargedPowerRules);
        }
    }

}
