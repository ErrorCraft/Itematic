package net.errorcraft.itematic.world.item.weapon.shooter.method.methods;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.mixin.world.item.CrossbowItemAccessor;
import net.errorcraft.itematic.mixin.world.item.ProjectileWeaponItemAccessor;
import net.errorcraft.itematic.util.ItematicCodecs;
import net.errorcraft.itematic.world.item.behavior.behaviors.ShooterItemBehavior;
import net.errorcraft.itematic.world.item.weapon.shooter.ChargingSounds;
import net.errorcraft.itematic.world.item.weapon.shooter.method.ShooterMethod;
import net.errorcraft.itematic.world.item.weapon.shooter.method.ShooterMethodType;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
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
import java.util.Optional;
import java.util.OptionalInt;

public record ChargeableShooterMethod(float defaultChargeTime, CrossbowItem.ChargingSounds defaultChargingSounds, ChargedPowerRules chargedPowerRules) implements ShooterMethod {
    public static final MapCodec<ChargeableShooterMethod> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        ItematicCodecs.NON_NEGATIVE_FLOAT.fieldOf("default_charge_time").forGetter(ChargeableShooterMethod::defaultChargeTime),
        CrossbowItem.ChargingSounds.CODEC.fieldOf("default_charging_sounds").forGetter(ChargeableShooterMethod::defaultChargingSounds),
        ChargedPowerRules.CODEC.fieldOf("charged_power_rules").forGetter(ChargeableShooterMethod::chargedPowerRules)
    ).apply(instance, ChargeableShooterMethod::new));
    private static final float START_SOUND_PROGRESS = CrossbowItemAccessor.startSoundProgress();
    private static final float MID_SOUND_PROGRESS = CrossbowItemAccessor.midSoundProgress();
    private static final int EXTRA_USE_TIME = 3;
    private static final CrossbowItem DUMMY = new CrossbowItem(new Item.Properties());

    public static ChargeableShooterMethod of(CrossbowItem.ChargingSounds defaultChargingSounds, ChargedPowerRules.Rule... chargedPowerRules) {
        return new ChargeableShooterMethod(CrossbowItemAccessor.defaultChargeTime(), defaultChargingSounds, new ChargedPowerRules(List.of(chargedPowerRules), CrossbowItemAccessor.defaultPower()));
    }

    @Override
    public ShooterMethodType<?> type() {
        return ShooterMethodType.CHARGEABLE;
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY);
        builder.set(ItematicDataComponents.SHOOTER_DEFAULT_CHARGE_TIME, this.defaultChargeTime);
        builder.set(ItematicDataComponents.SHOOTER_DEFAULT_CHARGING_SOUNDS, this.defaultChargingSounds);
        builder.set(ItematicDataComponents.SHOOTER_CHARGED_POWER_RULES, this.chargedPowerRules);
    }

    @Override
    public boolean tryShoot(ShooterItemBehavior component, ItemStack stack, Level level, LivingEntity user, InteractionHand hand) {
        if (!CrossbowItem.isCharged(stack)) {
            return false;
        }

        ChargedPowerRules chargedPowerRules = stack.get(ItematicDataComponents.SHOOTER_CHARGED_POWER_RULES);
        if (chargedPowerRules == null) {
            return false;
        }

        this.shoot(component, level, user, hand, stack, chargedPowerRules.power(stack), 1.0f, null);
        return true;
    }

    @Override
    public void hold(ShooterItemBehavior shooter, ItemStack stack, Level level, LivingEntity user, int usedTicks) {
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
    public boolean stop(ShooterItemBehavior shooter, ItemStack stack, Level level, LivingEntity user, int usedTicks) {
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

        return OptionalInt.of(CrossbowItem.getChargeDuration(stack, user) + EXTRA_USE_TIME);
    }

    @Override
    public float pullProgress(ItemStack stack, LivingEntity user, int usedTicks) {
        return ((float)usedTicks) / CrossbowItem.getChargeDuration(stack, user);
    }

    public void shoot(ShooterItemBehavior shooter, Level level, LivingEntity user, InteractionHand hand, ItemStack stack, float power, float divergence, @Nullable LivingEntity targetOverride) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }

        ChargedProjectiles chargedProjectiles = stack.set(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY);
        if (chargedProjectiles == null || chargedProjectiles.isEmpty()) {
            return;
        }

        shooter.shoot(serverLevel, user, hand, stack, chargedProjectiles.getItems(), power, divergence, user instanceof Player, targetOverride);
        if (user instanceof ServerPlayer player) {
            CriteriaTriggers.SHOT_CROSSBOW.trigger(player, stack);
            player.awardStat(Stats.ITEM_USED.itematic$get(stack.getItemHolder()));
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

        stack.set(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.of(projectiles));
        return true;
    }

    public record ChargedPowerRules(List<Rule> rules, float defaultPower) {
        public static final Codec<ChargedPowerRules> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Rule.CODEC.listOf().fieldOf("rules").forGetter(ChargedPowerRules::rules),
            ItematicCodecs.NON_NEGATIVE_FLOAT.fieldOf("default_power").forGetter(ChargedPowerRules::defaultPower)
        ).apply(instance, ChargedPowerRules::new));
        public static final StreamCodec<RegistryFriendlyByteBuf, ChargedPowerRules> STREAM_CODEC = StreamCodec.composite(
            Rule.STREAM_CODEC.apply(ByteBufCodecs.list()), ChargedPowerRules::rules,
            ByteBufCodecs.FLOAT, ChargedPowerRules::defaultPower,
            ChargedPowerRules::new
        );

        public float power(ItemStack stack) {
            for (Rule rule : this.rules) {
                if (rule.power.isPresent() && rule.matches(stack)) {
                    return rule.power.get();
                }
            }

            return this.defaultPower;
        }

        public record Rule(HolderSet<Item> items, Optional<Float> power) {
            public static final Codec<Rule> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                RegistryCodecs.homogeneousList(Registries.ITEM).fieldOf("items").forGetter(Rule::items),
                ItematicCodecs.NON_NEGATIVE_FLOAT.optionalFieldOf("power").forGetter(Rule::power)
            ).apply(instance, Rule::new));
            public static final StreamCodec<RegistryFriendlyByteBuf, Rule> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.holderSet(Registries.ITEM), Rule::items,
                ByteBufCodecs.FLOAT.apply(ByteBufCodecs::optional), Rule::power,
                Rule::new
            );

            public static Rule of(HolderSet<Item> items, float power) {
                return new Rule(items, Optional.of(power));
            }

            public boolean matches(ItemStack stack) {
                return stack.is(this.items);
            }
        }
    }
}
