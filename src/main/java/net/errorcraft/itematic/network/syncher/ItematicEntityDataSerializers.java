package net.errorcraft.itematic.network.syncher;

import net.errorcraft.itematic.world.item.component.GliderFuel;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityDataRegistry;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.Identifier;

public class ItematicEntityDataSerializers {
    public static final EntityDataSerializer<GliderFuel.Computed> GLIDER_FUEL = EntityDataSerializer.forValueType(GliderFuel.Computed.STREAM_CODEC);

    private ItematicEntityDataSerializers() {}

    public static void bootstrap() {
        FabricEntityDataRegistry.register(Identifier.fromNamespaceAndPath("itematic", "glider_fuel"), GLIDER_FUEL);
    }
}
