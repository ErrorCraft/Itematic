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
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import java.util.List;

public record EquipEntityAtPositionAction(PositionTarget position) implements Action<EquipEntityAtPositionAction> {
    public static final MapCodec<EquipEntityAtPositionAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        PositionTarget.CODEC.fieldOf("position").forGetter(EquipEntityAtPositionAction::position)
    ).apply(instance, EquipEntityAtPositionAction::new));

    public static EquipEntityAtPositionAction of(PositionTarget position) {
        return new EquipEntityAtPositionAction(position);
    }

    @Override
    public ActionType<EquipEntityAtPositionAction> type() {
        return ActionTypes.EQUIP_ENTITY_AT_POSITION;
    }

    @Override
    public boolean execute(ActionContext context) {
        return false;
    }

    @Override
    public boolean execute(NewActionContext context) {
        ItemStack equipment = context.get(LootContextParameters.TOOL);
        if (ItemStackUtil.isNullOrEmpty(equipment)) {
            return false;
        }

        BlockPos pos = context.getBlockPos(this.position.parameter());
        if (pos == null) {
            return false;
        }

        List<LivingEntity> entities = context.world().getEntitiesByClass(
            LivingEntity.class,
            new Box(pos),
            entity -> entity.canEquipFromDispenser(equipment)
        );
        if (entities.isEmpty()) {
            return false;
        }

        equip(entities.getFirst(), equipment.copyWithCount(1));
        return true;
    }

    private static void equip(LivingEntity target, ItemStack equipment) {
        EquipmentSlot slot = target.getPreferredEquipmentSlot(equipment);
        target.equipStack(slot, equipment);
        if (target instanceof MobEntity mobTarget) {
            mobTarget.setEquipmentDropChance(slot, 2.0f);
            mobTarget.setPersistent();
        }
    }
}
