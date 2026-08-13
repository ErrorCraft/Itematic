package net.errorcraft.itematic.mixin.structure;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.structure.structures.EndCityPieces;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

public class EndCityGeneratorExtender {
    @Mixin(EndCityPieces.EndCityPiece.class)
    public static class PieceExtender {
        @Redirect(
            method = "handleDataMarker",
            at = @At(
                value = "NEW",
                target = "net/minecraft/world/item/ItemStack"
            )
        )
        private ItemStack newItemStackForElytraUseCreateStack(ItemLike item, @Local(argsOnly = true) ServerLevelAccessor world) {
            return world.itematic$createStack(ItemIds.ELYTRA);
        }
    }
}
