package net.errorcraft.itematic.mixin.entity.passive;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.equine.AbstractChestedHorse;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractChestedHorse.class)
public abstract class AbstractDonkeyEntityExtender extends AbstractHorse {
    protected AbstractDonkeyEntityExtender(EntityType<? extends AbstractHorse> entityType, Level world) {
        super(entityType, world);
    }

    @Redirect(
        method = "dropEquipment",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/animal/equine/AbstractChestedHorse;spawnAtLocation(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/entity/item/ItemEntity;"
        )
    )
    private ItemEntity dropItemForChestUseRegistryKey(AbstractChestedHorse instance, ServerLevel world, ItemLike itemConvertible) {
        return this.itematic$dropItem(world, ItemKeys.CHEST);
    }

    @Redirect(
        method = "mobInteract",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    private boolean isOfForChestUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.CHEST);
    }

    @Mixin(targets = "net/minecraft/world/entity/animal/equine/AbstractChestedHorse$1")
    public static class StackReferenceExtender {
        @Shadow
        @Final
        AbstractChestedHorse field_27867;

        @Redirect(
            method = "get",
            at = @At(
                value = "NEW",
                target = "net/minecraft/world/item/ItemStack"
            )
        )
        private ItemStack newItemStackForChestUseCreateStack(ItemLike item) {
            return this.field_27867.level().itematic$createStack(ItemKeys.CHEST);
        }

        @Redirect(
            method = "set",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
            )
        )
        private boolean isOfForChestUseRegistryKeyCheck(ItemStack instance, Item item) {
            return instance.itematic$isOf(ItemKeys.CHEST);
        }
    }
}
