package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public record SetEntityNameFromItemAction(LootContext.EntityTarget entity) implements Action<SetEntityNameFromItemAction> {
    public static final MapCodec<SetEntityNameFromItemAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        LootContext.EntityTarget.CODEC.fieldOf("entity").forGetter(SetEntityNameFromItemAction::entity)
    ).apply(instance, SetEntityNameFromItemAction::new));

    public static SetEntityNameFromItemAction of(LootContext.EntityTarget entity) {
        return new SetEntityNameFromItemAction(entity);
    }

    @Override
    public ActionType<SetEntityNameFromItemAction> type() {
        return ActionType.SET_ENTITY_NAME_FROM_ITEM;
    }

    @Override
    public boolean execute(ActionContext context) {
        ItemStack stack = context.get(LootContextParams.TOOL);
        if (stack == null || stack.isEmpty()) {
            return false;
        }

        Component customName = stack.get(DataComponents.CUSTOM_NAME);
        if (customName == null) {
            return false;
        }

        Entity entity = context.get(this.entity.contextParam());
        if (entity instanceof LivingEntity livingEntity) {
            return trySetName(livingEntity, customName);
        }

        return false;
    }

    private static boolean trySetName(LivingEntity target, Component name) {
        if (!target.getType().canSerialize() || !target.isAlive()) {
            return false;
        }

        target.setCustomName(name);
        if (target instanceof Mob mobTarget) {
            mobTarget.setPersistenceRequired();
        }

        return true;
    }
}
