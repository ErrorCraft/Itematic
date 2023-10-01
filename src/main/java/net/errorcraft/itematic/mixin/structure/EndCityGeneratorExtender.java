package net.errorcraft.itematic.mixin.structure;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.structure.EndCityGenerator;
import net.minecraft.world.ServerWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

public class EndCityGeneratorExtender {
    @Mixin(EndCityGenerator.Piece.class)
    public static class PieceExtender {
        @Redirect(
            method = "handleMetadata",
            at = @At(
                value = "NEW",
                target = "net/minecraft/item/ItemStack"
            )
        )
        private static ItemStack newItemStackForElytraUseRegistryEntry(ItemConvertible item, @Local ServerWorldAccess world) {
            return new ItemStack(world.getItem(ItemKeys.ELYTRA));
        }
    }
}
