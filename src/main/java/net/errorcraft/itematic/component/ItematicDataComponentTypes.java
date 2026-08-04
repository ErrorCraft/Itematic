package net.errorcraft.itematic.component;

import net.errorcraft.itematic.component.type.*;
import net.errorcraft.itematic.item.component.components.ItemHolderItemComponent;
import net.errorcraft.itematic.item.holder.rule.ItemHolderRules;
import net.errorcraft.itematic.item.shooter.ChargingSoundsUtil;
import net.errorcraft.itematic.item.shooter.method.methods.ChargeableShooterMethod;
import net.errorcraft.itematic.mixin.component.DataComponentTypesAccessor;
import net.errorcraft.itematic.network.codec.PacketCodecUtil;
import net.errorcraft.itematic.serialization.ItematicCodecs;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemUseAnimation;
import org.apache.commons.lang3.math.Fraction;

public class ItematicDataComponentTypes {
    public static final DataComponentType<UseDurationDataComponent> USE_DURATION = DataComponentTypesAccessor.register("use_duration", builder -> builder.persistent(UseDurationDataComponent.CODEC).networkSynchronized(UseDurationDataComponent.PACKET_CODEC).cacheEncoding());
    public static final DataComponentType<ItemUseAnimation> USE_ANIMATION = DataComponentTypesAccessor.register("use_animation", builder -> builder.persistent(ItemUseAnimation.CODEC).networkSynchronized(ItemUseAnimation.STREAM_CODEC).cacheEncoding());
    public static final DataComponentType<ItemListDataComponent> SHOOTER_AMMUNITION = DataComponentTypesAccessor.register("shooter_ammunition", builder -> builder.persistent(ItemListDataComponent.CODEC).networkSynchronized(ItemListDataComponent.PACKET_CODEC).cacheEncoding());
    public static final DataComponentType<ItemListDataComponent> SHOOTER_HELD_AMMUNITION = DataComponentTypesAccessor.register("shooter_held_ammunition", builder -> builder.persistent(ItemListDataComponent.CODEC).networkSynchronized(ItemListDataComponent.PACKET_CODEC).cacheEncoding());
    public static final DataComponentType<Double> ATTACK_SPEED_MULTIPLIER = DataComponentTypesAccessor.register("attack_speed_multiplier", builder -> builder.persistent(ItematicCodecs.NON_NEGATIVE_DOUBLE).networkSynchronized(ByteBufCodecs.DOUBLE).cacheEncoding());
    public static final DataComponentType<WeaponAttackDamageDataComponent> WEAPON_ATTACK_DAMAGE = DataComponentTypesAccessor.register("weapon_attack_damage", builder -> builder.persistent(WeaponAttackDamageDataComponent.CODEC).networkSynchronized(WeaponAttackDamageDataComponent.PACKET_CODEC).cacheEncoding());
    public static final DataComponentType<Identifier> ITEM_BAR_STYLE = DataComponentTypesAccessor.register("item_bar_style", builder -> builder.persistent(Identifier.CODEC).networkSynchronized(Identifier.STREAM_CODEC).cacheEncoding());
    public static final DataComponentType<Fraction> ITEM_HOLDER_CAPACITY = DataComponentTypesAccessor.register("item_holder_capacity", builder -> builder.persistent(ItemHolderItemComponent.CAPACITY_CODEC).networkSynchronized(PacketCodecUtil.FRACTION).cacheEncoding());
    public static final DataComponentType<ItemHolderRules> ITEM_HOLDER_RULES = DataComponentTypesAccessor.register("item_holder_rules", builder -> builder.persistent(ItemHolderRules.CODEC).networkSynchronized(ItemHolderRules.PACKET_CODEC).cacheEncoding());
    public static final DataComponentType<ItemDamageRulesDataComponent> SHOOTER_DAMAGE_RULES = DataComponentTypesAccessor.register("shooter_damage_rules", builder -> builder.persistent(ItemDamageRulesDataComponent.CODEC).networkSynchronized(ItemDamageRulesDataComponent.PACKET_CODEC).cacheEncoding());
    public static final DataComponentType<Float> SHOOTER_DEFAULT_CHARGE_TIME = DataComponentTypesAccessor.register("shooter_default_charge_time", builder -> builder.persistent(ItematicCodecs.NON_NEGATIVE_FLOAT).networkSynchronized(ByteBufCodecs.FLOAT).cacheEncoding());
    public static final DataComponentType<CrossbowItem.ChargingSounds> SHOOTER_DEFAULT_CHARGING_SOUNDS = DataComponentTypesAccessor.register("shooter_default_charging_sounds", builder -> builder.persistent(CrossbowItem.ChargingSounds.CODEC).networkSynchronized(ChargingSoundsUtil.PACKET_CODEC).cacheEncoding());
    public static final DataComponentType<ChargeableShooterMethod.ChargedPowerRules> SHOOTER_CHARGED_POWER_RULES = DataComponentTypesAccessor.register("shooter_charged_power_rules", builder -> builder.persistent(ChargeableShooterMethod.ChargedPowerRules.CODEC).networkSynchronized(ChargeableShooterMethod.ChargedPowerRules.PACKET_CODEC));
    public static final DataComponentType<Holder<SoundEvent>> SHOOTER_SHOOT_SOUND = DataComponentTypesAccessor.register("shooter_shoot_sound", builder -> builder.persistent(SoundEvent.CODEC).networkSynchronized(SoundEvent.STREAM_CODEC));
    public static final DataComponentType<GliderDataComponent> GLIDER = DataComponentTypesAccessor.register("glider", builder -> builder.persistent(GliderDataComponent.CODEC).networkSynchronized(GliderDataComponent.PACKET_CODEC).cacheEncoding());
    public static final DataComponentType<SmashingWeaponDataComponent> SMASHING_WEAPON = DataComponentTypesAccessor.register("smashing_weapon", builder -> builder.persistent(SmashingWeaponDataComponent.CODEC).networkSynchronized(SmashingWeaponDataComponent.PACKET_CODEC).cacheEncoding());

    private ItematicDataComponentTypes() {}

    public static void init() {}
}
