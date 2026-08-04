package net.errorcraft.itematic.mixin.block;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.*;
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
public class CanReplaceBlockExtender extends Block {
    public CanReplaceBlockExtender(Properties settings) {
        super(settings);
    }

    @Redirect(
        method = "canBeReplaced(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/item/context/BlockPlaceContext;)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    private boolean isOfUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(this.itematic$asItemKey());
    }
}
