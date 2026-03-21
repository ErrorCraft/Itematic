package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.item.ItemStackUtil;
import net.errorcraft.itematic.util.context.ItematicContextParameters;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.NewActionContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.util.Hand;

public class OpenBookFromItemAction implements Action<OpenBookFromItemAction> {
    public static final OpenBookFromItemAction INSTANCE = new OpenBookFromItemAction();
    public static final MapCodec<OpenBookFromItemAction> CODEC = MapCodec.unit(INSTANCE);

    private OpenBookFromItemAction() {}

    @Override
    public ActionType<OpenBookFromItemAction> type() {
        return ActionTypes.OPEN_BOOK_FROM_ITEM;
    }

    @Override
    public boolean execute(NewActionContext context) {
        Entity entity = context.get(LootContextParameters.THIS_ENTITY);
        if (!(entity instanceof PlayerEntity player)) {
            return false;
        }

        ItemStack stack = context.get(LootContextParameters.TOOL);
        if (ItemStackUtil.isNullOrEmpty(stack)) {
            return false;
        }

        Hand hand = context.get(ItematicContextParameters.HAND);
        if (hand == null) {
            return false;
        }

        player.useBook(stack, hand);
        return true;
    }
}
