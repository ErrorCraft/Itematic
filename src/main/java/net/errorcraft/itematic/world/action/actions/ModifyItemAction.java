package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.loot.context.ItemStackTargetUtil;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.item.ItemStacks;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
import net.minecraft.world.level.storage.loot.functions.SequenceFunction;

import java.util.stream.Stream;

public record ModifyItemAction(LootContext.ItemStackTarget stack, LootItemFunction itemModifier) implements Action<ModifyItemAction> {
    public static final MapCodec<ModifyItemAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        ItemStackTargetUtil.CODEC.optionalFieldOf("stack", LootContext.ItemStackTarget.TOOL).forGetter(ModifyItemAction::stack),
        LootItemFunctions.ROOT_CODEC.fieldOf("item_modifier").forGetter(ModifyItemAction::itemModifier)
    ).apply(instance, ModifyItemAction::new));

    public static ModifyItemAction of(LootContext.ItemStackTarget stack, LootItemFunction.Builder itemModifier) {
        return new ModifyItemAction(stack, itemModifier.build());
    }

    public static ModifyItemAction of(LootContext.ItemStackTarget stack, LootItemFunction.Builder... itemModifiers) {
        SequenceFunction itemModifier = SequenceFunction.of(
            Stream.of(itemModifiers)
                .map(LootItemFunction.Builder::build)
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
        ItemStack stack = context.get(this.stack.contextParam());
        if (ItemStacks.isNullOrEmpty(stack)) {
            return false;
        }

        LootContext lootContext = context.lootContext();
        if (lootContext == null) {
            return false;
        }

        lootContext.pushVisitedElement(LootContext.createVisitedEntry(this.itemModifier));
        ItemStack resultStack = this.itemModifier.apply(stack, lootContext);
        if (resultStack != stack) {
            context.exchangeStack(resultStack);
        }

        return true;
    }
}
