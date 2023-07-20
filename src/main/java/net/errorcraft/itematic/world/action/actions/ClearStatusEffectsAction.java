package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.minecraft.entity.LivingEntity;

public record ClearStatusEffectsAction() implements Action {
    public static final ClearStatusEffectsAction INSTANCE = new ClearStatusEffectsAction();
    public static final Codec<ClearStatusEffectsAction> CODEC = Codec.unit(INSTANCE);

    @Override
    public ActionType<?> type() {
        return ActionTypes.CLEAR_STATUS_EFFECTS;
    }

    @Override
    public boolean execute(ActionContext context) {
        if (context.target().isEmpty()) {
            return false;
        }
        if (context.target().get() instanceof LivingEntity target) {
            target.clearStatusEffects();
            return true;
        }
        return false;
    }
}
