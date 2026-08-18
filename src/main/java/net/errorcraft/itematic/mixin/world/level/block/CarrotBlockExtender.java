package net.errorcraft.itematic.mixin.world.level.block;

import net.errorcraft.itematic.access.world.level.block.CropBlockAccess;
import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.CarrotBlock;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CarrotBlock.class)
public class CarrotBlockExtender implements CropBlockAccess {
    @Override
    public ResourceKey<Item> itematic$seedsItemId() {
        return ItemIds.CARROT;
    }
}
