package net.errorcraft.itematic.mixin.entity.passive;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PandaEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PandaEntity.class)
public abstract class PandaEntityExtender extends AnimalEntity {
    protected PandaEntityExtender(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(
        method = "sneeze",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/passive/PandaEntity;dropItem(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/entity/ItemEntity;"
        )
    )
    private ItemEntity dropItemForSlimeBallUseRegistryKey(PandaEntity instance, ItemConvertible itemConvertible) {
        return this.itematic$dropItem(ItemKeys.SLIME_BALL);
    }
}
