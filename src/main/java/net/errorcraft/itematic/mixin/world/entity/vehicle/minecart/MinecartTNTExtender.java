package net.errorcraft.itematic.mixin.world.entity.vehicle.minecart;

import net.errorcraft.itematic.mixin.world.entity.vehicle.VehicleEntityExtender;
import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.minecart.MinecartTNT;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MinecartTNT.class)
public abstract class MinecartTNTExtender extends VehicleEntityExtender {
    public MinecartTNTExtender(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Redirect(
        method = "getPickResult",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForTntMinecartUseCreateStack(ItemLike item) {
        return this.level().itematic$createStack(ItemIds.TNT_MINECART);
    }

    @Override
    protected ResourceKey<Item> asItemId() {
        return ItemIds.TNT_MINECART;
    }
}
