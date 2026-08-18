package net.errorcraft.itematic.mixin.world.entity.projectile.hurtingprojectile;

import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.hurtingprojectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.hurtingprojectile.Fireball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Fireball.class)
public class FireballExtender extends AbstractHurtingProjectile {
    protected FireballExtender(EntityType<? extends AbstractHurtingProjectile> type, Level level) {
        super(type, level);
    }

    @Redirect(
        method = "getDefaultItem",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForFireChargeUseCreateStack(ItemLike item) {
        return this.level().itematic$createStack(ItemIds.FIRE_CHARGE);
    }
}
