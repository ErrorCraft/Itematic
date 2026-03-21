package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.ItemResult;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.util.context.ItematicContextParameters;
import net.errorcraft.itematic.world.action.actions.ModifySignAction;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.errorcraft.itematic.world.action.context.NewActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.DyeColor;

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
    public ItemResult useOnBlock(ItemUsageContext context, ItemStackExchanger stackExchanger) {
        if (!(context.getWorld() instanceof ServerWorld world)) {
            return ItemResult.SUCCEED;
        }

        PlayerEntity player = context.getPlayer();
        ItemStack stack = context.getStack();
        NewActionContext actionContext = NewActionContext.builder(world)
            .possibleStackExchanger(player, stack)
            .addOptional(LootContextParameters.THIS_ENTITY, player)
            .addOptional(LootContextParameters.ORIGIN, player, Entity::getPos)
            .add(ItematicContextParameters.INTERACTED_POSITION, context.getBlockPos().toCenterPos())
            .add(LootContextParameters.TOOL, stack)
            .build();
        ModifySignAction action = ModifySignAction.dye(PositionTarget.INTERACTED_POSITION, this.color);
        if (action.execute(actionContext)) {
            context.getStack().decrementUnlessCreative(1, context.getPlayer());
            return ItemResult.CONSUME;
        }

        return ItemResult.PASS;
    }
}
