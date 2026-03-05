package net.errorcraft.itematic.mixin.block;

import net.errorcraft.itematic.access.block.AbstractBlockAccess;
import net.errorcraft.itematic.item.ItemUtil;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractBlock.class)
public abstract class AbstractBlockExtender implements AbstractBlockAccess {
    @Shadow
    protected abstract Block asBlock();

    @Unique
    private RegistryKey<Item> itemKey;

    @Redirect(
        method = "canReplace",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isOfUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemUtil.keyFromBlock(this.asBlock()));
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

    @Override
    public RegistryKey<Item> itematic$asItemKey() {
        if (this.itemKey == null) {
            this.itemKey = ItemUtil.keyFromBlock(this.asBlock());
        }

        return this.itemKey;
    }

    @Override
    public void itematic$setAsItemKey(RegistryKey<Item> pickBlockKey) {
        this.itemKey = pickBlockKey;
    }
}
