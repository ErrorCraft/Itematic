package net.errorcraft.itematic.mixin.block;

import net.errorcraft.itematic.access.block.AbstractPlantStemBlockAccess;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.block.TwistingVinesBlock;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(TwistingVinesBlock.class)
public class TwistingVinesBlockExtender implements AbstractPlantStemBlockAccess {
    @Override
    public RegistryKey<Item> itematic$stemItemKey() {
        return ItemKeys.TWISTING_VINES;
    }
}
