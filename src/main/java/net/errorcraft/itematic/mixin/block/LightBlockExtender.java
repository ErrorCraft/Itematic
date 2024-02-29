package net.errorcraft.itematic.mixin.block;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.item.ItemAccess;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.block.LightBlock;
import net.minecraft.item.Item;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LightBlock.class)
public class LightBlockExtender {
    @ModifyArg(
        method = "getOutlineShape",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/ShapeContext;isHolding(Lnet/minecraft/item/Item;)Z"
        )
    )
    private Item getLightUseDynamicRegistry(Item item, @Local(argsOnly = true) BlockView world) {
        if (world instanceof ItemAccess itemAccess) {
            return itemAccess.getOptionalEntry(ItemKeys.LIGHT)
                .map(RegistryEntry::value)
                .orElse(null);
        }
        return null;
    }
}
