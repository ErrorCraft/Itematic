package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUsage;
import net.minecraft.util.Hand;
import net.minecraft.util.dynamic.Codecs;

import java.util.Optional;

public record StartUsingItemAction(Optional<Integer> ticks) implements Action<StartUsingItemAction> {
    public static final Codec<StartUsingItemAction> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codecs.createStrictOptionalFieldCodec(Codec.INT, "ticks").forGetter(StartUsingItemAction::ticks)
    ).apply(instance, StartUsingItemAction::new));

    @Override
    public ActionType<StartUsingItemAction> type() {
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
        this.ticks.ifPresentOrElse(
            ticks -> player.itematic$startUsingHand(hand, ticks),
            () -> ItemUsage.consumeHeldItem(context.world(), player, hand)
        );
        return true;
    }

    public static StartUsingItemAction of(int duration) {
        return new StartUsingItemAction(Optional.of(duration));
    }

    public static StartUsingItemAction indefinitely() {
        return new StartUsingItemAction(Optional.empty());
    }
}
