package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.minecraft.loot.LootDataType;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.util.Identifier;

import java.util.Optional;

public record ModifyItemAction(Identifier itemModifier) implements Action {
    public static final Codec<ModifyItemAction> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Identifier.CODEC.fieldOf("item_modifier").forGetter(ModifyItemAction::itemModifier)
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
        Optional<LootFunction> itemModifier = context.world().getServer().getLootManager().getElementOptional(LootDataType.ITEM_MODIFIERS, this.itemModifier);
        if (itemModifier.isEmpty()) {
            return false;
        }
        LootContext lootContext = context.lootContext();
        lootContext.markActive(LootContext.itemModifier(itemModifier.get()));
        itemModifier.get().apply(context.stack(), lootContext);
        return true;
    }
}
