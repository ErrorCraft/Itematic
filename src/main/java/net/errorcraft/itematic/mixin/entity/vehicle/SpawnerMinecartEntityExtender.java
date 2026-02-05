package net.errorcraft.itematic.mixin.entity.vehicle;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.SpawnerMinecartEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SpawnerMinecartEntity.class)
public abstract class SpawnerMinecartEntityExtender extends AbstractMinecartEntity {
    protected SpawnerMinecartEntityExtender(EntityType<?> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(
        method = "getPickBlockStack",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack"
        )
    )
    private ItemStack newItemStackForMinecartUseCreateStack(ItemConvertible item) {
        return this.getWorld().itematic$createStack(ItemKeys.MINECART);
    }
}
