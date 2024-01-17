package net.errorcraft.itematic.mixin.entity.mob;

import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.components.ShooterItemComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.entity.mob.PillagerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PillagerEntity.class)
public abstract class PillagerEntityExtender extends IllagerEntity {
    protected PillagerEntityExtender(EntityType<? extends IllagerEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(
        method = "isRaidCaptain",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isOfForWhiteBannerUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.WHITE_BANNER);
    }

    @Redirect(
        method = "getState",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/mob/PillagerEntity;isHolding(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isHoldingForCrossbowUseRegistryKeyCheck(PillagerEntity instance, Item item) {
        return instance.itematic$isHolding(ItemKeys.CROSSBOW);
    }

    @Redirect(
        method = { "initEquipment", "addBonusForWave" },
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForCrossbowUseCreateStack(ItemConvertible item) {
        return this.getWorld().itematic$createStack(ItemKeys.CROSSBOW);
    }

    @Redirect(
        method = "enchantMainHandItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isOfForCrossbowUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.CROSSBOW);
    }

    @Override
    public boolean itematic$canUseShooter(ItemStack stack, ShooterItemComponent component) {
        return stack.itematic$isOf(ItemKeys.CROSSBOW);
    }
}
