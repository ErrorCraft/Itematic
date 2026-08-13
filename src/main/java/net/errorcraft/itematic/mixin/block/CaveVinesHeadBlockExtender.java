package net.errorcraft.itematic.mixin.block;

import net.errorcraft.itematic.access.block.AbstractPlantStemBlockAccess;
import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.CaveVinesBlock;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CaveVinesBlock.class)
public class CaveVinesHeadBlockExtender implements AbstractPlantStemBlockAccess {
    @Override
    public ResourceKey<Item> itematic$stemItemKey() {
        return ItemIds.GLOW_BERRIES;
    }
}
