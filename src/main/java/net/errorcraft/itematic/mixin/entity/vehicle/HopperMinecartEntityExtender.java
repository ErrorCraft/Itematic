package net.errorcraft.itematic.mixin.entity.vehicle;

import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.minecart.MinecartHopper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MinecartHopper.class)
public abstract class HopperMinecartEntityExtender extends VehicleEntityExtender {
    public HopperMinecartEntityExtender(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Redirect(
        method = "getPickResult",
        at = @At(
            value = "NEW",
            target = "net/minecraft/world/item/ItemStack"
        )
    )
    private ItemStack newItemStackForHopperMinecartUseCreateStack(ItemLike item) {
        return this.level().itematic$createStack(ItemIds.HOPPER_MINECART);
    }

    @Override
    protected ResourceKey<Item> asItemKey() {
        return ItemIds.HOPPER_MINECART;
    }
}
