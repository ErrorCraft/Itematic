package net.errorcraft.itematic.mixin.entity.projectile;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FireworkRocketEntity.class)
public abstract class FireworkRocketEntityExtender extends ProjectileEntity {
    public FireworkRocketEntityExtender(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyArg(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/LivingEntity;getHandPosOffset(Lnet/minecraft/item/Item;)Lnet/minecraft/util/math/Vec3d;"
        )
    )
    private Item getHandPosOffsetUseRegistryEntry(Item item) {
        return this.getWorld().itematic$getItem(ItemKeys.FIREWORK_ROCKET).value();
    }

    @Redirect(
        method = {
            "initDataTracker",
            "readCustomDataFromNbt"
        },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/projectile/FireworkRocketEntity;getDefaultStack()Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForFireworkRocketUseCreateStack() {
        return this.getWorld().itematic$createStack(ItemKeys.FIREWORK_ROCKET);
    }
}
