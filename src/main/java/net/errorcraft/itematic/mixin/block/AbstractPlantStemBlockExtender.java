package net.errorcraft.itematic.mixin.block;

import net.errorcraft.itematic.access.block.AbstractPlantStemBlockAccess;
import net.minecraft.block.AbstractPlantStemBlock;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(AbstractPlantStemBlock.class)
public class AbstractPlantStemBlockExtender implements AbstractPlantStemBlockAccess {
    @Unique
    private RegistryKey<Item> stemItemKey;

    @Override
    public RegistryKey<Item> itematic$stemItemKey() {
        return this.stemItemKey;
    }

    @Override
    public void itematic$setStemItemKey(RegistryKey<Item> stemItemKey) {
        this.stemItemKey = stemItemKey;
    }
}
