package net.errorcraft.itematic.mixin.client.multiplayer;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.tags.ItematicBlockTags;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.behavior.behaviors.BlockItemBehavior;
import net.errorcraft.itematic.world.item.placement.block.picker.BlockPicker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Optional;
import java.util.Set;

@Mixin(ClientLevel.class)
public class ClientLevelExtender {
    @WrapOperation(
        method = "getMarkerParticleTarget",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Set;contains(Ljava/lang/Object;)Z"
        )
    )
    private boolean doNotCheckItem(Set<Item> instance, Object o, Operation<Boolean> original) {
        return true;
    }

    @ModifyConstant(
        method = "getMarkerParticleTarget",
        constant = @Constant(
            classValue = BlockItem.class,
            ordinal = 0
        )
    )
    private boolean instanceOfBlockItemUseItemBehavior(Object reference, Class<BlockItem> clazz, @Local(name = "carriedItemStack") ItemStack carriedItemStack, @Share("block") LocalRef<Holder<Block>> blockReference) {
        Optional<Holder<Block>> block = carriedItemStack.itematic$getBehavior(ItemBehaviorType.BLOCK)
            .map(BlockItemBehavior::block)
            .map(BlockPicker::defaultBlock)
            .filter(defaultBlock -> defaultBlock.is(ItematicBlockTags.HAS_MARKER_PARTICLE));
        block.ifPresent(blockReference::set);
        return block.isPresent();
    }

    @ModifyVariable(
        method = "getMarkerParticleTarget",
        at = @At("LOAD"),
        name = "carriedItem"
    )
    @Nullable
    private Item castToBlockItemUseNull(Item carriedItem) {
        return null;
    }

    @WrapOperation(
        method = "getMarkerParticleTarget",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/BlockItem;getBlock()Lnet/minecraft/world/level/block/Block;"
        )
    )
    private Block getBlockUseItemBehavior(BlockItem instance, Operation<Block> original, @Share("block") LocalRef<Holder<Block>> blockReference) {
        return blockReference.get().value();
    }
}
