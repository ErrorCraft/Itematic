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
            target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;",
            ordinal = 0
        )
    )
    private ItemStack newItemStackForEmeraldUseCreateStack(ItemConvertible item) {
        return this.getWorld().itematic$createStack(ItemKeys.EMERALD);
    }

    @Redirect(
        method = "initEquipment",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;",
            ordinal = 1
        )
    )
    private ItemStack newItemStackForEggUseCreateStack(ItemConvertible item) {
        return this.getWorld().itematic$createStack(ItemKeys.EGG);
    }

    @Redirect(
        method = "initEquipment",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "CONSTANT",
                args = "floatValue=0.4f"
            )
        )
    )
    private ItemStack newItemStackForRabbitFootUseCreateStack(ItemConvertible item) {
        return this.getWorld().itematic$createStack(ItemKeys.RABBIT_FOOT);
    }

    @Redirect(
        method = "initEquipment",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;",
            ordinal = 1
        ),
        slice = @Slice(
            from = @At(
                value = "CONSTANT",
                args = "floatValue=0.4f"
            )
        )
    )
    private ItemStack newItemStackForRabbitHideUseCreateStack(ItemConvertible item) {
        return this.getWorld().itematic$createStack(ItemKeys.RABBIT_HIDE);
    }

    @Redirect(
        method = "initEquipment",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "CONSTANT",
                args = "floatValue=0.6f"
            )
        )
    )
    private ItemStack newItemStackForWheatUseCreateStack(ItemConvertible item) {
        return this.getWorld().itematic$createStack(ItemKeys.WHEAT);
    }

    @Redirect(
        method = "initEquipment",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "CONSTANT",
                args = "floatValue=0.8f"
            )
        )
    )
    private ItemStack newItemStackForLeatherUseCreateStack(ItemConvertible item) {
        return this.getWorld().itematic$createStack(ItemKeys.LEATHER);
    }

    @Redirect(
        method = "initEquipment",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;",
            ordinal = 1
        ),
        slice = @Slice(
            from = @At(
                value = "CONSTANT",
                args = "floatValue=0.8f"
            )
        )
    )
    private ItemStack newItemStackForFeatherUseCreateStack(ItemConvertible item) {
        return this.getWorld().itematic$createStack(ItemKeys.FEATHER);
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
                target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;",
                ordinal = 0
            )
        )
        private ItemStack newItemStackForSweetBerriesUseCreateStack(ItemConvertible item) {
            return this.field_17975.getWorld().itematic$createStack(ItemKeys.SWEET_BERRIES);
        }

        @Redirect(
            method = "pickSweetBerries",
            at = @At(
                value = "NEW",
                target = "(Lnet/minecraft/item/ItemConvertible;I)Lnet/minecraft/item/ItemStack;",
                ordinal = 0
            ),
            slice = @Slice(
                from = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/passive/FoxEntity;equipStack(Lnet/minecraft/entity/EquipmentSlot;Lnet/minecraft/item/ItemStack;)V"
                )
            )
        )
        private ItemStack newItemStackForSweetBerriesUseCreateStack(ItemConvertible item, int count) {
            return this.field_17975.getWorld().itematic$createStack(ItemKeys.SWEET_BERRIES, count);
        }
    }
}
