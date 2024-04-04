package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.entity.passive.AbstractDonkeyEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import java.util.List;

public record EquipHorseWithChestAtPositionAction(ActionContextParameter position) implements Action<EquipHorseWithChestAtPositionAction> {
    public static final MapCodec<EquipHorseWithChestAtPositionAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        ActionContextParameter.CODEC.fieldOf("position").forGetter(EquipHorseWithChestAtPositionAction::position)
    ).apply(instance, EquipHorseWithChestAtPositionAction::new));

    public static EquipHorseWithChestAtPositionAction of(ActionContextParameter position) {
        return new EquipHorseWithChestAtPositionAction(position);
    }

    @Override
    public ActionType<EquipHorseWithChestAtPositionAction> type() {
        return ActionTypes.EQUIP_HORSE_WITH_CHEST_AT_POSITION;
    }

    @Override
    public boolean execute(ActionContext context) {
        BlockPos pos = context.blockPos(this.position);
        List<AbstractDonkeyEntity> donkeys = context.world().getEntitiesByClass(AbstractDonkeyEntity.class, new Box(pos), donkey -> donkey.isAlive() && !donkey.hasChest());
        for (AbstractDonkeyEntity donkey : donkeys) {
            if (donkey.isTame() && donkey.getStackReference(AbstractHorseEntity.field_30414).set(context.stack())) {
                return true;
            }
        }
        return false;
    }
}
