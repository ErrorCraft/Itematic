package net.errorcraft.itematic.mixin.world.level.block;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.GrowingPlantBlock;
import net.minecraft.world.level.block.GrowingPlantBodyBlock;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GrowingPlantBodyBlock.class)
public abstract class GrowingPlantBodyBlockExtender extends GrowingPlantBlock {
    protected GrowingPlantBodyBlockExtender(Properties settings, Direction growthDirection, VoxelShape outlineShape, boolean tickWater) {
        super(settings, growthDirection, outlineShape, tickWater);
    }

    @Redirect(
        method = "getCloneItemStack",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackUseCreateStack(ItemLike item, LevelReader level) {
        ResourceKey<Item> itemId = this.getHeadBlock().itematic$stemItemId();
        if (itemId == null) {
            return ItemStack.EMPTY;
        }

        return level.itematic$createStack(itemId);
    }

    @Redirect(
        method = "canBeReplaced(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/item/context/BlockPlaceContext;)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    private boolean isItemCheckId(ItemStack instance, Item item) {
        ResourceKey<Item> itemId = this.getHeadBlock().itematic$stemItemId();
        return itemId != null && instance.itematic$is(itemId);
    }
}
