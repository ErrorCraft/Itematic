package net.errorcraft.itematic.mixin.client.multiplayer;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.behavior.behaviors.BlockItemBehavior;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;

import java.util.Optional;
import java.util.Set;

@Mixin(ClientLevel.class)
public class ClientLevelExtender {
    @Unique
    private static final Set<ResourceKey<Item>> MARKER_PARTICLE_ITEM_KEYS = Set.of(
        ItemIds.BARRIER,
        ItemIds.LIGHT
    );

    @Redirect(
        method = "getMarkerParticleTarget",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Set;contains(Ljava/lang/Object;)Z"
        )
    )
    private boolean isMarkerParticleItemUseId(Set<Item> instance, Object o, @Local ItemStack stack) {
        return MARKER_PARTICLE_ITEM_KEYS.contains(stack.itematic$key());
    }

    @ModifyConstant(
        method = "getMarkerParticleTarget",
        constant = @Constant(
            classValue = BlockItem.class,
            ordinal = 0
        )
    )
    private boolean instanceOfBlockItemUseItemBehavior(Object reference, Class<BlockItem> clazz, @Local ItemStack stack, @Share("block") LocalRef<BlockItemBehavior> blockReference) {
        Optional<BlockItemBehavior> block = stack.itematic$getBehavior(ItemBehaviorType.BLOCK);
        block.ifPresent(blockReference::set);
        return block.isPresent();
    }

    @ModifyVariable(
        method = "getMarkerParticleTarget",
        at = @At("LOAD"),
        ordinal = 0
    )
    @Nullable
    private Item castToBlockItemUseNull(Item instance) {
        return null;
    }

    @Redirect(
        method = "getMarkerParticleTarget",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/BlockItem;getBlock()Lnet/minecraft/world/level/block/Block;"
        )
    )
    private Block getBlockUseItemBehavior(BlockItem instance, @Share("block") LocalRef<BlockItemBehavior> blockReference) {
        return blockReference.get()
            .block()
            .defaultBlock()
            .value();
    }
}
