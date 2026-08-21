package net.errorcraft.itematic.world.action.context;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.util.context.ItematicContextKeys;
import net.minecraft.util.StringRepresentable;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.level.storage.loot.LootContextArg;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

public enum PositionTarget implements StringRepresentable, LootContextArg.SimpleGetter<Vec3> {
    ORIGIN("origin", LootContextParams.ORIGIN),
    INTERACTED("interacted", ItematicContextKeys.INTERACTED_POSITION),
    SPAWNED("spawned", ItematicContextKeys.SPAWNED_POSITION);

    public static final Codec<PositionTarget> CODEC = StringRepresentable.fromEnum(PositionTarget::values);

    private final String name;
    private final ContextKey<Vec3> parameter;

    PositionTarget(String name, ContextKey<Vec3> parameter) {
        this.name = name;
        this.parameter = parameter;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }

    @Override
    public ContextKey<? extends Vec3> contextParam() {
        return this.parameter;
    }
}
