package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.mixin.block.dispenser.ShearsDispenserBehaviorAccessor;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.NewActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public record ShearAtPositionAction(PositionTarget position) implements Action<ShearAtPositionAction> {
    public static final MapCodec<ShearAtPositionAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        PositionTarget.CODEC.fieldOf("position").forGetter(ShearAtPositionAction::position)
    ).apply(instance, ShearAtPositionAction::new));

    public static ShearAtPositionAction of(PositionTarget position) {
        return new ShearAtPositionAction(position);
    }

    @Override
    public ActionType<ShearAtPositionAction> type() {
        return ActionTypes.SHEAR_AT_POSITION;
    }

    @Override
    public boolean execute(NewActionContext context) {
        BlockPos pos = context.getBlockPos(this.position.parameter());
        if (pos == null) {
            return false;
        }

        ServerWorld world = context.world();
        return ShearsDispenserBehaviorAccessor.tryShearBlock(world, pos)
            || ShearsDispenserBehaviorAccessor.tryShearEntity(world, pos, context.getOrDefault(LootContextParameters.TOOL, ItemStack.EMPTY));
    }
}
