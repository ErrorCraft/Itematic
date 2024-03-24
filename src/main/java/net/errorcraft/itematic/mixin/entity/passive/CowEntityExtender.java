package net.errorcraft.itematic.mixin.entity.passive;

import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.mixin.entity.mob.MobEntityExtender;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CowEntity.class)
public abstract class CowEntityExtender extends MobEntityExtender {
    protected CowEntityExtender(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected @Nullable RegistryKey<Item> pickBlockKey() {
        return ItemKeys.COW_SPAWN_EGG;
    }
}
