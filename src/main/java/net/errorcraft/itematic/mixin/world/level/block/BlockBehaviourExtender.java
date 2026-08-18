package net.errorcraft.itematic.mixin.world.level.block;

import net.errorcraft.itematic.access.world.level.block.state.BlockBehaviourAccess;
import net.errorcraft.itematic.world.item.Items;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockBehaviour.class)
public abstract class BlockBehaviourExtender implements BlockBehaviourAccess {
    @Shadow
    public abstract Item asItem();

    @Shadow
    protected abstract Block asBlock();

    @Unique
    @Nullable
    private ResourceKey<Item> itemId;

    @Redirect(
        method = "canBeReplaced(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/item/context/BlockPlaceContext;)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    private boolean isItemCheckId(ItemStack instance, Item item) {
        return instance.itematic$is(this.itematic$asItemId());
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

    @Override
    public ResourceKey<Item> itematic$asItemId() {
        if (this.itemId == null) {
            this.itemId = BuiltInRegistries.ITEM.getResourceKey(this.asItem())
                .orElseGet(() -> Items.keyFromBlock(this.asBlock()));
        }

        return this.itemId;
    }

    @Override
    public void itematic$setAsItemId(ResourceKey<Item> itemId) {
        this.itemId = itemId;
    }
}
