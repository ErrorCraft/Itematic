package net.errorcraft.itematic.mixin.block;

import net.errorcraft.itematic.access.block.AbstractPlantStemBlockAccess;
import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.TwistingVinesBlock;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(TwistingVinesBlock.class)
public class TwistingVinesBlockExtender implements AbstractPlantStemBlockAccess {
    @Override
    public ResourceKey<Item> itematic$stemItemKey() {
        return ItemIds.TWISTING_VINES;
    }
}
