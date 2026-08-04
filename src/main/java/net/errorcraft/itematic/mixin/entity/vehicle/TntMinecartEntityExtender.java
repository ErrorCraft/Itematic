package net.errorcraft.itematic.mixin.entity.vehicle;

import net.errorcraft.itematic.item.ItemKeys;
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
public abstract class TntMinecartEntityExtender extends VehicleEntityExtender {
    public TntMinecartEntityExtender(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Redirect(
        method = "getPickResult",
        at = @At(
            value = "NEW",
            target = "net/minecraft/world/item/ItemStack"
        )
    )
    private ItemStack newItemStackForTntMinecartUseCreateStack(ItemLike item) {
        return this.level().itematic$createStack(ItemKeys.TNT_MINECART);
    }

    @Override
    protected ResourceKey<Item> asItemKey() {
        return ItemKeys.TNT_MINECART;
    }
}
