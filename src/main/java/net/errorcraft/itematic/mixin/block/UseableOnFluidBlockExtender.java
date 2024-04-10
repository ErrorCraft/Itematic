package net.errorcraft.itematic.mixin.block;

import net.errorcraft.itematic.access.block.BlockAccess;
import net.errorcraft.itematic.access.item.ItemPlacementContextAccess;
import net.minecraft.block.FrogspawnBlock;
import net.minecraft.block.LilyPadBlock;
import net.minecraft.item.ItemPlacementContext;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ LilyPadBlock.class, FrogspawnBlock.class })
public class UseableOnFluidBlockExtender implements BlockAccess {
    @Override
    public ItemPlacementContext itematic$placementContext(ItemPlacementContext context) {
        return ((ItemPlacementContextAccess) context).itematic$offset(0, 1, 0);
    }
}
