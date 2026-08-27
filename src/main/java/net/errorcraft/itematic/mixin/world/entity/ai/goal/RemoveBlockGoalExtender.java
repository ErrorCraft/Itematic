package net.errorcraft.itematic.mixin.world.entity.ai.goal;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.entity.ai.goal.RemoveBlockGoal;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RemoveBlockGoal.class)
public class RemoveBlockGoalExtender {
    @WrapOperation(
        method = "tick",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/core/particles/ParticleType;Lnet/minecraft/world/item/Item;)Lnet/minecraft/core/particles/ItemParticleOption;"
        )
    )
    private ItemParticleOption newItemStackTemplateForEggUseCreateStackTemplate(ParticleType<ItemParticleOption> type, Item item, Operation<ItemParticleOption> original, @Local(name = "level") Level level) {
        return new ItemParticleOption(
            type,
            level.itematic$createStackTemplate(ItemIds.EGG)
        );
    }
}
