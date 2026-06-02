package net.errorcraft.itematic.util.context;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Hand;
import net.minecraft.util.context.ContextParameter;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class ItematicContextParameters {
    public static final ContextParameter<Direction> SIDE = ContextParameter.of("side");
    public static final ContextParameter<Vec3d> INTERACTED_POSITION = ContextParameter.of("interacted_position");
    public static final ContextParameter<EquipmentSlot> EQUIPMENT_SLOT = ContextParameter.of("equipment_slot");
    public static final ContextParameter<Hand> HAND = ContextParameter.of("hand");
    public static final ContextParameter<Entity> TARGET_ENTITY = ContextParameter.of("target_entity");
    public static final ContextParameter<Entity> SPAWNED_ENTITY = ContextParameter.of("spawned_entity");
    public static final ContextParameter<Vec3d> SPAWNED_POSITION = ContextParameter.of("spawned_position");

    private ItematicContextParameters() {}

    public static void init() {}
}
