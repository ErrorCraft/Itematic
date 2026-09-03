package net.errorcraft.itematic.mixin.world.item.enchantment;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Enchantment.class)
public class EnchantmentExtender {
    @WrapMethod(
        method = "canEnchant"
    )
    private boolean checkSuccessfullyLoaded(ItemStack itemStack, Operation<Boolean> original) {
        return itemStack.itematic$isSuccessfullyLoaded() && original.call(itemStack);
    }
}
