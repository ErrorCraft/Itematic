package net.errorcraft.itematic.mixin.client.world;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.BlockItemComponent;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;

import java.util.Optional;
import java.util.Set;

@Mixin(ClientLevel.class)
public class ClientWorldExtender {
    @Unique
    private static final Set<ResourceKey<Item>> BLOCK_MARKER_ITEM_KEYS = Set.of(ItemKeys.BARRIER, ItemKeys.LIGHT);

    @Redirect(
        method = "getMarkerParticleTarget",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Set;contains(Ljava/lang/Object;)Z"
        )
    )
    private boolean isBlockMarkerItemUseRegistryKey(Set<Item> instance, Object o, @Local ItemStack stack) {
        return BLOCK_MARKER_ITEM_KEYS.contains(stack.itematic$key());
    }

    @ModifyConstant(
        method = "getMarkerParticleTarget",
        constant = @Constant(
            classValue = BlockItem.class,
            ordinal = 0
        )
    )
    private boolean instanceOfBlockItemUseItemComponentCheck(Object reference, Class<BlockItem> clazz, @Local ItemStack itemStack, @Share("blockItemComponent") LocalRef<BlockItemComponent> blockItemComponent) {
        Optional<BlockItemComponent> optionalComponent = itemStack.itematic$getBehavior(ItemComponentTypes.BLOCK);
        optionalComponent.ifPresent(blockItemComponent::set);
        return optionalComponent.isPresent();
    }

    @ModifyVariable(
        method = "getMarkerParticleTarget",
        at = @At("LOAD"),
        ordinal = 0
    )
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
    private Block blockUseItemComponent(BlockItem instance, @Share("blockItemComponent") LocalRef<BlockItemComponent> blockItemComponent) {
        return blockItemComponent.get()
            .block()
            .defaultBlock()
            .value();
    }
}
