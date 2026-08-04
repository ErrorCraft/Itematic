package net.errorcraft.itematic.util.context;

import net.minecraft.core.Direction;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.phys.Vec3;

public class ItematicContextParameters {
    public static final ContextKey<Direction> SIDE = ContextKey.vanilla("side");
    public static final ContextKey<Vec3> INTERACTED_POSITION = ContextKey.vanilla("interacted_position");
    public static final ContextKey<EquipmentSlot> EQUIPMENT_SLOT = ContextKey.vanilla("equipment_slot");
    public static final ContextKey<InteractionHand> HAND = ContextKey.vanilla("hand");
    public static final ContextKey<Entity> SPAWNED_ENTITY = ContextKey.vanilla("spawned_entity");
    public static final ContextKey<Vec3> SPAWNED_POSITION = ContextKey.vanilla("spawned_position");

    private ItematicContextParameters() {}

    public static void init() {}
}
