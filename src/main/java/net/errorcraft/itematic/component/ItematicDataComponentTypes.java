package net.errorcraft.itematic.component;

import net.errorcraft.itematic.component.type.ImmuneToDamageComponent;
import net.errorcraft.itematic.component.type.UseDurationDataComponent;
import net.errorcraft.itematic.mixin.component.DataComponentTypesAccessor;
import net.minecraft.component.DataComponentType;

public class ItematicDataComponentTypes {
    public static final DataComponentType<ImmuneToDamageComponent> IMMUNE_TO_DAMAGE = DataComponentTypesAccessor.register("immune_to_damage", builder -> builder.codec(ImmuneToDamageComponent.CODEC).packetCodec(ImmuneToDamageComponent.PACKET_CODEC));
    public static final DataComponentType<UseDurationDataComponent> USE_DURATION = DataComponentTypesAccessor.register("use_duration", builder -> builder.codec(UseDurationDataComponent.CODEC).packetCodec(UseDurationDataComponent.PACKET_CODEC));

    public static void init() {}
}
