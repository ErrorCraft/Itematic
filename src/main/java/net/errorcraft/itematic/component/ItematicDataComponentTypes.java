package net.errorcraft.itematic.component;

import net.errorcraft.itematic.component.type.*;
import net.errorcraft.itematic.item.component.components.ItemHolderItemComponent;
import net.errorcraft.itematic.item.holder.rule.ItemHolderRules;
import net.errorcraft.itematic.item.shooter.ChargingSoundsUtil;
import net.errorcraft.itematic.item.shooter.method.methods.ChargeableShooterMethod;
import net.errorcraft.itematic.mixin.component.DataComponentTypesAccessor;
import net.errorcraft.itematic.network.codec.PacketCodecUtil;
import net.errorcraft.itematic.serialization.ItematicCodecs;
import net.minecraft.component.ComponentType;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.consume.UseAction;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.math.Fraction;

public class ItematicDataComponentTypes {
    public static final ComponentType<UseDurationDataComponent> USE_DURATION = DataComponentTypesAccessor.register("use_duration", builder -> builder.codec(UseDurationDataComponent.CODEC).packetCodec(UseDurationDataComponent.PACKET_CODEC).cache());
    public static final ComponentType<UseAction> USE_ANIMATION = DataComponentTypesAccessor.register("use_animation", builder -> builder.codec(UseAction.CODEC).packetCodec(UseAction.PACKET_CODEC).cache());
    public static final ComponentType<ItemListDataComponent> SHOOTER_AMMUNITION = DataComponentTypesAccessor.register("shooter_ammunition", builder -> builder.codec(ItemListDataComponent.CODEC).packetCodec(ItemListDataComponent.PACKET_CODEC).cache());
    public static final ComponentType<ItemListDataComponent> SHOOTER_HELD_AMMUNITION = DataComponentTypesAccessor.register("shooter_held_ammunition", builder -> builder.codec(ItemListDataComponent.CODEC).packetCodec(ItemListDataComponent.PACKET_CODEC).cache());
    public static final ComponentType<Double> ATTACK_SPEED_MULTIPLIER = DataComponentTypesAccessor.register("attack_speed_multiplier", builder -> builder.codec(ItematicCodecs.NON_NEGATIVE_DOUBLE).packetCodec(PacketCodecs.DOUBLE).cache());
    public static final ComponentType<WeaponAttackDamageDataComponent> WEAPON_ATTACK_DAMAGE = DataComponentTypesAccessor.register("weapon_attack_damage", builder -> builder.codec(WeaponAttackDamageDataComponent.CODEC).packetCodec(WeaponAttackDamageDataComponent.PACKET_CODEC).cache());
    public static final ComponentType<Identifier> ITEM_BAR_STYLE = DataComponentTypesAccessor.register("item_bar_style", builder -> builder.codec(Identifier.CODEC).packetCodec(Identifier.PACKET_CODEC).cache());
    public static final ComponentType<Fraction> ITEM_HOLDER_CAPACITY = DataComponentTypesAccessor.register("item_holder_capacity", builder -> builder.codec(ItemHolderItemComponent.CAPACITY_CODEC).packetCodec(PacketCodecUtil.FRACTION).cache());
    public static final ComponentType<ItemHolderRules> ITEM_HOLDER_RULES = DataComponentTypesAccessor.register("item_holder_rules", builder -> builder.codec(ItemHolderRules.CODEC).packetCodec(ItemHolderRules.PACKET_CODEC).cache());
    public static final ComponentType<ItemDamageRulesDataComponent> SHOOTER_DAMAGE_RULES = DataComponentTypesAccessor.register("shooter_damage_rules", builder -> builder.codec(ItemDamageRulesDataComponent.CODEC).packetCodec(ItemDamageRulesDataComponent.PACKET_CODEC).cache());
    public static final ComponentType<Float> SHOOTER_DEFAULT_CHARGE_TIME = DataComponentTypesAccessor.register("shooter_default_charge_time", builder -> builder.codec(ItematicCodecs.NON_NEGATIVE_FLOAT).packetCodec(PacketCodecs.FLOAT).cache());
    public static final ComponentType<CrossbowItem.LoadingSounds> SHOOTER_DEFAULT_CHARGING_SOUNDS = DataComponentTypesAccessor.register("shooter_default_charging_sounds", builder -> builder.codec(CrossbowItem.LoadingSounds.CODEC).packetCodec(ChargingSoundsUtil.PACKET_CODEC).cache());
    public static final ComponentType<ChargeableShooterMethod.ChargedPowerRules> SHOOTER_CHARGED_POWER_RULES = DataComponentTypesAccessor.register("shooter_charged_power_rules", builder -> builder.codec(ChargeableShooterMethod.ChargedPowerRules.CODEC).packetCodec(ChargeableShooterMethod.ChargedPowerRules.PACKET_CODEC));
    public static final ComponentType<RegistryEntry<SoundEvent>> SHOOTER_SHOOT_SOUND = DataComponentTypesAccessor.register("shooter_shoot_sound", builder -> builder.codec(SoundEvent.ENTRY_CODEC).packetCodec(SoundEvent.ENTRY_PACKET_CODEC));
    public static final ComponentType<GliderDataComponent> GLIDER = DataComponentTypesAccessor.register("glider", builder -> builder.codec(GliderDataComponent.CODEC).packetCodec(GliderDataComponent.PACKET_CODEC).cache());

    private ItematicDataComponentTypes() {}

    public static void init() {}
}
