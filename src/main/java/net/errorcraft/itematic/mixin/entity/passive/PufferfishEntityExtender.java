package net.errorcraft.itematic.mixin.entity.passive;

import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.mixin.entity.mob.MobEntityExtender;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.FishEntity;
import net.minecraft.entity.passive.PufferfishEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PufferfishEntity.class)
public abstract class PufferfishEntityExtender extends MobEntityExtender {
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
    private ItemStack newItemStackForPufferfishBucketUseCreateStack(ItemConvertible item) {
        return this.getWorld().itematic$createStack(ItemKeys.PUFFERFISH_BUCKET);
    }

    @Override
    protected @Nullable RegistryKey<Item> pickBlockKey() {
        return ItemKeys.PUFFERFISH_SPAWN_EGG;
    }
}
