package net.errorcraft.itematic.mixin.entity.projectile.thrown;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownLingeringPotion;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ThrownLingeringPotion.class)
public abstract class LingeringPotionEntityExtender extends ThrownItemEntityExtender {
    protected LingeringPotionEntityExtender(EntityType<? extends ThrowableProjectile> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    protected ResourceKey<Item> getDefaultItemKey() {
        return ItemKeys.LINGERING_POTION;
    }
}
