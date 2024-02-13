package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.ItemStackConsumer;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.world.action.actions.ModifySignAction;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;

public record DyeItemComponent(DyeColor color) implements ItemComponent<DyeItemComponent> {
    public static final Codec<DyeItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        DyeColor.CODEC.fieldOf("color").forGetter(DyeItemComponent::color)
    ).apply(instance, DyeItemComponent::new));

    @Override
    public ItemComponentType<DyeItemComponent> type() {
        return ItemComponentTypes.DYE;
    }

    @Override
    public Codec<DyeItemComponent> codec() {
        return CODEC;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context, ItemStackConsumer resultStackConsumer) {
        if (!(context.getWorld() instanceof ServerWorld world)) {
            return ActionResult.SUCCESS;
        }
        ActionContext actionContext = ActionContext.builder(world, context.getStack(), resultStackConsumer)
            .entityPosition(ActionContextParameter.THIS, context.getPlayer())
            .position(ActionContextParameter.TARGET, context.getBlockPos())
            .build();
        ModifySignAction action = ModifySignAction.dye(ActionContextParameter.TARGET, this.color);
        if (action.execute(actionContext)) {
            context.getStack().decrementUnlessCreative(1, context.getPlayer());
            return ActionResult.CONSUME;
        }
        return ActionResult.PASS;
    }

    public static DyeItemComponent of(DyeColor color) {
        return new DyeItemComponent(color);
    }
}
