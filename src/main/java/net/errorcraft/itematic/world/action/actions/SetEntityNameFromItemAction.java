package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.text.Text;

public record SetEntityNameFromItemAction(LootContext.EntityTarget entity) implements Action<SetEntityNameFromItemAction> {
    public static final MapCodec<SetEntityNameFromItemAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        LootContext.EntityTarget.CODEC.fieldOf("entity").forGetter(SetEntityNameFromItemAction::entity)
    ).apply(instance, SetEntityNameFromItemAction::new));

    public static SetEntityNameFromItemAction of(LootContext.EntityTarget entity) {
        return new SetEntityNameFromItemAction(entity);
    }

    @Override
    public ActionType<SetEntityNameFromItemAction> type() {
        return ActionTypes.SET_ENTITY_NAME_FROM_ITEM;
    }

    @Override
    public boolean execute(ActionContext context) {
        ItemStack stack = context.get(LootContextParameters.TOOL);
        if (stack == null || stack.isEmpty()) {
            return false;
        }

        Text customName = stack.get(DataComponentTypes.CUSTOM_NAME);
        if (customName == null) {
            return false;
        }

        Entity entity = context.get(this.entity.getParameter());
        if (entity instanceof LivingEntity livingEntity) {
            return trySetName(livingEntity, customName);
        }

        return false;
    }

    private static boolean trySetName(LivingEntity target, Text name) {
        if (!target.getType().isSaveable() || !target.isAlive()) {
            return false;
        }

        target.setCustomName(name);
        if (target instanceof MobEntity mobTarget) {
            mobTarget.setPersistent();
        }

        return true;
    }
}
