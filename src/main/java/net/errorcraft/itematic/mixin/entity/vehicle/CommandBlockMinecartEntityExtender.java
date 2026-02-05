package net.errorcraft.itematic.mixin.entity.vehicle;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.CommandBlockMinecartEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CommandBlockMinecartEntity.class)
public abstract class CommandBlockMinecartEntityExtender extends AbstractMinecartEntity {
    protected CommandBlockMinecartEntityExtender(EntityType<?> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(
        method = "getPickBlockStack",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack"
        )
    )
    private ItemStack newItemStackForCommandBlockMinecartUseCreateStack(ItemConvertible item) {
        return this.getWorld().itematic$createStack(ItemKeys.COMMAND_BLOCK_MINECART);
    }
}
