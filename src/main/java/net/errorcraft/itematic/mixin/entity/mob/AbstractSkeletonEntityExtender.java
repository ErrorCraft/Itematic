package net.errorcraft.itematic.mixin.entity.mob;

import net.errorcraft.itematic.entity.projectile.ItematicProjectileUtil;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractSkeletonEntity.class)
public class AbstractSkeletonEntityExtender extends HostileEntity {
    protected AbstractSkeletonEntityExtender(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(
        method = "initEquipment",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack"
        )
    )
    private ItemStack newItemStackForBowUseRegistryEntry(ItemConvertible item) {
        return this.getWorld().itematic$createStack(ItemKeys.BOW);
    }

    @Redirect(
        method = { "updateAttackType", "shootAt" },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/projectile/ProjectileUtil;getHandPossiblyHolding(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/Item;)Lnet/minecraft/util/Hand;"
        )
    )
    private Hand getHandPossiblyHoldingForBowUseRegistryKey(LivingEntity entity, Item item) {
        return ItematicProjectileUtil.getHandPossiblyHolding(entity, ItemKeys.BOW);
    }

    @Redirect(
        method = "updateAttackType",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isOfForBowUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.BOW);
    }
}
