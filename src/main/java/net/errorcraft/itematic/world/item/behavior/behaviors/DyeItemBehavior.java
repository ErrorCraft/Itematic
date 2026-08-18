package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.util.context.ItematicContextParameters;
import net.errorcraft.itematic.world.ItemResult;
import net.errorcraft.itematic.world.action.actions.ModifySignAction;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public record DyeItemBehavior(DyeColor color) implements ItemBehavior<DyeItemBehavior> {
    public static final Codec<DyeItemBehavior> CODEC = DyeColor.CODEC.xmap(DyeItemBehavior::new, DyeItemBehavior::color);

    public static DyeItemBehavior of(DyeColor color) {
        return new DyeItemBehavior(color);
    }

    @Override
    public ItemBehaviorType<DyeItemBehavior> type() {
        return ItemBehaviorType.DYE;
    }

    @Override
    public ItemResult useOnBlock(UseOnContext context, ItemStackExchanger stackExchanger) {
        if (!(context.getLevel() instanceof ServerLevel world)) {
            return ItemResult.SUCCEED;
        }

        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();
        ActionContext actionContext = ActionContext.builder(world)
            .possibleStackExchanger(player, stack)
            .addOptional(LootContextParams.THIS_ENTITY, player)
            .addOptional(LootContextParams.ORIGIN, player, Entity::position)
            .add(ItematicContextParameters.INTERACTED_POSITION, context.getClickedPos().getCenter())
            .add(LootContextParams.TOOL, stack)
            .build();
        ModifySignAction action = ModifySignAction.dye(PositionTarget.INTERACTED, this.color);
        if (action.execute(actionContext)) {
            context.getItemInHand().consume(1, context.getPlayer());
            return ItemResult.CONSUME;
        }

        return ItemResult.PASS;
    }
}
