package net.errorcraft.itematic.mixin.world.entity.ai.behavior;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ProjectileWeaponItem;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(BehaviorUtils.class)
public class BehaviorUtilsExtender {
    @ModifyConstant(
        method = "isWithinAttackRange",
        constant = @Constant(
            classValue = ProjectileWeaponItem.class,
            ordinal = 0
        )
    )
    private static boolean instanceOfProjectileWeaponItemUseDataComponent(Object reference, Class<ProjectileWeaponItem> clazz, Mob body, @Share("range") LocalIntRef rangeReference) {
        Integer range = body.getMainHandItem().get(ItematicDataComponents.SHOOTER_RANGE);
        if (range != null) {
            rangeReference.set(range);
            return true;
        }

        return false;
    }

    @ModifyVariable(
        method = "isWithinAttackRange",
        at = @At("LOAD"),
        ordinal = 0
    )
    @Nullable
    private static Item castToProjectileWeaponItemUseNull(Item instance) {
        return null;
    }

    @WrapOperation(
        method = "isWithinAttackRange",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ProjectileWeaponItem;getDefaultProjectileRange()I"
        )
    )
    private static int getDefaultProjectileRangeUseDataComponent(ProjectileWeaponItem instance, Operation<Integer> original, @Share("range") LocalIntRef rangeReference) {
        return rangeReference.get();
    }
}
