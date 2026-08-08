package net.errorcraft.itematic.world.entity.initializer.initializers;

import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.entity.initializer.EntityInitializer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public record MinecartEntityInitializer<T extends AbstractMinecart>(EntityType<T> type) implements EntityInitializer<T> {
    @Override
    public T create(ActionContext context, EntitySpawnReason reason) {
        Level level = context.world();
        T entity = this.type.create(level, reason);
        if (entity == null) {
            return null;
        }

        if (AbstractMinecart.useExperimentalMovement(level)) {
            for (Entity otherEntity : level.getEntities(null, entity.getBoundingBox())) {
                if (otherEntity instanceof AbstractMinecart) {
                    return null;
                }
            }
        }

        Component customName = context.getOrDefault(LootContextParams.TOOL, ItemStack.EMPTY)
            .get(DataComponents.CUSTOM_NAME);
        if (customName != null) {
            entity.setCustomName(customName);
        }

        return entity;
    }
}
