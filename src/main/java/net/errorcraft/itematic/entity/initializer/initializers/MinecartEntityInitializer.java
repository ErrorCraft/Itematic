package net.errorcraft.itematic.entity.initializer.initializers;

import net.errorcraft.itematic.entity.initializer.EntityInitializer;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.text.Text;
import net.minecraft.world.World;

public record MinecartEntityInitializer<T extends AbstractMinecartEntity>(EntityType<T> type) implements EntityInitializer<T> {
    @Override
    public T create(ActionContext context, SpawnReason reason) {
        World world = context.world();
        T entity = this.type.create(world, reason);
        if (entity == null) {
            return null;
        }

        if (AbstractMinecartEntity.areMinecartImprovementsEnabled(world)) {
            for (Entity otherEntity : world.getOtherEntities(null, entity.getBoundingBox())) {
                if (otherEntity instanceof AbstractMinecartEntity) {
                    return null;
                }
            }
        }

        Text customName = context.getOrDefault(LootContextParameters.TOOL, ItemStack.EMPTY)
            .get(DataComponentTypes.CUSTOM_NAME);
        if (customName != null) {
            entity.setCustomName(customName);
        }

        return entity;
    }
}
