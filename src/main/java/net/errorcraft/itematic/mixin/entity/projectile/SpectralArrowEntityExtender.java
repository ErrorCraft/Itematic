package net.errorcraft.itematic.mixin.entity.projectile;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.projectile.arrow.SpectralArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SpectralArrow.class)
public abstract class SpectralArrowEntityExtender extends AbstractArrow {
    protected SpectralArrowEntityExtender(EntityType<? extends AbstractArrow> entityType, Level world) {
        super(entityType, world);
    }

    @Redirect(
        method = "getDefaultPickupItem",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForSpectralArrowUseCreateStack(ItemLike item) {
        return this.level().itematic$createStack(ItemKeys.SPECTRAL_ARROW);
    }
}
