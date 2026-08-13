package net.errorcraft.itematic.mixin.block;

import net.errorcraft.itematic.access.block.CropBlockAccess;
import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.CarrotBlock;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CarrotBlock.class)
public class CarrotsBlockExtender implements CropBlockAccess {
    @Override
    public ResourceKey<Item> itematic$seedsItemKey() {
        return ItemIds.CARROT;
    }
}
