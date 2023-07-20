package net.errorcraft.itematic.world.action.context.parameter;

import net.minecraft.util.StringIdentifiable;

public enum ActionContextParameter implements StringIdentifiable {
    THIS("this"),
    TARGET("target");

    @SuppressWarnings("deprecation")
    public static final Codec<ActionContextParameter> CODEC = StringIdentifiable.createCodec(ActionContextParameter::values);

    private final String name;

    ActionContextParameter(String name) {
        this.name = name;
    }

    @Override
    public String asString() {
        return this.name;
    }
}
