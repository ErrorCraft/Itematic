package net.errorcraft.itematic.mixin.block;

import net.minecraft.block.FlowerbedBlock;
import net.minecraft.block.PlantBlock;
import net.minecraft.block.TallFlowerBlock;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ FlowerbedBlock.class, TallFlowerBlock.class })
public abstract class FertilizableFlowerBlockExtender extends PlantBlock {
    protected FertilizableFlowerBlockExtender(Settings settings) {
        super(settings);
    }

    @Redirect(
        method = "grow",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack newItemStackUseCreateStack(ItemConvertible item, ServerWorld world) {
        return world.itematic$createStack(this.itematic$asItemKey());
    }
}
