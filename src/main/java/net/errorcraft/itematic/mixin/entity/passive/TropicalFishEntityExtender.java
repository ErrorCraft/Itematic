package net.errorcraft.itematic.mixin.entity.passive;

import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.mixin.entity.mob.MobEntityExtender;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.SchoolingFishEntity;
import net.minecraft.entity.passive.TropicalFishEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TropicalFishEntity.class)
public abstract class TropicalFishEntityExtender extends MobEntityExtender {
    public TropicalFishEntityExtender(EntityType<? extends SchoolingFishEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(
        method = "getBucketItem",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack"
        )
    )
    private ItemStack newItemStackForTropicalFishBucketUseCreateStack(ItemConvertible item) {
        return this.getWorld().itematic$createStack(ItemKeys.TROPICAL_FISH_BUCKET);
    }

    @Override
    protected @Nullable RegistryKey<Item> pickBlockKey() {
        return ItemKeys.TROPICAL_FISH_SPAWN_EGG;
    }
}
