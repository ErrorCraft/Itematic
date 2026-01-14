package net.errorcraft.itematic.mixin.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(RangedWeaponItem.class)
public interface RangedWeaponItemAccessor {
    @Invoker("load")
    static List<ItemStack> load(ItemStack stack, ItemStack projectile, LivingEntity shooter) {
        throw new AssertionError();
    }

    @Invoker("shoot")
    void shoot(LivingEntity user, ProjectileEntity projectile, int index, float power, float uncertainty, float angle, @Nullable LivingEntity target);
}
