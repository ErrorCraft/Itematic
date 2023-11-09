package net.errorcraft.itematic.mixin.entity.passive;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.FishEntity;
import net.minecraft.entity.passive.PufferfishEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PufferfishEntity.class)
public abstract class PufferfishEntityExtender extends FishEntity {
    public PufferfishEntityExtender(EntityType<? extends FishEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(
        method = "getBucketItem",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack"
        )
    )
    private ItemStack getBucketItemNewItemStackUseRegistryEntry(ItemConvertible item) {
        return new ItemStack(this.getWorld().itematic$getItem(ItemKeys.PUFFERFISH_BUCKET));
    }
}
