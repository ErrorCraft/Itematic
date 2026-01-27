package net.errorcraft.itematic.mixin.block;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FlowerPotBlock.class)
public class FlowerPotBlockExtender extends BlockExtender {
    // todo fix
//    /**
//     * @author ErrorCraft
//     * @reason Uses the Action implementation for data-driven items.
//     */
//    @Overwrite
//    public ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
//        if (stack.isIn(ItematicItemTags.PREVENTS_TAKING_POTTED_ITEM_OUT)) {
//            return ItemActionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
//        }
//        return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
//    }

    @Redirect(
        method = "onUse",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack newItemStackUseCreateStack(ItemConvertible item, @Local(argsOnly = true) World world) {
        return world.itematic$createStack(this.itematic$asItemKey());
    }

    @Redirect(
        method = "getPickStack",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack newItemStackUseCreateStack(ItemConvertible item, WorldView world) {
        return world.itematic$createStack(this.itematic$asItemKey());
    }
}
