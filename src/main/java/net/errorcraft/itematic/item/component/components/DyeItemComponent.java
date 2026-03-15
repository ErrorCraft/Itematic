package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.ItemResult;
import net.errorcraft.itematic.item.ItemStackConsumer;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.world.action.actions.ModifySignAction;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.item.ItemUsageContext;
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
    public ItemResult useOnBlock(ItemUsageContext context, ItemStackConsumer resultStackConsumer) {
        if (!(context.getWorld() instanceof ServerWorld world)) {
            return ItemResult.SUCCEED;
        }

        ActionContext actionContext = ActionContext.builder(world, context.getStack(), resultStackConsumer)
            .entityPosition(ActionContextParameter.THIS, context.getPlayer())
            .position(ActionContextParameter.TARGET, context.getBlockPos())
            .build();
        ModifySignAction action = ModifySignAction.dye(PositionTarget.INTERACTED_POSITION, this.color);
        if (action.execute(actionContext)) {
            context.getStack().decrementUnlessCreative(1, context.getPlayer());
            return ItemResult.CONSUME;
        }

        return ItemResult.PASS;
    }
}
