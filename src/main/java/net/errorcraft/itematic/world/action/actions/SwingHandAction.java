package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.util.context.ItematicContextParameters;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.loot.context.LootContext;
import net.minecraft.util.Hand;

public record SwingHandAction(LootContext.EntityTarget entity) implements Action<SwingHandAction> {
    public static final MapCodec<SwingHandAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        LootContext.EntityTarget.CODEC.fieldOf("entity").forGetter(SwingHandAction::entity)
    ).apply(instance, SwingHandAction::new));

    public static SwingHandAction of(LootContext.EntityTarget entity) {
        return new SwingHandAction(entity);
    }

    @Override
    public ActionType<SwingHandAction> type() {
        return ActionTypes.SWING_HAND;
    }

    @Override
    public boolean execute(ActionContext context) {
        Entity entity = context.get(this.entity.getParameter());
        if (!(entity instanceof LivingEntity target)) {
            return false;
        }

        Hand hand = context.get(ItematicContextParameters.HAND);
        if (hand == null) {
            return false;
        }

        target.swingHand(hand, true);
        return true;
    }
}
