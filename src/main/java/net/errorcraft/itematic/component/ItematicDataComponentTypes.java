package net.errorcraft.itematic.component;

import net.errorcraft.itematic.component.type.ChargeablePullProgressComponent;
import net.errorcraft.itematic.component.type.ImmuneToDamageComponent;
import net.errorcraft.itematic.mixin.component.DataComponentTypesAccessor;
import net.minecraft.component.DataComponentType;

public class ItematicDataComponentTypes {
    public static final DataComponentType<ChargeablePullProgressComponent> CHARGEABLE_PULL_PROGRESS = DataComponentTypesAccessor.register("chargeable_pull_progress", builder -> builder.packetCodec(ChargeablePullProgressComponent.PACKET_CODEC));
    public static final DataComponentType<ImmuneToDamageComponent> IMMUNE_TO_DAMAGE = DataComponentTypesAccessor.register("immune_to_damage", builder -> builder.codec(ImmuneToDamageComponent.CODEC).packetCodec(ImmuneToDamageComponent.PACKET_CODEC));

    public static void init() {}
}
