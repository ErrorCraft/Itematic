package net.errorcraft.itematic.mixin.world.level.levelgen.structure.structures;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.structure.structures.EndCityPieces;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

public class EndCityPiecesExtender {
    @Mixin(EndCityPieces.EndCityPiece.class)
    public static class EndCityPieceExtender {
        @Redirect(
            method = "handleDataMarker",
            at = @At(
                value = "NEW",
                target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
            )
        )
        private ItemStack newItemStackForElytraUseCreateStack(ItemLike item, @Local(name = "level", argsOnly = true) ServerLevelAccessor level) {
            return level.itematic$createStack(ItemIds.ELYTRA);
        }
    }
}
