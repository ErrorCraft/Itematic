package net.errorcraft.itematic.mixin.block;

import net.errorcraft.itematic.access.block.CropBlockAccess;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.block.CarrotsBlock;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CarrotsBlock.class)
public class CarrotsBlockExtender implements CropBlockAccess {
    @Override
    public RegistryKey<Item> getSeedsItemKey() {
        return ItemKeys.CARROT;
    }
}
