package net.errorcraft.itematic.mixin.block;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.FlowerBedBlock;
import net.minecraft.world.level.block.TallFlowerBlock;
import net.minecraft.world.level.block.VegetationBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ FlowerBedBlock.class, TallFlowerBlock.class })
public abstract class FertilizableFlowerBlockExtender extends VegetationBlock {
    protected FertilizableFlowerBlockExtender(Properties settings) {
        super(settings);
    }

    @Redirect(
        method = "performBonemeal",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackUseCreateStack(ItemLike item, ServerLevel world) {
        return world.itematic$createStack(this.itematic$asItemKey());
    }
}
