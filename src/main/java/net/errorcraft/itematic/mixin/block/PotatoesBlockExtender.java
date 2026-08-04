package net.errorcraft.itematic.mixin.block;

import net.errorcraft.itematic.access.block.CropBlockAccess;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.PotatoBlock;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PotatoBlock.class)
public class PotatoesBlockExtender implements CropBlockAccess {
    @Override
    public ResourceKey<Item> itematic$seedsItemKey() {
        return ItemKeys.POTATO;
    }
}
