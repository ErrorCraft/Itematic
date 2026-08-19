package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.errorcraft.itematic.world.item.ItemStacks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.animal.equine.AbstractChestedHorse;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;

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
        return ActionType.EQUIP_HORSE_WITH_CHEST_AT_POSITION;
    }

    @Override
    public boolean execute(ActionContext context) {
        ItemStack stack = context.get(LootContextParams.TOOL);
        if (ItemStacks.isNullOrEmpty(stack)) {
            return false;
        }

        BlockPos pos = context.get(this.position.contextParam(), BlockPos::containing);
        if (pos == null) {
            return false;
        }

        List<AbstractChestedHorse> donkeys = context.level().getEntitiesOfClass(
            AbstractChestedHorse.class,
            new AABB(pos),
            donkey -> donkey.isAlive() && !donkey.hasChest()
        );
        for (AbstractChestedHorse donkey : donkeys) {
            if (donkey.isTamed() && donkey.getSlot(AbstractHorse.CHEST_SLOT_OFFSET).set(stack.copy())) {
                return true;
            }
        }

        return false;
    }
}
