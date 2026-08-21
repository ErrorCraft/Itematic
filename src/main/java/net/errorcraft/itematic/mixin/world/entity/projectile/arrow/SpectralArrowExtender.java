package net.errorcraft.itematic.mixin.world.entity.projectile.arrow;

import net.errorcraft.itematic.references.ItemIds;
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
public abstract class SpectralArrowExtender extends AbstractArrow {
    protected SpectralArrowExtender(EntityType<? extends AbstractArrow> type, Level level) {
        super(type, level);
    }

    @Redirect(
        method = "getDefaultPickupItem",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForSpectralArrowUseCreateStack(ItemLike item) {
        return this.level().itematic$createStack(ItemIds.SPECTRAL_ARROW);
    }
}
