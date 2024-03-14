package net.errorcraft.itematic.mixin.entity.projectile.thrown;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SnowballEntity.class)
public abstract class SnowballEntityExtender extends ThrownItemEntityExtender {
    public SnowballEntityExtender(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(
        method = "getParticleParameters",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isOfForDefaultItemUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(this.getDefaultItemKey());
    }

    @Override
    protected RegistryKey<Item> getDefaultItemKey() {
        return ItemKeys.SNOWBALL;
    }
}
