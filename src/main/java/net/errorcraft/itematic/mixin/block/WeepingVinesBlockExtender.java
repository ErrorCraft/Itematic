package net.errorcraft.itematic.mixin.block;

import net.errorcraft.itematic.access.block.AbstractPlantStemBlockAccess;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.block.WeepingVinesBlock;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(WeepingVinesBlock.class)
public class WeepingVinesBlockExtender implements AbstractPlantStemBlockAccess {
    @Override
    public RegistryKey<Item> itematic$stemItemKey() {
        return ItemKeys.WEEPING_VINES;
    }
}
