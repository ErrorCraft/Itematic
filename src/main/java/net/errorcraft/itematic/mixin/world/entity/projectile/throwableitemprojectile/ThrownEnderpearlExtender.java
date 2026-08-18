package net.errorcraft.itematic.mixin.world.entity.projectile.throwableitemprojectile;

import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownEnderpearl;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ThrownEnderpearl.class)
public abstract class ThrownEnderpearlExtender extends ThrowableItemProjectileExtender {
    public ThrownEnderpearlExtender(EntityType<? extends ThrowableItemProjectile> type, Level level) {
        super(type, level);
    }

    @Override
    protected ResourceKey<Item> getDefaultItemId() {
        return ItemIds.ENDER_PEARL;
    }
}
