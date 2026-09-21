package net.errorcraft.itematic.mixin.world.entity.monster.cubemob;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.errorcraft.itematic.mixin.world.entity.MobExtender;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.monster.cubemob.AbstractCubeMob;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractCubeMob.class)
public abstract class AbstractCubeMobExtender extends MobExtender {
    protected AbstractCubeMobExtender(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    @WrapOperation(
        method = "getAttackDamage",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/monster/cubemob/AbstractCubeMob;getAttributeValue(Lnet/minecraft/core/Holder;)D"
        )
    )
    private double useCustomAttackDamage(AbstractCubeMob instance, Holder<Attribute> holder, Operation<Double> original) {
        return this.itematic$getAttackDamage();
    }
}
