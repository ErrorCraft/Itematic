package net.errorcraft.itematic.world.action;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.registry.ItematicRegistries;
import net.errorcraft.itematic.world.action.context.ActionContext;

public interface Action {
    Codec<Action> CODEC = ItematicRegistries.ACTION_TYPE.getCodec().dispatch("action", Action::type, ActionType::codec);

    ActionType<?> type();
    boolean execute(ActionContext context);
}
