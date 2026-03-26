package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.function.LootFunctionTypes;

public record ModifyItemAction(LootFunction itemModifier) implements Action<ModifyItemAction> {
    public static final MapCodec<ModifyItemAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        LootFunctionTypes.CODEC.fieldOf("item_modifier").forGetter(ModifyItemAction::itemModifier)
    ).apply(instance, ModifyItemAction::new));

    @Override
    public ActionType<ModifyItemAction> type() {
        return ActionTypes.MODIFY_ITEM;
    }

    @Override
    public boolean execute(ActionContext context) {
        ItemStack stack = context.get(LootContextParameters.TOOL);
        if (stack == null || stack.isEmpty()) {
            return false;
        }

        LootContext lootContext = context.lootContext();
        lootContext.markActive(LootContext.itemModifier(this.itemModifier));
        ItemStack resultStack = this.itemModifier.apply(stack, lootContext);
        if (resultStack != stack) {
            context.exchangeStack(resultStack);
        }

        return true;
    }
}
