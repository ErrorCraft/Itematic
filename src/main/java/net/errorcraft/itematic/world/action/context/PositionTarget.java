package net.errorcraft.itematic.world.action.context;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.util.context.ItematicContextParameters;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.context.ContextParameter;
import net.minecraft.util.math.Vec3d;

public enum PositionTarget implements StringIdentifiable {
    ORIGIN("origin", LootContextParameters.ORIGIN),
    INTERACTED_POSITION("interacted_position", ItematicContextParameters.INTERACTED_POSITION);

    public static final Codec<PositionTarget> CODEC = StringIdentifiable.createCodec(PositionTarget::values);

    private final String name;
    private final ContextParameter<Vec3d> parameter;

    PositionTarget(String name, ContextParameter<Vec3d> parameter) {
        this.name = name;
        this.parameter = parameter;
    }

    @Override
    public String asString() {
        return this.name;
    }

    public ContextParameter<Vec3d> parameter() {
        return this.parameter;
    }
}
