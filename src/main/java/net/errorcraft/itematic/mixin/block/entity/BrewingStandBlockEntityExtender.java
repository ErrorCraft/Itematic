package net.errorcraft.itematic.mixin.block.entity;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.block.entity.BrewingStandBlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(BrewingStandBlockEntity.class)
public class BrewingStandBlockEntityExtender {
    @Redirect(
        method = "isValid",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;POTION:Lnet/minecraft/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean isValidIsOfForPotionUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.isOf(ItemKeys.POTION);
    }

    @Redirect(
        method = { "isValid", "canExtract" },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;GLASS_BOTTLE:Lnet/minecraft/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean isValidIsOfForGlassBottleUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.isOf(ItemKeys.GLASS_BOTTLE);
    }
}
