package net.errorcraft.itematic.world.action.context.parameter;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record ActionContextParameters(ActionContextParameter entity, ActionContextParameter position) {
    public static final Codec<ActionContextParameters> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ActionContextParameter.CODEC.fieldOf("entity").forGetter(ActionContextParameters::entity),
        ActionContextParameter.CODEC.fieldOf("position").forGetter(ActionContextParameters::position)
    ).apply(instance, ActionContextParameters::new));

    public static ActionContextParameters self() {
        return new ActionContextParameters(ActionContextParameter.THIS, ActionContextParameter.THIS);
    }

    public static ActionContextParameters of(ActionContextParameter entity, ActionContextParameter position) {
        return new ActionContextParameters(entity, position);
    }
}
