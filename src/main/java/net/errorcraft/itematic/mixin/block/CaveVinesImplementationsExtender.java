package net.errorcraft.itematic.mixin.block;

import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.CaveVinesBlock;
import net.minecraft.world.level.block.CaveVinesPlantBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ CaveVinesPlantBlock.class, CaveVinesBlock.class })
public class CaveVinesImplementationsExtender {
    @Redirect(
        method = "getCloneItemStack",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForGlowBerriesUseCreateStack(ItemLike item, LevelReader world) {
        return world.itematic$createStack(ItemIds.GLOW_BERRIES);
    }
}
