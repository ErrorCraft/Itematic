package net.errorcraft.itematic.mixin.entity.mob;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.WitchEntity;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WitchEntity.class)
public abstract class WitchEntityExtender extends RaiderEntity {
    protected WitchEntityExtender(EntityType<? extends RaiderEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(
        method = "tickMovement",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean tickMovementIsOfUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.isOf(ItemKeys.POTION);
    }

    @Redirect(
        method = "tickMovement",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack"
        )
    )
    private ItemStack tickMovementNewItemStackUseRegistryEntry(ItemConvertible item) {
        return new ItemStack(this.getWorld().getItem(ItemKeys.POTION));
    }

    @Redirect(
        method = "attack",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack"
        )
    )
    private ItemStack attackNewItemStackUseRegistryEntry(ItemConvertible item) {
        return new ItemStack(this.getWorld().getItem(ItemKeys.SPLASH_POTION));
    }
}
