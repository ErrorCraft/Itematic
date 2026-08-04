package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.ItemResult;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.util.context.ItematicContextParameters;
import net.errorcraft.itematic.world.action.actions.ModifySignAction;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public record DyeItemComponent(DyeColor color) implements ItemComponent<DyeItemComponent> {
    public static final Codec<DyeItemComponent> CODEC = DyeColor.CODEC.xmap(DyeItemComponent::new, DyeItemComponent::color);

    public static DyeItemComponent of(DyeColor color) {
        return new DyeItemComponent(color);
    }

    @Override
    public ItemComponentType<DyeItemComponent> type() {
        return ItemComponentTypes.DYE;
    }

    @Override
    public Codec<DyeItemComponent> codec() {
        return CODEC;
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
