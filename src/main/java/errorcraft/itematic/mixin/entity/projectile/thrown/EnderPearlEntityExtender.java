package errorcraft.itematic.mixin.entity.projectile.thrown;

import errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EnderPearlEntity.class)
public abstract class EnderPearlEntityExtender extends ThrownItemEntity {
    public EnderPearlEntityExtender(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public RegistryKey<Item> getDefaultItemKey() {
        return ItemKeys.ENDER_PEARL;
    }
}
