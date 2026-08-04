package net.errorcraft.itematic.mixin.block;

import net.errorcraft.itematic.access.block.AbstractPlantStemBlockAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(GrowingPlantHeadBlock.class)
public class AbstractPlantStemBlockExtender implements AbstractPlantStemBlockAccess {
    @Unique
    private ResourceKey<Item> stemItemKey;

    @Override
    public ResourceKey<Item> itematic$stemItemKey() {
        return this.stemItemKey;
    }

    @Override
    public void itematic$setStemItemKey(ResourceKey<Item> stemItemKey) {
        this.stemItemKey = stemItemKey;
    }
}
