package net.errorcraft.itematic.mixin.entity.passive;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.StriderEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(StriderEntity.class)
public abstract class StriderEntityExtender extends AnimalEntity {
    protected StriderEntityExtender(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(
        method = "interactMob",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isOfForSaddleUseRegistryKey(ItemStack instance, Item item) {
        return instance.isOf(ItemKeys.SADDLE);
    }

    @Redirect(
        method = "dropInventory",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/passive/StriderEntity;dropItem(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/entity/ItemEntity;"
        )
    )
    private ItemEntity dropItemForSaddleUseRegistryKey(StriderEntity instance, ItemConvertible itemConvertible) {
        return this.dropItem(ItemKeys.SADDLE);
    }
}
