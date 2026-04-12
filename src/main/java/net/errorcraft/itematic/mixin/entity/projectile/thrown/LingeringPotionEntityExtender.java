package net.errorcraft.itematic.mixin.entity.projectile.thrown;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.thrown.LingeringPotionEntity;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LingeringPotionEntity.class)
public abstract class LingeringPotionEntityExtender extends ThrownItemEntityExtender {
    protected LingeringPotionEntityExtender(EntityType<? extends ThrownEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected RegistryKey<Item> getDefaultItemKey() {
        return ItemKeys.LINGERING_POTION;
    }
}
