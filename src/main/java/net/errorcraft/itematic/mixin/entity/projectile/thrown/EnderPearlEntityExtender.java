package net.errorcraft.itematic.mixin.entity.projectile.thrown;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EnderPearlEntity.class)
public abstract class EnderPearlEntityExtender extends ThrownItemEntityExtender {
    public EnderPearlEntityExtender(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected RegistryKey<Item> getDefaultItemKey() {
        return ItemKeys.ENDER_PEARL;
    }
}
