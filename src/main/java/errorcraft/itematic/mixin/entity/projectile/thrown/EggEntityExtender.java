package errorcraft.itematic.mixin.entity.projectile.thrown;

import errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.thrown.EggEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EggEntity.class)
public abstract class EggEntityExtender extends ThrownItemEntity {
    public EggEntityExtender(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public RegistryKey<Item> getDefaultItemKey() {
        return ItemKeys.EGG;
    }
}
