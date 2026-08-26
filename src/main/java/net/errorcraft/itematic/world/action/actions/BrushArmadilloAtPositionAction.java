package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.errorcraft.itematic.world.item.ItemStacks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.animal.armadillo.Armadillo;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import java.util.List;

public record BrushArmadilloAtPositionAction(PositionTarget position) implements Action<BrushArmadilloAtPositionAction> {
    public static final MapCodec<BrushArmadilloAtPositionAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        PositionTarget.CODEC.fieldOf("position").forGetter(BrushArmadilloAtPositionAction::position)
    ).apply(instance, BrushArmadilloAtPositionAction::new));

    public static BrushArmadilloAtPositionAction of(PositionTarget position) {
        return new BrushArmadilloAtPositionAction(position);
    }

    @Override
    public ActionType<BrushArmadilloAtPositionAction> type() {
        return ActionType.BRUSH_ARMADILLO_AT_POSITION;
    }

    @Override
    public boolean execute(ActionContext context) {
        BlockPos pos = context.get(this.position.contextParam(), BlockPos::containing);
        if (pos == null) {
            return false;
        }

        List<Armadillo> armadillos = context.level().getEntitiesOfClass(
            Armadillo.class,
            new AABB(pos),
            EntitySelector.NO_SPECTATORS
        );
        if (armadillos.isEmpty()) {
            return false;
        }

        Entity interactingEntity = context.get(LootContextParams.THIS_ENTITY);
        ItemStack usedStack = context.getOrDefault(
            LootContextParams.TOOL,
            ItemStacks::fromItemInstance,
            ItemStack.EMPTY
        );
        for (Armadillo armadillo : armadillos) {
            if (armadillo.brushOffScute(interactingEntity, usedStack)) {
                return true;
            }
        }

        return false;
    }
}
