package net.errorcraft.itematic.mixin.client.renderer.item.properties.numeric;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.renderer.item.properties.numeric.UseDuration;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(UseDuration.class)
public class UseDurationExtender {
    @WrapMethod(
        method = "useDuration"
    )
    private static int useUsedItemTicksDirectly(ItemStack itemStack, LivingEntity owner, Operation<Integer> original) {
        return owner.itematic$usedItemTicks();
    }
}
