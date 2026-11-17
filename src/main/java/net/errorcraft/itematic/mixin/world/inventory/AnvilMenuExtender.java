package net.errorcraft.itematic.mixin.world.inventory;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(AnvilMenu.class)
public class AnvilMenuExtender {
    @WrapOperation(
        method = "createResult",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z",
            ordinal = 0
        )
    )
    private boolean isItemCheckHolder(ItemStack instance, Object object, Operation<Boolean> original, @Local(name = "addition") ItemStack addition) {
        return instance.is(addition.typeHolder());
    }

    @WrapOperation(
        method = "createResult",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;ENCHANTED_BOOK:Lnet/minecraft/world/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean isEnchantedBookCheckStoredEnchantmentsDataComponent(ItemStack instance, Object object, Operation<Boolean> original) {
        return instance.has(DataComponents.STORED_ENCHANTMENTS);
    }
}
