package net.errorcraft.itematic.mixin.entity.projectile;

import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ThrownTrident.class)
public abstract class TridentEntityExtender extends AbstractArrow {
    protected TridentEntityExtender(EntityType<? extends AbstractArrow> entityType, Level world) {
        super(entityType, world);
    }

    @Redirect(
        method = "getDefaultPickupItem",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForTridentUseCreateStack(ItemLike item) {
        return this.level().itematic$createStack(ItemIds.TRIDENT);
    }
}
