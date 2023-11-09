package net.errorcraft.itematic.mixin.block;

import net.errorcraft.itematic.access.block.StemBlockAccess;
import net.minecraft.block.AttachedStemBlock;
import net.minecraft.block.StemBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ StemBlock.class, AttachedStemBlock.class })
public class StemBlockExtender implements StemBlockAccess {
    private RegistryKey<Item> pickBlockKey;

    @Override
    public void setPickBlockItemKey(RegistryKey<Item> key) {
        this.pickBlockKey = key;
    }

    @Redirect(
        method = "getPickStack",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack"
        )
    )
    private ItemStack newItemStackUseCreateStack(ItemConvertible item, WorldView world) {
        return world.itematic$createStack(this.pickBlockKey);
    }
}
