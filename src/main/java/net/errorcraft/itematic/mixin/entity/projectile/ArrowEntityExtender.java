package net.errorcraft.itematic.mixin.entity.projectile;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ArrowEntity.class)
public abstract class ArrowEntityExtender extends PersistentProjectileEntity {
    protected ArrowEntityExtender(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(
        method = "asItemStack",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack",
            ordinal = 0
        )
    )
    private ItemStack newItemStackForArrowUseRegistryEntry(ItemConvertible item) {
        return this.getWorld().itematic$createStack(ItemKeys.ARROW);
    }

    @Redirect(
        method = "asItemStack",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack",
            ordinal = 1
        )
    )
    private ItemStack newItemStackForTippedArrowUseRegistryEntry(ItemConvertible item) {
        return this.getWorld().itematic$createStack(ItemKeys.TIPPED_ARROW);
    }

    @Redirect(
        method = "initFromStack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
            ordinal = 0
        )
    )
    private boolean isOfForTippedArrowUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.TIPPED_ARROW);
    }

    @Redirect(
        method = "initFromStack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
            ordinal = 1
        )
    )
    private boolean isOfForArrowUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.ARROW);
    }
}
