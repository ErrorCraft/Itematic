package net.errorcraft.itematic.mixin.client.multiplayer;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.tags.ItematicBlockTags;
import net.errorcraft.itematic.world.item.component.BlockPlacerComponent;
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
    private boolean instanceOfBlockItemUseDataComponent(Object reference, Class<BlockItem> clazz, @Local(name = "carriedItemStack") ItemStack carriedItemStack, @Share("block") LocalRef<Holder<Block>> blockReference) {
        BlockPlacerComponent blockPlacer = carriedItemStack.get(ItematicDataComponents.BLOCK_PLACER);
        if (blockPlacer == null) {
            return false;
        }

        Holder<Block> block = blockPlacer.block().defaultBlock();
        if (!block.is(ItematicBlockTags.HAS_MARKER_PARTICLE)) {
            return false;
        }

        blockReference.set(block);
        return true;
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
