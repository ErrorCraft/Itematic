package net.errorcraft.itematic.mixin.entity.passive;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.WanderingTraderEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(WanderingTraderEntity.class)
public abstract class WanderingTraderEntityExtender extends MerchantEntity {
    public WanderingTraderEntityExtender(EntityType<? extends MerchantEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(
        method = "initGoals",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack",
            ordinal = 0
        )
    )
    private ItemStack newItemStackForPotionUseRegistryEntry(ItemConvertible item) {
        return this.getWorld().itematic$createStack(ItemKeys.POTION);
    }

    @Redirect(
        method = "initGoals",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;POTION:Lnet/minecraft/item/Item;"
            )
        )
    )
    private ItemStack newItemStackForMilkBucketUseRegistryEntry(ItemConvertible item) {
        return this.getWorld().itematic$createStack(ItemKeys.MILK_BUCKET);
    }

    @Redirect(
        method = "getDrinkSound",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isOfForMilkBucketUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.MILK_BUCKET);
    }
}
