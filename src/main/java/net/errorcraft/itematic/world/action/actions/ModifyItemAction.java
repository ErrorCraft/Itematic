package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.ItemStackUtil;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.ItemStackTarget;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.AndLootFunction;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.function.LootFunctionTypes;

import java.util.stream.Stream;

public record ModifyItemAction(ItemStackTarget stack, LootFunction itemModifier) implements Action<ModifyItemAction> {
    public static final MapCodec<ModifyItemAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        ItemStackTarget.CODEC.optionalFieldOf("stack", ItemStackTarget.TOOL).forGetter(ModifyItemAction::stack),
        LootFunctionTypes.CODEC.fieldOf("item_modifier").forGetter(ModifyItemAction::itemModifier)
    ).apply(instance, ModifyItemAction::new));

    public static ModifyItemAction of(ItemStackTarget stack, LootFunction.Builder itemModifier) {
        return new ModifyItemAction(stack, itemModifier.build());
    }

    public static ModifyItemAction of(ItemStackTarget stack, LootFunction.Builder... itemModifiers) {
        AndLootFunction itemModifier = AndLootFunction.create(
            Stream.of(itemModifiers)
                .map(LootFunction.Builder::build)
                .toList()
        );
        return new ModifyItemAction(stack, itemModifier);
    }

    @Override
    public ActionType<ModifyItemAction> type() {
        return ActionTypes.MODIFY_ITEM;
    }

    @Override
    public boolean execute(ActionContext context) {
        ItemStack stack = this.stack.get(context);
        if (ItemStackUtil.isNullOrEmpty(stack)) {
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
