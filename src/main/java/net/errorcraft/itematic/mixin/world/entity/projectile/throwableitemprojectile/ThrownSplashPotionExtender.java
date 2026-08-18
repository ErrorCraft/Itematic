package net.errorcraft.itematic.mixin.world.entity.projectile.throwableitemprojectile;

import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownSplashPotion;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ThrownSplashPotion.class)
public abstract class ThrownSplashPotionExtender extends ThrowableItemProjectileExtender {
    protected ThrownSplashPotionExtender(EntityType<? extends ThrowableProjectile> type, Level level) {
        super(type, level);
    }

    @Override
    protected ResourceKey<Item> getDefaultItemId() {
        return ItemIds.SPLASH_POTION;
    }
}
