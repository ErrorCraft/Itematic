package net.errorcraft.itematic.mixin.entity.projectile;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.SpectralArrowEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SpectralArrowEntity.class)
public abstract class SpectralArrowEntityExtender extends PersistentProjectileEntity {
    protected SpectralArrowEntityExtender(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(
        method = "asItemStack",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack"
        )
    )
    private ItemStack asItemStackNewItemStackForSpectralArrowUseRegistryEntry(ItemConvertible item) {
        return new ItemStack(this.getWorld().itematic$getItem(ItemKeys.SPECTRAL_ARROW));
    }
}
