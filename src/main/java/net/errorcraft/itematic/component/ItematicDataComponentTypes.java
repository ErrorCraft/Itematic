package net.errorcraft.itematic.component;

import net.errorcraft.itematic.component.type.ImmuneToDamageComponent;
import net.errorcraft.itematic.component.type.ItemListDataComponent;
import net.errorcraft.itematic.component.type.UseDurationDataComponent;
import net.errorcraft.itematic.mixin.component.DataComponentTypesAccessor;
import net.errorcraft.itematic.util.UseActionUtil;
import net.minecraft.component.DataComponentType;
import net.minecraft.util.UseAction;

public class ItematicDataComponentTypes {
    public static final DataComponentType<ImmuneToDamageComponent> IMMUNE_TO_DAMAGE = DataComponentTypesAccessor.register("immune_to_damage", builder -> builder.codec(ImmuneToDamageComponent.CODEC).packetCodec(ImmuneToDamageComponent.PACKET_CODEC));
    public static final DataComponentType<UseDurationDataComponent> USE_DURATION = DataComponentTypesAccessor.register("use_duration", builder -> builder.codec(UseDurationDataComponent.CODEC).packetCodec(UseDurationDataComponent.PACKET_CODEC));
    public static final DataComponentType<UseAction> USE_ANIMATION = DataComponentTypesAccessor.register("use_animation", builder -> builder.codec(UseActionUtil.CODEC).packetCodec(UseActionUtil.PACKET_CODEC));
    public static final DataComponentType<ItemListDataComponent> SHOOTER_AMMUNITION = DataComponentTypesAccessor.register("shooter_ammunition", builder -> builder.codec(ItemListDataComponent.CODEC).packetCodec(ItemListDataComponent.PACKET_CODEC));
    public static final DataComponentType<ItemListDataComponent> SHOOTER_HELD_AMMUNITION = DataComponentTypesAccessor.register("shooter_held_ammunition", builder -> builder.codec(ItemListDataComponent.CODEC).packetCodec(ItemListDataComponent.PACKET_CODEC));

    public static void init() {}
}
