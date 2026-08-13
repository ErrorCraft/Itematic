package net.errorcraft.itematic.mixin.block;

import net.errorcraft.itematic.access.block.AbstractBlockAccess;
import net.errorcraft.itematic.world.item.Items;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockBehaviour.class)
public abstract class AbstractBlockExtender implements AbstractBlockAccess {
    @Shadow
    public abstract Item asItem();

    @Shadow
    protected abstract Block asBlock();

    @Unique
    private ResourceKey<Item> itemKey;

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

    @Redirect(
        method = "getCloneItemStack",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackUseCreateStack(ItemLike item, LevelReader world) {
        return world.itematic$createStack(this.itematic$asItemKey());
    }

    @Override
    public ResourceKey<Item> itematic$asItemKey() {
        if (this.itemKey == null) {
            this.itemKey = BuiltInRegistries.ITEM.getResourceKey(this.asItem())
                .orElseGet(() -> Items.keyFromBlock(this.asBlock()));
        }

        return this.itemKey;
    }

    @Override
    public void itematic$setAsItemKey(ResourceKey<Item> pickBlockKey) {
        this.itemKey = pickBlockKey;
    }
}
