package net.errorcraft.itematic.mixin.entity.mob;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CreeperEntity.class)
public class CreeperEntityExtender extends HostileEntity {
    protected CreeperEntityExtender(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(
        method = "interactMob",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean interactMobIsOfUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.isOf(ItemKeys.FIRE_CHARGE);
    }

    @Redirect(
        method = "dropEquipment",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/mob/CreeperEntity;dropItem(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/entity/ItemEntity;"
        )
    )
    private ItemEntity dropEquipmentDropItemUseRegistryKey(CreeperEntity instance, ItemConvertible itemConvertible) {
        return this.dropItem(ItemKeys.CREEPER_HEAD);
    }
}
