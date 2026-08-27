package net.errorcraft.itematic.mixin.world.level.block;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ScaffoldingBlock;
import net.minecraft.world.level.block.SeaPickleBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.TurtleEggBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({
    ScaffoldingBlock.class,
    SeaPickleBlock.class,
    SlabBlock.class,
    SnowLayerBlock.class,
    TurtleEggBlock.class
})
public class CanBeReplacedBlockExtender extends Block {
    public CanBeReplacedBlockExtender(Properties settings) {
        super(settings);
    }

    @Redirect(
        method = "canBeReplaced(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/item/context/BlockPlaceContext;)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"
        )
    )
    private boolean isItemCheckId(ItemStack instance, Object o) {
        return instance.is(this.itematic$asItemId());
    }
}
