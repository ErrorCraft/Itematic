package net.errorcraft.itematic.mixin.client.world;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.BlockItemComponent;
import net.minecraft.block.Block;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;

import java.util.Optional;
import java.util.Set;

@Mixin(ClientWorld.class)
public class ClientWorldExtender {
    @Unique
    private static final Set<RegistryKey<Item>> BLOCK_MARKER_ITEM_KEYS = Set.of(ItemKeys.BARRIER, ItemKeys.LIGHT);

    @Redirect(
        method = "getBlockParticle",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Set;contains(Ljava/lang/Object;)Z"
        )
    )
    private boolean isBlockMarkerItemUseRegistryKey(Set<Item> instance, Object o, @Local ItemStack stack) {
        return BLOCK_MARKER_ITEM_KEYS.contains(stack.itematic$key());
    }

    @ModifyConstant(
        method = "getBlockParticle",
        constant = @Constant(
            classValue = BlockItem.class,
            ordinal = 0
        )
    )
    private boolean instanceOfBlockItemUseItemComponentCheck(Object reference, Class<BlockItem> clazz, @Local ItemStack itemStack, @Share("blockItemComponent") LocalRef<BlockItemComponent> blockItemComponent) {
        Optional<BlockItemComponent> optionalComponent = itemStack.itematic$getComponent(ItemComponentTypes.BLOCK);
        optionalComponent.ifPresent(blockItemComponent::set);
        return optionalComponent.isPresent();
    }

    @ModifyVariable(
        method = "getBlockParticle",
        at = @At("LOAD"),
        ordinal = 0
    )
    private Item castToBlockItemUseNull(Item instance) {
        return null;
    }

    @Redirect(
        method = "getBlockParticle",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/BlockItem;getBlock()Lnet/minecraft/block/Block;"
        )
    )
    private Block blockUseItemComponent(BlockItem instance, @Share("blockItemComponent") LocalRef<BlockItemComponent> blockItemComponent) {
        return blockItemComponent.get()
            .block()
            .defaultBlock()
            .value();
    }
}
