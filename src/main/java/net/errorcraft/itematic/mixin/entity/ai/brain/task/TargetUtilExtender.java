package net.errorcraft.itematic.mixin.entity.ai.brain.task;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.behavior.behaviors.ShooterItemBehavior;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ProjectileWeaponItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

import java.util.Optional;

@Mixin(BehaviorUtils.class)
public class TargetUtilExtender {
    @ModifyConstant(
        method = "isWithinAttackRange",
        constant = @Constant(
            classValue = ProjectileWeaponItem.class,
            ordinal = 0
        )
    )
    private static boolean instanceOfRangedWeaponItemUseItemBehaviorCheck(Object reference, Class<ProjectileWeaponItem> clazz, Mob mob, @Share("shooter") LocalRef<ShooterItemBehavior> shooter) {
        Optional<ShooterItemBehavior> optionalShooter = mob.getMainHandItem().itematic$getBehavior(ItemBehaviorType.SHOOTER);
        optionalShooter.ifPresent(shooter::set);
        return optionalShooter.isPresent();
    }

    @ModifyVariable(
        method = "isWithinAttackRange",
        at = @At("LOAD"),
        ordinal = 0
    )
    private static Item castToRangedWeaponItemUseNull(Item instance) {
        return null;
    }

    @Redirect(
        method = "isWithinAttackRange",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ProjectileWeaponItem;getDefaultProjectileRange()I"
        )
    )
    private static int getRangeUseItemBehavior(ProjectileWeaponItem instance, @Share("shooter") LocalRef<ShooterItemBehavior> shooter) {
        return shooter.get().range();
    }
}
