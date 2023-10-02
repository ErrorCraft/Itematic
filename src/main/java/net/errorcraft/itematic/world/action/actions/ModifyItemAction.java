package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameters;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.function.LootFunctionTypes;

public record ModifyItemAction(LootFunction itemModifier, ActionContextParameters context) implements Action {
    public static final Codec<ModifyItemAction> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        LootFunctionTypes.CODEC.fieldOf("item_modifier").forGetter(ModifyItemAction::itemModifier),
        ActionContextParameters.CODEC.fieldOf("context").forGetter(ModifyItemAction::context)
    ).apply(instance, ModifyItemAction::new));

    @Override
    public ActionType<?> type() {
        return ActionTypes.MODIFY_ITEM;
    }

    @Override
    public boolean execute(ActionContext context) {
        if (context.stack().isEmpty()) {
            return false;
        }
        LootContext lootContext = context.createLootContext(this.context);
        lootContext.markActive(LootContext.itemModifier(this.itemModifier));
        this.itemModifier.apply(context.stack(), lootContext);
        return true;
    }
}
