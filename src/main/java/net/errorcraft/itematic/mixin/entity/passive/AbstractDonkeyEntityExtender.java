package net.errorcraft.itematic.mixin.entity.passive;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.passive.AbstractDonkeyEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractDonkeyEntity.class)
public abstract class AbstractDonkeyEntityExtender extends AbstractHorseEntity {
    protected AbstractDonkeyEntityExtender(EntityType<? extends AbstractHorseEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(
        method = "dropInventory",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/passive/AbstractDonkeyEntity;dropItem(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/entity/ItemEntity;"
        )
    )
    private ItemEntity dropItemForChestUseRegistryKey(AbstractDonkeyEntity instance, ServerWorld world, ItemConvertible itemConvertible) {
        return this.itematic$dropItem(world, ItemKeys.CHEST);
    }

    @Redirect(
        method = "interactMob",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isOfForChestUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.CHEST);
    }

    @Mixin(targets = "net/minecraft/entity/passive/AbstractDonkeyEntity$1")
    public static class StackReferenceExtender {
        @Shadow
        @Final
        AbstractDonkeyEntity field_27867;

        @Redirect(
            method = "get",
            at = @At(
                value = "NEW",
                target = "net/minecraft/item/ItemStack"
            )
        )
        private ItemStack newItemStackForChestUseCreateStack(ItemConvertible item) {
            return this.field_27867.getWorld().itematic$createStack(ItemKeys.CHEST);
        }

        @Redirect(
            method = "set",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
            )
        )
        private boolean isOfForChestUseRegistryKeyCheck(ItemStack instance, Item item) {
            return instance.itematic$isOf(ItemKeys.CHEST);
        }
    }
}
