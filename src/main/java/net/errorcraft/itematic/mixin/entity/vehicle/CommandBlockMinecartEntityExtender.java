package net.errorcraft.itematic.mixin.entity.vehicle;

import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.entity.vehicle.minecart.MinecartCommandBlock;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MinecartCommandBlock.class)
public abstract class CommandBlockMinecartEntityExtender extends AbstractMinecart {
    protected CommandBlockMinecartEntityExtender(EntityType<?> entityType, Level world) {
        super(entityType, world);
    }

    @Redirect(
        method = "getPickResult",
        at = @At(
            value = "NEW",
            target = "net/minecraft/world/item/ItemStack"
        )
    )
    private ItemStack newItemStackForCommandBlockMinecartUseCreateStack(ItemLike item) {
        return this.level().itematic$createStack(ItemIds.COMMAND_BLOCK_MINECART);
    }
}
