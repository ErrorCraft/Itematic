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
    private RegistryKey<Item> pickBlockKey;

    @Redirect(
        method = "getPickStack",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack newItemStackUseCreateStack(ItemConvertible item, WorldView world) {
        return world.itematic$createStack(this.pickBlockKey());
    }

    @Override
    public RegistryKey<Item> itematic$pickBlockKey() {
        return this.pickBlockKey;
    }

    @Override
    public void itematic$setPickBlockKey(RegistryKey<Item> pickBlockKey) {
        this.pickBlockKey = pickBlockKey;
    }

    @Unique
    private RegistryKey<Item> pickBlockKey() {
        if (this.pickBlockKey != null) {
            return this.pickBlockKey;
        }
        return ItemUtil.keyFromBlock((Block)(Object) this);
    }
}
