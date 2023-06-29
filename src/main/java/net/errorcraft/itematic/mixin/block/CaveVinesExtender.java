package net.errorcraft.itematic.mixin.block;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.block.CaveVines;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CaveVines.class)
public interface CaveVinesExtender {
    @Redirect(
        method = "pickBerries",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack"
        )
    )
    private static ItemStack pickBerriesNewItemStackUseRegistryEntry(ItemConvertible item, int count, @Local World world) {
        return new ItemStack(world.getItem(ItemKeys.GLOW_BERRIES), count);
    }
}
