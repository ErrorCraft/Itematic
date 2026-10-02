package net.errorcraft.itematic.mixin.world.level.storage.loot.functions;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LootItemConditionalFunction.class)
public class LootItemConditionalFunctionExtender {
    @WrapMethod(
        method = "apply(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/storage/loot/LootContext;)Lnet/minecraft/world/item/ItemStack;"
    )
    private ItemStack checkSuccessfullyLoaded(ItemStack itemStack, LootContext context, Operation<ItemStack> original) {
        if (itemStack.itematic$isSuccessfullyLoaded()) {
            return original.call(itemStack, context);
        }

        return itemStack;
    }
}
