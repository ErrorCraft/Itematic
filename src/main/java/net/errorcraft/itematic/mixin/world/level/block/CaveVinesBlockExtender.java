package net.errorcraft.itematic.mixin.world.level.block;

import net.errorcraft.itematic.access.world.level.block.GrowingPlantHeadBlockAccess;
import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.CaveVinesBlock;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CaveVinesBlock.class)
public class CaveVinesBlockExtender implements GrowingPlantHeadBlockAccess {
    @Override
    public @Nullable ResourceKey<Item> itematic$stemItemId() {
        return ItemIds.GLOW_BERRIES;
    }
}
