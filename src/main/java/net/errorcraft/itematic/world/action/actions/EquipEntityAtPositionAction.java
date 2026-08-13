package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.errorcraft.itematic.world.item.ItemStacks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;

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
        ItemStack equipment = context.get(LootContextParams.TOOL);
        if (ItemStacks.isNullOrEmpty(equipment)) {
            return false;
        }

        BlockPos pos = context.get(this.position.contextParam(), BlockPos::containing);
        if (pos == null) {
            return false;
        }

        List<LivingEntity> entities = context.world().getEntitiesOfClass(
            LivingEntity.class,
            new AABB(pos),
            entity -> entity.canEquipWithDispenser(equipment)
        );
        if (entities.isEmpty()) {
            return false;
        }

        equip(entities.getFirst(), equipment.copyWithCount(1));
        return true;
    }

    private static void equip(LivingEntity target, ItemStack equipment) {
        EquipmentSlot slot = target.getEquipmentSlotForItem(equipment);
        target.setItemSlot(slot, equipment);
        if (target instanceof Mob mobTarget) {
            mobTarget.setDropChance(slot, 2.0f);
            mobTarget.setPersistenceRequired();
        }
    }
}
