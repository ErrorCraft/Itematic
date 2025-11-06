package net.errorcraft.itematic.component;

import net.errorcraft.itematic.component.type.ImmuneToDamageComponent;
import net.errorcraft.itematic.mixin.component.DataComponentTypesAccessor;
import net.minecraft.component.DataComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.dynamic.Codecs;

public class ItematicDataComponentTypes {
    public static final DataComponentType<ImmuneToDamageComponent> IMMUNE_TO_DAMAGE = DataComponentTypesAccessor.register("immune_to_damage", builder -> builder.codec(ImmuneToDamageComponent.CODEC).packetCodec(ImmuneToDamageComponent.PACKET_CODEC));
    public static final DataComponentType<Integer> USE_DURATION = DataComponentTypesAccessor.register("use_duration", builder -> builder.codec(Codecs.POSITIVE_INT).packetCodec(PacketCodecs.VAR_INT));

    public static void init() {}
}
