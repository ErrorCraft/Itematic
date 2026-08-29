package net.errorcraft.itematic.mixin.world.level.block;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.tags.ItematicItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FlowerPotBlock.class)
public abstract class FlowerPotBlockExtender extends BlockBehaviourExtender {
    @WrapMethod(
        method = "useItemOn"
    )
    public InteractionResult onlyRemovePottedItemWhenAllowed(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult, Operation<InteractionResult> original) {
        if (itemStack.is(ItematicItemTags.PREVENTS_TAKING_POTTED_ITEM_OUT)) {
            return InteractionResult.PASS;
        }

        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }

    @Redirect(
        method = "useWithoutItem",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackUseCreateStack(ItemLike item, @Local(name = "level", argsOnly = true) Level level) {
        return level.itematic$createStack(this.itematic$asItemId());
    }

    @ModifyReturnValue(
        method = "useWithoutItem",
        at = @At("RETURN")
    )
    private InteractionResult passWhenConsumedToRunItemStackInteraction(InteractionResult original) {
        if (original == InteractionResult.CONSUME) {
            return InteractionResult.PASS;
        }

        return original;
    }

    @Redirect(
        method = "getCloneItemStack",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackUseCreateStack(ItemLike item, LevelReader level) {
        return level.itematic$createStack(this.itematic$asItemId());
    }
}
