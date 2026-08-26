package net.errorcraft.itematic.mixin.world.entity.projectile;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.projectile.Projectile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Objects;

@Mixin(Projectile.class)
public class ProjectileExtender {
    @WrapOperation(
        method = "applyOnProjectileSpawned",
        at = @At(
            value = "INVOKE",
            target = "Ljava/lang/Object;equals(Ljava/lang/Object;)Z"
        )
    )
    private boolean useNullSafeEquals(Object instance, Object o, Operation<Boolean> original) {
        return Objects.equals(instance, o);
    }
}
