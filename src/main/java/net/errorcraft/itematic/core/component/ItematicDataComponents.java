package net.errorcraft.itematic.core.component;

import net.errorcraft.itematic.network.codec.ItematicStreamCodecs;
import net.errorcraft.itematic.util.ItematicCodecs;
import net.errorcraft.itematic.world.item.Items;
import net.errorcraft.itematic.world.item.behavior.behaviors.ItemHolderItemBehavior;
import net.errorcraft.itematic.world.item.component.ItemDamageRules;
import net.errorcraft.itematic.world.item.equipment.Glider;
import net.errorcraft.itematic.world.item.holder.rule.ItemHolderRules;
import net.errorcraft.itematic.world.item.use.duration.UseDuration;
import net.errorcraft.itematic.world.item.weapon.melee.SmashingWeapon;
import net.errorcraft.itematic.world.item.weapon.melee.WeaponAttackDamage;
import net.errorcraft.itematic.world.item.weapon.shooter.ChargingSounds;
import net.errorcraft.itematic.world.item.weapon.shooter.method.methods.ChargeableShooterMethod;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemUseAnimation;
import org.apache.commons.lang3.math.Fraction;

import java.util.function.UnaryOperator;

public class ItematicDataComponents {
    public static final DataComponentType<UseDuration> USE_DURATION = register(
        "use_duration",
        builder -> builder.persistent(UseDuration.CODEC)
            .networkSynchronized(UseDuration.STREAM_CODEC)
            .cacheEncoding()
    );
    public static final DataComponentType<ItemUseAnimation> USE_ANIMATION = register(
        "use_animation",
        builder -> builder.persistent(ItemUseAnimation.CODEC)
            .networkSynchronized(ItemUseAnimation.STREAM_CODEC)
            .cacheEncoding()
    );
    public static final DataComponentType<HolderSet<Item>> SHOOTER_AMMUNITION = register(
        "shooter_ammunition",
        builder -> builder.persistent(Items.LIST_CODEC)
            .networkSynchronized(Items.LIST_STREAM_CODEC)
            .cacheEncoding()
    );
    public static final DataComponentType<HolderSet<Item>> SHOOTER_HELD_AMMUNITION = register(
        "shooter_held_ammunition",
        builder -> builder.persistent(Items.LIST_CODEC)
            .networkSynchronized(Items.LIST_STREAM_CODEC)
            .cacheEncoding()
    );
    public static final DataComponentType<Double> ATTACK_SPEED_MULTIPLIER = register(
        "attack_speed_multiplier",
        builder -> builder.persistent(ItematicCodecs.NON_NEGATIVE_DOUBLE)
            .networkSynchronized(ByteBufCodecs.DOUBLE)
            .cacheEncoding()
    );
    public static final DataComponentType<WeaponAttackDamage> WEAPON_ATTACK_DAMAGE = register(
        "weapon_attack_damage",
        builder -> builder.persistent(WeaponAttackDamage.CODEC)
            .networkSynchronized(WeaponAttackDamage.STREAM_CODEC)
            .cacheEncoding()
    );
    public static final DataComponentType<Identifier> ITEM_BAR_STYLE = register(
        "item_bar_style",
        builder -> builder.persistent(Identifier.CODEC)
            .networkSynchronized(Identifier.STREAM_CODEC)
            .cacheEncoding()
    );
    public static final DataComponentType<Fraction> ITEM_HOLDER_CAPACITY = register(
        "item_holder_capacity",
        builder -> builder.persistent(ItemHolderItemBehavior.CAPACITY_CODEC)
            .networkSynchronized(ItematicStreamCodecs.FRACTION)
            .cacheEncoding()
    );
    public static final DataComponentType<ItemHolderRules> ITEM_HOLDER_RULES = register(
        "item_holder_rules",
        builder -> builder.persistent(ItemHolderRules.CODEC)
            .networkSynchronized(ItemHolderRules.STREAM_CODEC)
            .cacheEncoding()
    );
    public static final DataComponentType<ItemDamageRules> SHOOTER_DAMAGE_RULES = register(
        "shooter_damage_rules",
        builder -> builder.persistent(ItemDamageRules.CODEC)
            .networkSynchronized(ItemDamageRules.STREAM_CODEC)
            .cacheEncoding()
    );
    public static final DataComponentType<Float> SHOOTER_DEFAULT_CHARGE_TIME = register(
        "shooter_default_charge_time",
        builder -> builder.persistent(ItematicCodecs.NON_NEGATIVE_FLOAT)
            .networkSynchronized(ByteBufCodecs.FLOAT)
            .cacheEncoding()
    );
    public static final DataComponentType<CrossbowItem.ChargingSounds> SHOOTER_DEFAULT_CHARGING_SOUNDS = register(
        "shooter_default_charging_sounds",
        builder -> builder.persistent(CrossbowItem.ChargingSounds.CODEC)
            .networkSynchronized(ChargingSounds.STREAM_CODEC)
            .cacheEncoding()
    );
    public static final DataComponentType<ChargeableShooterMethod.ChargedPowerRules> SHOOTER_CHARGED_POWER_RULES = register(
        "shooter_charged_power_rules",
        builder -> builder.persistent(ChargeableShooterMethod.ChargedPowerRules.CODEC)
            .networkSynchronized(ChargeableShooterMethod.ChargedPowerRules.STREAM_CODEC)
    );
    public static final DataComponentType<Holder<SoundEvent>> SHOOTER_SHOOT_SOUND = register(
        "shooter_shoot_sound",
        builder -> builder.persistent(SoundEvent.CODEC)
            .networkSynchronized(SoundEvent.STREAM_CODEC)
    );
    public static final DataComponentType<Glider> GLIDER = register(
        "glider",
        builder -> builder.persistent(Glider.CODEC)
            .networkSynchronized(Glider.STREAM_CODEC)
            .cacheEncoding()
    );
    public static final DataComponentType<SmashingWeapon> SMASHING_WEAPON = register(
        "smashing_weapon",
        builder -> builder.persistent(SmashingWeapon.CODEC)
            .networkSynchronized(SmashingWeapon.STREAM_CODEC)
            .cacheEncoding()
    );

    private ItematicDataComponents() {}

    public static void init() {}

    private static <T> DataComponentType<T> register(String id, UnaryOperator<DataComponentType.Builder<T>> builder) {
        return Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            id,
            builder.apply(DataComponentType.builder()).build()
        );
    }
}
