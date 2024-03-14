package net.errorcraft.itematic.mixin.entity.projectile.thrown;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ThrownItemEntity.class)
public abstract class ThrownItemEntityExtender extends ThrownEntity {
    protected ThrownItemEntityExtender(EntityType<? extends ThrownEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(
        method = { "initDataTracker", "readCustomDataFromNbt", "method_57319" },
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack newItemStackUseRegistryEntry(ItemConvertible item) {
        return this.getWorld().itematic$createStack(this.getDefaultItemKey());
    }

    @Unique
    protected RegistryKey<Item> getDefaultItemKey() {
        return ItemKeys.AIR;
    }
}
