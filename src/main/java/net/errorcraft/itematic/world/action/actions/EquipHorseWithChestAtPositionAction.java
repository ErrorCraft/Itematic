package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.ItemStackUtil;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.NewActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.entity.passive.AbstractDonkeyEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import java.util.List;

public record EquipHorseWithChestAtPositionAction(PositionTarget position) implements Action<EquipHorseWithChestAtPositionAction> {
    public static final MapCodec<EquipHorseWithChestAtPositionAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        PositionTarget.CODEC.fieldOf("position").forGetter(EquipHorseWithChestAtPositionAction::position)
    ).apply(instance, EquipHorseWithChestAtPositionAction::new));

    public static EquipHorseWithChestAtPositionAction of(PositionTarget position) {
        return new EquipHorseWithChestAtPositionAction(position);
    }

    @Override
    public ActionType<EquipHorseWithChestAtPositionAction> type() {
        return ActionTypes.EQUIP_HORSE_WITH_CHEST_AT_POSITION;
    }

    @Override
    public boolean execute(ActionContext context) {
        return false;
    }

    @Override
    public boolean execute(NewActionContext context) {
        ItemStack stack = context.get(LootContextParameters.TOOL);
        if (ItemStackUtil.isNullOrEmpty(stack)) {
            return false;
        }

        BlockPos pos = context.getBlockPos(this.position.parameter());
        if (pos == null) {
            return false;
        }

        List<AbstractDonkeyEntity> donkeys = context.world().getEntitiesByClass(
            AbstractDonkeyEntity.class,
            new Box(pos),
            donkey -> donkey.isAlive() && !donkey.hasChest()
        );
        for (AbstractDonkeyEntity donkey : donkeys) {
            if (donkey.isTame() && donkey.getStackReference(AbstractHorseEntity.field_30414).set(stack.copy())) {
                return true;
            }
        }

        return false;
    }
}
