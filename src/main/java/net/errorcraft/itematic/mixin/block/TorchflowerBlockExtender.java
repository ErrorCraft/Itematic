package net.errorcraft.itematic.mixin.block;

import net.errorcraft.itematic.access.block.CropBlockAccess;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.block.TorchflowerBlock;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(TorchflowerBlock.class)
public class TorchflowerBlockExtender implements CropBlockAccess {
    @Override
    public RegistryKey<Item> itematic$seedsItemKey() {
        return ItemKeys.TORCHFLOWER_SEEDS;
    }
}
