package net.errorcraft.itematic.mixin.entity.vehicle;

import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.entity.vehicle.minecart.Minecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Minecart.class)
public abstract class MinecartEntityExtender extends AbstractMinecart {
    protected MinecartEntityExtender(EntityType<?> entityType, Level world) {
        super(entityType, world);
    }

    @Redirect(
        method = "getPickResult",
        at = @At(
            value = "NEW",
            target = "net/minecraft/world/item/ItemStack"
        )
    )
    private ItemStack newItemStackForMinecartUseCreateStack(ItemLike item) {
        return this.level().itematic$createStack(ItemIds.MINECART);
    }
}
