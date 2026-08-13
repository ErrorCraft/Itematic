package net.errorcraft.itematic.mixin.entity.projectile.thrown;

import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.throwableitemprojectile.Snowball;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Snowball.class)
public abstract class SnowballEntityExtender extends ThrownItemEntityExtender {
    public SnowballEntityExtender(EntityType<? extends ThrowableItemProjectile> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    protected ResourceKey<Item> getDefaultItemKey() {
        return ItemIds.SNOWBALL;
    }
}
