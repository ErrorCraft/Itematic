package net.errorcraft.itematic.mixin.world.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(ProjectileWeaponItem.class)
public interface ProjectileWeaponItemAccessor {
    @Invoker("draw")
    static List<ItemStack> draw(ItemStack weapon, ItemStack projectile, LivingEntity shooter) {
        throw new AssertionError();
    }

    @Invoker("shootProjectile")
    void itematic$shootProjectile(LivingEntity user, Projectile projectileEntity, int index, float power, float uncertainty, float angle, @Nullable LivingEntity targetOverrride);
}
