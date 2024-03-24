package net.errorcraft.itematic.mixin.entity.passive;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.mixin.entity.mob.MobEntityExtender;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.StriderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(StriderEntity.class)
public abstract class StriderEntityExtender extends MobEntityExtender {
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
        return instance.itematic$isOf(ItemKeys.SADDLE);
    }

    @Redirect(
        method = "dropInventory",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/passive/StriderEntity;dropItem(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/entity/ItemEntity;"
        )
    )
    private ItemEntity dropItemForSaddleUseRegistryKey(StriderEntity instance, ItemConvertible itemConvertible) {
        return this.itematic$dropItem(ItemKeys.SADDLE);
    }

    @Redirect(
        method = "getControllingPassenger",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/player/PlayerEntity;isHolding(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isHoldingForWarpedFungusOnAStickUseRegistryKeyCheck(PlayerEntity instance, Item item) {
        return instance.itematic$isHolding(ItemKeys.WARPED_FUNGUS_ON_A_STICK);
    }

    @Redirect(
        method = "initialize",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForWarpedFungusOnAStickUseCreateStack(ItemConvertible item, @Local(argsOnly = true) ServerWorldAccess world) {
        return world.itematic$createStack(ItemKeys.WARPED_FUNGUS_ON_A_STICK);
    }

    @Override
    protected @Nullable RegistryKey<Item> pickBlockKey() {
        return ItemKeys.STRIDER_SPAWN_EGG;
    }
}
