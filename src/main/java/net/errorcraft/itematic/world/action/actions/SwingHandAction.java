package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;

public record SwingHandAction() implements Action {
    public static final SwingHandAction INSTANCE = new SwingHandAction();
    public static final Codec<SwingHandAction> CODEC = Codec.unit(INSTANCE);

    @Override
    public ActionType<?> type() {
        return ActionTypes.SWING_HAND;
    }

    @Override
    public boolean execute(ActionContext context) {
        if (!(context.entity(ActionContextParameter.THIS).orElse(null) instanceof LivingEntity entity)) {
            return false;
        }
        Hand hand = context.hand();
        if (hand == null) {
            return false;
        }
        entity.swingHand(hand, true);
        return true;
    }
}
