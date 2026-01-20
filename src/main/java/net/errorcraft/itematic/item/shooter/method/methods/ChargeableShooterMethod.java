package net.errorcraft.itematic.item.shooter.method.methods;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.component.ItematicDataComponentTypes;
import net.errorcraft.itematic.item.component.components.ShooterItemComponent;
import net.errorcraft.itematic.item.shooter.method.ShooterMethod;
import net.errorcraft.itematic.item.shooter.method.ShooterMethodType;
import net.errorcraft.itematic.item.shooter.method.ShooterMethodTypes;
import net.errorcraft.itematic.mixin.item.CrossbowItemAccessor;
import net.errorcraft.itematic.mixin.item.RangedWeaponItemAccessor;
import net.errorcraft.itematic.serialization.ItematicCodecs;
import net.minecraft.SharedConstants;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ChargedProjectilesComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryCodecs;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Stream;

public record ChargeableShooterMethod(float defaultChargeTime, ChargingSounds defaultChargingSounds, ChargedPowerRules chargedPowerRules) implements ShooterMethod {
    public static final MapCodec<ChargeableShooterMethod> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        ItematicCodecs.NON_NEGATIVE_FLOAT.fieldOf("default_charge_time").forGetter(ChargeableShooterMethod::defaultChargeTime),
        ChargingSounds.CODEC.fieldOf("default_charging_sounds").forGetter(ChargeableShooterMethod::defaultChargingSounds),
        ChargedPowerRules.CODEC.fieldOf("charged_power_rules").forGetter(ChargeableShooterMethod::chargedPowerRules)
    ).apply(instance, ChargeableShooterMethod::new));
    private static final float START_SOUND_PROGRESS = CrossbowItemAccessor.startSoundProgress();
    private static final float MID_SOUND_PROGRESS = CrossbowItemAccessor.midSoundProgress();
    private static final int EXTRA_USE_TIME = 3;
    private static final CrossbowItem DUMMY = new CrossbowItem(new Item.Settings());

    public static ChargeableShooterMethod of(ChargingSounds defaultChargingSounds, ChargedPowerRules.Rule... chargedPowerRules) {
        return new ChargeableShooterMethod((float) CrossbowItemAccessor.defaultChargeTime() / SharedConstants.TICKS_PER_SECOND, defaultChargingSounds, new ChargedPowerRules(List.of(chargedPowerRules), CrossbowItemAccessor.defaultPower()));
    }

    @Override
    public ShooterMethodType<?> type() {
        return ShooterMethodTypes.CHARGEABLE;
    }

    @Override
    public void addComponents(ComponentMap.Builder builder) {
        builder.add(DataComponentTypes.CHARGED_PROJECTILES, ChargedProjectilesComponent.DEFAULT);
        builder.add(ItematicDataComponentTypes.SHOOTER_DEFAULT_CHARGE_TIME, this.defaultChargeTime);
        builder.add(ItematicDataComponentTypes.SHOOTER_DEFAULT_CHARGING_SOUNDS, this.defaultChargingSounds);
        builder.add(ItematicDataComponentTypes.SHOOTER_CHARGED_POWER_RULES, this.chargedPowerRules);
    }

    @Override
    public boolean tryShoot(ShooterItemComponent component, ItemStack stack, World world, LivingEntity user, Hand hand) {
        if (!CrossbowItem.isCharged(stack)) {
            return false;
        }

        ChargedPowerRules chargedPowerRules = stack.get(ItematicDataComponentTypes.SHOOTER_CHARGED_POWER_RULES);
        if (chargedPowerRules == null) {
            return false;
        }

        this.shoot(component, world, user, hand, stack, chargedPowerRules.power(stack), 1.0f, null);
        return true;
    }

    @Override
    public void hold(ShooterItemComponent shooter, ItemStack stack, World world, LivingEntity user, int usedTicks) {
        if (world.isClient()) {
            return;
        }

        int chargeTime = CrossbowItem.getPullTime(stack);
        if (usedTicks >= chargeTime) {
            return;
        }

        ChargingSounds chargingSounds = this.chargingSounds(stack);
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
        if (usedTicks < CrossbowItem.getPullTime(stack)) {
            return;
        }

        if (CrossbowItem.isCharged(stack) || !chargeProjectiles(user, stack)) {
            return;
        }

        ChargingSounds chargingSounds = this.chargingSounds(stack);
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

        return OptionalInt.of(CrossbowItem.getPullTime(stack) + EXTRA_USE_TIME);
    }

    @Override
    public float pullProgress(ItemStack stack, LivingEntity user, int usedTicks) {
        return ((float)usedTicks) / CrossbowItem.getPullTime(stack);
    }

    public void shoot(ShooterItemComponent shooter, World world, LivingEntity user, Hand hand, ItemStack stack, float power, float divergence, @Nullable LivingEntity livingEntity) {
        if (!(world instanceof ServerWorld serverWorld)) {
            return;
        }

        ChargedProjectilesComponent chargedProjectiles = stack.set(DataComponentTypes.CHARGED_PROJECTILES, ChargedProjectilesComponent.DEFAULT);
        if (chargedProjectiles == null || chargedProjectiles.isEmpty()) {
            return;
        }

        shooter.shoot(serverWorld, user, hand, stack, chargedProjectiles.getProjectiles(), power, divergence, user instanceof PlayerEntity, livingEntity);
        if (user instanceof ServerPlayerEntity player) {
            Criteria.SHOT_CROSSBOW.trigger(player, stack);
            player.incrementStat(Stats.USED.itematic$getOrCreateStat(stack.getRegistryEntry()));
        }
    }

    private static int getChargeTimeAt(int chargeTime, float progress) {
        return MathHelper.floor(progress * chargeTime);
    }

    private ChargingSounds chargingSounds(ItemStack stack) {
        int quickChargeLevel = EnchantmentHelper.getLevel(Enchantments.QUICK_CHARGE, stack);
        if (quickChargeLevel == 0) {
            return stack.getOrDefault(ItematicDataComponentTypes.SHOOTER_DEFAULT_CHARGING_SOUNDS, ChargingSounds.EMPTY);
        }

        return ChargingSounds.enchanted(quickChargeLevel);
    }

    private static boolean chargeProjectiles(LivingEntity user, ItemStack stack) {
        List<ItemStack> projectiles = RangedWeaponItemAccessor.load(stack, user.itematic$getAmmunition(stack), user);
        if (projectiles.isEmpty()) {
            return false;
        }

        stack.set(DataComponentTypes.CHARGED_PROJECTILES, ChargedProjectilesComponent.of(projectiles));
        return true;
    }

    public record ChargingSounds(Optional<RegistryEntry<SoundEvent>> start, Optional<RegistryEntry<SoundEvent>> mid, Optional<RegistryEntry<SoundEvent>> end) {
        public static final Codec<ChargingSounds> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            SoundEvent.ENTRY_CODEC.optionalFieldOf("start").forGetter(ChargingSounds::start),
            SoundEvent.ENTRY_CODEC.optionalFieldOf("mid").forGetter(ChargingSounds::mid),
            SoundEvent.ENTRY_CODEC.optionalFieldOf("end").forGetter(ChargingSounds::end)
        ).apply(instance, ChargingSounds::new));
        public static final PacketCodec<RegistryByteBuf, ChargingSounds> PACKET_CODEC = PacketCodec.tuple(
            SoundEvent.ENTRY_PACKET_CODEC.collect(PacketCodecs::optional), ChargingSounds::start,
            SoundEvent.ENTRY_PACKET_CODEC.collect(PacketCodecs::optional), ChargingSounds::mid,
            SoundEvent.ENTRY_PACKET_CODEC.collect(PacketCodecs::optional), ChargingSounds::end,
            ChargingSounds::new
        );
        public static final ChargingSounds DEFAULT = new ChargingSounds(
            Optional.of(Registries.SOUND_EVENT.getEntry(SoundEvents.ITEM_CROSSBOW_LOADING_START)),
            Optional.of(Registries.SOUND_EVENT.getEntry(SoundEvents.ITEM_CROSSBOW_LOADING_MIDDLE)),
            Optional.of(Registries.SOUND_EVENT.getEntry(SoundEvents.ITEM_CROSSBOW_LOADING_END))
        );
        private static final ChargingSounds EMPTY = new ChargingSounds(
            Optional.empty(),
            Optional.empty(),
            Optional.empty()
        );
        private static final List<ChargingSounds> QUICK_CHARGE = Stream.of(
            Registries.SOUND_EVENT.getEntry(SoundEvents.ITEM_CROSSBOW_QUICK_CHARGE_1),
            Registries.SOUND_EVENT.getEntry(SoundEvents.ITEM_CROSSBOW_QUICK_CHARGE_2),
            Registries.SOUND_EVENT.getEntry(SoundEvents.ITEM_CROSSBOW_QUICK_CHARGE_3)
        )
            .map(startSound -> new ChargingSounds(
                Optional.of(startSound),
                Optional.empty(),
                Optional.of(Registries.SOUND_EVENT.getEntry(SoundEvents.ITEM_CROSSBOW_LOADING_END))
            ))
            .toList();

        public static ChargingSounds enchanted(int quickChargeLevel) {
            int index = quickChargeLevel - 1;
            if (quickChargeLevel < 0) {
                return QUICK_CHARGE.getFirst();
            }

            if (quickChargeLevel >= QUICK_CHARGE.size()) {
                return QUICK_CHARGE.getLast();
            }

            return QUICK_CHARGE.get(index);
        }
    }

    public record ChargedPowerRules(List<Rule> rules, float defaultPower) {
        public static final Codec<ChargedPowerRules> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Rule.CODEC.listOf().fieldOf("rules").forGetter(ChargedPowerRules::rules),
            ItematicCodecs.NON_NEGATIVE_FLOAT.fieldOf("default_power").forGetter(ChargedPowerRules::defaultPower)
        ).apply(instance, ChargedPowerRules::new));
        public static final PacketCodec<RegistryByteBuf, ChargedPowerRules> PACKET_CODEC = PacketCodec.tuple(
            Rule.PACKET_CODEC.collect(PacketCodecs.toList()), ChargedPowerRules::rules,
            PacketCodecs.FLOAT, ChargedPowerRules::defaultPower,
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

        public record Rule(RegistryEntryList<Item> items, Optional<Float> power) {
            public static final Codec<Rule> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                RegistryCodecs.entryList(RegistryKeys.ITEM).fieldOf("items").forGetter(Rule::items),
                ItematicCodecs.NON_NEGATIVE_FLOAT.optionalFieldOf("power").forGetter(Rule::power)
            ).apply(instance, Rule::new));
            public static final PacketCodec<RegistryByteBuf, Rule> PACKET_CODEC = PacketCodec.tuple(
                PacketCodecs.registryEntryList(RegistryKeys.ITEM), Rule::items,
                PacketCodecs.FLOAT.collect(PacketCodecs::optional), Rule::power,
                Rule::new
            );

            public static Rule of(RegistryEntryList<Item> items, float power) {
                return new Rule(items, Optional.of(power));
            }

            public boolean matches(ItemStack stack) {
                return stack.isIn(this.items);
            }
        }
    }
}
