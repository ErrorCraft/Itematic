package net.errorcraft.itematic.mixin.entity.passive;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(FoxEntity.class)
public abstract class FoxEntityExtender extends AnimalEntity {
    protected FoxEntityExtender(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(
        method = "initEquipment",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack",
            ordinal = 1
        )
    )
    private ItemStack initEquipmentNewItemStackForEggUseRegistryEntry(ItemConvertible item) {
        return new ItemStack(this.getWorld().getItem(ItemKeys.EGG));
    }

    @Redirect(
        method = "initEquipment",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "CONSTANT",
                args = "floatValue=0.6f"
            )
        )
    )
    private ItemStack initEquipmentNewItemStackForWheatUseRegistryEntry(ItemConvertible item) {
        return new ItemStack(this.getWorld().getItem(ItemKeys.WHEAT));
    }

    @Redirect(
        method = "initEquipment",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "CONSTANT",
                args = "floatValue=0.8f"
            )
        )
    )
    private ItemStack initEquipmentNewItemStackForLeatherUseRegistryEntry(ItemConvertible item) {
        return new ItemStack(this.getWorld().getItem(ItemKeys.LEATHER));
    }

    @Redirect(
        method = "initEquipment",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack",
            ordinal = 1
        ),
        slice = @Slice(
            from = @At(
                value = "CONSTANT",
                args = "floatValue=0.8f"
            )
        )
    )
    private ItemStack initEquipmentNewItemStackForFeatherUseRegistryEntry(ItemConvertible item) {
        return new ItemStack(this.getWorld().getItem(ItemKeys.FEATHER));
    }

    @Mixin(FoxEntity.EatBerriesGoal.class)
    public static class EatBerriesGoalExtender {
        @Shadow
        @Final
        FoxEntity field_17975;

        @Redirect(
            method = "pickSweetBerries",
            at = @At(
                value = "NEW",
                target = "net/minecraft/item/ItemStack",
                ordinal = 0
            )
        )
        private ItemStack pickSweetBerriesNewItemStackUseRegistryEntry(ItemConvertible item) {
            return new ItemStack(this.field_17975.getWorld().getItem(ItemKeys.SWEET_BERRIES));
        }

        @Redirect(
            method = "pickSweetBerries",
            at = @At(
                value = "NEW",
                target = "net/minecraft/item/ItemStack",
                ordinal = 1
            )
        )
        private ItemStack pickSweetBerriesNewItemStackUseRegistryEntry(ItemConvertible item, int count) {
            return new ItemStack(this.field_17975.getWorld().getItem(ItemKeys.SWEET_BERRIES), count);
        }
    }
}
