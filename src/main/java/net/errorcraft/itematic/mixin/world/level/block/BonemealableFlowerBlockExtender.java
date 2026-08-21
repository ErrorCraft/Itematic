package net.errorcraft.itematic.mixin.world.level.block;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.FlowerBedBlock;
import net.minecraft.world.level.block.TallFlowerBlock;
import net.minecraft.world.level.block.VegetationBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({
    FlowerBedBlock.class,
    TallFlowerBlock.class
})
public abstract class BonemealableFlowerBlockExtender extends VegetationBlock {
    protected BonemealableFlowerBlockExtender(Properties settings) {
        super(settings);
    }

    @Redirect(
        method = "performBonemeal",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackUseCreateStack(ItemLike item, ServerLevel level) {
        return level.itematic$createStack(this.itematic$asItemId());
    }
}
