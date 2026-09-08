package net.errorcraft.itematic.mixin.world.item.crafting;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipePropertySet;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(RecipePropertySet.class)
public class RecipePropertySetExtender {
    @WrapMethod(
        method = "test"
    )
    private boolean checkInteractableStack(ItemStack itemStack, Operation<Boolean> original) {
        if (itemStack.itematic$cannotBeInteractedWith()) {
            return false;
        }

        return original.call(itemStack);
    }
}
