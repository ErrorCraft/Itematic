package net.errorcraft.itematic.mixin.block;

import net.errorcraft.itematic.access.block.BlockAccess;
import net.errorcraft.itematic.item.ItemUtil;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Block.class)
public abstract class BlockExtender implements BlockAccess {
    @Unique
    private RegistryKey<Item> itemKey;

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
        if (this.itemKey != null) {
            return this.itemKey;
        }
        return ItemUtil.keyFromBlock((Block)(Object) this);
    }

    @Override
    public void itematic$setAsItemKey(RegistryKey<Item> pickBlockKey) {
        this.itemKey = pickBlockKey;
    }
}
