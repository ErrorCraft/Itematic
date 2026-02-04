package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Box;

import java.util.List;

public record EquipEntityAtPositionAction(ActionContextParameter position) implements Action<EquipEntityAtPositionAction> {
    public static final MapCodec<EquipEntityAtPositionAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        ActionContextParameter.CODEC.fieldOf("position").forGetter(EquipEntityAtPositionAction::position)
    ).apply(instance, EquipEntityAtPositionAction::new));

    public static EquipEntityAtPositionAction of(ActionContextParameter position) {
        return new EquipEntityAtPositionAction(position);
    }

    @Override
    public ActionType<EquipEntityAtPositionAction> type() {
        return ActionTypes.EQUIP_ENTITY_AT_POSITION;
    }

    @Override
    public boolean execute(ActionContext context) {
        ItemStack equipment = context.stack();
        List<LivingEntity> entities = context.world().getEntitiesByClass(
            LivingEntity.class,
            new Box(context.blockPos(this.position)),
            entity -> entity.canEquipFromDispenser(equipment)
        );
        if (entities.isEmpty()) {
            return false;
        }

        LivingEntity target = entities.getFirst();
        EquipmentSlot equipmentSlot = target.getPreferredEquipmentSlot(equipment);
        ItemStack equippedStack = equipment.copyWithCount(1);
        target.equipStack(equipmentSlot, equippedStack);
        if (target instanceof MobEntity mobEntity) {
            mobEntity.setEquipmentDropChance(equipmentSlot, 2.0f);
            mobEntity.setPersistent();
        }

        return true;
    }
}
