package net.errorcraft.itematic.mixin.client.color.block;

import net.errorcraft.itematic.access.client.color.block.BlockColorsAccess;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.item.Item;
import net.minecraft.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockColors.class)
public class BlockColorsExtender implements BlockColorsAccess {
    private Registry<Item> itemRegistry;

    @Override
    public Registry<Item> itemRegistry() {
        return this.itemRegistry;
    }

    @Override
    public void setItemRegistry(Registry<Item> itemRegistry) {
        this.itemRegistry = itemRegistry;
    }
}
