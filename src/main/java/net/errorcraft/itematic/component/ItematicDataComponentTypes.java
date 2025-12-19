package net.errorcraft.itematic.component;

import net.errorcraft.itematic.component.type.ImmuneToDamageComponent;
import net.errorcraft.itematic.component.type.ItemListDataComponent;
import net.errorcraft.itematic.component.type.UseDurationDataComponent;
import net.errorcraft.itematic.component.type.WeaponAttackDamageDataComponent;
import net.errorcraft.itematic.item.component.components.ItemHolderItemComponent;
import net.errorcraft.itematic.mixin.component.DataComponentTypesAccessor;
import net.errorcraft.itematic.network.codec.PacketCodecUtil;
import net.errorcraft.itematic.serialization.ItematicCodecs;
import net.errorcraft.itematic.util.UseActionUtil;
import net.minecraft.component.DataComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.UseAction;
import org.apache.commons.lang3.math.Fraction;

public class ItematicDataComponentTypes {
    public static final DataComponentType<ImmuneToDamageComponent> IMMUNE_TO_DAMAGE = DataComponentTypesAccessor.register("immune_to_damage", builder -> builder.codec(ImmuneToDamageComponent.CODEC).packetCodec(ImmuneToDamageComponent.PACKET_CODEC));
    public static final DataComponentType<UseDurationDataComponent> USE_DURATION = DataComponentTypesAccessor.register("use_duration", builder -> builder.codec(UseDurationDataComponent.CODEC).packetCodec(UseDurationDataComponent.PACKET_CODEC));
    public static final DataComponentType<UseAction> USE_ANIMATION = DataComponentTypesAccessor.register("use_animation", builder -> builder.codec(UseActionUtil.CODEC).packetCodec(UseActionUtil.PACKET_CODEC));
    public static final DataComponentType<ItemListDataComponent> SHOOTER_AMMUNITION = DataComponentTypesAccessor.register("shooter_ammunition", builder -> builder.codec(ItemListDataComponent.CODEC).packetCodec(ItemListDataComponent.PACKET_CODEC));
    public static final DataComponentType<ItemListDataComponent> SHOOTER_HELD_AMMUNITION = DataComponentTypesAccessor.register("shooter_held_ammunition", builder -> builder.codec(ItemListDataComponent.CODEC).packetCodec(ItemListDataComponent.PACKET_CODEC));
    public static final DataComponentType<Double> ATTACK_SPEED_MULTIPLIER = DataComponentTypesAccessor.register("attack_speed_multiplier", builder -> builder.codec(ItematicCodecs.NON_NEGATIVE_DOUBLE).packetCodec(PacketCodecs.DOUBLE));
    public static final DataComponentType<WeaponAttackDamageDataComponent> WEAPON_ATTACK_DAMAGE = DataComponentTypesAccessor.register("weapon_attack_damage", builder -> builder.codec(WeaponAttackDamageDataComponent.CODEC).packetCodec(WeaponAttackDamageDataComponent.PACKET_CODEC));
    public static final DataComponentType<Fraction> ITEM_HOLDER_CAPACITY = DataComponentTypesAccessor.register("item_holder_capacity", builder -> builder.codec(ItemHolderItemComponent.CAPACITY_CODEC).packetCodec(PacketCodecUtil.FRACTION));

    public static void init() {}
}
