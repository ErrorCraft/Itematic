package net.errorcraft.itematic.mixin.entity.passive;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.WanderingTraderEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

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
    private ItemStack initGoalsNewItemStackUseRegistryEntry(ItemConvertible item) {
        return new ItemStack(this.getWorld().getItem(ItemKeys.POTION));
    }
}
