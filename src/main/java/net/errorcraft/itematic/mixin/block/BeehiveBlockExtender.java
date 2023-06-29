package net.errorcraft.itematic.mixin.block;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.block.BeehiveBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(BeehiveBlock.class)
public class BeehiveBlockExtender {
    @Redirect(
        method = "onUse",
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
    private boolean onUseIsOfForGlassBottleUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.isOf(ItemKeys.GLASS_BOTTLE);
    }

    @Redirect(
        method = "onUse",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack"
        )
    )
    private ItemStack onUseNewItemStackUseRegistryEntry(ItemConvertible item, @Local World world) {
        return new ItemStack(world.getItem(ItemKeys.HONEY_BOTTLE));
    }
}
