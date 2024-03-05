package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;

public record OpenBookFromItemAction() implements Action<OpenBookFromItemAction> {
    public static final OpenBookFromItemAction INSTANCE = new OpenBookFromItemAction();
    public static final Codec<OpenBookFromItemAction> CODEC = Codec.unit(INSTANCE);

    @Override
    public ActionType<OpenBookFromItemAction> type() {
        return ActionTypes.OPEN_BOOK_FROM_ITEM;
    }

    @Override
    public boolean execute(ActionContext context) {
        return context.player(ActionContextParameter.THIS)
            .map(player -> {
                ItemStack stack = context.stack();
                player.useBook(stack, context.hand());
                player.incrementStat(Stats.USED.itematic$getOrCreateStat(stack.getRegistryEntry()));
                return true;
            })
            .orElse(false);
    }
}
