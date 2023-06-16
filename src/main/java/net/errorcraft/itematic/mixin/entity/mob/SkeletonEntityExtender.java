package net.errorcraft.itematic.mixin.entity.mob;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SkeletonEntity.class)
public abstract class SkeletonEntityExtender extends AbstractSkeletonEntity {
    protected SkeletonEntityExtender(EntityType<? extends AbstractSkeletonEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(
        method = "dropEquipment",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/mob/SkeletonEntity;dropItem(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/entity/ItemEntity;"
        )
    )
    private ItemEntity dropEquipmentDropItemUseRegistryKey(SkeletonEntity instance, ItemConvertible itemConvertible) {
        return this.dropItem(ItemKeys.SKELETON_SKULL);
    }
}
