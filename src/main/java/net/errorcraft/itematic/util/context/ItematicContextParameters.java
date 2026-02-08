package net.errorcraft.itematic.util.context;

import net.minecraft.util.context.ContextParameter;
import net.minecraft.util.math.Direction;

public class ItematicContextParameters {
    public static final ContextParameter<Direction> SIDE = ContextParameter.of("side");

    private ItematicContextParameters() {}

    public static void init() {}
}
