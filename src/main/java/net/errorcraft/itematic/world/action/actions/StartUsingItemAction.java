package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUsage;
import net.minecraft.util.Hand;

import java.util.Optional;

public record StartUsingItemAction() implements Action {
    public static final StartUsingItemAction INSTANCE = new StartUsingItemAction();
    public static final Codec<StartUsingItemAction> CODEC = Codec.unit(INSTANCE);

    @Override
    public ActionType<?> type() {
        return ActionTypes.START_USING_ITEM;
    }

    @Override
    public boolean execute(ActionContext context) {
        Optional<Entity> entity = context.entity(ActionContextParameter.THIS);
        if (entity.isEmpty()) {
            return false;
        }
        if (!(entity.get() instanceof PlayerEntity player)) {
            return false;
        }
        if (player.isUsingItem()) {
            return false;
        }
        Hand hand = context.hand();
        if (hand == null) {
            return false;
        }
        return ItemUsage.consumeHeldItem(context.world(), player, hand).getResult().isAccepted();
    }
}
