package net.errorcraft.itematic.mixin.block;

import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.CandleCakeBlock;
import net.minecraft.world.level.block.TntBlock;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin({ CandleCakeBlock.class, TntBlock.class })
public class IgnitableBlockExtender {
    @Redirect(
        method = "useItemOn",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;FIRE_CHARGE:Lnet/minecraft/world/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean isOfForFireChargeUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemIds.FIRE_CHARGE);
    }

    @Redirect(
        method = "useItemOn",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;FLINT_AND_STEEL:Lnet/minecraft/world/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean isOfForFlintAndSteelUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemIds.FLINT_AND_STEEL);
    }
}
