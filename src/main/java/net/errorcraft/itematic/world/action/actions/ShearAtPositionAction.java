package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.mixin.core.dispenser.ShearsDispenseItemBehaviorAccessor;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.errorcraft.itematic.world.item.ItemStacks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public record ShearAtPositionAction(PositionTarget position) implements Action<ShearAtPositionAction> {
    public static final MapCodec<ShearAtPositionAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        PositionTarget.CODEC.fieldOf("position").forGetter(ShearAtPositionAction::position)
    ).apply(instance, ShearAtPositionAction::new));

    public static ShearAtPositionAction of(PositionTarget position) {
        return new ShearAtPositionAction(position);
    }

    @Override
    public ActionType<ShearAtPositionAction> type() {
        return ActionType.SHEAR_AT_POSITION;
    }

    @Override
    public boolean execute(ActionContext context) {
        if (!(context.level() instanceof ServerLevel level)) {
            return false;
        }

        BlockPos pos = context.get(this.position.contextParam(), BlockPos::containing);
        if (pos == null) {
            return false;
        }

        ItemStack tool = context.getOrDefault(LootContextParams.TOOL, ItemStacks::fromItemInstance, ItemStack.EMPTY);
        return ShearsDispenseItemBehaviorAccessor.tryShearBeehive(level, tool, pos)
            || ShearsDispenseItemBehaviorAccessor.tryShearEntity(level, pos, tool);
    }
}
