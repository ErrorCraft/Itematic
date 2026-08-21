package net.errorcraft.itematic.mixin.world.entity.projectile.throwableitemprojectile;

import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownLingeringPotion;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ThrownLingeringPotion.class)
public abstract class ThrownLingeringPotionExtender extends ThrowableItemProjectileExtender {
    protected ThrownLingeringPotionExtender(EntityType<? extends ThrowableProjectile> type, Level level) {
        super(type, level);
    }

    @Override
    protected ResourceKey<Item> getDefaultItemId() {
        return ItemIds.LINGERING_POTION;
    }
}
